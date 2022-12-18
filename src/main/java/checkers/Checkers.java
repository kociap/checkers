package checkers;

import checkers.server.MoveResult;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import checkers.server.Server;
import checkers.modes.*;

/** Class handles all the graphical content of the game.
 *  Creates board of set type and store pieces. Method in
 *  this class provides a graphical gameplay
 *
 * @author Adam Zeid
 * */
public class Checkers {

    public static final int tileSize = 50; // Size of the single tile
    private final int width; // Board's width
    private final int height; // Board's height
    private final Tile[][] board; // Board witch is a 2D array of tiles

    /**
     * Sets the height and width of the board and creates the board.
     *
     * @param width number of horizontal tiles
     * @param height number of vertical tiles
     */
    public Checkers(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new Tile[width][height];
    }

    private final Group tileGroup = new Group(); // JavaFX group that contains all tiles
    private final Group pieceGroup = new Group(); // JavaFX group that contains all pieces
    private final Server server = new Server();

    /**
     * Sets pieces at right tiles and returns board with
     * pieces.
     *
     * @return root witch is actual content of the scene.
     */
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
                if(y <= width / 2 - 2 && (x + y) % 2 != 0) {
                    piece = makePiece(PieceType.RED, x, y);
                }
                if(y >= width / 2 + 1 && (x + y) % 2 != 0) {
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

    /**
     * Checks if the piece after move should convert to the king,
     * if yes changes it to a king.
     *
     * @param piece a piece that player moved
     * @param newX vertical position of piece after move was made
     * @param newY horizontal position of piece after move was made
     */
    public void setKing(Piece piece, int newX, int newY) {

        if(piece.getType() == PieceType.WHITE && newY < 1) {
            board[newX][newY].setPiece(null);
            pieceGroup.getChildren().remove(piece);

            Piece whiteKing = new Piece(PieceType.WHITE_KING, newX, newY);
            board[newX][newY].setPiece(whiteKing);
            pieceGroup.getChildren().add(whiteKing);
        } else if(piece.getType() == PieceType.RED && newY > height - 2){
            board[newX][newY].setPiece(null);
            pieceGroup.getChildren().remove(piece);

            Piece redKing = new Piece(PieceType.RED_KING, newX, newY);
            board[newX][newY].setPiece(redKing);
            pieceGroup.getChildren().add(redKing);
        }
    }

    /**
     * Makes pieces movable and changes the board after every legal move.
     *
     * @param type type of piece player moved
     * @param x vertical position of the piece
     * @param y horizontal position of the piece
     * @return a new piece that is effect of the done move.
     */
    private Piece makePiece(PieceType type, int x, int y) {
        Piece piece = new Piece(type, x, y);

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

        piece.setOnMouseReleased(e -> {
            int newX = toBoard(piece.getLayoutX());
            int newY = toBoard(piece.getLayoutY());

            MoveResult result = tryMove(piece, newX, newY);
            setKing(piece, newX, newY);

            int x0 = toBoard(piece.getOldX());
            int y0 = toBoard(piece.getOldY());

            switch (result) {
                case none -> piece.abortMove();
                case normal -> {
                    piece.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);
                }
                // Beating is now possible
                case kill -> {
                    piece.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);

                    int beatenX = (x0 - newX) / 2;
                    int beatenY = (y0 - newY) / 2;
                    Piece beatenPiece = board[x0 - beatenX][y0 - beatenY].getPiece();
                    board[x0 - beatenX][y0 - beatenY].setPiece(null);
                    pieceGroup.getChildren().remove(beatenPiece);
                }
            }
        });

        return piece;
    }

    /**
     * Simple void that runs the game. It makes UI visible.
     */
    public void run() {
        final Stage stage = new Stage();
        Scene scene = new Scene(createContent());
        stage.setTitle("Checkers");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * UI manu that sets what type of checkers will be played
     */
    public void menu() {
        final Stage stageMenu = new Stage();
        VBox menuVBox = new VBox();
        menuVBox.setAlignment(Pos.CENTER);

        Scene menu = new Scene(menuVBox,400, 400);

        Button button8x8 = new Button("New 8x8 game");
        button8x8.setOnAction(e -> {
            Checkers game = new Checkers(8,8);
            game.run();
            stageMenu.close();
            this.server.setEngine(new EnglishDraughts8x8Engine());
        });

        // Same rules as EnglishDraughts different board 10x10 canadian variant
        Button button10x10 = new Button("New 10x10 game");
        button10x10.setOnAction(e -> {
            Checkers game = new Checkers(10,10);
            game.run();
            stageMenu.close();
//            this.server.setEngine(new EnglishDraughts8x8Engine());
        });

        // Same rules as EnglishDraughts different board 12x12 canadian variant
        Button button12x12 = new Button("New 12x12 game");
        button12x12.setOnAction(e -> {
            Checkers game = new Checkers(12,12);
            game.run();
            stageMenu.close();
//            this.server.setEngine(new EnglishDraughts8x8Engine());
        });

        menuVBox.setSpacing(5);
        menuVBox.getChildren().addAll(button8x8, button10x10, button12x12);

        stageMenu.setTitle("Checkers");
        stageMenu.setResizable(false);
        stageMenu.setScene(menu);
        stageMenu.show();
    }

    public static void main(String[] args) {
        Platform.startup(() -> {
            Checkers newGame = new Checkers(1,1);
            newGame.menu();
        });
    }
}
