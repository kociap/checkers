package checkers;

import java.util.Iterator;

public class PieceIterable implements Iterable<Piece> {
    private final Iterator<Piece> iterator;

    public PieceIterable(Iterator<Piece> iterator) {
        this.iterator = iterator;
    }

    public Iterator<Piece> iterator() {
        return iterator;
    }
}