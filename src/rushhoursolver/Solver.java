package rushhoursolver;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.lang.Math;

public class Solver {

    public static void solveFromFile(String inputPath, String outputPath) {
        RushHour game = new RushHour();
        try {
            game = new RushHour(inputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // bfs with queue
        Queue<RushHour> states = new LinkedList<RushHour>();
        HashSet<Long> seen = new HashSet<>();

        states.add(game);
        seen.add(game.hash());

        boolean solved = false;
        RushHour finalBoard = null;
        int farthest = 0;
        while (!solved) {
            RushHour top = states.poll();
            for (Car c : top.cars) {
                for (int direction : RushHour.directions) {
                    if (c.direction == Car.HORIZONTAL && direction < 2 || c.direction == Car.VERTICAL && direction > 1)
                        continue;
                    try {
                        RushHour nextState = new RushHour(top);
                        nextState.makeMove(c.name, direction, 1);
                        farthest = Math.max(farthest, nextState.moves);
                        if (!seen.contains(nextState.hash())) {
                            seen.add(nextState.hash());
                            states.add(nextState);
                        }
                        if (nextState.isSolved()) {
                            finalBoard = nextState;
                            solved = true;
                        }
                    } catch (IllegalMoveException e) {
                    }
                }

            }

        }
        finalBoard.printMoves();
        System.out.println("solved");

    }
}
