public class Day21 {

    public static void main(String[] args) {
        new Day21().doChallenge();
    }

    private void doChallenge() {
        String input = getInput();
        doPart1(input);
    }


    private void doPart1(String input) {
//        long total = doRobot("803A") + doRobot("528A") + doRobot("586A")+ doRobot("341A")+doRobot("319A");
//        long total2 = doRobot2("803A") + doRobot2("528A") + doRobot2("586A")+ doRobot2("341A")+doRobot2("319A");
        long total = doRobot("029A") + doRobot("980A") + doRobot("179A") + doRobot("456A") + doRobot("379A");
        System.out.println("P1 is " + total);
//        System.out.println("P2 is " + total2);
    }


    private long doRobot2(String input) {
        StringBuilder initialMoves = new StringBuilder();
        NumPadRobot numPadRobot = new NumPadRobot();
        for (char c : input.toCharArray()) {
            initialMoves.append(numPadRobot.getRequiredMovesTo(c));
        }

        long complexity = 0;
        char[] previousMoves = initialMoves.toString().toCharArray();
        for (int i = 0; i < previousMoves.length; i++) {
            complexity += Robot.getComplexity(previousMoves[i], 2);
        }

        System.out.println("Part 2: Complexity: " + complexity);
        System.out.println();
        return complexity;
    }

    private long doRobot(String input) {
        StringBuilder initialMoves = new StringBuilder();
        NumPadRobot numPadRobot = new NumPadRobot();
        for (char c : input.toCharArray()) {
            initialMoves.append(numPadRobot.getRequiredMovesTo(c));
        }

        char[] previousMoves = initialMoves.toString().toCharArray();
        for (int i = 0; i < 2; i++) {
            StringBuilder nextMoves = new StringBuilder();
            Robot robot2 = new Robot();
            for (char c : previousMoves) {
                nextMoves.append(robot2.getRequiredMovesTo(c));
            }
            previousMoves = null;
            System.gc();
            char[] charArray = new char[nextMoves.length()];
            nextMoves.getChars(0, nextMoves.length(), charArray, 0);
            previousMoves = charArray;
        }

        int length = previousMoves.length;
        int i = Integer.valueOf(input.substring(0, 3));
        long complexity = (long) length * i;

        System.out.println("Part1: Complexity: of " + length + " * " + i + " = " + complexity);
        System.out.println();
        return complexity;
    }

    private String adjustMoves(String moves1) {
        char[] items = moves1.toCharArray();
        char lastChar;
        for (int i = 0; i < items.length - 2; i++) {
            if (items[i] != items[i + 1] && items[i] != 'A' && items[i + 1] != 'A' && items[i + 2] == items[i]) {
                char c1 = items[i];
                items[i] = items[i + 1];
                items[i + 1] = c1;
            }
        }
        return new String(items);
    }

    private void doPart2(String input) {

    }

    private String getInput() {
        return """
                803A
                528A
                586A
                341A
                319A""";
    }

    static class Robot {
        int x = 2;
        int y = 0;

        Robot toApplyTo;

        public static long getComplexity(char toItem, int robotNumber) {
            if (robotNumber == 0) return 1;

            long total = 0;
            String requiredMoves = new Robot().getRequiredMovesTo(toItem).toString();
            for (int i = 0; i < requiredMoves.length(); i++) {
                total += getComplexity(requiredMoves.charAt(i), --robotNumber);
            }
            return total;
        }

        void moveUp() {
            // move to up row.
            y--;
            if (!isValid()) throw new RuntimeException();
        }

        void moveDown() {
            y++;
            if (!isValid()) throw new RuntimeException();
        }

        void moveLeft() {
            x--;
            if (!isValid()) throw new RuntimeException(x + ", " + y);
        }

        void moveRight() {
            x++;
            if (!isValid()) throw new RuntimeException();
        }

        boolean isValid() {
            if (x == 0 && y == 0) return false;
            else return x >= 0 && x <= 2 && y >= 0 && y <= 1;
        }

        void moveInSequence(StringBuilder sequence) {
            char[] charArray = new char[sequence.length()];
            sequence.getChars(0, sequence.length(), charArray, 0);
            for (char c : charArray) {
                switch (c) {
                    case '^' -> moveUp();
                    case 'A' -> press();
                    case '>' -> moveRight();
                    case '<' -> moveLeft();
                    case 'v' -> moveDown();
                }
            }
        }

        StringBuilder getRequiredMovesTo(char toItem) {
            StringBuilder movesToDo = new StringBuilder();
            switch (toItem) {
                case '^' -> {
                    if (x == 2) movesToDo.append("<");
                    if (x == 0) movesToDo.append(">");
                    if (y == 1) movesToDo.append("^");
                }
                case 'A' -> {
                    if (y == 1 && x == 0) movesToDo.append(">>^");
                    if (y == 1 && x == 1) movesToDo.append("^>");
                    if (y == 1 && x == 2) movesToDo.append("^");
                    if (y == 0 && x == 1) movesToDo.append(">");
                }
                case '>' -> {
                    if (y == 0) movesToDo.append("v");
                    if (x == 1) movesToDo.append(">");
                    if (x == 0) movesToDo.append(">>");
                }
                case '<' -> {
                    if (y == 0) movesToDo.append("v");
                    if (x == 1) movesToDo.append("<");
                    if (x == 2) movesToDo.append("<<");
                }
                case 'v' -> {
                    if (x == 2) movesToDo.append("<");
                    if (y == 0) movesToDo.append("v");
                    if (x == 0) movesToDo.append(">");
                }
            }
            movesToDo.append('A');
            moveInSequence(movesToDo);
            return movesToDo;
        }

        void press() {
//            String currentItem = getCurrentItem();
//
//            switch (currentItem) {
//                case "^" -> toApplyTo.moveUp();
//                case "A" -> toApplyTo.press();
//                case ">" -> toApplyTo.moveRight();
//                case "<" -> toApplyTo.moveLeft();
//                case "v" -> toApplyTo.moveDown();
//            }
        }


        String getCurrentItem() {
            if (x == 1 && y == 0) return "^";
            else if (x == 2 && y == 0) return "A";
            else if (x == 0 && y == 1) return "<";
            else if (x == 1 && y == 1) return "v";
            else if (x == 2 && y == 1) return ">";
            else throw new Error("Error: " + x + ", " + y);
        }
    }

    static class NumPadRobot extends Robot {
        String code = "";

        public NumPadRobot() {
            x = 2;
            y = 3;
        }

        @Override
        boolean isValid() {
            if (x == 0 && y == 3) return false;
            else return x >= 0 && x <= 2 && y >= 0 && y <= 3;
        }

        @Override
        StringBuilder getRequiredMovesTo(char toItem) {
            StringBuilder moves = new StringBuilder();
            switch (toItem) {
                case '9' -> {
                    if (y == 1) moves.append("^");
                    if (y == 2) moves.append("^^");
                    if (y == 3) moves.append("^^^");
                    if (x == 0) moves.append(">>");
                    if (x == 1) moves.append(">");
                }
                case '8' -> {
                    if (x == 2) moves.append("<");
                    if (y == 1) moves.append("^");
                    if (y == 2) moves.append("^^");
                    if (y == 3) moves.append("^^^");
                    if (x == 0) moves.append(">");
                }
                case '7' -> {
                    if (y == 3) moves.append("^^^");
                    if (x == 1) moves.append("<");
                    if (x == 2) moves.append("<<");
                    if (y == 1) moves.append("^");
                    if (y == 2) moves.append("^^");
                }
                case '6' -> {
                    if (y == 0) moves.append("v");
                    if (y == 2) moves.append("^");
                    if (y == 3) moves.append("^^");
                    if (x == 0) moves.append(">>");
                    if (x == 1) moves.append(">");
                }
                case '5' -> {
                    if (x == 2) moves.append("<");
                    if (x == 0) moves.append(">");
                    if (y == 0) moves.append("v");
                    if (y == 2) moves.append("^");
                    if (y == 3) moves.append("^^");
                }
                case '4' -> {
                    if (y == 3) moves.append("^^");
                    if (x == 1) moves.append("<");
                    if (x == 2) moves.append("<<");
                    if (y == 0) moves.append("v");
                    if (y == 2) moves.append("^");
                }
                case '3' -> {
                    if (x == 0) moves.append(">>");
                    if (x == 1) moves.append(">");
                    if (y == 0) moves.append("vv");
                    if (y == 1) moves.append("v");
                    if (y == 3) moves.append("^");
                }
                case '2' -> {
                    if (x == 0) moves.append(">");
                    if (x == 2) moves.append("<");
                    if (y == 0) moves.append("vv");
                    if (y == 1) moves.append("v");
                    if (y == 3) moves.append("^");
                }
                case '1' -> {
                    if (y == 3) moves.append("^");
                    if (x == 1) moves.append("<");
                    if (x == 2) moves.append("<<");
                    if (y == 0) moves.append("vv");
                    if (y == 1) moves.append("v");
                }
                case '0' -> {
                    if (x == 2) moves.append("<");
                    if (x == 0) moves.append(">"); // so we don't collide with empty
                    if (y == 0) moves.append("vvv");
                    if (y == 1) moves.append("vv");
                    if (y == 2) moves.append("v");
                }
                case 'A' -> {
                    if (x == 0) moves.append(">>"); // so we don't collide with empty
                    if (y == 0) moves.append("vvv");
                    if (y == 1) moves.append("vv");
                    if (y == 2) moves.append("v");
                    if (x == 1) moves.append(">");
                }
            }
            moves.append('A');
            moveInSequence(moves);
            return moves;
        }

        @Override
        void press() {
//            String currentItem = getCurrentItem();
//
//            switch (currentItem) {
//                case "9" -> code+=currentItem;
//                case "8" -> code+=currentItem;
//                case "7" -> code+=currentItem;
//                case "6" -> code+=currentItem;
//                case "5" -> code+=currentItem;
//                case "4" -> code+=currentItem;
//                case "3" -> code+=currentItem;
//                case "2" -> code+=currentItem;
//                case "1" -> code+=currentItem;
//                case "0" -> code+=currentItem;
//                case "A" -> code+=currentItem;
//            }
//            System.out.println(code);
        }

        @Override
        String getCurrentItem() {
            if (x == 0 && y == 0) return "7";
            else if (x == 1 && y == 0) return "8";
            else if (x == 2 && y == 0) return "9";
            else if (x == 0 && y == 1) return "4";
            else if (x == 1 && y == 1) return "5";
            else if (x == 2 && y == 1) return "6";
            else if (x == 0 && y == 2) return "1";
            else if (x == 1 && y == 2) return "2";
            else if (x == 2 && y == 2) return "3";
            else if (x == 1 && y == 3) return "0";
            else if (x == 2 && y == 3) return "A";
            else throw new Error("Error: " + x + ", " + y);
        }
    }


}
