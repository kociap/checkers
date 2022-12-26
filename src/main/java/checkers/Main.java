package checkers;

import javafx.application.Platform;

public class Main {
    public static void main(String[] args) {
        Platform.startup(() -> {
            checkers.server.Coordinator serverCoordinator =
                new checkers.server.Coordinator();
            serverCoordinator.run();
        });
    }
}