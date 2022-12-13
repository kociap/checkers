package checkers;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import checkers.server.Server;
import checkers.modes.*;

public class Checkers {
    public static final int tileSize = 100;
    public static final int width = 8;
    public static final int height = 8;

    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();
    private Server server = new Server();

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(width * tileSize, height * tileSize);
        root.getChildren().addAll(tileGroup, pieceGroup);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y, tileSize);

                tileGroup.getChildren().add(tile);
            }
        }

        return root;
    }

    public void run() {
        this.server.setEngine(new EnglishDraughts8x8Engine());
        final Stage stage = new Stage();
        Scene scene = new Scene(createContent());
        stage.setTitle("Checkers");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Platform.startup(() -> {
            final Checkers game = new Checkers();
            game.run();
        });
    }
}
