package checkers.client;

import checkers.Piece;
import checkers.server.ServerPiece;
import checkers.utility.Dimensions2D;
import checkers.utility.Point;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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

    private List<ClientPiece> pieces = new ArrayList<>();
    private Client client;

    public Game(Client client) {
        this.client = client;
    }

    /**
     * Sets pieces at right tiles and returns board with
     * pieces.
     *
     * @return root witch is actual content of the scene.
     */
    private Parent initialiseContent() {
        Pane root = new Pane();

        // TODO: Move the below code into command handlers.

        final Dimensions2D dimensions = new Dimensions2D(8, 8);
        root.setPrefSize(dimensions.width * tileSize,
                         dimensions.height * tileSize);
        root.getChildren().addAll(tileGroup, pieceGroup);

        board = new Tile[dimensions.width][dimensions.height];

        int ID = 0;
        for(int y = 0; y < dimensions.height; y++) {
            for(int x = 0; x < dimensions.width; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y, tileSize);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);

                ClientPiece clientPiece;
                ServerPiece serverPiece;

                if(y <= (dimensions.height / 2 - 2) && (x + y) % 2 != 0) {
                    Point point = new Point(x, y);
                    // TODO Have to find the way to put kings on the board
                    serverPiece = new ServerPiece(ID, point, Piece.Color.black, Piece.Kind.pawn);
                    clientPiece = makePiece(serverPiece);
                    pieces.add(clientPiece);
                    ID++;
                }
                if(y >= (dimensions.height / 2 + 1) && (x + y) % 2 != 0) {
                    Point point = new Point(x, y);
                    // TODO Have to find the way to put kings on the board
                    serverPiece = new ServerPiece(ID, point, Piece.Color.white, Piece.Kind.pawn);
                    clientPiece = makePiece(serverPiece);
                    pieces.add(clientPiece);
                    ID++;
                }
            }
        }

        // pieces = client.listPieces();
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
     * @param clientPiece a piece that player moved
     * @param newX vertical position of piece after move was made
     * @param newY horizontal position of piece after move was made
     * @return A tape of move that was made.
     */
    private String tryMove(ClientPiece clientPiece, int newX, int newY) {
        int moveDir = 0;

         if (board[newX][newY].hasPiece() || (newX + newY) % 2 == 0) {
             return "none";
         }

         int x0 = toBoard(clientPiece.getOldX());
         int y0 = toBoard(clientPiece.getOldY());
         if(clientPiece.getKind() == Piece.Kind.pawn && clientPiece.getColor() == Piece.Color.black) {
             moveDir = 1;
         }
         if(clientPiece.getKind() == Piece.Kind.pawn && clientPiece.getColor() == Piece.Color.white) {
             moveDir = -1;
         }

         // check what kind of move was made
         if (Math.abs(newX - x0) == 1 && newY - y0 == moveDir) {
             return "normal";
         } else if (Math.abs(newX - x0) == 2 && newY - y0 == moveDir) {
             int x1 = x0 + (newX - x0) / 2;
             int y1 = y0 + (newY - y0) / 2;

             if(board[x1][y1].hasPiece() && board[x1][y1].getPiece().getKind() != clientPiece.getKind()) {
                return "beat";
             }
         }
         return "none";
    }

    /**
     * Checks if the piece after move should convert to the king,
     * if yes changes it to a king.
     *
     * @param piece a piece that player moved
     * @param newX vertical position of piece after move was made
     * @param newY horizontal position of piece after move was made
     */
    public void setKing(ClientPiece piece, int newX, int newY) {
         if(piece.getColor() == Piece.Color.white && newY < 1) {
             board[newX][newY].setPiece(null);
             pieceGroup.getChildren().remove(piece);
             pieces.remove(piece);

             Point point = new Point(newX, newY);
             ClientPiece whiteKing = new ClientPiece(piece.getID(), Piece.Kind.king, Piece.Color.white, point);
             board[newX][newY].setPiece(whiteKing);
             pieceGroup.getChildren().add(whiteKing);
             pieces.add(whiteKing);
         } else if(piece.getColor() == Piece.Color.black && newY > 8 - 2){
             board[newX][newY].setPiece(null);
             pieceGroup.getChildren().remove(piece);
             pieces.remove(piece);

             Point point = new Point(newX, newY);
             ClientPiece blackKing = new ClientPiece(piece.getID(), Piece.Kind.king, Piece.Color.black, point);
             board[newX][newY].setPiece(blackKing);
             pieceGroup.getChildren().add(blackKing);
             pieces.add(blackKing);
         }
    }

    /**
     * Makes pieces movable and changes the board after every legal move.
     *
     * @param serverPiece server interpretation of the piece on the board
     * @return a new piece that is effect of the done move.
     */
     private ClientPiece makePiece(ServerPiece serverPiece) {
         ClientPiece clientPiece = new ClientPiece(serverPiece.getID(), serverPiece.getKind(), serverPiece.getColor(), serverPiece.getPosition());
//
////          Here I will try to provide a light up tiles of possible moves for single piece
//            clientPiece.setOnMouseEntered(e -> {
//                int x0 = toBoard(clientPiece.getLayoutX());
//                int y0 = toBoard(clientPiece.getLayoutY());
//
//                board[x0 + 1][y0 + 1].setFill(Color.BLUE);
//                board[x0 - 1][y0 + 1].setFill(Color.BLUE);
//            });
//
//            clientPiece.setOnMouseExited(e -> {
//                int x0 = toBoard(clientPiece.getLayoutX());
//                int y0 = toBoard(clientPiece.getLayoutY());
//
//                board[x0 + 1][y0 + 1].setFill(Color.GREEN);
//                board[x0 - 1][y0 + 1].setFill(Color.GREEN);
//            });

     clientPiece.setOnMouseReleased(e -> {
         int newX = toBoard(clientPiece.getLayoutX());
         int newY = toBoard(clientPiece.getLayoutY());

         String result = tryMove(clientPiece, newX, newY);
         setKing(clientPiece, newX, newY);

         int x0 = toBoard(clientPiece.getOldX());
         int y0 = toBoard(clientPiece.getOldY());

         switch (result) {
             case "none" -> clientPiece.abortMove();
             case "normal" -> {
                 Point point = new Point(newX, newY);
                 clientPiece.move(point);
                 board[x0][y0].setPiece(null);
                 board[newX][newY].setPiece(clientPiece);
             }
             // TODO I don't know why it doesn't work :C
             case "beat" -> {
                 Point point = new Point(newX, newY);
                 clientPiece.move(point);
                 board[x0][y0].setPiece(null);
                 board[newX][newY].setPiece(clientPiece);

                 int beatenX = (x0 - newX) / 2;
                 int beatenY = (y0 - newY) / 2;
                 ClientPiece beatenPiece = board[x0 - beatenX][y0 - beatenY].getPiece();
                 board[x0 - beatenX][y0 - beatenY].setPiece(null);
                 pieces.remove(beatenPiece);
                 pieceGroup.getChildren().remove(beatenPiece);
             }
         }
     });

         return clientPiece;
     }

    public void run() {
        final Stage stage = new Stage();
        Parent parent = initialiseContent();
        Scene scene = new Scene(parent);
        stage.setTitle("Checkers");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
