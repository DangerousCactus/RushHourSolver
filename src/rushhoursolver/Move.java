package rushhoursolver;

public class Move {
    private static final char[] intToMove = {'U', 'D', 'L', 'R'};
    char carName;
    int direction;

    public Move(char name, int d) {
        this.carName = name;
        this.direction = d;
    }

    public Move(Move m) {
        this.carName = m.carName;
        this.direction = m.direction;
    }

    public void print(int distance){
        System.out.print(carName);
        System.out.print(intToMove[direction]);
        System.out.println(distance);
    }

    public boolean equals(Move m){
        return this.carName == m.carName && this.direction == m.direction;
    }
}
