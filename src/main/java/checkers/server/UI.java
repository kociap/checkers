package checkers.server;

import checkers.Dimensions2D;
import checkers.server.modes.EnglishDraughts;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UI {
    private final Stage window = new Stage();
    private final Server server;

    public UI(Server server) {
        this.server = server;
    }

    public void show() {
        VBox menuVBox = new VBox();
        menuVBox.setAlignment(Pos.CENTER);

        Button button8x8 = new Button("English Draughts 8x8 game");
        button8x8.setOnAction(e -> {
            server.notifyEngineSelected(
                new EnglishDraughts(new Dimensions2D(8, 8)));
            setViewWaitingForClientsToConnect();
        });

        Button button10x10 = new Button("English Draughts 10x10 game");
        button10x10.setOnAction(e -> {
            server.notifyEngineSelected(
                new EnglishDraughts(new Dimensions2D(10, 10)));
            setViewWaitingForClientsToConnect();
        });

        Button button12x12 = new Button("English Draughts 12x12 game");
        button12x12.setOnAction(e -> {
            server.notifyEngineSelected(
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

    public void notifyClientWhiteConnected() {}

    public void notifyClientBlackConnected() {}

    public void notifyClientConnectComplete() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Text text = new Text("Game in progress.");
        vbox.getChildren().addAll(text);
        Scene scene = new Scene(vbox);
        window.setScene(scene);
    }

    private void setViewWaitingForClientsToConnect() {
        // TODO: Prevent window from resizing, center and enlarge text.
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Text text1 = new Text("Waiting for white to connect.");
        Text text2 = new Text("Waiting for black to connect.");
        vbox.getChildren().addAll(text1, text2);
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
