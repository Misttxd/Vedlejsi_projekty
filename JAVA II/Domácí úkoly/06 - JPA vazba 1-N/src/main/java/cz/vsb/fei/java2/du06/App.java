package cz.vsb.fei.java2.du06;

import cz.vsb.fei.java2.du06.model.Player;
import cz.vsb.fei.java2.du06.model.ScoreRecord;
import cz.vsb.fei.java2.du06.storage.ScoreStorage;

public class App {

    public static void main(String[] args) {
        try (ScoreStorage storage = new ScoreStorage()) {
            Player player = new Player("Andreas");
            player.addScore(new ScoreRecord("Big City Life", 9850));
            player.addScore(new ScoreRecord("Silicon Wings", 7420));

            storage.save(player);

            storage.findAllPlayers().forEach(System.out::println);
            storage.findAllScores().forEach(System.out::println);
        }
    }
}
