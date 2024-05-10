import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HSO {

    // Size of HM Set
    private static final int HM_SIZE = 100;
    // Number of Iterations
    private static final int MAX_ITERATIONS = 1000;
    // HMCR
    private static final double HMCR = 0.9;
    // PAR
    private static final double PAR = 0.3;
    // Mutation Area
    private static final int R = 1000;
    // Bandwidth factor
    private static final double BW = 0.1;

    private Random random;

    public HSO() {
        random = new Random();
    }

    public int generateSecretKey() {

        int[] harmonyMemory = initializeHarmonyMemory();

        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {

            int newHarmony = generateNewHarmony(harmonyMemory);


            updateHarmonyMemory(harmonyMemory, newHarmony);
        }


        int bestHarmony = selectBestHarmony(harmonyMemory);
        return bestHarmony;
    }

    private int[] initializeHarmonyMemory() {
        int[] harmonyMemory = new int[HM_SIZE];
        for (int i = 0; i < HM_SIZE; i++) {
            harmonyMemory[i] = random.nextInt(R) * random.nextInt(R);
        }
        return harmonyMemory;
    }

    private int generateNewHarmony(int[] harmonyMemory) {

        int x = harmonyMemory[random.nextInt(HM_SIZE)];

        if (random.nextDouble() < HMCR) {
            return x;
        }
        else if (random.nextDouble() < PAR) {
            x = adjustPitch(x);
        }

        return x;
    }

    private int adjustPitch(int x) {
        // Mutation Steps
        if (random.nextBoolean()) {
            return (int) (x + BW * R);
        } else {
            return (int) (x - BW * R);
        }
    }

    private void updateHarmonyMemory(int[] harmonyMemory, int newHarmony) {

        int worstIndex = 0;
        double worstFitness = Double.MAX_VALUE;
        for (int i = 0; i < HM_SIZE; i++) {
            double fitness = evaluateFitness(harmonyMemory[i]);
            if (fitness < worstFitness) {
                worstFitness = fitness;
                worstIndex = i;
            }
        }

        if (evaluateFitness(newHarmony) > worstFitness) {
            harmonyMemory[worstIndex] = newHarmony;
        }
    }

    private int selectBestHarmony(int[] harmonyMemory) {
        int bestIndex = 0;
        double bestFitness = Double.MIN_VALUE;
        for (int i = 0; i < HM_SIZE; i++) {
            double fitness = evaluateFitness(harmonyMemory[i]);
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestIndex = i;
            }
        }
        return harmonyMemory[bestIndex];
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
        HSO hso = new HSO();
        int secretKey = hso.generateSecretKey();
        System.out.println("Generated secret key: " + secretKey);
    }
}
