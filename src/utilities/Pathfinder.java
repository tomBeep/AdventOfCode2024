package utilities;

import java.util.*;
import java.util.function.Function;

public class Pathfinder<Location> {

    private final Function<Location, List<Location>> possiblePathsGetter;

    public Pathfinder(Function<Location, List<Location>> possiblePathsGetter) {
        this.possiblePathsGetter = possiblePathsGetter;
    }

    public List<Location> getShortestPath(Location start, Location end) {
        Queue<List<Location>> possiblePaths = new ArrayDeque<>();
        Set<Location> seenLocations = new HashSet<>();

        possiblePaths.offer(new ArrayList<>(List.of(start)));

        while (!possiblePaths.isEmpty()) {
            List<Location> currentPath = possiblePaths.poll();

            if (!seenLocations.add(currentPath.getLast())) {
                continue;
            }

            if (currentPath.getLast().equals(end)) {
                return currentPath;
            }

            List<Location> nextPossibleSteps = this.possiblePathsGetter.apply(currentPath.getLast());
            for (Location nextStep : nextPossibleSteps) {
                if (seenLocations.contains(nextStep)) {
                    continue;
                }
                List<Location> nextPath = new ArrayList<>(currentPath);
                nextPath.add(nextStep);
                possiblePaths.offer(nextPath);
            }
        }
        return null;
    }

    public List<List<Location>> getAllPossiblePaths(Location start, Location end) {
        List<List<Location>> possiblePaths = new ArrayList<>();

        Stack<List<Location>> partialPaths = new Stack<>();
        Set<List<Location>> seenPaths = new HashSet<>();

        partialPaths.push(new ArrayList<>(List.of(start)));

        while (!partialPaths.isEmpty()) {
            List<Location> currentPath = partialPaths.pop();

            if (!seenPaths.add(currentPath)) {
                continue;
            }

            if (currentPath.getLast().equals(end)) {
                possiblePaths.add(currentPath);
                continue;
            }

            List<Location> nextPossibleSteps = this.possiblePathsGetter.apply(currentPath.getLast());
            for (Location nextStep : nextPossibleSteps) {
                List<Location> nextPath = new ArrayList<>(currentPath);
                nextPath.add(nextStep);
                partialPaths.push(nextPath);
            }
        }
        return null;
    }
}
