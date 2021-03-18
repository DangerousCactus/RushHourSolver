package rushhour;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class RushHour {
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;
	public final static int[] directions = { 0, 1, 2, 3 };

	public final static int SIZE = 6;

	char[][] gameBoard = new char[SIZE][SIZE];
	ArrayList<Car> cars = new ArrayList<Car>();
	ArrayList<Move> moveList = new ArrayList<Move>();
	int moves = 0;

	public RushHour() {
	}

	public Long hash() {
		long out = 0;
		for (Car c : cars) {
			out += c.getDominantCoordinate();
			out = out << 3;
		}

		return out;
	}

	public RushHour(RushHour rh) {
		for (int i = 0; i < SIZE; i++)
			this.gameBoard[i] = Arrays.copyOf(rh.gameBoard[i], rh.gameBoard[i].length);

		for (Car c : rh.cars)
			this.cars.add(new Car(c));
		this.moves = rh.moves;
		for (Move m : rh.moveList)
			this.moveList.add(new Move(m));

	}

	/**
	 * @param fileName Reads a board from file and creates the board
	 * @throws Exception if the file not found or the board is bad
	 */

	public RushHour(String fileName) throws BadBoardException, FileNotFoundException {
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
				if (gameBoard[row][col] != '.' && !hasCar(gameBoard[row][col])) {
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
					cars.add(tempCar);
				}
			}
		}

		if (!hasCar('X')) { // There must be an X car
			throw new BadBoardException("the board doesn't have an X car");
		}

		if (getCar('X').direction != Car.HORIZONTAL) { // X car must be horizonal
			throw new BadBoardException("the X car must be horizontal");
		}

		// Check that are all cars are indeed 1 by Y, where Y > 1
		HashMap<Character, Integer> carSizes = new HashMap<Character, Integer>();
		for (char[] row : gameBoard)
			for (char c : row)
				carSizes.put(c, carSizes.getOrDefault(c, 0) + 1);

		for (Car c : cars)
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
		if (!hasCar(carName)) {
			throw new IllegalMoveException("Specified car does not exist.");
		}

		Car c = getCar(carName);
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

		moves++;
		moveList.add(new Move(carName, dir));
	}

	public boolean hasCar(char ch) {
		boolean found = false;

		for (Car c : cars) {
			if (c.name == ch) {
				found = true;
				break;
			}
		}
		return found;
	}

	public Car getCar(char ch) {
		Car ca = null;
		for (Car c : cars)
			if (c.name == ch)
				ca = c;

		return ca;
	}

	public String printMoves() {
		String out = new String();

		int start = 0;
		int dupes = 1;
		while (start < moveList.size()) {
			Move currMove = moveList.get(start);
			while (start + dupes < moveList.size() && moveList.get(start + dupes).equals(currMove))
				dupes++;

			out += moveList.get(start).print(dupes) + '\n';
			start += dupes;
			dupes = 1;
		}
		return out;
	}

	/**
	 * @return true if and only if the board is solved, i.e., the XX car is touching
	 *         the right edge of the board
	 */
	public boolean isSolved() {
		Car xCar = getCar('X');
		return xCar.x + xCar.length >= 6;
	}

}
