/**
  * Contains the sidewalk object and the main simulation method, getRainedOn().
  *
  * Date: April 9, 2016
  */

import java.util.ArrayList;
import java.util.Random;

public class Sidewalk {

	/* Length of the sidewalk. */
	public double sidewalkLength;
	/* Counter for the number of drops on fallen on the sidewalk thus far. */
	public int numberDrops;
	/* true if the the sidewalk is completely wet. */
	public boolean completelyWet;
	/* Holds droplets, which store the wet locations of the sidewalk.
	 * Droplets in wetLocations are sorted by their hitLocation. */
	public ArrayList<Droplet> wetLocations = new ArrayList<Droplet>();
	/* Length of the sidewalk that is currently wet. */
	public ArrayList<Double> coverage = new ArrayList<Double>();

	/* Constructor. Creates a dry sidewalk. */
	public Sidewalk(double sidewalkLength) {
		this.sidewalkLength = sidewalkLength;
		this.numberDrops = 0;
		this.completelyWet = false;
	}

	/* Simulates rain on a Sidewalk object. */
	public void getRainedOn(double dropDiameter) {
		Random unifGenerator = new Random();
		/* Insert the first drop for free. */
		double unifValue = unifGenerator.nextFloat() * this.sidewalkLength;
		unifValue = Math.round(unifValue * 100d) / 100d;
		Droplet drop = new Droplet(unifValue, 
									Math.max(0, unifValue - dropDiameter / 2),
									Math.min(sidewalkLength, unifValue + dropDiameter / 2));
		wetLocations.add(drop);
		numberDrops += 1;
		updateCoverage();

		/* For dubug purpose, or to see the rain fall live, uncomment */
		// System.out.println("New drop: " + unifValue);
		// printLocs();
		// String msg = "Coverage: " + this.coverage + "  Drops: " + numberDrops;
		// System.out.println(msg);
		while (!this.completelyWet) {
			unifValue = unifGenerator.nextFloat() * this.sidewalkLength;
			unifValue = Math.round(unifValue * 100d) / 100d;
			/* add drop to wetLocations data structure */
			drop = new Droplet(unifValue, 
									Math.max(0, unifValue - dropDiameter / 2),
									Math.min(sidewalkLength, unifValue + dropDiameter / 2));
			this.addDrop(drop);
			this.numberDrops += 1;
			this.updateCoverage();
			this.updateWetStatus();

			/* For dubug purpose, or to see the rain fall live, uncomment */
			// System.out.println("New drop: " + unifValue);
			// this.printLocs();
			// String msg = "Coverage: " + this.coverage + "  Drops: " + numberDrops;
			// System.out.println(msg); 
		}
	}

	/* Adds a drop to the sidewalk by updating wetLocations. */
	public void addDrop(Droplet drop) {
		int loc = indexToAddDrop(drop);
		// if drop has both left and right neighbor
		if (loc != 0 && loc != wetLocations.size()) {
			// neighbors are too far
			if (drop.leftEndpoint > wetLocations.get(loc-1).rightEndpoint &&
				drop.rightEndpoint < wetLocations.get(loc).leftEndpoint) {
				wetLocations.add(loc, drop);
			// drop is to be combined with both left and right
			} else if (drop.leftEndpoint < wetLocations.get(loc-1).rightEndpoint &&
					drop.rightEndpoint > wetLocations.get(loc).leftEndpoint) {
				combinedDrops(drop, true, true, loc);
			// drop is to be combined with only left and not right
			} else if (drop.leftEndpoint < wetLocations.get(loc-1).rightEndpoint &&
					drop.rightEndpoint < wetLocations.get(loc).leftEndpoint) {
				combinedDrops(drop, true, false, loc);
			// drop is to be combined with only right and not left 
			} else {
				combinedDrops(drop, false, true, loc);
			}
		} else if (loc == wetLocations.size()) {
		// else if drop has only a left neighbor
			if (drop.leftEndpoint > wetLocations.get(loc-1).rightEndpoint) {
			// if the neighbor is too far
				wetLocations.add(loc, drop);
			} else {
			// else drop is to be combined with left neighbor
				combinedDrops(drop, true, false, loc);
			}
		} else {
			// if the right neighbor is too far
			if (drop.rightEndpoint < wetLocations.get(loc).leftEndpoint) {
				wetLocations.add(loc, drop);
			} else {
			// else drop is to be combined with right neighbor
				combinedDrops(drop, false, true, loc);
			}
		}
	}

	/* Adds the drop to wetLocations by merging it with the left and/or 
	 * right neighbor drops, depending if they are close enough.
	 */
	public void combinedDrops(Droplet drop, boolean left, boolean right, int loc) {
		if (left && right) {
			Droplet combo = new Droplet();
			combo.leftEndpoint = wetLocations.get(loc-1).leftEndpoint;
			combo.rightEndpoint = wetLocations.get(loc).rightEndpoint;
			combo.hitLocation = (combo.leftEndpoint + combo.rightEndpoint) / 2;
			combo.diameter = combo.rightEndpoint - combo.leftEndpoint;
			wetLocations.remove(loc);
			wetLocations.remove(loc-1);
			wetLocations.add(loc-1, combo);
		} else if (left && !right) {
			Droplet combo = new Droplet();
			combo.leftEndpoint = Math.min(drop.leftEndpoint, wetLocations.get(loc-1).leftEndpoint);
			combo.rightEndpoint = Math.max(drop.rightEndpoint, wetLocations.get(loc-1).rightEndpoint);
			combo.hitLocation = (combo.leftEndpoint + combo.rightEndpoint) / 2;
			combo.diameter = combo.rightEndpoint - combo.leftEndpoint;
			wetLocations.remove(loc-1);
			wetLocations.add(loc-1, combo);
		} else { /* Combining only with right drop */
			Droplet combo = new Droplet();
			combo.leftEndpoint = Math.min(drop.leftEndpoint, wetLocations.get(loc).leftEndpoint);
			combo.rightEndpoint = Math.max(drop.rightEndpoint, wetLocations.get(loc).rightEndpoint);
			combo.hitLocation = (combo.leftEndpoint + combo.rightEndpoint) / 2;
			combo.diameter = combo.rightEndpoint - combo.leftEndpoint;
			wetLocations.remove(loc);
			wetLocations.add(loc, combo);
		}
	}

	/* Given 2 Droplets D1 and D2, returns true if their intervals overlap. */
	public boolean intervalsOverlap(Droplet d1, Droplet d2) {
		if (d1.leftEndpoint == d2.leftEndpoint) {
			return true;
		} else if (d1.leftEndpoint < d2.leftEndpoint) {
			if (d2.leftEndpoint <= d1.rightEndpoint) {
				return true;
			} else {
				return false;
			}
		} else {
			if (d1.leftEndpoint <= d2.rightEndpoint) {
				return true;
			} else {
				return false;
			}
		}
	}

	/* Return the index of wetLocations for which to add DROP at. */
	public int indexToAddDrop(Droplet drop) {
		int loc = 0;
		/* The sidewalk is dry */
		if (this.wetLocations.size() == 0) {
			return loc; 
		} else if (drop.hitLocation > 
					wetLocations.get(wetLocations.size() - 1).hitLocation) {
			 /* Drop is to be added to the end */
			 return wetLocations.size();
		} else {
			while (drop.hitLocation > wetLocations.get(loc).hitLocation) {
				loc += 1;
			}
			return loc;
		}
	}

	/* Prints out all the covered intervals from wetLocations. */
	public void printLocs() {
		String msg = "Locations: ";
		for (int i = 0; i < wetLocations.size(); i += 1) {
			msg = msg + "(" + wetLocations.get(i).leftEndpoint + ","
						+ wetLocations.get(i).rightEndpoint + "),";
		}
		System.out.println(msg);
	}

	/* Iterates through each droplet in wetLocations to calculate coverage. 
	 * This is slow. It's fasters to just look at the diff, when the drop 
	 * is added to wetLocations. 
 	 */
	public void updateCoverage() {
		double sum = 0;
		for (int i = 0; i < wetLocations.size(); i += 1) {
			sum += wetLocations.get(i).diameter;
		}
		this.coverage.add(Math.round(sum * 100d) / 100d);
	}

	/* If the sidewalk is completely wet, udate completelyWet to true.*/
	public void updateWetStatus() {
		if (this.wetLocations.size() == 1 && 
			this.wetLocations.get(0).leftEndpoint == 0 &&
			this.wetLocations.get(0).rightEndpoint == this.sidewalkLength) {
			this.completelyWet = true;
		}
	}

	public static void main(String[] args) {
		/* For dubug purpose, or to see the rain fall live. 
		 * Uncomment getRainedOn() too
		 */
		/* double swl = 6;
		double rdd = 1;
		Sidewalk s = new Sidewalk(swl);
		s.getRainedOn(rdd);
		String msg = "Sidewalk length = " + swl;
		msg += " Diameter = " + rdd;
		msg += " Total drops = " + s.numberDrops;
		System.out.println(msg); */
	}
}
