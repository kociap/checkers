package checkers;

import checkers.server.MoveCommand;
import checkers.server.MoveResult;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import checkers.server.Server;
import checkers.modes.*;

public class Checkers {
    public static final int tileSize = 100; // singe tile size can stay at it is
    public static final int width = 8; // TODO make manu where you can choose witch checkers wariant player want to play
    public static final int height = 8; // TODO same as previous one

    private final Tile[][] board = new Tile[width][height]; // board made from single tales
    private final Group tileGroup = new Group();
    private final Group pieceGroup = new Group();
    private final Server server = new Server();

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(width * tileSize, height * tileSize);
        root.getChildren().addAll(tileGroup, pieceGroup);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y, tileSize);
                // create board
                board[x][y] = tile;

                tileGroup.getChildren().add(tile);

                Piece piece = null;

                // add red and white pieces
                if(y <= 2 && (x + y) % 2 != 0) {
                    piece = makePiece(PieceType.RED, x, y);
                }
                if(y >= 5 && (x + y) % 2 != 0) {
                    piece = makePiece(PieceType.WHITE, x, y);
                }

                if (piece != null) {
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);
                }
            }
        }

        return root;
    }

    private int toBoard(double pixel) {
        return (int)(pixel + tileSize / 2) / tileSize;
    }

    private MoveResult tryMove(Piece piece, int newX, int newY) {
        if (board[newX][newY].hasPiece() || (newX + newY) % 2 == 0) {
            return MoveResult.none;
        }

        int x0 = toBoard(piece.getOldX());
        int y0 = toBoard(piece.getOldY());

        // check what kind of move was made
        if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getType().moveDir) {
            return MoveResult.normal;
        } else if (Math.abs(newX - x0) == 2 && newY - y0 == piece.getType().moveDir * 2) {
            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;

            if(board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()) {
                return MoveResult.kill;
            }
        }

        return MoveResult.none;
    }

    // This class affects the board after move
    private Piece makePiece(PieceType type, int x, int y) {
        Piece piece = new Piece(type, x, y);

        piece.setOnMouseReleased(e -> {
            int newX = toBoard(piece.getLayoutX());
            int newY = toBoard(piece.getLayoutY());

            MoveResult result = tryMove(piece, newX, newY);

            int x0 = toBoard(piece.getOldX());
            int y0 = toBoard(piece.getOldY());

            switch (result) {
                case none -> piece.abortMove();
                case normal -> {
                    piece.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);
                }
                // TODO need to find the way what beaten piece was and remove it
                case kill -> {

                }
            }
        });

        return piece;
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
