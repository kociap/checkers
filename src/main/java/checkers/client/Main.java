package checkers.client;

import javafx.application.Platform;

public class Main {
    public static void main(String[] args) {
        Platform.startup(() -> {
            Client client = new Client();
            client.run();
        });
    }
}
