package checkers.utility;

import checkers.Piece;

public class CommandParser {
    private String buffer;
    private int i = 0;

    public CommandParser(String buffer) {
        this.buffer = buffer;
    }

    public void skipWhitespace() {
        while(Character.isWhitespace(buffer.charAt(i))) {
            i += 1;
        }
    }

    public boolean match(String string) {
        skipWhitespace();
        for(int k = 0; k < string.length(); k += 1) {
            if(buffer.charAt(i + k) != string.charAt(k)) {
                return false;
            }
        }
        i += string.length();
        return true;
    }

    public int matchInteger() {
        skipWhitespace();
        int end = i;
        while(Character.isDigit(buffer.charAt(end))) {
            end += 1;
        }
        final String substring = buffer.substring(i, end);
        final int result = Integer.parseInt(substring);
        i += substring.length();
        return result;
    }

    public Piece.Color matchPieceColor() {
        final int value = matchInteger();
        return Piece.Color.values()[value];
    }

    public Piece.Kind matchPieceKind() {
        final int value = matchInteger();
        return Piece.Kind.values()[value];
    }
}
