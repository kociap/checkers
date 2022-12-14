package checkers.server;

// TODO: Needs to be more specific in order for the client to be able 
//       to provide visual feedback.
public enum MoveResult {
    // kill another piece
    kill,
    // do nothing
    none,
    // make move without killing
    normal,
    // can't make move because it's against the rules
    illegal,
}
