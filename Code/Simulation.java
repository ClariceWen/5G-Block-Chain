import java.util.function.Supplier;
public class Simulation {

    public static void main(String[] args) {
        Simulation();
    }

    public static void Simulation() {
        Supplier<Integer> hsoSupplier = () -> new HSO().generateSecretKey();
        Supplier<Integer> bsoSupplier = () -> new BSO().generateSecretKey();
        Supplier<Integer> fwaSupplier = () -> new FWA().generateSecretKey();




        System.out.println("Running HSO Algorithm...");
        long startTime = System.currentTimeMillis();
        int hsoKey = hsoSupplier.get();
        long endTime = System.currentTimeMillis();
        double hsoQuality = evaluateKeyQuality(hsoKey);

        System.out.println("HSO Key: " + hsoKey + " Time Taken: " + (endTime - startTime) + " ms");
        System.out.println("HSO Key Quality: " + hsoQuality);


        System.out.println("Running BSO Algorithm...");
        startTime = System.currentTimeMillis();
        int bsoKey = bsoSupplier.get();
        endTime = System.currentTimeMillis();
        double bsoQuality = evaluateKeyQuality(bsoKey);

        System.out.println("BSO Key: " + bsoKey + " Time Taken: " + (endTime - startTime) + " ms");
        System.out.println("BSO Key Quality: " + bsoQuality);


        System.out.println("Running FWA Algorithm...");
        startTime = System.currentTimeMillis();
        int fwaKey = fwaSupplier.get();
        endTime = System.currentTimeMillis();
        double fwaQuality = evaluateKeyQuality(fwaKey);

        System.out.println("FWA Key: " + fwaKey + " Time Taken: " + (endTime - startTime) + " ms");
        System.out.println("FWA Key Quality: " + fwaQuality);

    }
    public static double evaluateKeyQuality(int key) {

        String binaryKey = Integer.toBinaryString(key);

        double lengthScore = binaryKey.length();

        int complexityScore = 0;
        for (int i = 0; i < binaryKey.length() - 1; i++) {
            if (binaryKey.charAt(i) != binaryKey.charAt(i + 1)) {
                complexityScore++;
            }
        }

        return lengthScore + complexityScore;
    }
}