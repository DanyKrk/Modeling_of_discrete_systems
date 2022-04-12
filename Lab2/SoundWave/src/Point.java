public class Point {

	public Point nNeighbor;
	public Point wNeighbor;
	public Point eNeighbor;
	public Point sNeighbor;
	public float nVel;
	public float eVel;
	public float wVel;
	public float sVel;
	public float pressure;

	public static Integer []types ={0,1,2};
	public int type;

	public int sinInput;

	public Point() {
		clear();
		this.type = 0;
	}

	public void clicked() {
		pressure = 1;
	}
	
	public void clear() {
		// TODO: clear velocity and pressure
		nVel = 0;
		eVel = 0;
		sVel = 0;
		wVel = 0;
		pressure = 0;
	}

	public void updateVelocity() {
		if (this.type != 0) return;
		// TODO: velocity update
		nVel = nVel - (nNeighbor.getPressure() - this.pressure);
		eVel = eVel - (eNeighbor.getPressure() - this.pressure);
		sVel = sVel - (sNeighbor.getPressure() - this.pressure);
		wVel = wVel - (wNeighbor.getPressure() - this.pressure);
	}

	public void updatePresure() {
		if (this.type == 1) return;
		if (this.type == 2){
			double radians = Math.toRadians(sinInput);
			pressure = (float) (Math.sin(radians));

			this.sinInput = (this.sinInput + 20) % 360;
			return;
		}
		// TODO: pressure update
		this.pressure = (float) (this.pressure - 0.5 * (nVel + eVel + sVel + wVel));
	}

	public float getPressure() {
		return pressure;
	}
}