import java.util.Random;

public class Point {
    Random rand = new Random();

    private int type = 0;
    private Point next;
    private boolean moved = false;
    private int speed = 0;
    private int maxSpeed = 5;
    private double breakingProbability = 0.7;



    public void move() {
        if(this.type == 0) return;
        Point destinationPoint = this;

        for(int i = 0; i < this.speed; i++){
            destinationPoint = destinationPoint.next;
        }

        if(destinationPoint.getType() == 0 && !this.moved){
            destinationPoint.setType(1);
            destinationPoint.setSpeed(this.speed);
            destinationPoint.setMoved(true);
            this.setType(0);
            this.setSpeed(0);
            this.setMoved(true);
        }
    }

    public void slowDown(){
        if(this.type == 0) return;
        Point checkedPoint = this;
        for(int i = 0; i < this.speed; i++){
             checkedPoint = checkedPoint.next;
             if (checkedPoint.getType() == 1){
                 this.speed = i;
             }
        }
    }

    public void clicked() {
        this.setType(1);
    }

    public void clear() {
        this.setType(0);
    }

    public void setType(int type){
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setMoved(boolean status) {
        this.moved = status;
    }


    public void setNext(Point point) {
        this.next = point;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public void accelerate(){
        if(this.type == 0) return;
        if(this.speed < maxSpeed){
            this.speed += 1;
        }
    }

    public void decelerate(){
        if(this.type == 0) return;
        if(this.speed > 0){
            this.speed -= 1;
        }
    }

    public void randomBreaking(){
        if(this.type == 0) return;
        if(this.speed >= 1 && rand.nextDouble() < breakingProbability){
            this.decelerate();
        }
    }
}