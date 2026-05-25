package utilities;

import java.util.Arrays;

public class Parsing {


    public static char [][] parseInputIntoCharacterGrid(String input){
        String [] lines = input.split("\n");

        char [][] grid = new char[lines.length][];

        for (int i = 0; i < lines.length; i++) {
            char[] line = lines[i].toCharArray();
            grid[i] = line;
        }

        return grid;
    }

    public static String convert2DArrayToString(boolean[][] arr) {
        StringBuilder sb = new StringBuilder("\n");
        for (int i = 0; i < arr.length; i++) {
            sb.append(Arrays.toString(arr[i])).append("\n");
        }
        return sb.toString();
    }

    public static String convert2DArrayToString(int[][] arr) {
        StringBuilder sb = new StringBuilder("\n");
        for (int i = 0; i < arr.length; i++) {
            sb.append(Arrays.toString(arr[i])).append("\n");
        }
        return sb.toString();
    }

    public static String convert2DArrayToString(char[][] arr) {
        StringBuilder sb = new StringBuilder("\n");
        for (int i = 0; i < arr.length; i++) {
            sb.append(Arrays.toString(arr[i])).append("\n");
        }
        return sb.toString();
    }
}
