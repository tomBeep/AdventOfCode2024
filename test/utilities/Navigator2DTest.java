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

        assertEquals(Parsing.convert2DArrayToString(array), 24, numberOfApplys.get());

        // Expected:
        // [true , false, true, false, true ]
        // [false, true , true, true , false]
        // [true , true , true, true , true ]
        // [false, true , true, true , false]
        // [true , false, true, false, true ]

        assertFalse(Parsing.convert2DArrayToString(array), array[0][1]);
        assertFalse(Parsing.convert2DArrayToString(array), array[0][3]);

        assertFalse(Parsing.convert2DArrayToString(array), array[1][0]);
        assertFalse(Parsing.convert2DArrayToString(array), array[1][4]);

        assertFalse(Parsing.convert2DArrayToString(array), array[3][0]);
        assertFalse(Parsing.convert2DArrayToString(array), array[3][4]);

        assertFalse(Parsing.convert2DArrayToString(array), array[4][1]);
        assertFalse(Parsing.convert2DArrayToString(array), array[4][3]);
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
        }, Direction.EAST);
        assertEquals(horizontalSize, numberOfApplys.get());

        numberOfApplys.set(0);
        navigator.iterateInFixedDirection((x, y) -> {
            array[y][x] = true;
            numberOfApplys.incrementAndGet();
        }, Direction.WEST);
        assertEquals(horizontalSize, numberOfApplys.get());

        numberOfApplys.set(0);
        navigator.iterateInFixedDirection((x, y) -> {
            array[y][x] = true;
            numberOfApplys.incrementAndGet();
        }, Direction.SOUTH);
        assertEquals(verticalSize, numberOfApplys.get());

        numberOfApplys.set(0);
        navigator.iterateInFixedDirection((x, y) -> {
            array[y][x] = true;
            numberOfApplys.incrementAndGet();
        }, Direction.NORTH);
        assertEquals(verticalSize, numberOfApplys.get());

        numberOfApplys.set(0);
        navigator.iterateInFixedDirection((x, y) -> {
            array[y][x] = true;
            numberOfApplys.incrementAndGet();
        }, Direction.NORTH_EAST);
        assertEquals(Parsing.convert2DArrayToString(array), 1, numberOfApplys.get());

        numberOfApplys.set(0);
        navigator.iterateInFixedDirection((x, y) -> {
            array[y][x] = true;
            numberOfApplys.incrementAndGet();
        }, Direction.SOUTH_EAST);
        assertEquals(Parsing.convert2DArrayToString(array), Math.min(horizontalSize, verticalSize), numberOfApplys.get());
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

    private void check(boolean[][] test, AtomicInteger numberOfApplys) {
        assertEquals(Parsing.convert2DArrayToString(test), test.length * test[0].length, numberOfApplys.get());
        for (int x = 0; x < test[0].length; x++) {
            for (int y = 0; y < test.length; y++) {
                assertTrue(Parsing.convert2DArrayToString(test), test[y][x]);
            }
        }
    }

}