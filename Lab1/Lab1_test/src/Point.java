import java.util.ArrayList;
import java.util.Random;

public class Point {
	private ArrayList<Point> neighbors;
	private int currentState;
	private int nextState;
	private int numStates = 6;
	
	public Point() {
		currentState = 0;
		nextState = 0;
		neighbors = new ArrayList<Point>();
	}

	public void clicked() {
		currentState=(++currentState)%numStates;	
	}
	
	public int getState() {
		return currentState;
	}

	public void setState(int s) {
		currentState = s;
	}

	public void calculateNewState(String mode) {
		//TODO: insert logic which updates according to currentState and

		if (mode.equals("rain")){
			if (this.currentState > 0) this.nextState = this.currentState - 1;
			else if (this.neighbors.get(0).getState() > 0) this.nextState = 6;
			else this.nextState = this.currentState;
		}
		else {
//			NORMAL
					int livingNeighboursNum = this.livingNeighboursNum();
					if (this.getState() == 0){
						if (livingNeighboursNum == 3){
							this.nextState = 1;
						}
						else{
							this.nextState = 0;
						}
					}
					else{
						if (livingNeighboursNum == 2 || livingNeighboursNum == 3){
							this.nextState = 1;
						}
						else{
							this.nextState = 0;
						}
					}

//					//CITIES
//					int livingNeighboursNum = this.livingNeighboursNum();
//					if (this.getState() == 0){
//						if (livingNeighboursNum >=2 && livingNeighboursNum <=5){
//							this.nextState = 1;
//						}
//						else{
//							this.nextState = 0;
//						}
//					}
//					else{
//						if (livingNeighboursNum >= 4 && livingNeighboursNum <=8){
//							this.nextState = 1;
//						}
//						else{
//							this.nextState = 0;
//						}
//					}

//			//CORAL
//			int livingNeighboursNum = this.livingNeighboursNum();
//			if (this.getState() == 0) {
//				if (livingNeighboursNum >= 4 && livingNeighboursNum <= 8) {
//					this.nextState = 1;
//				} else {
//					this.nextState = 0;
//				}
//			} else {
//				if (livingNeighboursNum == 3) {
//					this.nextState = 1;
//				} else {
//					this.nextState = 0;
//				}
//			}
		}
	}

	public void changeState() {
		currentState = nextState;
	}
	
	public void addNeighbor(Point nei) {
		neighbors.add(nei);
	}

	//TODO: write method counting all active neighbors of THIS point
	public int livingNeighboursNum(){
		int counter = 0;
		for (int i = 0; i < neighbors.size(); i++) {
			if(neighbors.get(i).getState() == 1){
				counter += 1;
			}
		}
		return counter;
	}

	public void drop(){
		Random random = new Random();
		int s = random.nextInt(100);
		if (s < 5) this.setState(6);
	}

	public void eraseNeighbours() {
		this.neighbors.clear();
	}
}
