package checkers.server;

import checkers.utility.CommandBuilder;
import checkers.utility.CommandParser;
import checkers.Piece;
import checkers.PieceIterable;
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
            }
        }
    }

    public void sendCommand(String command) {
        socket.write(command);
    }

    private String getListPiecesCommand() {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("list-pieces");
        for(Piece p: new PieceIterable(server.listPieces())) {
            builder.parameter(p);
        }
        return builder.finalise();
    }
}
