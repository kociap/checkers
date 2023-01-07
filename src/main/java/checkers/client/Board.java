package checkers.client;

import checkers.Dimensions2D;
import checkers.Piece;
import checkers.Point;
import checkers.server.ServerPiece;
import java.util.ArrayList;
import java.util.List;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Board extends Pane {
    private final Group tileGroup =
        new Group(); // JavaFX group that contains all tiles
    private final Group pieceGroup =
        new Group(); // JavaFX group that contains all pieces
    private List<Tile> tiles = new ArrayList<>();
    private int size = 0;
    private RequestService requester;
    private Dimensions2D dimensions = null;

    public Board(RequestService requester) {
        this.requester = requester;
        getChildren().addAll(tileGroup, pieceGroup);
    }

    public void initialise(final Dimensions2D dimensions, final int size) {
        this.size = size;
        this.dimensions = dimensions;

        tileGroup.getChildren().clear();
        pieceGroup.getChildren().clear();

        tiles = new ArrayList<>();
        setPrefSize(dimensions.width * size, dimensions.height * size);
        for(int y = 0; y < dimensions.height; y++) {
            for(int x = 0; x < dimensions.width; x++) {
                final Tile tile = new Tile(x, y, size);
                tile.setColor((x + y) % 2 == 0 ? ColorPalette.squareLight
                                               : ColorPalette.squareDark);
                tiles.add(tile);
                tileGroup.getChildren().add(tile);

                // Forward mouse events from the tile to the piece that
                // currently sits on this tile.

                tile.setOnMousePressed(e -> {
                    final ClientPiece piece = tile.getPiece();
                    if(piece != null) {
                        Event.fireEvent(piece, e);
                    }
                });

                tile.setOnMouseDragged(e -> {
                    final ClientPiece piece = tile.getPiece();
                    if(piece != null) {
                        Event.fireEvent(piece, e);
                    }
                });

                tile.setOnMouseReleased(e -> {
                    final ClientPiece piece = tile.getPiece();
                    if(piece != null) {
                        Event.fireEvent(piece, e);
                    }
                });
            }
        }
    }

    public void addPiece(final ClientPiece piece) {
        pieceGroup.getChildren().add(piece);

        final Point position = piece.getPosition();
        getTileAt(position).setPiece(piece);

        piece.resize(size);
        relocatePiece(piece);

        piece.setOnMousePressed(e -> {
            // Move the piece to the front of the scenegraph in order to make it
            // appear above all other pieces while it's being dragged.
            piece.toFront();
            piece.relocate(e.getSceneX() - size / 2, e.getSceneY() - size / 2);
        });

        piece.setOnMouseDragged(e -> {
            piece.relocate(e.getSceneX() - size / 2, e.getSceneY() - size / 2);
        });

        piece.setOnMouseReleased(e -> {
            final Point targetPosition =
                getCoordinatesUnderCursor(e.getSceneX(), e.getSceneY());
            System.out.println("target " + targetPosition.x + " " +
                               targetPosition.y);
            requester.requestMove(piece.getID(), targetPosition);
        });
    }

    public void handleRequestMove(final int pieceID, final Point position) {
        final ClientPiece piece = findPieceWithID(pieceID);
        if(piece == null) {
            return;
        }

        if(position.x == -1 || position.y == -1) {
            relocatePiece(piece);
        } else {
            final Tile prevTile = getTileAt(piece.getPosition());
            prevTile.setPiece(null);
            piece.setPosition(position);
            relocatePiece(piece);
            final Tile nextTile = getTileAt(piece.getPosition());
            nextTile.setPiece(piece);
        }
    }

    private ClientPiece findPieceWithID(final int pieceID) {
        for(final Tile tile: tiles) {
            final ClientPiece piece = tile.getPiece();
            if(piece != null && piece.getID() == pieceID) {
                return piece;
            }
        }
        return null;
    }

    private Tile getTileAt(final Point position) {
        return tiles.get(dimensions.width * position.x + position.y);
    }

    private void setTileAt(final Point position, final Tile tile) {
        tiles.set(dimensions.width * position.x + position.y, tile);
    }

    private Point getCoordinatesUnderCursor(final double x, final double y) {
        final int cx = (int)(x / size);
        final int cy = (int)(y / size);
        return new Point(cx, cy);
    }

    private void relocatePiece(final ClientPiece piece) {
        final Point position = piece.getPosition();
        piece.relocate(size * position.x, size * position.y);
    }
}
