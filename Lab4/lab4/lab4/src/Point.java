import java.util.Arrays;
import java.util.Random;

public class Point {
    Random rand = new Random();

    private int type = 0;
    private Point next;
    private Point previous;
    private Point above;
    private Point bellow;

    private boolean moved = false;
    private int speed = 0;


    public static Integer[] types = {0, 1, 2, 3, 5};
    public static Integer[] max_speed_of_type = {0, 3, 5, 7, 0, 0};

    public boolean is_car_type(int type) {
        return type >= 1 && type <= 3;
    }

    public int overall_max_speed(){
        int types_num = max_speed_of_type.length;
        int overall_max_speed = 0;
        for(int i = 0; i < types_num; i++){
            if(max_speed_of_type[i] > overall_max_speed) overall_max_speed = max_speed_of_type[i];
        }
        return overall_max_speed;
    }

    private void change_position(Point destinationPoint) {
        destinationPoint.setType(this.type);
        destinationPoint.setSpeed(this.speed);
        destinationPoint.setMoved(true);
        this.setType(0);
        this.setSpeed(0);
        this.setMoved(true);
    }

    private boolean is_in_left_lane() {
        return this.above.getType() == 5 && this.bellow.getType() != 5;
    }

    private int distance_to_nearest_car_behind_in_left_lane() {
        Point start_point = this;
        if (!start_point.is_in_left_lane()) start_point = start_point.above;

        Point nearest_car = start_point.previous;
        int distance_to_nearest_car = 0;
        while (!is_car_type(nearest_car.getType()) && nearest_car != start_point){
            nearest_car = nearest_car.previous;
            distance_to_nearest_car += 1;
        }
        return distance_to_nearest_car;
    }

    private int distance_to_nearest_car_behind_in_right_lane() {
        Point start_point = this;
        if (start_point.is_in_left_lane()) start_point = start_point.bellow;

        Point nearest_car = start_point.previous;
        int distance_to_nearest_car = 0;
        while (!is_car_type(nearest_car.getType()) && nearest_car != start_point){
            nearest_car = nearest_car.previous;
            distance_to_nearest_car += 1;
        }
        return distance_to_nearest_car;
    }

    private int distance_to_nearest_car_ahead_in_left_lane() {
        Point start_point = this;
        if (!start_point.is_in_left_lane()) start_point = start_point.above;

        Point nearest_car = start_point.next;
        int distance_to_nearest_car = 0;
        while (!is_car_type(nearest_car.getType()) && nearest_car != start_point){
            nearest_car = nearest_car.next;
            distance_to_nearest_car += 1;
        }
        return distance_to_nearest_car;
    }


    public boolean return_manoeuvre(){
        int overall_max_speed = overall_max_speed();
        if(! this.is_in_left_lane()) return false;
        if(this.bellow.getType() != 0) return false;
        if(this.distance_to_nearest_car_behind_in_left_lane() < overall_max_speed) return false;
        if(this.distance_to_nearest_car_behind_in_right_lane() > overall_max_speed) return false;
        if(this.distance_to_nearest_car_ahead_in_left_lane() < this.speed) return false;
        
        Point destinationPoint = this.bellow;
        for(int i = 0; i < this.speed - 1; i++){
            destinationPoint = destinationPoint.next;
        }

        change_position(destinationPoint);
        return true;
    }


    public boolean overtake_manoeuvre(){
        int overall_max_speed = overall_max_speed();
        if(this.is_in_left_lane()) return false;
        if(this.above.getType() != 0) return false;
        if(this.speed >= max_speed_of_type[this.type]) return false;
        if(this.distance_to_nearest_car_behind_in_left_lane() < overall_max_speed) return false;
        if(this.distance_to_nearest_car_behind_in_right_lane() < overall_max_speed) return false;
        if(this.distance_to_nearest_car_ahead_in_left_lane() < this.speed) return false;

        this.accelerate();
        Point destinationPoint = this.above;
        for(int i = 0; i < this.speed - 1; i++){
            destinationPoint = destinationPoint.next;
        }
        change_position(destinationPoint);
        return true;
    }


    public void move() {
        if(this.type == 0) return;
        Point destinationPoint = this;

        for(int i = 0; i < this.speed; i++){
            destinationPoint = destinationPoint.next;
        }

        if(destinationPoint.getType() == 0 && !this.moved){
            change_position(destinationPoint);
        }
    }

    public void slowDown(){
        if(!is_car_type(this.getType())) return;
        Point checkedPoint = this;
        for(int i = 0; i < this.speed; i++){
             checkedPoint = checkedPoint.next;
             if (checkedPoint.is_car_type(checkedPoint.getType())){
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

    public void setPrevious(Point point) {
        this.previous = point;
    }

    public void setBellow(Point point) {
        this.bellow = point;
    }

    public void setAbove(Point point) {
        this.above = point;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public void accelerate(){
        if(this.type == 0) return;
        if(this.speed < max_speed_of_type[this.type]){
            this.speed += 1;
        }
    }

    public void decelerate(){
        if(this.type == 0) return;
        if(this.speed > 0){
            this.speed -= 1;
        }
    }
}