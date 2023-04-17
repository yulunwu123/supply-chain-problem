import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main {

	public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
		String filename = "Complete Graph 14.txt";
		if (args.length == 0) {
			System.out.println("User did not specify test file, using default (Complete Graph 14.txt).");
		}
		else {
			filename = args[0];
			System.out.println("Using " + filename);
		}

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
			    lines.add(line.trim());
            }        

			// Call method and print the result
            Long start = System.currentTimeMillis();
            Supply supply = new Supply();
            int edgeWeightSum = supply.compute(lines);
            System.out.println("edge weight sum: " + edgeWeightSum);
            Long end = System.currentTimeMillis();
            System.out.println("time: " + ((end - start) / 1000.0));

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error occurred when reading file");
		}
	}

}
