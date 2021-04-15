import java.io.FileNotFoundException;

import rushhour.Solver;

public class RushHourSolverTester {
    public static void main(String[] args) {
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        long startTime = System.nanoTime();
        try{
        for (int i = 0; i < 10; i++)
            Solver.solveFromFile("testcases/A0" + i + ".txt", "solutions/A0" + i + ".txt");

        Solver.solveFromFile("testcases/A10.txt", "solutions/A10.txt");

        for (int i = 11; i <= 20; i++)
            Solver.solveFromFile("testcases/B" + i + ".txt", "solutions/B" + i + ".txt");

        for (int i = 21; i < 30; i++)
            Solver.solveFromFile("testcases/C" + i + ".txt", "solutions/C" + i + ".txt");

        for (int i = 30; i <= 35; i++)
            Solver.solveFromFile("testcases/D" + i + ".txt", "solutions/D" + i + ".txt");

        for (int i = 1; i <= 5; i++)
            Solver.solveFromFile("testcases/hard" + i + ".txt", "solutions/hard" + i + ".txt");
        } catch (FileNotFoundException e){
            System.out.println("File Not Found.");
        }
        long endTime = System.nanoTime();
        System.out.print((endTime - startTime) / 1000000000.0);
        System.out.println(" s");
    }
}