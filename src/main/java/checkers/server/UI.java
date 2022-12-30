package checkers.server;

import checkers.server.modes.EnglishDraughts;
import checkers.utility.Dimensions2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UI {
    private final Stage window = new Stage();
    private final Coordinator coordinator;

    public UI(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public void show() {
        VBox menuVBox = new VBox();
        menuVBox.setAlignment(Pos.CENTER);

        Button button8x8 = new Button("English Draughts 8x8 game");
        button8x8.setOnAction(e -> {
            coordinator.notifyEngineSelected(
                new EnglishDraughts(new Dimensions2D(8, 8)));
            setViewWaitingForClientsToConnect();
        });

        Button button10x10 = new Button("English Draughts 10x10 game");
        button10x10.setOnAction(e -> {
            coordinator.notifyEngineSelected(
                new EnglishDraughts(new Dimensions2D(10, 10)));
            setViewWaitingForClientsToConnect();
        });

        Button button12x12 = new Button("English Draughts 12x12 game");
        button12x12.setOnAction(e -> {
            coordinator.notifyEngineSelected(
                new EnglishDraughts(new Dimensions2D(12, 12)));
            setViewWaitingForClientsToConnect();
        });

        menuVBox.setSpacing(5);
        menuVBox.getChildren().addAll(button8x8, button10x10, button12x12);

        Scene menu = new Scene(menuVBox, 400, 400);
        window.setTitle("Checkers");
        window.setResizable(false);
        window.setScene(menu);
        window.show();
    }

    public void notifyClientConnected() {
        // TODO: Implement.
        //       Change state of the user interface to let the user of
        //       the server know a player has connected.
    }

    private void setViewWaitingForClientsToConnect() {
        // TODO: Prevent window from resizing, center and enlarge text.
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Text text = new Text("Waiting for players to connect");
        vbox.getChildren().addAll(text);
        Scene scene = new Scene(vbox);
        window.setScene(scene);
    }

    private void setViewGameInProgress() {
        // TODO: Prevent window from resizing, center and enlarge text.
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Text text = new Text("Game in progress");
        vbox.getChildren().addAll(text);
        Scene scene = new Scene(vbox);
        window.setScene(scene);
    }
}
