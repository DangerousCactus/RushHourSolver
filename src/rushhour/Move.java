package rushhour;

public class Move {
    private static final char[] intToMove = { 'U', 'D', 'L', 'R' };
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

    public String print(int distance) {
        String out = new String();
        out += carName;
        out += intToMove[direction];
        out += distance;
        return out;
    }

    public boolean equals(Move m) {
        return this.carName == m.carName && this.direction == m.direction;
    }
}
