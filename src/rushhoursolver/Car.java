package rushhoursolver;

public class Car {
    final static boolean HORIZONTAL = true;
    final static boolean VERTICAL = false;

    char name;
    int x;
    int y;
    int length;
    boolean direction;

    public Car(char name, int x, int y, int length, boolean direction){
        this.name = name;
        this.x = x;
        this.y = y;
        this.length = length;
        this.direction = direction;
    }
}
