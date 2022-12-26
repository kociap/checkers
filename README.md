
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
hello;
# bye
# Issued when the server closes or in a response to a client's bye.
bye;
promote piece-id;
# list-pieces
# piece is a structure of ID, kind, color, x, y.
list-pieces piece[,piece]...;
list-moves move[,move]...;
move result;
list-game-properties board-width,board-height;
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
