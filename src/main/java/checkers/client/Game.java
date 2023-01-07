package checkers.client;

import checkers.Dimensions2D;
import checkers.Point;
import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Game {
    private static final int defaultTileSize = 50; // Size of a single tile

    private RequestService requester;
    private final Board board;
    private final Stage stage = new Stage();

    public Game(RequestService requester) {
        this.requester = requester;
        this.board = new Board(requester);
    }

    public void handleRequestSize(final Dimensions2D dimensions) {
        board.initialise(dimensions, defaultTileSize);
        setViewBoard();
    }

    public void handleRequestListPieces(final List<ClientPiece> pieces) {
        for(final ClientPiece piece: pieces) {
            board.addPiece(piece);
        }
    }

    public void handleRequestMove(final int pieceID, final Point position) {
        board.handleRequestMove(pieceID, position);
    }

    public void handleRequestTake(final int pieceID) {
        board.handleRequestTake(pieceID);
    }

    public void handleRequestPromote(final int pieceID) {
        board.handleRequestPromote(pieceID);
    }

    public void run() {
        requester.requestSize();
        requester.requestListPieces();

        stage.setTitle("Checkers");
        stage.setResizable(false);
        stage.show();
    }

    private void setViewBoard() {
        final Scene scene = new Scene(board);
        stage.setScene(scene);
    }
}
