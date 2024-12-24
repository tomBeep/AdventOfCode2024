import utilities.*;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import static utilities.Direction.*;

public class Day20 {

    private static final int PATH_CHEAT_SPEED_INCREASE_MIN = 50;

    public static void main(String[] args) {
        new Day20().doChallenge();
    }

    private void doChallenge() {
        String input = getExampleInput();
//        String input = getInput();
        doPart1(input);
//        doPart2(input);
    }

    private void doPart1(String input) {
        char[][] maze = Parsing.parseInputIntoCharacterGrid(input);

        AtomicInteger atomicStartX = new AtomicInteger(0);
        AtomicInteger atomicStartY = new AtomicInteger(0);
        AtomicInteger atomicEndX = new AtomicInteger(0);
        AtomicInteger atomicEndY = new AtomicInteger(0);

        new Navigator2D(0, 0, maze.length, maze[0].length).iterateInReadingDirection((x, y) -> {
            if (maze[y][x] == 'S') {
                atomicStartX.set(x);
                atomicStartY.set(y);
            }
            if (maze[y][x] == 'E') {
                atomicEndX.set(x);
                atomicEndY.set(y);
            }
        });

        Point start = new Point(atomicStartX.get(), atomicStartY.get());
        Point end = new Point(atomicEndX.get(), atomicEndY.get());

        int numberOfCheats = evaluateGoodCheatsFast(maze, start, end);
        System.out.println("Part 1: " + numberOfCheats);

        int numberOfCheats2 = evaluateGoodCheatsMultiple(maze, start, end);
        System.out.println("Part 2: " + numberOfCheats2);
    }

    private static int evaluateGoodCheatsFast(char[][] maze, Point start, Point end) {
        Function<Point, List<Point>> getterFunction = (point) ->
                Stream.of(
                                NORTH.getPointBeingMovedTo(point),
                                EAST.getPointBeingMovedTo(point),
                                SOUTH.getPointBeingMovedTo(point),
                                WEST.getPointBeingMovedTo(point)
                        )
                        .filter(p -> maze[p.y][p.x] != '#')
                        .toList();

        Pathfinder<Point> pathfinder = new Pathfinder<>(getterFunction);
        List<Point> basePath = pathfinder.getShortestPath(start, end);

        Map<Point, Integer> basePathAsIndexedSet = new HashMap<>();
        for (int i = 0; i < basePath.size(); i++) {
            Point point = basePath.get(i);
            basePathAsIndexedSet.put(point, i);
        }

        int numberOfCheatsThatSave100Seconds = 0;
        for (int i = 1; i < maze.length - 1; i++) {
            for (int j = 1; j < maze[i].length - 1; j++) {
                Point potentialCheatPoint = new Point(j, i);


                if (maze[i][j] != '#') {
                    continue;
                }
                int numberOfAdjacentDots = (maze[i][j + 1] != '#' ? 1 : 0) + (maze[i][j - 1] != '#' ? 1 : 0) + (maze[i + 1][j] != '#' ? 1 : 0) + (maze[i - 1][j] != '#' ? 1 : 0);
                if (numberOfAdjacentDots < 2) {
                    continue;
                }

                int minIndex = Integer.MAX_VALUE;
                int maxIndex = Integer.MIN_VALUE;
                for (Direction direction : new Direction[]{NORTH, SOUTH, EAST, WEST}) {
                    Point pointToConsider = direction.getPointBeingMovedTo(potentialCheatPoint);
                    Integer index = basePathAsIndexedSet.get(pointToConsider);
                    if (index == null) continue;
                    if (index < minIndex) minIndex = index;
                    if (index > maxIndex) maxIndex = index;
                }

                int movesWithCheat = maxIndex - minIndex - 2;

                if (movesWithCheat >= PATH_CHEAT_SPEED_INCREASE_MIN) {
                    numberOfCheatsThatSave100Seconds++;
                }
            }
        }


        return numberOfCheatsThatSave100Seconds;
    }

    private static int evaluateGoodCheatsMultiple(char[][] maze, Point start, Point end) {
        Function<Point, List<Point>> getterFunction = (point) ->
                Stream.of(
                                NORTH.getPointBeingMovedTo(point),
                                EAST.getPointBeingMovedTo(point),
                                SOUTH.getPointBeingMovedTo(point),
                                WEST.getPointBeingMovedTo(point)
                        )
                        .filter(p -> maze[p.y][p.x] != '#')
                        .toList();

        Pathfinder<Point> pathfinder = new Pathfinder<>(getterFunction);
        List<Point> basePath = pathfinder.getShortestPath(start, end);

        Map<Point, Integer> basePathAsIndexedSet = new HashMap<>();
        for (int i = 0; i < basePath.size(); i++) {
            Point point = basePath.get(i);
            basePathAsIndexedSet.put(point, i);
        }

        Set<Point> seenCheatStartPoints = new HashSet<>();

        int numberOfCheatsThatSave100Seconds = 0;
        for (int i = 1; i < maze.length - 1; i++) {
            for (int j = 1; j < maze[i].length - 1; j++) {
                if (maze[i][j] != '#') {
                    continue;
                }

                int numberOfAdjacentDots = (maze[i][j + 1] != '#' ? 1 : 0) + (maze[i][j - 1] != '#' ? 1 : 0) + (maze[i + 1][j] != '#' ? 1 : 0) + (maze[i - 1][j] != '#' ? 1 : 0);
                if (numberOfAdjacentDots < 1) {
                    continue;
                }

                Point cheatStart = new Point(j, i);
                int earliestPathPointToCheat = Stream.of(
                                NORTH.getPointBeingMovedTo(cheatStart),
                                EAST.getPointBeingMovedTo(cheatStart),
                                SOUTH.getPointBeingMovedTo(cheatStart),
                                WEST.getPointBeingMovedTo(cheatStart)
                        )
                        .filter(p -> maze[p.y][p.x] != '#')
                        .mapToInt(p -> basePathAsIndexedSet.get(p))
                        .min()
                        .getAsInt();

                if(!seenCheatStartPoints.add(basePath.get(earliestPathPointToCheat))){
                    continue;
                }

                Set<Point> possibleCheatEndPoints = new HashSet<>();
                getPossibleExitsWithin(maze, cheatStart, 20, possibleCheatEndPoints, new HashSet<>());

                for (Point cheatEnd : possibleCheatEndPoints) {
                    int pathEndPoint = basePathAsIndexedSet.get(cheatEnd);

                    int movesSaved = earliestPathPointToCheat - pathEndPoint - 2;

                    if(movesSaved == 76){
                        System.out.println(cheatStart +", "+cheatEnd);
                    }
                    if (movesSaved >= PATH_CHEAT_SPEED_INCREASE_MIN) {
                        numberOfCheatsThatSave100Seconds++;
                    }
                }


            }
        }


        return numberOfCheatsThatSave100Seconds;
    }

    private static void getPossibleExitsWithin(char[][] maze, Point cheatStartPoint, int secondsOfPassage, Set<Point> possibleCheatEndPoints, Set<Point> seenPoints) {
        if (secondsOfPassage == 0) return;
        for (Direction direction : Direction.getMain4()) {
            Point p = direction.getPointBeingMovedTo(cheatStartPoint);
            if (p.y >= maze.length || p.y < 0 || p.x < 0 || p.x >= maze[p.y].length) {
                continue;
            } else if (!seenPoints.add(p)) {
                continue;
            } else if (maze[p.y][p.x] != '#') {
                possibleCheatEndPoints.add(p);
            } else {
                getPossibleExitsWithin(maze, p, secondsOfPassage - 1, possibleCheatEndPoints, seenPoints);
            }
        }
    }


    private String getExampleInput() {
        return """
                ###############
                #...#...#.....#
                #.#.#.#.#.###.#
                #S#...#.#.#...#
                #######.#.#.###
                #######.#.#...#
                #######.#.###.#
                ###..E#...#...#
                ###.#######.###
                #...###...#...#
                #.#####.#.###.#
                #.#...#.#.#...#
                #.#.#.#.#.#.###
                #...#...#...###
                ###############""";
    }

    private String getInput() {
        return """
                #############################################################################################################################################
                #...........###...#...........###...###...#.....#...#...###.......#.....#.......#...#.......###...#...#.....#...#...#...###...#.............#
                #.#########.###.#.#.#########.###.#.###.#.#.###.#.#.#.#.###.#####.#.###.#.#####.#.#.#.#####.###.#.#.#.#.###.#.#.#.#.#.#.###.#.#.###########.#
                #.#...#...#...#.#.#.........#.#...#.#...#.#...#.#.#.#.#.#...#...#.#.#...#.....#.#.#.#.....#...#.#.#.#.#...#...#.#.#.#.#...#.#...#...#.......#
                #.#.#.#.#.###.#.#.#########.#.#.###.#.###.###.#.#.#.#.#.#.###.#.#.#.#.#######.#.#.#.#####.###.#.#.#.#.###.#####.#.#.#.###.#.#####.#.#.#######
                #...#...#...#.#.#...#.....#.#.#...#.#...#...#.#.#.#.#.#.#.#...#...#.#.#...###.#.#.#.#...#...#...#...#.#...#.....#.#...#...#...#...#...#.....#
                ###########.#.#.###.#.###.#.#.###.#.###.###.#.#.#.#.#.#.#.#.#######.#.#.#.###.#.#.#.#.#.###.#########.#.###.#####.#####.#####.#.#######.###.#
                #...........#.#.#...#...#.#.#.#...#...#.#...#.#.#.#.#.#...#.#...###.#.#.#.#...#...#...#...#.#.........#...#.....#...#...#.....#.....#...#...#
                #.###########.#.#.#####.#.#.#.#.#####.#.#.###.#.#.#.#.#####.#.#.###.#.#.#.#.#############.#.#.###########.#####.###.#.###.#########.#.###.###
                #...........#.#.#.#.....#.#.#.#.....#.#.#.#...#...#...#...#.#.#.....#...#.#.#...#.......#...#...###...###.#...#.#...#...#...#...#...#...#.###
                ###########.#.#.#.#.#####.#.#.#####.#.#.#.#.###########.#.#.#.###########.#.#.#.#.#####.#######.###.#.###.#.#.#.#.#####.###.#.#.#.#####.#.###
                #.....#.....#...#.#.....#.#.#.###...#.#.#.#...#.........#...#.#...........#...#...#...#.....#...#...#.#...#.#...#.....#.....#.#...#...#.#...#
                #.###.#.#########.#####.#.#.#.###.###.#.#.###.#.#############.#.###################.#.#####.#.###.###.#.###.#########.#######.#####.#.#.###.#
                #...#.#.........#.#...#.#.#.#...#.#...#.#.#...#.....#...###...#...........###.......#.....#.#.#...#...#...#...###...#.#.......#...#.#.#...#.#
                ###.#.#########.#.#.#.#.#.#.###.#.#.###.#.#.#######.#.#.###.#############.###.###########.#.#.#.###.#####.###.###.#.#.#.#######.#.#.#.###.#.#
                ###.#.#...#...#.#.#.#.#.#.#...#.#.#...#.#...#.......#.#.#...#.............#...#.........#.#.#...#...#...#...#.#...#...#...#.....#.#.#...#.#.#
                ###.#.#.#.#.#.#.#.#.#.#.#.###.#.#.###.#.#####.#######.#.#.###.#############.###.#######.#.#.#####.###.#.###.#.#.#########.#.#####.#.###.#.#.#
                #...#...#...#...#.#.#.#.#.###.#.#.#...#.....#.#...###.#.#...#.#...#...#...#...#.#.......#...#.....###.#.....#...#.........#.#.....#.#...#.#.#
                #.###############.#.#.#.#.###.#.#.#.#######.#.#.#.###.#.###.#.#.#.#.#.#.#.###.#.#.###########.#######.###########.#########.#.#####.#.###.#.#
                #.#...#.........#...#...#.#...#...#.#...#...#...#...#.#.#...#...#.#.#.#.#...#...#...........#...#...#.........#...###...###.#.......#.....#.#
                #.#.#.#.#######.#########.#.#######.#.#.#.#########.#.#.#.#######.#.#.#.###.###############.###.#.#.#########.#.#####.#.###.###############.#
                #...#...#...###.........#.#...#.....#.#...#.........#.#.#...#.....#.#.#.#...#.........#...#.#...#.#...###...#.#.....#.#.#...#...............#
                #########.#.###########.#.###.#.#####.#####.#########.#.###.#.#####.#.#.#.###.#######.#.#.#.#.###.###.###.#.#.#####.#.#.#.###.###############
                #.....#...#.......#.....#.#...#...###.....#.....#...#.#.#...#.#...#.#.#.#...#.......#...#.#.#.###.#...#...#.#.#.....#.#.#...#.###.......#...#
                #.###.#.#########.#.#####.#.#####.#######.#####.#.#.#.#.#.###.#.#.#.#.#.###.#######.#####.#.#.###.#.###.###.#.#.#####.#.###.#.###.#####.#.#.#
                #...#.#.........#.#.....#.#.....#.......#.#.....#.#.#.#.#...#.#.#.#.#.#...#...#...#.....#...#...#.#...#...#.#.#.....#.#.#...#...#...#...#.#.#
                ###.#.#########.#.#####.#.#####.#######.#.#.#####.#.#.#.###.#.#.#.#.#.###.###.#.#.#####.#######.#.###.###.#.#.#####.#.#.#.#####.###.#.###.#.#
                #...#.........#.#.......#.....#.#.......#.#.....#.#.#.#.#...#.#.#.#.#...#.#...#.#.#...#.#.......#...#...#.#.#.#.....#.#...#...#.....#.....#.#
                #.###########.#.#############.#.#.#######.#####.#.#.#.#.#.###.#.#.#.###.#.#.###.#.#.#.#.#.#########.###.#.#.#.#.#####.#####.#.#############.#
                #...........#...#.....#.....#...#...###...#.....#.#.#.#.#...#...#.#...#...#.#...#.#.#.#.#.###...###...#...#.#.#.....#.#.....#.#...#.........#
                ###########.#####.###.#.###.#######.###.###.#####.#.#.#.###.#####.###.#####.#.###.#.#.#.#.###.#.#####.#####.#.#####.#.#.#####.#.#.#.#########
                #...#.......#.....###...###...#...#.#...#...#...#.#...#...#.#.....###...#...#...#.#.#...#...#.#.#...#.#.....#.....#...#.....#...#...#.......#
                #.#.#.#######.###############.#.#.#.#.###.###.#.#.#######.#.#.#########.#.#####.#.#.#######.#.#.#.#.#.#.#########.#########.#########.#####.#
                #.#.#.........#...#...#...###...#.#.#...#.###.#.#.#.......#.#.#...#...#.#.#...#.#.#.....#...#.#.#.#.#.#.###...###...#.....#.#.....###.#.....#
                #.#.###########.#.#.#.#.#.#######.#.###.#.###.#.#.#.#######.#.#.#.#.#.#.#.#.#.#.#.#####.#.###.#.#.#.#.#.###.#.#####.#.###.#.#.###.###.#.#####
                #.#.............#...#...#.........#...#.#.#...#.#.#.#...#...#.#.#...#.#.#.#.#.#.#.#...#.#.#...#.#.#.#.#...#.#.#...#.#.###...#.#...#...#...###
                #.###################################.#.#.#.###.#.#.#.#.#.###.#.#####.#.#.#.#.#.#.#.#.#.#.#.###.#.#.#.###.#.#.#.#.#.#.#######.#.###.#####.###
                #...............................#...#...#...###.#.#...#.#...#.#...#...#.#...#.#.#.#.#...#.#...#.#.#...###...#...#.#.#...#...#.#.....#...#...#
                ###############################.#.#.###########.#.#####.###.#.###.#.###.#####.#.#.#.#####.###.#.#.###############.#.###.#.#.#.#######.#.###.#
                #...#...###...#.................#.#.#...#.....#.#.#.....#...#.#...#...#...#...#.#.#...###.#...#.#.#...............#.#...#.#.#.#.......#.....#
                #.#.#.#.###.#.#.#################.#.#.#.#.###.#.#.#.#####.###.#.#####.###.#.###.#.###.###.#.###.#.#.###############.#.###.#.#.#.#############
                #.#.#.#.#...#...#...#.............#...#...#...#...#.....#.#...#.....#...#.#...#.#...#...#.#...#...#.......#.........#.....#...#...#.....#...#
                #.#.#.#.#.#######.#.#.#####################.###########.#.#.#######.###.#.###.#.###.###.#.###.###########.#.#####################.#.###.#.#.#
                #.#.#.#.#...#.....#...#...................#.....#.......#.#.#...#...#...#...#.#.###...#.#.....#...........#...#.....#.....#.......#.#...#.#.#
                #.#.#.#.###.#.#########.#################.#####.#.#######.#.#.#.#.###.#####.#.#.#####.#.#######.#############.#.###.#.###.#.#######.#.###.#.#
                #.#...#.....#.#.........#...#...#.......#...#...#...#...#.#.#.#.#.###.#...#.#...#.....#...#...#.....#.........#...#.#.#...#.........#...#.#.#
                #.###########.#.#########.#.#.#.#.#####.###.#.#####.#.#.#.#.#.#.#.###.#.#.#.#####.#######.#.#.#####.#.###########.#.#.#.###############.#.#.#
                #.#...#...#...#...#.....#.#.#.#...#...#...#...#...#.#.#.#.#.#.#.#.#...#.#.#.....#.......#...#.....#...#.....#.....#.#.#...#...#...#...#...#.#
                #.#.#.#.#.#.#####.#.###.#.#.#.#####.#.###.#####.#.#.#.#.#.#.#.#.#.#.###.#.#####.#######.#########.#####.###.#.#####.#.###.#.#.#.#.#.#.#####.#
                #...#...#...#.....#.#...#.#...#...#.#...#.......#.#.#.#.#.#.#.#.#.#...#.#.#...#.#.......#...#.....#...#...#.#.....#.#.###.#.#.#.#.#.#.#.....#
                #############.#####.#.###.#####.#.#.###.#########.#.#.#.#.#.#.#.#.###.#.#.#.#.#.#.#######.#.#.#####.#.###.#.#####.#.#.###.#.#.#.#.#.#.#.#####
                #.....###...#.....#.#...#.#.....#.#...#.#.........#...#...#...#...###...#.#.#...#.......#.#.#...#...#.....#.......#...#...#.#.#.#.#.#.#.#...#
                #.###.###.#.#####.#.###.#.#.#####.###.#.#.###############################.#.###########.#.#.###.#.#####################.###.#.#.#.#.#.#.#.#.#
                #...#.....#.#.....#.###...#.....#.....#...###############...###...........#...#.........#.#.#...#.....#.......#...#...#...#.#.#.#...#.#...#.#
                ###.#######.#.#####.###########.#########################.#.###.#############.#.#########.#.#.#######.#.#####.#.#.#.#.###.#.#.#.#####.#####.#
                ###.......#.#.......#...#...###.........###############...#...#.#...#...#...#.#.#...#...#.#.#.#.......#.#.....#.#.#.#.###...#...#...#.....#.#
                #########.#.#########.#.#.#.###########.###############.#####.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#######.#.#####.#.#.#.###########.#.#####.#.#
                #.........#.....#.....#...#.............###############...#...#...#...#.#.#.#.#...#...#...#...#.........#.....#.#...#.............#.....#.#.#
                #.#############.#.#######################################.#.###########.#.#.#.###############################.#.#######################.#.#.#
                #...#.........#.#.......................###############...#.........###...#...#.....###...#...#...............#.#.................#.....#...#
                ###.#.#######.#.#######################.###############.###########.###########.###.###.#.#.#.#.###############.#.###############.#.#########
                ###...#.......#.......................#...#############S#.....#.....#...#...###...#.....#.#.#.#.................#.............###.#.........#
                #######.#############################.###.###############.###.#.#####.#.#.#.#####.#######.#.#.###############################.###.#########.#
                #.......#...#...................#####.....#######.........###...###...#...#...###.......#.#.#.#...###...#.....#...#...#.....#...#.#.....#...#
                #.#######.#.#.#################.#################.#################.#########.#########.#.#.#.#.#.###.#.#.###.#.#.#.#.#.###.###.#.#.###.#.###
                #...#.....#...#.................#################...........#.....#.#...#...#...#.......#...#.#.#.#...#...###...#...#.#...#.#...#.#...#.#...#
                ###.#.#########.###########################################.#.###.#.#.#.#.#.###.#.###########.#.#.#.#################.###.#.#.###.###.#.###.#
                ###.#.#.......#...#...#.........#E#...................#...#...#...#.#.#...#...#...#.........#...#.#...#.............#.#...#...###...#.#.....#
                ###.#.#.#####.###.#.#.#.#######.#.#.#################.#.#.#####.###.#.#######.#####.#######.#####.###.#.###########.#.#.###########.#.#######
                #...#.#.....#...#...#...###...#...#.................#...#.....#...#...#.....#...#...#...###.....#.....#.#...........#...#.........#.#.#.....#
                #.###.#####.###.###########.#.#####################.#########.###.#####.###.###.#.###.#.#######.#######.#.###############.#######.#.#.#.###.#
                #.....#...#...#.............#.................#...#.......###...#.....#...#.###...#...#.........#...#...#...#.......#...#.#.......#...#...#.#
                #######.#.###.###############################.#.#.#######.#####.#####.###.#.#######.#############.#.#.#####.#.#####.#.#.#.#.#############.#.#
                #.......#...#.......#.......................#...#.......#.....#.#.....#...#.#.......#...#.....#...#...###...#.....#...#...#.....#...#...#.#.#
                #.#########.#######.#.#####################.###########.#####.#.#.#####.###.#.#######.#.#.###.#.#########.#######.#############.#.#.#.#.#.#.#
                #.#.......#.......#.#.#.........###...#...#.....###...#.......#...#...#.#...#.........#.#...#.#.#.........#...###.#.............#.#...#.#.#.#
                #.#.#####.#######.#.#.#.#######.###.#.#.#.#####.###.#.#############.#.#.#.#############.###.#.#.#.#########.#.###.#.#############.#####.#.#.#
                #.#.#...#.....#...#...#.#.....#.....#...#.....#.#...#.............#.#.#.#...###...#...#.....#...#...........#.....#.....#...#...#...#...#.#.#
                #.#.#.#.#####.#.#######.#.###.###############.#.#.###############.#.#.#.###.###.#.#.#.#################################.#.#.#.#.###.#.###.#.#
                #.#.#.#.......#...#...#...###...............#...#...#.............#.#.#.#...#...#...#.#.......###...#.............#.....#.#...#.....#.#...#.#
                #.#.#.###########.#.#.#####################.#######.#.#############.#.#.#.###.#######.#.#####.###.#.#.###########.#.#####.###########.#.###.#
                #.#.#...........#.#.#.#...#...#...#.......#.....###.#.#.....###...#.#.#.#...#.......#...#.....#...#.#.....###...#...###...#.........#...#...#
                #.#.###########.#.#.#.#.#.#.#.#.#.#.#####.#####.###.#.#.###.###.#.#.#.#.###.#######.#####.#####.###.#####.###.#.#######.###.#######.#####.###
                #.#.#.......###.#...#...#...#...#.#...#...#...#.....#.#...#.#...#...#.#.#...#.......#...#.......#...#...#.....#...###...#...#...###.#.....###
                #.#.#.#####.###.#################.###.#.###.#.#######.###.#.#.#######.#.#.###.#######.#.#########.###.#.#########.###.###.###.#.###.#.#######
                #.#.#.#...#.#...#.......#.......#.....#.....#.#.....#...#.#...#.......#.#...#.........#...#.....#.....#.........#...#.#...#...#...#...#...###
                #.#.#.#.#.#.#.###.#####.#.#####.#############.#.###.###.#.#####.#######.###.#############.#.###.###############.###.#.#.###.#####.#####.#.###
                #.#.#...#.#.#...#...###...#.....#...........#.#.###.....#.#.....###...#.#...#.........###...###.#.............#...#...#.###...#...#.....#...#
                #.#.#####.#.###.###.#######.#####.#########.#.#.#########.#.#######.#.#.#.###.#######.#########.#.###########.###.#####.#####.#.###.#######.#
                #.#.#.....#.....#...#.......#...#.#.........#...#...#.....#...#...#.#.#.#.###...#.....#.......#...#...........###.....#.......#.....#...#...#
                #.#.#.###########.###.#######.#.#.#.#############.#.#.#######.#.#.#.#.#.#.#####.#.#####.#####.#####.#################.###############.#.#.###
                #...#...#...#...#...#.....#...#.#.#.#...###.......#.#.....#...#.#.#.#.#.#.....#.#.#...#.....#.......#...............#.#.....#...#.....#...###
                #######.#.#.#.#.###.#####.#.###.#.#.#.#.###.#######.#####.#.###.#.#.#.#.#####.#.#.#.#.#####.#########.#############.#.#.###.#.#.#.###########
                #.......#.#...#.....#...#...###...#...#...#.#...#...#...#.#...#.#.#.#.#...#...#.#.#.#.#...#.....#...#.#.............#...###.#.#.#...........#
                #.#######.###########.#.#################.#.#.#.#.###.#.#.###.#.#.#.#.###.#.###.#.#.#.#.#.#####.#.#.#.#.###################.#.#.###########.#
                #...#...#...#.........#...###.............#.#.#...#...#...#...#.#.#.#.....#.....#.#.#.#.#.###...#.#...#...................#.#.#...#.....#...#
                ###.#.#.###.#.###########.###.#############.#.#####.#######.###.#.#.#############.#.#.#.#.###.###.#######################.#.#.###.#.###.#.###
                ###...#.....#...........#...#.............#.#.....#.......#.###.#.#.......#.......#.#.#.#...#.....###...#.............#...#...#...#.###...###
                #######################.###.#############.#.#####.#######.#.###.#.#######.#.#######.#.#.###.#########.#.#.###########.#.#######.###.#########
                #...................###...#...............#.....#...#.....#...#.#.#...#...#.....#...#...#...###...#...#...#.....#...#...###...#...#.....#...#
                #.#################.#####.#####################.###.#.#######.#.#.#.#.#.#######.#.#######.#####.#.#.#######.###.#.#.#######.#.###.#####.#.#.#
                #.................#.#...#...........#.....#...#.#...#.....#...#.#.#.#.#.#.......#.......#.#...#.#.#.......#...#...#.........#...#.....#...#.#
                #################.#.#.#.###########.#.###.#.#.#.#.#######.#.###.#.#.#.#.#.#############.#.#.#.#.#.#######.###.#################.#####.#####.#
                #...#...#...#...#.#...#.......#.....#.#...#.#.#.#.#...###.#.###.#.#.#.#.#.#...###...###.#.#.#.#.#.#...###.....#...#...........#.....#.....#.#
                #.#.#.#.#.#.#.#.#.###########.#.#####.#.###.#.#.#.#.#.###.#.###.#.#.#.#.#.#.#.###.#.###.#.#.#.#.#.#.#.#########.#.#.#########.#####.#####.#.#
                #.#...#...#...#.#...........#.#.....#.#.#...#.#.#.#.#.#...#...#.#.#.#.#.#.#.#...#.#.#...#...#...#.#.#.#.......#.#.#.........#.#...#.#.....#.#
                #.#############.###########.#.#####.#.#.#.###.#.#.#.#.#.#####.#.#.#.#.#.#.#.###.#.#.#.###########.#.#.#.#####.#.#.#########.#.#.#.#.#.#####.#
                #.......#.....#.#...........#.......#.#.#...#.#.#.#.#...#.....#.#...#.#.#.#.#...#.#.#.......#.....#.#.#...###.#.#.#.....#...#...#...#.#...#.#
                #######.#.###.#.#.###################.#.###.#.#.#.#.#####.#####.#####.#.#.#.#.###.#.#######.#.#####.#.###.###.#.#.#.###.#.###########.#.#.#.#
                #.......#.#...#.#...................#.#.#...#.#.#...#.....#...#.....#.#.#.#.#...#.#...#.....#...#...#.#...#...#.#.#...#...#.....#...#.#.#.#.#
                #.#######.#.###.###################.#.#.#.###.#.#####.#####.#.#####.#.#.#.#.###.#.###.#.#######.#.###.#.###.###.#.###.#####.###.#.#.#.#.#.#.#
                #.#.......#...#.....................#.#.#...#.#.#.....#.....#...#...#.#.#...#...#...#.#.#.......#...#.#.###...#.#.#...#...#.#...#.#.#.#.#.#.#
                #.#.#########.#######################.#.###.#.#.#.#####.#######.#.###.#.#####.#####.#.#.#.#########.#.#.#####.#.#.#.###.#.#.#.###.#.#.#.#.#.#
                #...#.........#.......#.....#...#...#.#...#.#...#.....#...#.....#.###.#.#...#.#.....#.#.#...#...#...#.#.....#...#.#.....#...#...#.#.#...#...#
                #####.#########.#####.#.###.#.#.#.#.#.###.#.#########.###.#.#####.###.#.#.#.#.#.#####.#.###.#.#.#.###.#####.#####.#############.#.#.#########
                #...#.........#.#...#...###.#.#...#...###.#.........#.....#.....#.#...#.#.#...#.....#...###...#.#...#...#...#.....#.....#.....#...#...#.....#
                #.#.#########.#.#.#.#######.#.###########.#########.###########.#.#.###.#.#########.###########.###.###.#.###.#####.###.#.###.#######.#.###.#
                #.#...#.......#.#.#.........#...#.........#.........###...#.....#.#...#.#...#.......#...........#...#...#...#.......#...#...#...#...#...#...#
                #.###.#.#######.#.#############.#.#########.###########.#.#.#####.###.#.###.#.#######.###########.###.#####.#########.#####.###.#.#.#####.###
                #...#.#...#.....#.#...#...#...#.#.........#...........#.#...#.....###...###.#.....#...#.......#...#...#...#.........#.#.....#...#.#.....#...#
                ###.#.###.#.#####.#.#.#.#.#.#.#.#########.###########.#.#####.#############.#####.#.###.#####.#.###.###.#.#########.#.#.#####.###.#####.###.#
                #...#.#...#.....#...#.#.#.#.#...#.........#...#.......#.#...#...###.........#.....#...#.#...#.#.###...#.#...#.....#.#.#...###...#...#...#...#
                #.###.#.#######.#####.#.#.#.#####.#########.#.#.#######.#.#.###.###.#########.#######.#.#.#.#.#.#####.#.###.#.###.#.#.###.#####.###.#.###.###
                #...#...#.....#.#.....#.#.#.#...#.....#...#.#.#.....###...#...#.#...#.....#...#...###...#.#...#.....#.#.#...#...#...#.....#...#...#.#...#...#
                ###.#####.###.#.#.#####.#.#.#.#.#####.#.#.#.#.#####.#########.#.#.###.###.#.###.#.#######.#########.#.#.#.#####.###########.#.###.#.###.###.#
                #...#...#...#.#.#.#...#.#.#.#.#.......#.#.#.#.#...#.........#.#.#...#.#...#.....#.....#...#...#.....#.#.#.#...#...###...#...#...#.#.#...#...#
                #.###.#.###.#.#.#.#.#.#.#.#.#.#########.#.#.#.#.#.#########.#.#.###.#.#.#############.#.###.#.#.#####.#.#.#.#.###.###.#.#.#####.#.#.#.###.###
                #.....#.#...#.#.#...#.#.#.#.#.#.....#...#.#.#.#.#.#...#.....#.#...#...#.#...#...#...#.#...#.#.#.....#.#.#.#.#...#.....#...#.....#...#...#...#
                #######.#.###.#.#####.#.#.#.#.#.###.#.###.#.#.#.#.#.#.#.#####.###.#####.#.#.#.#.#.#.#.###.#.#.#####.#.#.#.#.###.###########.###########.###.#
                #.......#...#...###...#.#.#.#.#.###...###.#.#...#.#.#.#...###.#...#...#.#.#.#.#.#.#...###...#.#...#.#.#.#.#...#.#...#.....#.......#...#...#.#
                #.#########.#######.###.#.#.#.#.#########.#.#####.#.#.###.###.#.###.#.#.#.#.#.#.#.###########.#.#.#.#.#.#.###.#.#.#.#.###.#######.#.#.###.#.#
                #.....#.....#.......#...#...#...#.........#.....#.#.#...#...#.#.....#.#...#...#.#.#...###.....#.#.#.#.#.#.#...#...#.#...#.#.......#.#.#...#.#
                #####.#.#####.#######.###########.#############.#.#.###.###.#.#######.#########.#.#.#.###.#####.#.#.#.#.#.#.#######.###.#.#.#######.#.#.###.#
                #.....#.#...#.#...#...###.......#.......#...#...#.#...#...#.#.#...#...#.........#...#...#.....#.#...#...#.#.......#.#...#.#.........#.#...#.#
                #.#####.#.#.#.#.#.#.#####.#####.#######.#.#.#.###.###.###.#.#.#.#.#.###.###############.#####.#.#########.#######.#.#.###.###########.###.#.#
                #...#...#.#...#.#...#.....#...#.........#.#.#...#...#.#...#.#.#.#.#.###.....#...#...#...#.....#.......###.#.....#.#.#.###.....#.....#...#.#.#
                ###.#.###.#####.#####.#####.#.###########.#.###.###.#.#.###.#.#.#.#.#######.#.#.#.#.#.###.###########.###.#.###.#.#.#.#######.#.###.###.#.#.#
                #...#.#...#...#...#...#...#.#...#.....#...#.#...#...#.#...#.#.#.#.#...#.....#.#.#.#.#...#.#...#...#...#...#...#.#.#.#.......#.#...#.#...#.#.#
                #.###.#.###.#.###.#.###.#.#.###.#.###.#.###.#.###.###.###.#.#.#.#.###.#.#####.#.#.#.###.#.#.#.#.#.#.###.#####.#.#.#.#######.#.###.#.#.###.#.#
                #.....#.....#.....#.....#...###...###...###...###.....###...#...#.....#.......#...#.....#...#...#...###.......#...#.........#.....#...###...#
                #############################################################################################################################################""";
    }
}