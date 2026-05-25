package utilities;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class Navigator2DTest {

    @Test
    public void testIteratingInAllDirections() {
        boolean[][] array = new boolean[5][5];

        AtomicInteger numberOfApplys = new AtomicInteger(0);
        Navigator2D navigator = new Navigator2D(0, 0, array[0].length, array.length);
        navigator.move(2, 2); // centre us!

        navigator.iterateInAllDirections((x, y) -> {
            array[y][x] = true;
            numberOfApplys.incrementAndGet();
        });

        assertEquals(get2DArray(array), 24, numberOfApplys.get());

        // Expected:
        // [true , false, true, false, true ]
        // [false, true , true, true , false]
        // [true , true , true, true , true ]
        // [false, true , true, true , false]
        // [true , false, true, false, true ]

        assertFalse(get2DArray(array), array[0][1]);
        assertFalse(get2DArray(array), array[0][3]);

        assertFalse(get2DArray(array), array[1][0]);
        assertFalse(get2DArray(array), array[1][4]);

        assertFalse(get2DArray(array), array[3][0]);
        assertFalse(get2DArray(array), array[3][4]);

        assertFalse(get2DArray(array), array[4][1]);
        assertFalse(get2DArray(array), array[4][3]);
    }

    @Test
    public void testFixedDirectionWorks() {
        for (int i = 1; i <= 11; i++) {
            for (int j = 1; j <= 11; j++) {
                testFixedDirectionWorks(i, j);
            }
        }
    }

    @Test
    public void testReadDirectionWorks() {
        for (int i = 1; i <= 11; i++) {
            for (int j = 1; j <= 11; j++) {
                testReadingDirection(i, j);
            }
        }
    }

    @Test
    public void testSpiralWorks() {
        for (int i = 1; i <= 11; i++) {
            for (int j = 2; j <= 11; j++) {
                testSpiralOrder(i, j);
            }
        }
    }

    private void testFixedDirectionWorks(int horizontalSize, int verticalSize) {
        boolean[][] array = new boolean[verticalSize][horizontalSize];

        AtomicInteger numberOfApplys = new AtomicInteger(0);
        Navigator2D navigator = new Navigator2D(0, 0, array[0].length, array.length);

        navigator.iterateInFixedDirection((x, y) -> {
            array[y][x] = true;
            numberOfApplys.incrementAndGet();
        }, Navigator2D.Direction.EAST);
        assertEquals(horizontalSize, numberOfApplys.get());

        numberOfApplys.set(0);
        navigator.iterateInFixedDirection((x, y) -> {
            array[y][x] = true;
            numberOfApplys.incrementAndGet();
        }, Navigator2D.Direction.WEST);
        assertEquals(horizontalSize, numberOfApplys.get());

        numberOfApplys.set(0);
        navigator.iterateInFixedDirection((x, y) -> {
            array[y][x] = true;
            numberOfApplys.incrementAndGet();
        }, Navigator2D.Direction.SOUTH);
        assertEquals(verticalSize, numberOfApplys.get());

        numberOfApplys.set(0);
        navigator.iterateInFixedDirection((x, y) -> {
            array[y][x] = true;
            numberOfApplys.incrementAndGet();
        }, Navigator2D.Direction.NORTH);
        assertEquals(verticalSize, numberOfApplys.get());

        numberOfApplys.set(0);
        navigator.iterateInFixedDirection((x, y) -> {
            array[y][x] = true;
            numberOfApplys.incrementAndGet();
        }, Navigator2D.Direction.NORTH_EAST);
        assertEquals(get2DArray(array), 1, numberOfApplys.get());

        numberOfApplys.set(0);
        navigator.iterateInFixedDirection((x, y) -> {
            array[y][x] = true;
            numberOfApplys.incrementAndGet();
        }, Navigator2D.Direction.SOUTH_EAST);
        assertEquals(get2DArray(array), Math.min(horizontalSize, verticalSize), numberOfApplys.get());
    }

    private void testSpiralOrder(int horizontalSize, int verticalSize) {
        boolean[][] array = new boolean[verticalSize][horizontalSize];

        AtomicInteger numberOfApplys = new AtomicInteger(0);
        new Navigator2D(0, 0, array[0].length, array.length).iterateInShrinkingSpiral((x, y) -> {
            array[y][x] = true;
            numberOfApplys.incrementAndGet();
        });

        check(array, numberOfApplys);
    }

    private void testReadingDirection(int horizontalSize, int verticalSize) {
        boolean[][] array = new boolean[verticalSize][horizontalSize];

        AtomicInteger numberOfApplys = new AtomicInteger(0);
        new Navigator2D(0, 0, array[0].length, array.length).iterateInReadingDirection((x, y) -> {
            array[y][x] = true;
            numberOfApplys.incrementAndGet();
        });

        check(array, numberOfApplys);
    }

    private String get2DArray(boolean[][] arr) {
        StringBuilder sb = new StringBuilder("\n");
        for (int i = 0; i < arr.length; i++) {
            sb.append(Arrays.toString(arr[i])).append("\n");
        }
        return sb.toString();
    }

    private void check(boolean[][] test, AtomicInteger numberOfApplys) {
        assertEquals(get2DArray(test), test.length * test[0].length, numberOfApplys.get());
        for (int x = 0; x < test[0].length; x++) {
            for (int y = 0; y < test.length; y++) {
                assertTrue(get2DArray(test), test[y][x]);
            }
        }
    }

}