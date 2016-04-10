/**
  * Containes the rain drop object, which falls on the sidewalk.
  *
  * Date: April 9, 2016
  */

public class Droplet {

	/* The realization of unif(0, L). The center of the drop. */
	public double hitLocation;
	/* The left endpoint of the drop. */
	public double leftEndpoint;
	/* The right endpoint of the drop. */
	public double rightEndpoint;
	/* The diameter of the drop. */
	public double diameter;

	/* Constructor. Creates a rain droplet object. */
	public Droplet(double hitLocation, double leftEndpoint, 
									   double rightEndpoint) {
		this.hitLocation = hitLocation;
		this.leftEndpoint = leftEndpoint;
		this.rightEndpoint = rightEndpoint;
		this.diameter = rightEndpoint - leftEndpoint;
	}

	/* Constructor. For combining drops, as in Sidewalk.combinedDrops */
	public Droplet() {
	}

}
