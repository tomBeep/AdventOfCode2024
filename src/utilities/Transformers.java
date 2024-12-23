package utilities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Transformers {

    public static <T> T[] listToArray(List<T> list) {
        //noinspection unchecked
        return list.toArray((T[]) Array.newInstance(list.getFirst().getClass(), list.size()));
    }

    public static int[] intListToArray(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static long[] longListToArray(List<Long> list) {
        long[] array = new long[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static <T> List<T> arrayToList(T[] array) {
        return new ArrayList<>(List.of(array));
    }

    public static int[][] twoDimensionalIntListToArray(List<List<Integer>> list) {
        int[][] array = new int[list.size()][list.getFirst().size()];
        for (int i = 0; i < array.length; i++) {
            List<Integer> innerList = list.get(i);
            for (int j = 0; j < innerList.size(); j++) {
                array[i][j] = innerList.get(j);
            }
        }
        return array;
    }

    public static long[][] twoDimensionalLongListToArray(List<List<Long>> list) {
        long[][] array = new long[list.size()][list.getFirst().size()];
        for (int i = 0; i < array.length; i++) {
            List<Long> innerList = list.get(i);
            for (int j = 0; j < innerList.size(); j++) {
                array[i][j] = innerList.get(j);
            }
        }
        return array;
    }

    public static <T> T[][] twoDimensionalListToArray(List<List<T>> list) {
        //noinspection unchecked
        T[][] array = (T[][]) Array.newInstance(list.getFirst().getClass(), list.size());
        for (int i = 0; i < list.size(); i++) {
            List<T> innerList = list.get(i);
            T[] innerArray = listToArray(innerList);
            array[i] = innerArray;
        }
        return array;
    }

    public static <T> List<List<T>> twoDimensionalArrayToList(T[][] array) {
        List<List<T>> newList = new ArrayList<>();
        for (T[] innerArray : array) {
            newList.add(List.of(innerArray));
        }
        return newList;
    }
}
