package rushhour;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Solver {

    public static void solveFromFile(String inputPath, String outputPath) {
        RushHour game = new RushHour();
        try {
            game = new RushHour(inputPath);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // bfs with queue
        Queue<RushHour> states = new LinkedList<>();
        HashSet<Long> seen = new HashSet<>();

        states.add(game);
        seen.add(game.hash);

        while (!states.isEmpty()) {
            for (Car c : states.peek().cars) {
                for (int direction : c.getPossibleDirections()) {
                    try {
                        RushHour nextState = new RushHour(states.peek());
                        nextState.makeMove(c.name, direction, 1);

                        if (!seen.contains(nextState.hash)) {
                            seen.add(nextState.hash);
                            states.add(nextState);
                            if (nextState.isSolved()) {
                                File f = new File(outputPath);
                                FileWriter fw = null;
                                try {
                                    f.createNewFile();
                                    fw = new FileWriter(outputPath, false);
                                    fw.write(nextState.printMoves());
                                    fw.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                        }

                    } catch (IllegalMoveException e) {
                    }
                }

            }
            states.poll();
        }
    }
}
