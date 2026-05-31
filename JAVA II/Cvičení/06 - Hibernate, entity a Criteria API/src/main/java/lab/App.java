package lab;

import java.io.IOException;
import java.sql.SQLException;

import java.util.List;
import lab.data.Game;
import lab.data.Player;
import lab.data.Score;
import org.h2.tools.Server;

import lab.storage.JpaConnector;
import lombok.extern.log4j.Log4j2;

/**
 * Class <b>App</b> - extends class Application and it is an entry point of the
 * program
 *
 * @author Java I
 */
@Log4j2
public class App {

	public static void main(String[] args) {
		log.info("Application lauched");

		Server server = startDBWebServer();

		JpaConnector connector = new JpaConnector();

        for (int i = 0; i < 3; i++) {
            connector.save(Game.generateAny());
        }
        for (int i = 0; i < 5; i++) {
            connector.save(Player.generate());
        }
        List<Player> players = connector.getAll(Player.class);
        List<Game> games = connector.getAll(Game.class);
        for (int i = 0; i < 20; i++) {
            connector.save(Score.generate(players, games));
        }

        connector.getEntityManager().clear();
        connector.getAll(Score.class).forEach(log::info);
        connector.getAll(Player.class).forEach(App::printPlayer);
        connector.getAll(Game.class).forEach(App::printGame);

        log.info("==================================================================");
        log.info("----------- FindBy with no conditions ----------------------------");
        connector.findBy("", "", null).forEach(log::info);
        log.info("==================================================================");
        log.info("");
        log.info("");
        log.info("==================================================================");
        log.info("-----------'David', '', null--------------------------------------");
        connector.findBy("David","", null).forEach(log::info);
        log.info("==================================================================");
        log.info("");
        log.info("");
        log.info("==================================================================");
        log.info("-----------'avi ani Sedl Posp Geek', 'Halo', medium---------------");
        connector.findBy("avi ani Sedl Posp", "Halo Java", Score.Difficult.MEDIUM).forEach(log::info);
        log.info("==================================================================");
        log.info("");
        log.info("");
        log.info("==================================================================");
        log.info("-----------'', 'Java', null---------------------------------------");
        connector.findBy("", "Java", null).forEach(log::info);
        log.info("==================================================================");

        connector.deletePlayer(Tools.randomElementFrom(connector.getAll(Player.class)));
        connector.deleteGame(Tools.randomElementFrom(connector.getAll(Game.class)));

		waitForKeyPress();
		stopDBWebServer(server);
	}


    private static void printPlayer(Player player) {
        log.info(player);
        for (Score score : player.getScores()) {
            log.info("    -> " + score);
        }
    }

    private static void printGame(Game game) {
        log.info(game);
        for (Score score : game.getScores()) {
            log.info("    -> " + score);
        }
    }

    private static Server startDBWebServer() {
		// Start HTTP server for access H2 DB for look inside
		try {
			Server server = Server.createWebServer();
			log.info(server.getURL());
			server.start();
			log.info("DB Web server started!");
			return server;
		} catch (SQLException e) {
			log.error("Cannot create DB web server.", e);
			return null;
		}
	}

	private static void stopDBWebServer(Server server) {
		// Stop HTTP server for access H2 DB
        if(server != null) {
            log.info("Ending DB web server BYE.");
            server.stop();
        }
	}

	private static void waitForKeyPress() {
		log.info("Waitnig for Key press (ENTER)");
		try {
			if(System.in.read() < 0) {
                log.info("Input stream closed. Waiting finished automatically.");
			}
		} catch (IOException e) {
			log.error("Cannot read input from keyboard.", e);
		}
	}

}
