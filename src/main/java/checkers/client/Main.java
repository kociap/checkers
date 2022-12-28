package checkers.client;

import javafx.application.Platform;

public class Main {
    public static void main(String[] args) {
        Platform.startup(() -> {
            Game game = new Game();
            game.run();
        });
    }
}
