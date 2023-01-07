
# Checkers
# Documentation
## Socket Commands
### Grammar
```
comment -> # string
command -> command-name [parameter[,parameter]...];
parameter -> integer | structure
structure -> (integer[,integer]...)
```
### Server-issued commands
```
# hello
# Issued when a client connects and the server accepts the connection.
# The client must wait for hello before sending any data over the socket.
# Hello contains player information.
hello player-color;
# bye
# Issued when the server closes.
bye;
list-game-properties board-width,board-height;
# list-pieces
# piece is a structure of ID, kind, color, x, y.
list-pieces piece[,piece]...;
# list-moves
# In response to the list-moves client command.
list-moves move[,move]...;
# move
# Response to the client move command or as an update when the other player
# performs a valid move. When responding to a client, the x and y will be -1 if
# the requested move was invalid.
move piece-id,x,y;
# take
# After a move if a piece has been taken.
take piece-id;
# promote
# After a move if a piece has been promoted.
promote piece-id;
# begin-turn
# Issued only to the client whose turn has begun.
begin-turn;
# end-turn
# Issued only to the client whose turn has ended.
end-turn;
```
### Client-issued commands
```
# bye
# Issued when a client wishes to disconnect from the game.
bye;
move piece-id,x,y;
list-pieces;
list-moves piece-id;
list-game-properties;
```
