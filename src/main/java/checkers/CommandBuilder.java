package checkers;

import checkers.Piece;

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

    public CommandBuilder parameter(Point value) {
        if(startedParameters) {
            builder.append(",");
        } else {
            startedParameters = true;
            builder.append(" ");
        }
        builder.append("(");
        builder.append(value.x).append(",").append(value.y);
        builder.append(")");
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
            .append(value.getKind().ordinal())
            .append(",")
            .append(value.getColor().ordinal())
            .append(",")
            .append(position.x)
            .append(",")
            .append(position.y)
            .append(")");
        return this;
    }

    public String finalise() {
        builder.append(";\n");
        return builder.toString();
    }
}
