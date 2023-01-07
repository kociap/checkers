package checkers.client;

import checkers.Point;

public interface RequestService {
    void requestSize();
    void requestListPieces();
    void requestMove(int pieceID, Point position);
}
