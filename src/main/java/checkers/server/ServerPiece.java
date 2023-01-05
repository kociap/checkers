package checkers.server;

import checkers.Piece;
import checkers.utility.Point;

public class ServerPiece implements Piece {
    private final int ID;
    private Point position;
    private final Color color;
    private Kind kind;

    public ServerPiece(int ID, Point position, Color color, Kind kind) {
        this.ID = ID;
        this.position = position;
        this.color = color;
        this.kind = kind;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Kind getKind() {
        return kind;
    }

    public void move(Point point) {
        position = point;
    }

    public void promote() {
        kind = Kind.king;
    }
}
