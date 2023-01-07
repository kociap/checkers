package checkers.server;

import checkers.CommandQueue;
import checkers.Piece;
import checkers.SocketThread;
import checkers.SocketWrapper;

public class ClientSocketThread extends SocketThread {
    private Piece.Color color;

    public ClientSocketThread(Piece.Color color, SocketWrapper socket,
                              CommandQueue queue) {
        super(socket, queue);
        this.color = color;
    }

    public Piece.Color getClientColor() {
        return color;
    }
}
