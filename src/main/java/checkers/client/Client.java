package checkers.client;

import checkers.CommandParser;
import checkers.Dimensions2D;
import checkers.Piece;
import checkers.PieceIterator;
import checkers.Point;
import checkers.SocketWrapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Client {
    private SocketWrapper server;

    public PlayerInformation connect() {
        try {
            server = new SocketWrapper(new Socket("localhost", 8080));
        } catch(Exception e) { return null; }

        final String response = read();
        if(response == null) {
            return null;
        }

        final CommandParser parser = new CommandParser(response);

        if(!parser.match("hello")) {
            return null;
        }

        final Piece.Color color = parser.matchPieceColor();
        return new PlayerInformation(color);
    }

    public List<ClientPiece> listPieces() {
        final PrintWriter writer = server.getWriter();
        writer.format("list-pieces;");
        final String response = read();
        if(response == null) {
            return null;
        }

        final CommandParser parser = new CommandParser(response);

        if(!parser.match("list-pieces")) {
            return null;
        }

        List<ClientPiece> pieces = new ArrayList<>();
        while(true) {
            if(!parser.match("(")) {
                break;
            }

            // We are parsing a piece structure.
            final int ID = parser.matchInteger();
            // TODO: Theoretically invalid to ignore.
            parser.match(",");
            final Piece.Kind kind = parser.matchPieceKind();
            // TODO: Theoretically invalid to ignore.
            parser.match(",");
            final Piece.Color color = parser.matchPieceColor();
            // TODO: Theoretically invalid to ignore.
            parser.match(",");
            final int x = parser.matchInteger();
            // TODO: Theoretically invalid to ignore.
            parser.match(",");
            final int y = parser.matchInteger();
            pieces.add(new ClientPiece(ID, kind, color, new Point(x, y)));

            // TODO: Theoretically invalid to ignore.
            parser.match(")");

            if(!parser.match(",")) {
                break;
            }
        }
        return pieces;
    }

    public List<Point> listMoves(Piece piece) {
        return new ArrayList<>();
    }

    public Dimensions2D getBoardSize() {
        final PrintWriter writer = server.getWriter();
        writer.format("list-game-properties;");
        final String response = read();
        if(response == null) {
            return null;
        }

        final CommandParser parser = new CommandParser(response);

        if(!parser.match("list-game-properties")) {
            return null;
        }

        final int width = parser.matchInteger();
        parser.match(",");
        final int height = parser.matchInteger();
        return new Dimensions2D(width, height);
    }

    private String read() {
        final BufferedReader reader = server.getReader();
        try {
            return reader.readLine();
        } catch(Exception e) { return null; }
    }
}
