import utilities.MultiThread;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Day07 {

    public static void main(String[] args) {
        new Day07().doChallenge();
    }

    private void doChallenge() {
        String input = getInput();
        doPart1(input);
        doPart2(input);
    }

    private void doPart1(String input) {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        Scanner sc = new Scanner(input);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] lineItems = line.split("\\s+"); // split on 1 or more whitespace
            int item1 = Integer.parseInt(lineItems[0]);
            int item2 = Integer.parseInt(lineItems[1]);

            list1.add(item1);
            list2.add(item2);
        }

        Collections.sort(list1);
        Collections.sort(list2);

        long totalDistances = 0;
        for (int i = 0; i < list1.size(); i++) {
            int distance = Math.abs(list1.get(i) - list2.get(i));
            totalDistances += distance;
        }
        System.out.println("Part 1: " + totalDistances);
    }

    private void doPart2(String input) {
        Map<Integer, Integer> countOfItemsInSecondList = new HashMap<>();

        // Add up counts in the second list.
        Scanner sc = new Scanner(input);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] lineItems = line.split("\\s+"); // split on 1 or more whitespace
            int item2 = Integer.parseInt(lineItems[1]);

            if (countOfItemsInSecondList.containsKey(item2)) {
                countOfItemsInSecondList.put(item2, countOfItemsInSecondList.get(item2) + 1);
            } else {
                countOfItemsInSecondList.put(item2, 1);
            }
        }

        // go through first list and multiply each item against count in 2nd list.
        AtomicInteger similarityScore = new AtomicInteger();
        Scanner sc2 = new Scanner(input);
        while (sc2.hasNextLine()) {
            String line = sc2.nextLine();
            MultiThread.multiThread(()->{
                String[] lineItems = line.split("\\s+"); // split on 1 or more whitespace
                int item1 = Integer.parseInt(lineItems[0]);
                Integer countInSecondList = countOfItemsInSecondList.get(item1);

                long score = countInSecondList == null ? 0 : countInSecondList.longValue() * item1;
                similarityScore.addAndGet((int) score);
            });
        }
        System.out.println("Part 2: " + similarityScore);
    }

    private String getInput() {
        return """
                23238   26034
                94370   90190
                15509   72666
                48816   23909
                31300   40420
                14729   97519""";
    }
}