package checkers.client;

import checkers.modes.*;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Menu {
    public void show() {
        final Stage stageMenu = new Stage();
        VBox menuVBox = new VBox();
        menuVBox.setAlignment(Pos.CENTER);

        Scene menu = new Scene(menuVBox, 400, 400);

        Button button8x8 = new Button("New 8x8 game");
        button8x8.setOnAction(e -> {
            Game game = new Game();
            game.run(new EnglishDraughts8x8());
            stageMenu.close();
        });

        Button button10x10 = new Button("New 10x10 game");
        button10x10.setOnAction(e -> {
            Game game = new Game();
            game.run(new EnglishDraughts10x10());
            stageMenu.close();
        });

        Button button12x12 = new Button("New 12x12 game");
        button12x12.setOnAction(e -> {
            Game game = new Game();
            game.run(new EnglishDraughts12x12());
            stageMenu.close();
        });

        menuVBox.setSpacing(5);
        menuVBox.getChildren().addAll(button8x8, button10x10, button12x12);

        stageMenu.setTitle("Checkers");
        stageMenu.setResizable(false);
        stageMenu.setScene(menu);
        stageMenu.show();
    }
}
