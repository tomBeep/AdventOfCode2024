package utilities;

import java.util.Collections;
import java.util.List;
import java.awt.*;

public enum Direction {
    NORTH, EAST, SOUTH, WEST, NORTH_EAST, SOUTH_EAST, NORTH_WEST, SOUTH_WEST;

    private static final List<Direction> MAIN_DIRECTIONS = List.of(NORTH, EAST, SOUTH, WEST);

    public static List<Direction> getMain4() {
        return MAIN_DIRECTIONS;
    }

    public int getDx() {
        switch (this) {
            case EAST, NORTH_EAST, SOUTH_EAST -> {
                return 1;
            }
            case WEST, NORTH_WEST, SOUTH_WEST -> {
                return -1;
            }
            default -> {
                return 0;
            }
        }
    }

    public Point getPointBeingMovedTo(Point currentPoint){
        Point newPoint = new Point(currentPoint);
        newPoint.translate(this.getDx(), this.getDy());
        return newPoint;
    }

    public Direction getOpposite() {
        switch (this) {
            case NORTH -> {
                return SOUTH;
            }
            case NORTH_EAST -> {
                return SOUTH_WEST;
            }
            case EAST -> {
                return WEST;
            }
            case SOUTH_EAST -> {
                return NORTH_WEST;
            }
            case SOUTH -> {
                return NORTH;
            }
            case SOUTH_WEST -> {
                return NORTH_EAST;
            }
            case WEST -> {
                return EAST;
            }
            case NORTH_WEST -> {
                return SOUTH_EAST;
            }
            default -> {
                return null;
            }
        }
    }

    public int getDy() {
        switch (this) {
            case NORTH, NORTH_EAST, NORTH_WEST -> {
                return -1;
            }
            case SOUTH, SOUTH_WEST, SOUTH_EAST -> {
                return 1;
            }
            default -> {
                return 0;
            }
        }
    }
}
