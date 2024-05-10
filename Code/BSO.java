import java.util.Arrays;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BSO {

    // Population
    private static final int POPULATION_SIZE = 100;
    private static final int MAX_ITERATIONS = 1000;
    private static final int R = 1000;

    private Random random;

    public BSO() {
        random = new Random();
    }

    public int generateSecretKey() {

        int[] population = initializePopulation();

        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            // Mutation
            int[] newSolutions = brainstorm(population);

            updatePopulation(population, newSolutions);
        }

        int bestSolution = selectBestSolution(population);
        return bestSolution;
    }

    private int[] initializePopulation() {
        int[] population = new int[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = random.nextInt(R) + 1;
        }
        return population;
    }

    private int[] brainstorm(int[] population) {
        // Mutation based on group
        int[] newSolutions = new int[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            int newSolution = (population[i] + population[POPULATION_SIZE - i - 1] + random.nextInt(R) + 1) / 2;
            newSolutions[i] = newSolution;
        }
        return newSolutions;
    }

    private void updatePopulation(int[] population, int[] newSolutions) {
        // Merge new group
        int[] merged = Arrays.copyOf(population, population.length + newSolutions.length);
        System.arraycopy(newSolutions, 0, merged, population.length, newSolutions.length);
        Arrays.sort(merged);

        System.arraycopy(merged, merged.length - POPULATION_SIZE, population, 0, POPULATION_SIZE);
    }

    private int selectBestSolution(int[] population) {
        int bestIndex = 0;
        double bestFitness = Double.MIN_VALUE;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            double fitness = evaluateFitness(population[i]);
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestIndex = i;
            }
        }
        return population[bestIndex];
    }

    private double evaluateFitness(int x) {
        String numberString = Integer.toString(x);
        int lengthScore = numberString.length();  // L(n)

        // Counting the uniqueness and repetition of digits to calculate R(n)
        Map<Character, Integer> digitCount = new HashMap<>();
        for (char digit : numberString.toCharArray()) {
            digitCount.put(digit, digitCount.getOrDefault(digit, 0) + 1);
        }

        int uniqueDigits = digitCount.size();
        int repeatedPatterns = 0;
        for (int count : digitCount.values()) {
            if (count > 1) {
                repeatedPatterns += count;
            }
        }

        double randomnessScore = uniqueDigits;
        if (uniqueDigits > 1) {  // Avoid division by zero
            randomnessScore -= (double) repeatedPatterns / uniqueDigits;
        }

        // Final fitness score
        return lengthScore + randomnessScore;
    }

    public static void main(String[] args) {
        BSO bso = new BSO();
        int secretKey = bso.generateSecretKey();
        System.out.println("Generated secret key: " + secretKey);
    }
}

