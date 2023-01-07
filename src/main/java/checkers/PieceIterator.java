package checkers;

import checkers.Piece;
import java.util.Iterator;

public class PieceIterator<T extends Piece> implements Iterator<Piece> {
    private Iterator<T> iterator;

    public PieceIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Piece next() {
        return iterator.next();
    }
}
