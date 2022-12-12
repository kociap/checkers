package com.example.checkers;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class CheckersApp extends Application {
    public static final int tileSize = 100;
    public static final int width = 8;
    public static final int height = 8;

    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(width * tileSize, height * tileSize);
        root.getChildren().addAll(tileGroup, pieceGroup);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);

                tileGroup.getChildren().add(tile);
            }
        }

        return root;
    }

    @Override
    public void start(Stage primatyStage) throws Exception {
        Scene scene = new Scene(createContent());
        primatyStage.setTitle("Checkers");
        primatyStage.setScene(scene);
        primatyStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
