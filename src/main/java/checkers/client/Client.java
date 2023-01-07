package checkers.client;

import checkers.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements RequestService, CommandReceiver {
    private SocketThread server;
    private CommandQueue queue = new CommandQueue();
    private BgQueuePoller poller = new BgQueuePoller(0, this, this.queue);
    private Game game = new Game(this);

    public boolean run() {
        final PlayerInformation player = connect();
        if(player == null) {
            return false;
        }

        poller.start();

        // TODO: Pass PlayerInformation to game.
        game.run();

        return true;
    }

    private PlayerInformation connect() {
        SocketWrapper socket;
        try {
            socket = new SocketWrapper(new Socket("localhost", 8080));
        } catch(Exception e) {
            return null;
        }

        final String response = socket.read();
        if(response == null) {
            return null;
        }

        final CommandParser parser = new CommandParser(response);

        if(!parser.match("hello")) {
            return null;
        }

        final Piece.Color color = parser.matchPieceColor();
        final PlayerInformation player = new PlayerInformation(color);

        server = new SocketThread(socket, queue);
        server.start();

        return player;
    }

    @Override
    public void receiveCommand(final int socketID, final String command) {
        // TODO: Add missing server-issued commands.
        final CommandParser parser = new CommandParser(command);
        if(parser.match("list-game-properties")) {
            final Dimensions2D size = parseListGameProperties(parser);
            game.handleRequestSize(size);
            return;
        }

        if(parser.match("list-pieces")) {
            final List<ClientPiece> pieces = parseListPieces(parser);
            game.handleRequestListPieces(pieces);
            return;
        }

        if(parser.match("move")) {
            final int pieceID = parser.matchInteger();
            parser.match(",");
            final int x = parser.matchInteger();
            parser.match(",");
            final int y = parser.matchInteger();
            game.handleRequestMove(pieceID, new Point(x, y));
            return;
        }

        System.out.println("unknown command \"" + command + "\"");
    }

    public void requestSize() {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("list-game-properties");
        server.sendCommand(builder.finalise());
    }

    private Dimensions2D parseListGameProperties(final CommandParser parser) {
        final int width = parser.matchInteger();
        parser.match(",");
        final int height = parser.matchInteger();
        return new Dimensions2D(width, height);
    }

    public void requestListPieces() {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("list-pieces");
        server.sendCommand(builder.finalise());
    }

    private List<ClientPiece> parseListPieces(final CommandParser parser) {
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

    public void requestMove(final int pieceID, final Point position) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("move")
            .parameter(pieceID)
            .parameter(position.x)
            .parameter(position.y);
        server.sendCommand(builder.finalise());
    }
}
