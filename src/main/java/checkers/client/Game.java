package checkers.client;

import checkers.Dimensions2D;
import checkers.MoveResult;
import checkers.Piece;
import checkers.Point;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Class handles all the graphical content of the game.
 *  Creates board of set type and store pieces. Method in
 *  this class provides a graphical gameplay
 *
 * @author Adam Zeid
 * */
public class Game {
    private static final int tileSize = 50; // Size of a single tile
    private Tile[][] board;

    private final Group tileGroup =
        new Group(); // JavaFX group that contains all tiles
    private final Group pieceGroup =
        new Group(); // JavaFX group that contains all pieces

    private Client client;
    private List<ClientPiece> pieces;

    public Game() {
        client = new Client();
    }

    /**
     * Sets pieces at right tiles and returns board with
     * pieces.
     *
     * @return root witch is actual content of the scene.
     */
    private Parent initialiseContent() {
        Pane root = new Pane();

        final Dimensions2D dimensions = client.getBoardSize();
        root.setPrefSize(dimensions.width * tileSize,
                         dimensions.height * tileSize);
        root.getChildren().addAll(tileGroup, pieceGroup);

        board = new Tile[dimensions.width][dimensions.height];

        for(int y = 0; y < dimensions.height; y++) {
            for(int x = 0; x < dimensions.width; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y, tileSize);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);
            }
        }

        pieces = client.listPieces();
        for(ClientPiece piece: pieces) {
            piece.initialise(tileSize);
            final Point position = piece.getPosition();
            board[position.x][position.y].setPiece(piece);
            pieceGroup.getChildren().add(piece);
        }

        return root;
    }

    private int toBoard(double pixel) {
        return (int)(pixel + tileSize / 2) / tileSize;
    }

    /**
     * Checks if made move is legal and if it is returns what type of move
     * was made.
     *
     * @param piece a piece that player moved
     * @param newX vertical position of piece after move was made
     * @param newY horizontal position of piece after move was made
     * @return A tape of move that was made.
     */
    private MoveResult tryMove(Piece piece, int newX, int newY) {
        // if (board[newX][newY].hasPiece() || (newX + newY) % 2 == 0) {
        //     return MoveResult.none;
        // }

        // int x0 = toBoard(piece.getOldX());
        // int y0 = toBoard(piece.getOldY());

        // // check what kind of move was made
        // if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getType().moveDir) {
        //     return MoveResult.normal;
        // } else if (Math.abs(newX - x0) == 2 && newY - y0 == piece.getType().moveDir * 2) {
        //     int x1 = x0 + (newX - x0) / 2;
        //     int y1 = y0 + (newY - y0) / 2;

        //     if(board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()) {
        //         return MoveResult.kill;
        //     }
        // }

        return MoveResult.ok;
    }

    /**
     * Checks if the piece after move should convert to the king,
     * if yes changes it to a king.
     *
     * @param piece a piece that player moved
     * @param newX vertical position of piece after move was made
     * @param newY horizontal position of piece after move was made
     */
    public void setKing(Piece piece, int newX, int newY) {
        // if(piece.getType() == checkers.server.Piece.Kind.WHITE && newY < 1) {
        //     board[newX][newY].setPiece(null);
        //     pieceGroup.getChildren().remove(piece);

        //     Piece whiteKing = new Piece(checkers.server.Piece.Kind.WHITE_KING, newX, newY, tileSize);
        //     board[newX][newY].setPiece(whiteKing);
        //     pieceGroup.getChildren().add(whiteKing);
        // } else if(piece.getType() == checkers.server.Piece.Kind.RED && newY > height - 2){
        //     board[newX][newY].setPiece(null);
        //     pieceGroup.getChildren().remove(piece);

        //     Piece redKing = new Piece(checkers.server.Piece.Kind.RED_KING, newX, newY, tileSize);
        //     board[newX][newY].setPiece(redKing);
        //     pieceGroup.getChildren().add(redKing);
        // }
    }

    /**
     * Makes pieces movable and changes the board after every legal move.
     *
     * @param type type of piece player moved
     * @param x vertical position of the piece
     * @param y horizontal position of the piece
     * @return a new piece that is effect of the done move.
     */
    // private checkers.client.Piece makePiece(checkers.server.Piece serverPiece) {
    //     checkers.client.Piece piece =
    //         new checkers.client.Piece(serverPiece, tileSize);

    //      Here I will try to provide a light up tiles of possible moves for single piece
    //        piece.setOnMouseEntered(e -> {
    //            int x0 = toBoard(piece.getLayoutX());
    //            int y0 = toBoard(piece.getLayoutY());
    //
    //            board[x0 + 1][y0 + 1].setFill(Color.BLUE);
    //            board[x0 - 1][y0 + 1].setFill(Color.BLUE);
    //        });
    //
    //        piece.setOnMouseExited(e -> {
    //            int x0 = toBoard(piece.getLayoutX());
    //            int y0 = toBoard(piece.getLayoutY());
    //
    //            board[x0 + 1][y0 + 1].setFill(Color.GREEN);
    //            board[x0 - 1][y0 + 1].setFill(Color.GREEN);
    //        });

    // piece.setOnMouseReleased(e -> {
    //     int newX = toBoard(piece.getLayoutX());
    //     int newY = toBoard(piece.getLayoutY());

    //     MoveResult result = tryMove(piece, newX, newY);
    //     setKing(piece, newX, newY);

    //     int x0 = toBoard(piece.getOldX());
    //     int y0 = toBoard(piece.getOldY());

    //     switch (result) {
    //         case none -> piece.abortMove();
    //         case normal -> {
    //             piece.move(newX, newY);
    //             board[x0][y0].setPiece(null);
    //             board[newX][newY].setPiece(piece);
    //         }
    //         // Beating is now possible
    //         case kill -> {
    //             piece.move(newX, newY);
    //             board[x0][y0].setPiece(null);
    //             board[newX][newY].setPiece(piece);

    //             int beatenX = (x0 - newX) / 2;
    //             int beatenY = (y0 - newY) / 2;
    //             Piece beatenPiece = board[x0 - beatenX][y0 - beatenY].getPiece();
    //             board[x0 - beatenX][y0 - beatenY].setPiece(null);
    //             pieceGroup.getChildren().remove(beatenPiece);
    //         }
    //     }
    // });

    //     return piece;
    // }

    public void run() {
        final boolean connected = client.connect();
        if(!connected) {
            // TODO: Handle error.
            return;
        }

        final Stage stage = new Stage();
        Parent parent = initialiseContent();
        Scene scene = new Scene(parent);
        stage.setTitle("Checkers");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
