package rushhoursolver;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class RushHour {
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;

	public final static int SIZE = 6;

	char[][] gameBoard = new char[SIZE][SIZE];
	/*
	 * The board is index as follows (col, row): (0,0) (1,0) (2,0) (3,0) (4,0) (5,0)
	 * (0,1) (1,1) (2,1) (3,1) (4,1) (5,1) (0,2) (1,2) (2,2) (3,2) (4,2) (5,2) (0,3)
	 * (1,3) (2,3) (3,3) (4,3) (5,3) (0,4) (1,4) (2,4) (3,4) (4,4) (5,4) (0,5) (1,5)
	 * (2,5) (3,5) (4,5) (5,5)
	 */
	HashMap<Character, Car> cars = new HashMap<Character, Car>();

	/**
	 * @param fileName Reads a board from file and creates the board
	 * @throws Exception if the file not found or the board is bad
	 */

	public RushHour(String fileName) throws Exception {
		File f = new File(fileName);
		Scanner s = new Scanner(f);

		// Read the board into our char array, row by row
		int currRow = 0;

		try {
			while (s.hasNextLine()) {
				gameBoard[currRow] = s.nextLine().toCharArray(); // if we have more than 6 rows, enter catch
				if (gameBoard[currRow++].length != SIZE) // If the row doesn't have 6 columns
					throw new Exception(); // enter catch block
			}
		} catch (Exception e) {
			throw new BadBoardException("the dimentions of the board are not 6x6");
		} finally {
			s.close();
		}

		if (gameBoard.length != SIZE) { // If the board doesn't have 6 rows
			throw new BadBoardException("the dimentions of the board are not 6x6");
		}

		// Extract the cars from the char array
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {

				// If we haven't seen this car before
				if (gameBoard[row][col] != '.' && !cars.containsKey(gameBoard[row][col])) {
					Car tempCar;
					if (col < 5 && gameBoard[row][col] == gameBoard[row][col + 1]) {
						tempCar = new Car(gameBoard[row][col], col, row, 1, Car.HORIZONTAL);
						int c = col;
						while (c < 5 && gameBoard[row][c] == gameBoard[row][c + 1]) {
							c++;
							tempCar.length++;
						}
					} else {
						tempCar = new Car(gameBoard[row][col], col, row, 1, Car.VERTICAL);
						int r = row;
						while (r < 5 && gameBoard[r][col] == gameBoard[r + 1][col]) {
							r++;
							tempCar.length++;
						}
					}
					cars.put(gameBoard[row][col], tempCar);
				}
			}
		}

		if (!cars.containsKey('X')) { // There must be an X car
			throw new BadBoardException("the board doesn't have an X car");
		}

		if (cars.get('X').direction != Car.HORIZONTAL) { // X car must be horizonal
			throw new BadBoardException("the X car must be horizontal");
		}

		// Check that are all cars are indeed 1 by Y, where Y > 1
		HashMap<Character, Integer> carSizes = new HashMap<Character, Integer>();
		for (char[] row : gameBoard)
			for (char c : row)
				carSizes.put(c, carSizes.getOrDefault(c, 0) + 1);

		for (Car c : cars.values())
			if (c.length != carSizes.get(c.name) || c.length == 1)
				// A degenerate car is either detached or is 1x1
				throw new BadBoardException("the " + c.name + " car is degenerate");
	}

	/**
	 * @param carName
	 * @param dir
	 * @param length  Moves car with the given name for length steps in the given
	 *                direction
	 * @throws IllegalMoveException if the move is illegal
	 */
	public void makeMove(char carName, int dir, int length) throws IllegalMoveException {
		if (!cars.containsKey(carName)) {
			throw new IllegalMoveException("Specified car does not exist.");
		}

		Car c = cars.get(carName);
		if (c.direction == Car.HORIZONTAL && (dir == UP || dir == DOWN)
				|| (c.direction == Car.VERTICAL && (dir == LEFT || dir == RIGHT))) {
			throw new IllegalMoveException("Specified direction does not match car's direction");
		}

		int newX = c.x + (dir == RIGHT ? length : 0) - (dir == LEFT ? length : 0);
		int newY = c.y + (dir == DOWN ? length : 0) - (dir == UP ? length : 0);

		// Check if the combination of newX, newY, length, and collisions are valid

		if (c.direction == Car.VERTICAL) {
			if (newY < 0 || newY + c.length > SIZE)
				throw new IllegalMoveException("the car ends up outside of the board");

			for (int y = Math.min(c.y, newY); y < Math.max(c.y, newY) + c.length; y++)
				if (gameBoard[y][newX] != '.' && gameBoard[y][newX] != carName)
					throw new IllegalMoveException("the car collides with another during the move");

		} else {
			if (newX < 0 || newX + c.length > SIZE)
				throw new IllegalMoveException("the car ends up outside of the board");

			for (int x = Math.min(c.x, newX); x < Math.max(c.x, newX) + c.length; x++)
				if (gameBoard[newY][x] != '.' && gameBoard[newY][x] != carName)
					throw new IllegalMoveException("the car collides with another during the move");
		}

		// If we've made it this far into the function, it means the move is valid
		if (c.direction == Car.VERTICAL) {
			for (int y = c.y; y < c.y + c.length; y++)
				gameBoard[y][c.x] = '.';

			for (int y = newY; y < newY + c.length; y++)
				gameBoard[y][c.x] = c.name;

			c.y = newY;
		} else {
			for (int x = c.x; x < c.x + c.length; x++)
				gameBoard[c.y][x] = '.';

			for (int x = newX; x < newX + c.length; x++)
				gameBoard[c.y][x] = c.name;

			c.x = newX;
		}
	}

	/**
	 * @return true if and only if the board is solved, i.e., the XX car is touching
	 *         the right edge of the board
	 */
	public boolean isSolved() {
		Car xCar = cars.get('X');
		return xCar.x + xCar.length >= 6;
	}

}
