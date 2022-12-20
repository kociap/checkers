package checkers;

import checkers.client.Menu;
import javafx.application.Platform;

public class Main {
    public static void main(String[] args) {
        Platform.startup(() -> {
            Menu menu = new Menu();
            menu.show();
        });
    }
}