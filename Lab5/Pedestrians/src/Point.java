import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class Point {

	public ArrayList<Point> neighbors;
	public static Integer []types ={0,1,2,3};
	public int type;

	public int staticField;
	public boolean isPedestrian;

	private boolean blocked;

	public Point() {
		type=0;
		staticField = 100000;
		neighbors= new ArrayList<Point>();
		blocked = false;
	}
	
	public void clear() {
		staticField = 100000;
		
	}

	public boolean calcStaticField() {
		int minNeighField = Integer.MAX_VALUE;
		Iterator neighboursIterator = this.neighbors.iterator();
		while (neighboursIterator.hasNext()) {
			Point neigh = (Point) neighboursIterator.next();

			//TODO
			if (neigh.getStaticField() < 0) continue;

			minNeighField = Math.min(minNeighField, neigh.getStaticField());
		}

		if (this.staticField > minNeighField + 1){
			this.staticField = minNeighField + 1;
			return true;
		}
		else return false;
	}

	public void move(){
		if(isPedestrian && !blocked){
			Point destination = this;

			int minNeighField = Integer.MAX_VALUE;
			Iterator neighboursIterator = this.neighbors.iterator();
			while (neighboursIterator.hasNext()) {
				Point neigh = (Point) neighboursIterator.next();

				//TODO
				if (neigh.getStaticField() < 0) continue;

				if(neigh.getType() == 0 && neigh.getStaticField() < minNeighField){
					minNeighField = neigh.getStaticField();
					destination = neigh;
				}

				if(neigh.getType() == 2){
					this.becomeFloor();
					return;
				}
			}
			if(destination != this){
				destination.becomePedestrian();
				this.becomeFloor();
			}
		}
	}

	public void addNeighbor(Point nei) {
		neighbors.add(nei);
	}

	public int getType() {
		return this.type;
	}

	public void setStaticField(int staticField) {
		this.staticField = staticField;
	}

	public ArrayList<Point> getNeighbuors() {
		return neighbors;
	}


	private int getStaticField() {
		return this.staticField;
	}

	public void setBlocked(boolean status){
		this.blocked = status;
	}

	public void becomePedestrian(){
		this.type = 3;
		this.isPedestrian = true;
		this.blocked = true;
	}

	public void becomeFloor(){
		this.type = 0;
		this.isPedestrian = false;
	}

}