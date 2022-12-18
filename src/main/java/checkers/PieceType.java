package checkers;

public enum PieceType {
    RED(1), WHITE(-1), RED_KING(-1), WHITE_KING(1);

    final int moveDir;

    PieceType(int moveDir) {
        this.moveDir = moveDir;
    }
}
