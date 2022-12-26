package checkers.client;

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

// Socket Commands
// Grammar
//
// comment -> # string
// command -> command-name [parameter[,parameter]...];
// parameter -> integer | structure
// structure -> (integer[,integer]...)
//
// Server-issued commands
//
// # hello
// # Issued when a client connects and the server accepts the connection.
// # The client must wait for hello before sending any data over the socket.
// hello;
// # bye
// # Issued when the server closes or in a response to a client's bye.
// bye;
// promote piece-id;
// # list-pieces
// # piece is a structure of ID, kind, color, x, y.
// list-pieces piece[,piece]...;
// list-moves move[,move]...;
// move result;
// list-game-properties board-width,board-height;
// # begin-turn
// # Issued only to the client whose turn has begun.
// begin-turn;
// # end-turn
// # Issued only to the client whose turn has ended.
// end-turn;
//
// Client-issued commands
//
// # bye
// # Issued when a client wishes to disconnect from the game.
// bye;
// move piece-id,x,y;
// list-pieces;
// list-moves piece-id;
// list-game-properties;
//
public class Client {
    private SocketWrapper server;

    public boolean connect() {
        try {
            server = new SocketWrapper(new Socket("localhost", 8080));
        } catch(Exception e) { return false; }

        final String response = read();
        if(response == null) {
            return false;
        }

        return response.equals("hello;");
    }

    private String getIntegerSubstring(String str, int begin) {
        int end = begin;
        while(Character.isDigit(str.charAt(end))) {
            end += 1;
        }
        return str.substring(begin, end);
    }

    public List<ClientPiece> listPieces() {
        final PrintWriter writer = server.getWriter();
        writer.format("list-pieces;");
        final String response = read();
        if(response == null) {
            return null;
        }

        if(!response.startsWith("list-pieces")) {
            return null;
        }

        List<ClientPiece> pieces = new ArrayList<>();
        // Start the loop at the first character of the parameters.
        for(int i = "list-pieces".length() + 1; i < response.length();) {
            if(response.charAt(i) != '(') {
                i += 1;
            } else {
                // We are parsing a piece structure.
                // Skip the opening parenthesis.
                i += 1;
                int ID;
                {
                    final String valueString = getIntegerSubstring(response, i);
                    ID = Integer.parseInt(valueString);
                    // Advance past the comma.
                    i += valueString.length() + 1;
                }
                Piece.Kind kind;
                {
                    final String valueString = getIntegerSubstring(response, i);
                    final int value = Integer.parseInt(valueString);
                    kind = Piece.Kind.values()[value];
                    // Advance past the comma.
                    i += valueString.length() + 1;
                }
                Piece.Color color;
                {
                    final String valueString = getIntegerSubstring(response, i);
                    final int value = Integer.parseInt(valueString);
                    color = Piece.Color.values()[value];
                    // Advance past the comma.
                    i += valueString.length() + 1;
                }
                int x = 0;
                {
                    final String valueString = getIntegerSubstring(response, i);
                    x = Integer.parseInt(valueString);
                    // Advance past the comma.
                    i += valueString.length() + 1;
                }
                int y = 0;
                {
                    final String valueString = getIntegerSubstring(response, i);
                    y = Integer.parseInt(valueString);
                    // Advance past the comma.
                    i += valueString.length() + 1;
                }

                pieces.add(new ClientPiece(ID, kind, color, new Point(x, y)));
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

        if(!response.startsWith("list-game-properties")) {
            return null;
        }

        // Start the loop at the first character of the parameters.
        // TODO: Consider adding validation to ensure all received data is valid.
        for(int i = "list-game-properties".length() + 1;
            i < response.length();) {
            if(!Character.isDigit(response.charAt(i))) {
                i += 1;
            } else {
                int width;
                {
                    final String valueString = getIntegerSubstring(response, i);
                    width = Integer.parseInt(valueString);
                    // Advance past the comma.
                    i += valueString.length() + 1;
                }
                int height;
                {
                    final String valueString = getIntegerSubstring(response, i);
                    height = Integer.parseInt(valueString);
                    // Advance past the comma.
                    i += valueString.length() + 1;
                }
                return new Dimensions2D(width, height);
            }
        }

        // This return silences the "missing return statement" warning.
        return null;
    }

    private String read() {
        final BufferedReader reader = server.getReader();
        try {
            return reader.readLine();
        } catch(Exception e) { return new String(); }
    }
}
