import java.util.*;

public class TSP {

    // Function to calculate the total distance of a tour
    public static int calculateDistance(int[] tour, int[][] distanceMatrix) {
        int totalDistance = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            totalDistance += distanceMatrix[tour[i]][tour[i + 1]];
        }
        totalDistance += distanceMatrix[tour[tour.length - 1]][tour[0]];
        return totalDistance;
    }

    // Brute Force TSP Solution
    public static int[] bruteForceTSP(int[][] distanceMatrix) {
        int n = distanceMatrix.length;
        int[] cities = new int[n];
        for (int i = 0; i < n; i++) {
            cities[i] = i;
        }
        int[] minTour = null;
        int minDistance = Integer.MAX_VALUE;

        // Generate all permutations
        List<int[]> permutations = generatePermutations(cities);
        for (int[] tour : permutations) {
            int currentDistance = calculateDistance(tour, distanceMatrix);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                minTour = tour.clone();
            }
        }
        System.out.println("Brute Force TSP Distance: " + minDistance);
        return minTour;
    }

    // Helper function to generate permutations (Brute Force)
    public static List<int[]> generatePermutations(int[] cities) {
        List<int[]> permutations = new ArrayList<>();
        permute(cities, 0, permutations);
        return permutations;
    }

    public static void permute(int[] cities, int start, List<int[]> result) {
        if (start == cities.length - 1) {
            result.add(cities.clone());
        } else {
            for (int i = start; i < cities.length; i++) {
                swap(cities, i, start);
                permute(cities, start + 1, result);
                swap(cities, i, start); // backtrack
            }
        }
    }

    public static void swap(int[] cities, int i, int j) {
        int temp = cities[i];
        cities[i] = cities[j];
        cities[j] = temp;
    }

    // Held-Karp (Dynamic Programming) TSP Solution
    public static int heldKarp(int[][] distanceMatrix) {
        int n = distanceMatrix.length;
        Map<Pair, int[]> C = new HashMap<>();

        // Initialize for subsets of size 2
        for (int i = 1; i < n; i++) {
            C.put(new Pair(1 << i, i), new int[]{distanceMatrix[0][i], 0});
        }

        // Build up solutions for subsets of increasing size
        for (int r = 2; r <= n; r++) {
            for (List<Integer> subset : combinations(range(1, n), r)) {
                int subsetMask = 0;
                for (int i : subset) {
                    subsetMask |= (1 << i);
                }
                for (int j : subset) {
                    int prevMask = subsetMask ^ (1 << j);
                    int[] minCost = new int[]{Integer.MAX_VALUE, -1};
                    for (int k : subset) {
                        if (k != j) {
                            int[] tempCost = C.get(new Pair(prevMask, k));
                            if (tempCost != null) {
                                int newCost = tempCost[0] + distanceMatrix[k][j];
                                if (newCost < minCost[0]) {
                                    minCost = new int[]{newCost, k};
                                }
                            }
                        }
                    }
                    C.put(new Pair(subsetMask, j), minCost);
                }
            }
        }

        int lastMask = (1 << n) - 1;
        int[] optimalCost = new int[]{Integer.MAX_VALUE, -1};
        for (int j = 1; j < n; j++) {
            int[] cost = C.get(new Pair(lastMask, j));
            if (cost != null) {
                int finalCost = cost[0] + distanceMatrix[j][0];
                if (finalCost < optimalCost[0]) {
                    optimalCost = new int[]{finalCost, j};
                }
            }
        }
        return optimalCost[0];
    }

    // Helper function to generate combinations
    public static List<List<Integer>> combinations(List<Integer> list, int k) {
        List<List<Integer>> result = new ArrayList<>();
        combine(list, k, 0, new ArrayList<>(), result);
        return result;
    }

    public static void combine(List<Integer> list, int k, int start, List<Integer> current, List<List<Integer>> result) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
        } else {
            for (int i = start; i < list.size(); i++) {
                current.add(list.get(i));
                combine(list, k, i + 1, current, result);
                current.remove(current.size() - 1);
            }
        }
    }

    // Helper function to generate range
    public static List<Integer> range(int start, int end) {
        List<Integer> result = new ArrayList<>();
        for (int i = start; i < end; i++) {
            result.add(i);
        }
        return result;
    }

    // Pair class to represent a subset and a city
    static class Pair {
        int mask, city;

        Pair(int mask, int city) {
            this.mask = mask;
            this.city = city;
        }

        @Override
        public int hashCode() {
            return Objects.hash(mask, city);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return mask == pair.mask && city == pair.city;
        }
    }

    // Nearest Neighbor (Placeholder)
    public static int[] nearestNeighborTSP(int[][] distanceMatrix) {
        // Placeholder for the nearest neighbor TSP implementation
        return new int[0];
    }

    public static void main(String[] args) {
        int[][] distanceMatrix = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };

        // Brute Force TSP
        System.out.println("Brute Force TSP Tour: " + Arrays.toString(bruteForceTSP(distanceMatrix)));

        // Held-Karp TSP
        System.out.println("Held-Karp TSP Cost: " + heldKarp(distanceMatrix));

        // Nearest Neighbor (Placeholder)
        System.out.println("Nearest Neighbor TSP Tour: " + Arrays.toString(nearestNeighborTSP(distanceMatrix)));
    }
}

