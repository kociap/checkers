package checkers.utility;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Point other) {
        return this.x == other.x && this.y == other.y;
    }
}
