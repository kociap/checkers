package checkers.server;

import checkers.Piece;
import checkers.utility.CommandBuilder;
import checkers.utility.CommandParser;
import checkers.utility.Dimensions2D;
import checkers.utility.PieceIterable;
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
                final String command = getListPiecesCommand();
                socket.write(command);
                continue;
            }

            if(parser.match("list-game-properties")) {
                final String command = getListGamePropertiesCommand();
                socket.write(command);
                continue;
            }

            System.out.println("unknown command \"" + line + "\"");
        }
    }

    public void sendCommand(String command) {
        socket.write(command);
    }

    private String getListPiecesCommand() {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("list-pieces");
        for(Piece p: server.listPieces()) {
            builder.parameter(p);
        }
        return builder.finalise();
    }

    private String getListGamePropertiesCommand() {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("list-game-properties");
        final Dimensions2D boardSize = server.getBoardSize();
        builder.parameter(boardSize.width).parameter(boardSize.height);
        return builder.finalise();
    }
}
