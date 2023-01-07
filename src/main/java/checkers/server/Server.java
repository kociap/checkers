package checkers.server;

import checkers.*;
import java.net.ServerSocket;

// Server
// Listens on localhost port 8080.
//
public class Server implements CommandReceiver {
    private final static int port = 8080;
    private final static int socketIDWhite = 0;
    private final static int socketIDBlack = 1;

    private class ClientSocketThread extends SocketThread {
        private final Piece.Color color;

        public ClientSocketThread(Piece.Color color, SocketWrapper socket,
                                  CommandQueue queue) {
            super(socket, queue);
            this.color = color;
        }

        public Piece.Color getClientColor() {
            return color;
        }
    }

    private class ClientConnectionThread extends Thread {
        private final CommandQueue queue;
        private ClientSocketThread clientWhite = null;
        private ClientSocketThread clientBlack = null;

        public ClientConnectionThread(CommandQueue queue) {
            this.queue = queue;
        }

        public ClientSocketThread getClientWhite() {
            return clientWhite;
        }

        public ClientSocketThread getClientBlack() {
            return clientBlack;
        }

        @Override
        public void run() {
            try {
                clientWhite = new ClientSocketThread(
                    Piece.Color.white, new SocketWrapper(socket.accept()),
                    queueWhite);
                clientWhite.start();
                sendConnectWhite();
                clientBlack = new ClientSocketThread(
                    Piece.Color.black, new SocketWrapper(socket.accept()),
                    queueBlack);
                clientBlack.start();
                sendConnectBlack();
                sendConnectComplete();
            } catch(Exception e) {
                sendConnectFailure();
            }
        }

        private void sendConnectFailure() {
            final CommandBuilder builder = new CommandBuilder();
            builder.command("connect-failure");
            queue.push(builder.finalise());
        }

        private void sendConnectWhite() {
            final CommandBuilder builder = new CommandBuilder();
            builder.command("connect-white");
            queue.push(builder.finalise());
        }

        private void sendConnectBlack() {
            final CommandBuilder builder = new CommandBuilder();
            builder.command("connect-black");
            queue.push(builder.finalise());
        }

        private void sendConnectComplete() {
            final CommandBuilder builder = new CommandBuilder();
            builder.command("connect-complete");
            queue.push(builder.finalise());
        }
    }

    private ServerSocket socket = null;
    private final UI ui = new UI(this);

    private final CommandQueue queueWhite = new CommandQueue();
    private final BgQueuePoller pollerWhite =
        new BgQueuePoller(socketIDWhite, this, queueWhite);
    private final CommandQueue queueBlack = new CommandQueue();
    private final BgQueuePoller pollerBlack =
        new BgQueuePoller(socketIDBlack, this, queueBlack);
    private final CommandQueue queueConnection = new CommandQueue();
    private final BgQueuePoller pollerConnection =
        new BgQueuePoller(-1, this, queueConnection);

    private ClientSocketThread clientWhite = null;
    private ClientSocketThread clientBlack = null;
    private final ClientConnectionThread threadConnection =
        new ClientConnectionThread(queueConnection);

    private Engine engine = null;

    public void run() {
        try {
            socket = new ServerSocket(port);
        } catch(Exception e) {
            System.out.println("error: failed to start the server");
            return;
        }

        pollerWhite.start();
        pollerBlack.start();
        pollerConnection.start();

        ui.show();
    }

    public void notifyEngineSelected(Engine engine) {
        this.engine = engine;
        threadConnection.start();
    }

    @Override
    public void receiveCommand(final int socketID, final String command) {
        if(socketID < 0) {
            receiveConnectionCommand(command);
        } else {
            receiveClientCommand(socketID, command);
        }
    }

    private void receiveConnectionCommand(final String command) {
        final CommandParser parser = new CommandParser(command);
        if(parser.match("connect-white")) {
            clientWhite = threadConnection.getClientWhite();
            sendHello(clientWhite, Piece.Color.white);
            ui.notifyClientWhiteConnected();
            return;
        }

        if(parser.match("connect-black")) {
            clientBlack = threadConnection.getClientBlack();
            sendHello(clientBlack, Piece.Color.black);
            ui.notifyClientBlackConnected();
            return;
        }

        if(parser.match("connect-complete")) {
            sendBeginTurn(clientWhite);
            sendEndTurn(clientBlack);
            ui.notifyClientConnectComplete();
            return;
        }

        if(parser.match("connect-failure")) {
            // TODO: Terminate application.
            System.out.println("server received connect-failure");
            return;
        }

        System.out.println("unknown command \"" + command + "\"");
    }

    private void receiveClientCommand(final int socketID,
                                      final String command) {
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
        sendMove(clientBlack, pieceID, result.position.x, result.position.y);
        if(result.takenID != Piece.noneID) {
            sendTake(clientWhite, result.takenID);
            sendTake(clientBlack, result.takenID);
        }

        if(result.promoted) {
            sendPromote(clientWhite, pieceID);
            sendPromote(clientBlack, pieceID);
        }

        if(result.endTurn) {
            final Piece.Color color = engine.getCurrentColor();
            if(color == Piece.Color.white) {
                sendEndTurn(clientBlack);
                sendBeginTurn(clientWhite);
            } else {
                sendEndTurn(clientWhite);
                sendBeginTurn(clientBlack);
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
