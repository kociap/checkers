package checkers.server;

import checkers.*;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Server
// Listens on localhost port 8080.
//
public class Server implements CommandReceiver {
    private final static int port = 8080;
    private final static int socketIDWhite = 0;
    private final static int socketIDBlack = 1;

    private ServerSocket socket;
    private UI ui = new UI(this);

    private ClientSocketThread clientWhite;
    private CommandQueue queueWhite = new CommandQueue();
    private BgQueuePoller pollerWhite =
        new BgQueuePoller(socketIDWhite, this, queueWhite);
    private ClientSocketThread clientBlack;
    private CommandQueue queueBlack = new CommandQueue();
    private BgQueuePoller pollerBlack =
        new BgQueuePoller(socketIDBlack, this, queueBlack);

    private Engine engine = null;

    public boolean run() {
        try {
            socket = new ServerSocket(port);
        } catch(Exception e) {
            return false;
        }

        pollerWhite.start();
        pollerBlack.start();

        ui.show();

        return true;
    }

    public void notifyEngineSelected(Engine engine) {
        this.engine = engine;
        try {
            // TODO: Move to thread;
            clientWhite = new ClientSocketThread(
                Piece.Color.white, new SocketWrapper(socket.accept()),
                queueWhite);
            clientWhite.start();
            sendHello(clientWhite, Piece.Color.white);
            ui.notifyClientConnected();
            // clientWhite = new SocketWrapper(socket.accept());
            // sendHello(clientWhite, Piece.Color.white);
            // ui.notifyClientConnected();
            // TODO: Send begin turn
        } catch(Exception e) {
            return;
        }
    }

    @Override
    public void receiveCommand(final int socketID, final String command) {
        final ClientSocketThread client =
            (socketID == socketIDWhite ? clientWhite : clientBlack);
        final CommandParser parser = new CommandParser(command);
        if(parser.match("list-game-properties")) {
            handleListGameProperties(client);
            return;
        }

        if(parser.match("list-pieces")) {
            handleListPieces(client);
            return;
        }

        if(parser.match("list-moves")) {
            final int pieceID = parser.matchInteger();
            handleListMoves(client, pieceID);
            return;
        }

        if(parser.match("move")) {
            final int pieceID = parser.matchInteger();
            parser.match(",");
            final int x = parser.matchInteger();
            parser.match(",");
            final int y = parser.matchInteger();
            handleMove(client, pieceID, new Point(x, y));
            return;
        }

        System.out.println("unknown command \"" + command + "\"");
    }

    private void handleListGameProperties(final ClientSocketThread client) {
        sendListGameProperties(client);
    }

    private void handleListPieces(final ClientSocketThread client) {
        sendListPieces(client);
    }

    private void handleListMoves(final ClientSocketThread client,
                                 final int pieceID) {
        sendListMoves(client, pieceID);
    }

    private void handleMove(final ClientSocketThread client, final int pieceID,
                            final Point position) {
        // Prevent black and white moves being done by the same client.
        if(engine.getCurrentColor() != client.getClientColor()) {
            sendMove(client, pieceID, -1, -1);
            return;
        }

        final MoveResult result = engine.move(pieceID, position);
        if(result == null) {
            sendMove(client, pieceID, -1, -1);
            return;
        }

        sendMove(clientWhite, pieceID, result.position.x, result.position.y);
        // sendMove(clientBlack, pieceID, result.position.x, result.position.y);
        if(result.takenID != Piece.noneID) {
            sendTake(clientWhite, pieceID);
            // sendTake(clientBlack, pieceID);
        }

        if(result.promoted) {
            sendPromote(clientWhite, pieceID);
            // sendPromote(clientBlack, pieceID);
        }

        if(result.endTurn) {
            final Piece.Color color = engine.getCurrentColor();
            if(color == Piece.Color.white) {
                // sendEndTurn(clientBlack);
                sendBeginTurn(clientWhite);
            } else {
                sendEndTurn(clientWhite);
                // sendBeginTurn(clientBlack);
            }
        }
    }

    private void sendMove(final ClientSocketThread client, final int pieceID,
                          final int x, final int y) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("move").parameter(pieceID).parameter(x).parameter(y);
        client.sendCommand(builder.finalise());
    }

    private void sendTake(final ClientSocketThread client, final int pieceID) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("take").parameter(pieceID);
        client.sendCommand(builder.finalise());
    }

    private void sendPromote(final ClientSocketThread client,
                             final int pieceID) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("promote").parameter(pieceID);
        client.sendCommand(builder.finalise());
    }

    private void sendBeginTurn(final ClientSocketThread client) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("begin-turn");
        client.sendCommand(builder.finalise());
    }

    private void sendEndTurn(final ClientSocketThread client) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("end-turn");
        client.sendCommand(builder.finalise());
    }

    private void sendHello(final ClientSocketThread client,
                           final Piece.Color color) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("hello").parameter(color);
        client.sendCommand(builder.finalise());
    }

    private void sendListGameProperties(final ClientSocketThread client) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("list-game-properties");
        final Dimensions2D boardSize = engine.getBoardSize();
        builder.parameter(boardSize.width).parameter(boardSize.height);
        client.sendCommand(builder.finalise());
    }

    private void sendListPieces(final ClientSocketThread client) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("list-pieces");
        for(Piece p: engine.listPieces()) {
            builder.parameter(p);
        }
        client.sendCommand(builder.finalise());
    }

    private void sendListMoves(final ClientSocketThread client,
                               final int pieceID) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("list-moves");
        for(Point p: engine.listMoves(pieceID)) {
            builder.parameter(p);
        }
        client.sendCommand(builder.finalise());
    }
}
