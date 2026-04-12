const config = {
    type: Phaser.AUTO,
    width: 800,
    height: 600,
    //pixelArt: true,
    scale: {
        mode: Phaser.Scale.FIT,
        //mode: Phaser.Scale.FIT, //ENVELOP
        //autoCenter: Phaser.Scale.CENTER_BOTH,
        parent: 'game-container',
        // other scale options...
    },
    physics: {
        default: 'arcade',
        arcade: {
            gravity: { y: 0 },
            debug: false
        }
    },
    scene: {
        preload: preload,
        create: create,
        update: update
    },
    debug: true
};

const game = new Phaser.Game(config);
let player;
let vebak;
let map;

let enemy;
let enemyPointX = 100; //##
let enemyPointY = 100; //##

let backgroundLayer;
let collisionLayer;
let cursors;
let score = 0; //##
let bestScore = 0; //##
let scoreText; //##
let bestScoreText; //##
let lastCollectTime = 0; //##
let hasMoveTarget = false; //##
let moveTargetX = 0; //##
let moveTargetY = 0; //##

const pointsPerItem = 10; //##
const noHitPenaltyMs = 3000; //##
const penaltyPoints = 1; //##
const enemySpeed = 80; //##

function preload() {

    this.load.spritesheet('robot', 'assets/lego.png',
        { frameWidth: 37, frameHeight: 48 });

    this.load.image('vebak', 'maps/vebak.png');
    this.load.image('tiles', 'assets/map_tiles.png');
    this.load.tilemapTiledJSON('json_map', 'maps/bezjmena.json');

    this.load.image('enemy', 'maps/enemy.png');
    this.load.audio('coinSound', 'maps/coin_sound.mp3'); //##
    this.load.audio('deathSound', 'maps/death.mp3'); //##
    // this.load.tilemapTiledJSON('json_map', 'assets/json_map.json');


}

function create() {
    map = this.make.tilemap({ key: 'json_map' });
    //'test_Tiles' - name of the tileset in maps/bezjmena.json
    //'tiles' - name of the image in load.images()
    const tiles = map.addTilesetImage('test_Tiles', 'tiles');
    backgroundLayer = map.createLayer('Vrstva dlaždic 1', tiles, 0, 0);

    // Zachováno kvůli původní struktuře kódu: pokud collision vrstva neexistuje,
    // použije se background vrstva a nic to nerozbije.
    collisionLayer = map.getLayer('collision')
        ? map.createLayer('collision', tiles, 0, 0)
        : backgroundLayer;

    const structureTileIds = [ //##
        // 4 strechy (3x3 bloky) //##
        28, 29, 30, 36, 37, 38, 44, 45, 46, //##
        // fontana (3x3 blok) //##
        52, 53, 54, 60, 61, 62, 68, 69, 70 //##
    ]; //##

    // Hardcoded hitboxy na strechy + fontanu podle ID v bezjmena.json //##
    collisionLayer.setCollision(structureTileIds); //##


    bestScore = Number(localStorage.getItem('vebakBestScore') || 0); //##

    player = this.physics.add.sprite(100, 100, 'robot');
    player.body.setSize(30, 42, true); //##
    player.setCollideWorldBounds(true); //##

    const enemySpawn = getRandomWalkablePoint(40); //##
    enemy = this.physics.add.sprite(enemySpawn.x, enemySpawn.y, 'enemy');
    enemy.setScale(0.08);
    enemy.body.setSize(36, 36, true); //## 
    enemy.setCollideWorldBounds(true); //##

    const enemyTarget = getRandomWalkablePoint(40); //##
    enemyPointX = enemyTarget.x; //##
    enemyPointY = enemyTarget.y; //##


    vebak = this.physics.add.sprite(0, 0, 'vebak'); //##
    vebak.setScale(0.09);

    let vebakHalfW = Math.ceil(vebak.displayWidth / 2); //##
    let vebakHalfH = Math.ceil(vebak.displayHeight / 2); //##
    let startX = getRandomInt(vebakHalfW, map.widthInPixels - vebakHalfW); //##
    let startY = getRandomInt(vebakHalfH, map.heightInPixels - vebakHalfH); //##
    vebak.setPosition(startX, startY); //##

    cursors = this.input.keyboard.createCursorKeys();

    this.input.on('pointerdown', function (pointer) { //##
        moveTargetX = Phaser.Math.Clamp(pointer.worldX, 16, map.widthInPixels - 16); //##
        moveTargetY = Phaser.Math.Clamp(pointer.worldY, 16, map.heightInPixels - 16); //##
        hasMoveTarget = true; //##
    }); //##

    this.physics.add.collider(player, collisionLayer);
    this.physics.add.collider(enemy, collisionLayer); //##
    this.physics.add.overlap(player, backgroundLayer);
    this.physics.add.overlap(player, vebak, collisionPlayerVebak);
    this.physics.add.overlap(player, enemy, collisionPlayerEnemy);

    // https://docs.phaser.io/api-documentation/class/cameras-scene2d-camera#startfollow	

    this.anims.create({
        key: 'run',
        frames: this.anims.generateFrameNumbers('robot', { start: 0, end: 15 }),
        frameRate: 20,
        repeat: -1
    });

    scoreText = this.add.text(16, 16, '', { //##
        fontSize: '24px',
        color: '#ffffff',
        stroke: '#000000',
        strokeThickness: 4
    });

    bestScoreText = this.add.text(16, 46, '', { //##
        fontSize: '24px',
        color: '#ffff66',
        stroke: '#000000',
        strokeThickness: 4
    });

    lastCollectTime = this.time.now; //##
    updateText(); //##

}

//ukradnute z nejake dokumentacni chytre stranky
function getRandomInt(min, max) {
  const minCeiled = Math.ceil(min);
  const maxFloored = Math.floor(max);
  return Math.floor(Math.random() * (maxFloored - minCeiled) + minCeiled); // The maximum is exclusive and the minimum is inclusive
}

function getRandomWalkablePoint(padding) { //##
    for (let i = 0; i < 80; i++) { //##
        let x = getRandomInt(padding, map.widthInPixels - padding); //##
        let y = getRandomInt(padding, map.heightInPixels - padding); //##
        let tile = collisionLayer.getTileAtWorldXY(x, y); //##

        if (!tile || !tile.collides) { //##
            return { x: x, y: y }; //##
        } //##
    } //##

    return { x: padding, y: padding }; //##
} //##

function enemyMovement(){
    let distanceToTarget = Phaser.Math.Distance.Between(enemy.x, enemy.y, enemyPointX, enemyPointY); //##

    if (distanceToTarget < 8) { //##
        const nextPoint = getRandomWalkablePoint(40); //##
        enemyPointX = nextPoint.x; //##
        enemyPointY = nextPoint.y; //##
    } //##

    if (distanceToTarget > 16 && (enemy.body.blocked.left || enemy.body.blocked.right || enemy.body.blocked.up || enemy.body.blocked.down)) { //##
        const nextPoint = getRandomWalkablePoint(40); //##
        enemyPointX = nextPoint.x; //##
        enemyPointY = nextPoint.y; //##
    }

    let dx = enemyPointX - enemy.x; //##
    let dy = enemyPointY - enemy.y; //##
    let length = Math.hypot(dx, dy); //##

    if (length > 0) { //##
        enemy.setVelocity((dx / length) * enemySpeed, (dy / length) * enemySpeed); //##
    } else {
        enemy.setVelocity(0, 0); //##
    }
}

function update() {

    //player.anims.play('run', true);

    let isMoving = false;
    const usingKeys = cursors.left.isDown || cursors.right.isDown || cursors.up.isDown || cursors.down.isDown; //##

    if (usingKeys) { //##
        hasMoveTarget = false; //##
    } //##

    // Horizontal movement
    if (cursors.left.isDown) {
        player.body.setVelocityX(-150);
        player.angle = 90;
        isMoving = true;
    }
    else if (cursors.right.isDown) {
        player.body.setVelocityX(150);
        player.angle = 270;
        isMoving = true;
    }
    else {
        player.body.setVelocityX(0);
    }

    // Vertical movement
    if (cursors.up.isDown) {
        player.body.setVelocityY(-150);
        player.angle = 0;
        isMoving = true;
    }
    else if (cursors.down.isDown) {
        player.body.setVelocityY(150);
        player.angle = 180;
        isMoving = true;
    }
    else {
        player.body.setVelocityY(0);
    }

    if (!usingKeys && hasMoveTarget) { //##
        this.physics.moveTo(player, moveTargetX, moveTargetY, 200); //##
        let dist = Phaser.Math.Distance.Between(player.x, player.y, moveTargetX, moveTargetY); //##

        if (dist > 8) { //##
            isMoving = true; //##
            if (Math.abs(player.body.velocity.x) > Math.abs(player.body.velocity.y)) { //##
                player.angle = player.body.velocity.x > 0 ? 270 : 90; //##
            } else { //##
                player.angle = player.body.velocity.y > 0 ? 180 : 0; //##
            } //##
        } else { //##
            player.body.setVelocity(0, 0); //##
            hasMoveTarget = false; //##
        } //##
    } //##

    player.anims.play('run', isMoving);

    if (this.time.now - lastCollectTime >= noHitPenaltyMs) { //##
        score = Math.max(0, score - penaltyPoints); //##
        lastCollectTime = this.time.now; //##
        updateText(); //##
    }

   

    enemyMovement();


}

function updateText() {
    scoreText.setText('Skore: ' + score); //##
    bestScoreText.setText('Nejlepsi skore: ' + bestScore); //##
}

function collisionPlayerEnemy(player, enemy) {
    //console.log("collision")

    if (score > 0) { //##
        player.scene.sound.play('deathSound'); //##
    } //##

    score = 0; //##
    lastCollectTime = player.scene.time.now; //##
    updateText(); //##


}

function collisionPlayerVebak(player, item) {
    //console.log("collision")
    item.disableBody(true, true);

    player.scene.sound.play('coinSound'); //##

    score += pointsPerItem; //##

    if (score > bestScore) { //##
        bestScore = score; //##
        localStorage.setItem('vebakBestScore', String(bestScore)); //##
    }

    lastCollectTime = player.scene.time.now; //##
    updateText(); //##

    if (item.body.enable == false) {
        // https://docs.phaser.io/api-documentation/class/physics-arcade-sprite#enablebody
        let itemHalfW = Math.ceil(item.displayWidth / 2); //##
        let itemHalfH = Math.ceil(item.displayHeight / 2); //##
        let itemX = getRandomInt(itemHalfW, map.widthInPixels - itemHalfW); //##
        let itemY = getRandomInt(itemHalfH, map.heightInPixels - itemHalfH); //##
        item.enableBody(true, itemX, itemY, true, true);
    }


}

