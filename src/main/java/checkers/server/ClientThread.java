package checkers.server;

import checkers.Piece;
import checkers.utility.CommandBuilder;
import checkers.utility.CommandParser;
import checkers.utility.Dimensions2D;
import checkers.utility.PieceIterable;
import checkers.utility.Point;
import checkers.utility.SocketWrapper;

public class ClientThread extends Thread {
    private Server server;
    private SocketWrapper socket;

    public ClientThread(Server server, SocketWrapper socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        while(true) {
            final String line = socket.read();
            if(line == null) {
                break;
            }

            final CommandParser parser = new CommandParser(line);

            if(parser.match("list-pieces")) {
                server.notifyCommandListPieces(this);
                continue;
            }

            if(parser.match("list-game-properties")) {
                server.notifyCommandListGameProperties(this);
                continue;
            }

            if(parser.match("list-moves")) {
                final int pieceID = parser.matchInteger();
                server.notifyCommandListMoves(this, pieceID);
                continue;
            }

            if(parser.match("move")) {
                final int pieceID = parser.matchInteger();
                parser.match(",");
                final int x = parser.matchInteger();
                parser.match(",");
                final int y = parser.matchInteger();
                server.notifyCommandMove(this, pieceID, x, y);
                continue;
            }

            System.out.println("unknown command \"" + line + "\"");
        }
    }

    public void sendCommand(String command) {
        socket.write(command);
    }
}
