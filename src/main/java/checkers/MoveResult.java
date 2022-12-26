package checkers;

public enum MoveResult {
    // Normal move.
    ok,
    // Take another piece.
    take,
    // Illegal move against the rules.
    illegal,
}
