package rushhourtest;

import java.io.File;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner;

import rushhour.Solver;

public class TestRushHour {

	public static int dirChar2Int(char d) {
		switch (d) {
		case 'U': {
			return RushHour.UP;
		}
		case 'D': {
			return RushHour.DOWN;
		}
		case 'L': {
			return RushHour.LEFT;
		}
		case 'R': {
			return RushHour.RIGHT;
		}
		default:
			throw new IllegalArgumentException("Unexpected direction: " + d);
		}
	}

	public static void testSolution(String puzzleName, String solName) {
		try {

			RushHour puzzle = new RushHour(puzzleName);

			Scanner scannerSolution = new Scanner(new File(solName));
			while (scannerSolution.hasNextLine()) {
				String line = scannerSolution.nextLine();
				if (line.length() != 3)
					throw new IllegalMoveException(line);
				puzzle.makeMove(line.charAt(0), dirChar2Int(line.charAt(1)), line.charAt(2) - '0');
			}

			if (puzzle.isSolved()) {
				System.out.println(puzzleName + " Solved");
			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public static void main(String[] args) {
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		try {
			for (int i = 0; i < 10; i++) {
				Solver.solveFromFile("testcases/A0" + i + ".txt", "solutions/A0" + i + ".txt");
				testSolution("testcases/A0" + i + ".txt", "solutions/A0" + i + ".txt");
			}

			Solver.solveFromFile("testcases/A10.txt", "solutions/A10.txt");
			testSolution("testcases/A10.txt", "solutions/A10.txt");

			for (int i = 11; i <= 20; i++) {
				Solver.solveFromFile("testcases/B" + i + ".txt", "solutions/B" + i + ".txt");
				testSolution("testcases/B" + i + ".txt", "solutions/B" + i + ".txt");
			}

			for (int i = 21; i < 30; i++) {
				Solver.solveFromFile("testcases/C" + i + ".txt", "solutions/C" + i + ".txt");
				testSolution("testcases/C" + i + ".txt", "solutions/C" + i + ".txt");
			}

			for (int i = 30; i <= 35; i++) {
				Solver.solveFromFile("testcases/D" + i + ".txt", "solutions/D" + i + ".txt");
				testSolution("testcases/D" + i + ".txt", "solutions/D" + i + ".txt");
			}

			for (int i = 1; i <= 5; i++) {
				Solver.solveFromFile("testcases/hard" + i + ".txt", "solutions/hard" + i + ".txt");
				testSolution("testcases/hard" + i + ".txt", "solutions/hard" + i + ".txt");
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found.");
		}

	}
}