package checkers;

import checkers.Piece;
// import 

public class CommandBuilder {
    private StringBuilder builder = new StringBuilder();
    private boolean startedParameters = false;

    public CommandBuilder command(String command) {
        builder.append(command);
        return this;
    }

    public CommandBuilder parameter(int value) {
        if(startedParameters) {
            builder.append(",");
        } else {
            startedParameters = true;
            builder.append(" ");
        }
        builder.append(value);
        return this;
    }

    public CommandBuilder parameter(Piece.Color value) {
        if(startedParameters) {
            builder.append(",");
        } else {
            startedParameters = true;
            builder.append(" ");
        }
        builder.append(value.ordinal());
        return this;
    }

    public CommandBuilder parameter(Piece.Kind value) {
        if(startedParameters) {
            builder.append(",");
        } else {
            startedParameters = true;
            builder.append(" ");
        }
        builder.append(value.ordinal());
        return this;
    }

    public CommandBuilder parameter(Piece value) {
        if(startedParameters) {
            builder.append(",");
        } else {
            startedParameters = true;
            builder.append(" ");
        }
        final Point position = value.getPosition();
        builder.append("(")
            .append(value.getID())
            .append(",")
            .append(value.getKind())
            .append(",")
            .append(value.getColor())
            .append(",")
            .append(position.x)
            .append(",")
            .append(position.y)
            .append(")");
        return this;
    }

    public String finalise() {
        builder.append(";");
        return builder.toString();
    }
}
