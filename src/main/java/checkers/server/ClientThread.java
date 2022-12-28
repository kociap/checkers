package checkers.server;

import checkers.CommandBuilder;
import checkers.CommandParser;
import checkers.SocketWrapper;
import checkers.PieceIterable;

public class ClientThread extends Thread {
    private Server server;
    private SocketWrapper socket;
    
    public ClientThread(Server server, SocketWrapper socket) {
        this.server =server;
        this.socket = socket;
    }

    @Override
    public void run() {
        final BufferedReader reader = socket.getReader();
        final PrintWriter writer = socket.getWriter();
        while(true) {
            final String line = reader.readLine();
            if(line == null) {
                break;
            }

            final CommandParser parser = new CommandParser(line);

            if(parser.match("list-pieces")) {
                final String command = getListPiecesCommand();
                writer.format(command);
            }
        }
    }

    private String getListPiecesCommand() {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("list-pieces");
        // TODO: Get pieces.
        Iterator<Piece> iterator;
        for(Piece p: new PieceIterable(iterator)) {
            builder.parameter(p);
        }
        return builder.finalise();
    }
}
