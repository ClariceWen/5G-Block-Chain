import java.util.Arrays;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FWA {

    private static final int POPULATION_SIZE = 100;
    private static final int MAX_ITERATIONS = 1000;
    private static final int SPARKS = 100;
    private static final int R = 1000;

    private Random random;

    public FWA() {
        random = new Random();
    }

    public int generateSecretKey() {

        int[] population = initializePopulation();

        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {

            int[][] sparks = explode(population);


            population = selectAndUpdate(population, sparks);
        }


        int bestSolution = selectBestSolution(population);
        return bestSolution;
    }

    private int[] initializePopulation() {
        int[] population = new int[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = (random.nextInt(R) + random.nextInt(R) + random.nextInt(R)) / 2;
        }
        return population;
    }

    private int[][] explode(int[] population) {
        int[][] sparks = new int[POPULATION_SIZE][SPARKS];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            int amplitude = R / 10;
            for (int j = 0; j < SPARKS; j++) {
                int spark = population[i] * random.nextInt(2 * amplitude) / amplitude;
                sparks[i][j] = (Math.max(spark, 1) + random.nextInt(R) + random.nextInt(R)) / 2;
            }
        }
        return sparks;
    }

    private int[] selectAndUpdate(int[] population, int[][] sparks) {
        int[] combined = new int[POPULATION_SIZE + POPULATION_SIZE * SPARKS];
        System.arraycopy(population, 0, combined, 0, POPULATION_SIZE);
        for (int i = 0; i < POPULATION_SIZE; i++) {
            System.arraycopy(sparks[i], 0, combined, POPULATION_SIZE + i * SPARKS, SPARKS);
        }

        Arrays.sort(combined);


        int[] newPopulation = Arrays.copyOfRange(combined, combined.length - POPULATION_SIZE, combined.length);
        return newPopulation;
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
        FWA fwa = new FWA();
        int secretKey = fwa.generateSecretKey();
        System.out.println("Generated secret key: " + secretKey);
    }
}
