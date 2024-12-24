package utilities;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Navigates in weird and wonderful ways through a 2d space. It even has tests!
 */
public class Navigator2D {

    private final int maxX;
    private final int maxY;
    private int x;
    private int y;

    public Navigator2D(int x, int y, int maxX, int maxY) {
        this.x = x;
        this.y = y;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public void reset() {
        this.x = 0;
        this.y = 0;
    }

    public void resetToPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point getPoint() {
        return new Point(x, y);
    }


    /**
     * Iterates in the given direction until we reach the edge, applies the passed in function at each valid point,
     * including the initial point before any moves.
     *
     * @param applyToEachPoint
     * @param d
     */
    public void iterateInFixedDirection(BiConsumer<Integer, Integer> applyToEachPoint, Direction d) {
        int dx = d.getDx();
        int dy = d.getDy();


        while (isWithinBounds()) {
            applyToEachPoint.accept(this.x, this.y);
            if (!isWithinBounds(this.x + dx, this.y + dy)) {
                return;
            }
            move(dx, dy);
        }
    }

    /**
     * Applies #iterateInFixedDirection to all 8 directions one after the other from the centre point. Function will be
     * applied to the starting point only once.
     *
     * @param applyToEachPoint
     */
    public void iterateInAllDirections(BiConsumer<Integer, Integer> applyToEachPoint) {
        int startingX = this.x;
        int startingY = this.y;

        for (Direction d : Direction.values()) {
            resetToPoint(startingX, startingY);
            iterateInFixedDirection(applyToEachPoint, d);
        }
    }

    /**
     * Iterates through the array in reading direction ie left to right, line by line.
     */
    public void iterateInReadingDirection(BiConsumer<Integer, Integer> applyToEachPoint) {
        while (this.y < this.maxY) {
            iterateInFixedDirection(applyToEachPoint, Direction.EAST);
            this.y++;
            this.x = 0;
        }
    }

    /**
     * Iterates through the array in a spiral that runs along the outside of the array getting smaller and smaller.
     * Each location will have the function applied to it once only.
     */
    public void iterateInShrinkingSpiral(BiConsumer<Integer, Integer> applyToEachPoint) {
        reset();
        AtomicBoolean isFirstMove = new AtomicBoolean(true);

        AtomicInteger minX = new AtomicInteger(0);
        AtomicInteger maxX = new AtomicInteger(this.maxX);
        AtomicInteger minY = new AtomicInteger(1);
        AtomicInteger maxY = new AtomicInteger(this.maxY);
        BiFunction<Direction, Point, Direction> evaluateDirectionFunction = (currentDir, point) -> {
            int x = point.x;
            int y = point.y;
            applyToEachPoint.accept(x, y);

            if (isFirstMove.getAndSet(false)) {
                return maxX.get() > 1 ? Direction.EAST : Direction.SOUTH;
            }

            if (x == maxX.get() - 1 && currentDir == Direction.EAST) {
                maxX.set(maxX.get() - 1);
                return Direction.SOUTH;
            } else if (y == maxY.get() - 1 && currentDir == Direction.SOUTH) {
                maxY.set(maxY.get() - 1);
                return Direction.WEST;
            } else if (x == minX.get() && currentDir == Direction.WEST) {
                minX.set(minX.get() + 1);
                return Direction.NORTH;
            } else if (y == minY.get() && currentDir == Direction.NORTH) {
                minY.set(minY.get() + 1);
                return Direction.EAST;
            } else {
                return null;
            }
        };

        BiFunction<Direction, Point, Boolean> shouldContinue = (dir, point) -> {
            int futureX = point.x + dir.getDx();
            int futureY = point.y + dir.getDy();

            if (dir == Direction.EAST && futureX >= maxX.get()) {
                return false;
            } else if (dir == Direction.SOUTH && futureY >= maxY.get()) {
                return false;
            } else if (dir == Direction.WEST && futureX < minX.get()) {
                return false;
            } else return dir != Direction.NORTH || futureY >= minY.get();
        };

        iterateInChangingDirection(evaluateDirectionFunction, shouldContinue);
    }

    /**
     * Evaluates direction, then moves, repeatedly, stops when we move outside the boundries.
     *
     * @param evaluateDirection function that is called before each move to evaluate the new direction, return null to indicate the same direction, with no change.
     */
    public void iterateInChangingDirection(BiFunction<Direction, Point, Direction> evaluateDirection) {
        iterateInChangingDirection(evaluateDirection, (dir, point) -> isWithinBounds());
    }

    /**
     * @param evaluateDirection takes in the current point and evaluates what direction to move in, return null to indicate 'same direction' called on the starting point.
     * @param shouldContinue    called after we evaluate the direction, but before we move, returns true to indicate we can and should continue iterating in this direction.
     */
    public void iterateInChangingDirection(BiFunction<Direction, Point, Direction> evaluateDirection, BiFunction<Direction, Point, Boolean> shouldContinue) {
        Direction currentDir = null;

        while (isWithinBounds()) {
            Direction newDirection = evaluateDirection.apply(currentDir, this.getPoint());
            if (newDirection != null) {
                currentDir = newDirection;
            }

            if (!shouldContinue.apply(currentDir, this.getPoint())) {
                return;
            }

            int dx = currentDir.getDx();
            int dy = currentDir.getDy();
            move(dx, dy);
        }
    }

    public void move(int dx, int dy) {
        this.x = this.x + dx;
        this.y = this.y + dy;
    }

    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && y >= 0 && (maxY == -1 || y < maxY) && (maxX == -1 || x < maxX);
    }

    public boolean isWithinBounds() {
        return isWithinBounds(this.x, this.y);
    }
}
