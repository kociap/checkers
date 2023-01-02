package checkers.server;

import checkers.MoveCommand;
import checkers.Piece;
import checkers.utility.CommandBuilder;
import checkers.utility.Dimensions2D;
import checkers.utility.Point;
import checkers.utility.SocketWrapper;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Server
// Listens on localhost port 8080.
//
public class Server {
    private Engine engine;
    private ServerSocket socket;
    private Coordinator coordinator;

    private ClientThread clientBlack;
    private ClientThread clientWhite;

    private Lock engineLock = new ReentrantLock();

    public Server(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public boolean run() {
        try {
            // TODO: Hardcoded port.
            socket = new ServerSocket(8080);
        } catch(Exception e) {
            return false;
        }

        return true;
    }

    public void notifyEngineSelected(Engine engine) {
        this.engine = engine;
        try {
            // TODO: Move to thread;
            clientWhite =
                new ClientThread(this, new SocketWrapper(socket.accept()));
            clientWhite.start();
            sendHello(clientWhite, Piece.Color.white);
            coordinator.notifyClientConnected();
            // clientWhite = new SocketWrapper(socket.accept());
            // sendHello(clientWhite, Piece.Color.white);
            // coordinator.notifyClientConnected();
        } catch(Exception e) {
            return;
        }
    }

    public void notifyCommandListGameProperties(final ClientThread client) {
        sendListGameProperties(client);
    }

    public void notifyCommandListPieces(final ClientThread client) {
        sendListPieces(client);
    }

    public void notifyCommandListMoves(final ClientThread client,
                                       final int pieceID) {
        sendListMoves(client, pieceID);
    }

    public void notifyCommandMove(final ClientThread client, final int pieceID,
                                  final int x, final int y) {
        engineLock.lock();
        try {
            final MoveResult result =
                engine.move(new MoveCommand(pieceID, new Point(x, y)));
            if(result == null) {
                sendMove(client, 0, 0, 0);
                return;
            }

            sendMove(clientWhite, pieceID, result.position.x,
                     result.position.y);
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
        } catch(Exception e) {
            // Nothing.
        } finally {
            engineLock.unlock();
        }
    }

    private void sendMove(final ClientThread client, final int pieceID,
                          final int x, final int y) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("move").parameter(pieceID).parameter(x).parameter(y);
        client.sendCommand(builder.finalise());
    }

    private void sendTake(final ClientThread client, final int pieceID) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("take").parameter(pieceID);
        client.sendCommand(builder.finalise());
    }

    private void sendPromote(final ClientThread client, final int pieceID) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("promote").parameter(pieceID);
        client.sendCommand(builder.finalise());
    }

    private void sendBeginTurn(final ClientThread client) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("begin-turn");
        client.sendCommand(builder.finalise());
    }

    private void sendEndTurn(final ClientThread client) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("end-turn");
        client.sendCommand(builder.finalise());
    }

    private void sendHello(final ClientThread client, final Piece.Color color) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("hello").parameter(color);
        client.sendCommand(builder.finalise());
    }

    private void sendListGameProperties(final ClientThread client) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("list-game-properties");
        engineLock.lock();
        try {
            final Dimensions2D boardSize = engine.getBoardSize();
            builder.parameter(boardSize.width).parameter(boardSize.height);
        } catch(Exception e) {
            // Nothing.
        } finally {
            engineLock.unlock();
        }
        client.sendCommand(builder.finalise());
    }

    private void sendListPieces(final ClientThread client) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("list-pieces");
        engineLock.lock();
        try {
            for(Piece p: engine.listPieces()) {
                builder.parameter(p);
            }
        } catch(Exception e) {
            // Nothing.
        } finally {
            engineLock.unlock();
        }
        client.sendCommand(builder.finalise());
    }

    private void sendListMoves(final ClientThread client, final int pieceID) {
        final CommandBuilder builder = new CommandBuilder();
        builder.command("list-moves");
        engineLock.lock();
        try {
            for(Point p: engine.listMoves(pieceID)) {
                builder.parameter(p);
            }
        } catch(Exception e) {
            // Nothing.
        } finally {
            engineLock.unlock();
        }
        client.sendCommand(builder.finalise());
    }
}
