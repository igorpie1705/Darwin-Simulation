package agh.ics.oop.model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;



public record Vector2d(int x, int y) {

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    public boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public Vector2d upperRight(Vector2d other) {
        int x_add = Math.max(this.x, other.x);
        int y_add = Math.max(this.y, other.y);
        return new Vector2d(x_add, y_add);
    }

    public Vector2d lowerLeft(Vector2d other) {
        int x_add = Math.min(this.x, other.x);
        int y_add = Math.min(this.y, other.y);
        return new Vector2d(x_add, y_add);
    }

    public Vector2d opposite(Vector2d other) {
        return new Vector2d(-other.x, -other.y);
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d that))
            return false;
        return this.x == that.x && this.y == that.y;
    }

}

