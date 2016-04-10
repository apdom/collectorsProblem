/**
  * Title: Sidewalk Raindrop Simulation
  * Description: Rain begins to fall on a sidewalk of length L. 
  * Each rain droplet has diameter d and falls according to a unif(0, L). 
  * Let X be the number of rain drops until the sidewalk is completely
  * wet. Simulate the distribution of X.
  * Assume the rain drops do not evaporate.
  * Note that the rain drops can overlap each other on the sidewalk.
  * In some sense, this is a continuous version of the `Collector's Problem'.
  *
  * Date: April 9, 2016
  *
  * Files:
  *		1) Plots.java.......Plotting functions. Runs simulation, outputs csv.
  *		2) Sidewalk.java....Contains simulation function.
  *		3) Droplet.java.....Stores rain drop object. Helper class for Sidewalk.
  *
  * Usage:
  * 		Alter the inputs of the various plotting methods in main()
  *			> javac *.java 
  *			> java Plots
  *			csv files will be output to the current working directory
  *			Upload the csv files into R and plot / analyze from R.
  *
  * Future Work:
  * 	Q) How does E(X) look as a function of sidewalk length and drop diameter?
  * 	Q) How does quantile(x, 0.01) look a function of sidewalk len and drop diam?
  * 	Q) Let the drops follow Poison Process with rate lamba and the drops
  * 		evaporate according to Poison Process with rate mu. How does 
  * 		covarage and the number of drops distribution changes as the ratio
  *			lambda / mu changes?
  * 	Q) Repeate the analysis in 2 dimensions. That is, consider a square sidewalk
  * 		of length L and width L and drops of diameters d. 
  *		Q) Can the analysis be generalized to any dimension d?
  */

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class Plots {

	/** Given a sidewalk length and drop diameter, outputs a csv,
	  * where each row is the amount of the sidewalk that is wet.
	  * Take this .csv into R for plotting.
	  */
	public void plotCoverage(double sidewalkLength, double dropDiameter) {
		String fileName = "Coverage_sidewalkLength_" + sidewalkLength;
		fileName += "_dropDiameter_" + dropDiameter + ".csv";
		File f = new File(fileName);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(f));
			String header = "sidewalkLength:" + sidewalkLength + "_";
			header += "dropDiameter:" + dropDiameter;
			writer.write(header);
			Sidewalk sidewalk = new Sidewalk(sidewalkLength);
			sidewalk.getRainedOn(dropDiameter);
			for (int i = 0; i < sidewalk.coverage.size(); i += 1) {
				writer.newLine();
				String coverageString = String.valueOf(sidewalk.coverage.get(i));
				writer.write(coverageString);
			}
		} catch (IOException ex) {
			System.out.println("File was not found.");
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			System.out.println("Could not close.");
			}
		}
	}

	public void plotDistributionNumberDrops(double sidewalkLength,
											double dropDiameter,
											int numberOfTrials) {
		Sidewalk sidewalk;
		String fileName = "NumDropDistr_sidewalkLength_" + sidewalkLength;
		fileName += "_dropDiameter_" + dropDiameter + ".csv";
		File f = new File(fileName);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(f));
			String header = "sidewalkLength:" + sidewalkLength + "_";
			header += "dropDiameter:" + dropDiameter;
			writer.write(header);
			for (int i = 0; i < numberOfTrials; i += 1) {
				sidewalk = new Sidewalk(sidewalkLength);
				sidewalk.getRainedOn(dropDiameter);
				String strNumDrops = String.valueOf(sidewalk.numberDrops);
				writer.write(strNumDrops);
				writer.newLine();
			}
		} catch (IOException ex) {
			System.out.println("File was not found.");
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			System.out.println("Could not close.");
			}
		}
	}

	public static void main(String[] args) {
		Plots p = new Plots();
		p.plotCoverage(10, 1);
		p.plotDistributionNumberDrops(10, 1, 1000);
	}

}
