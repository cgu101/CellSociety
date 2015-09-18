package cellsociety.grid;

import java.util.Arrays;
import java.util.Random;

import cellsociety.cell.Cell;
import cellsociety.parameters.FireParameters;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class FireGrid extends AbstractGrid {
	private Double probCatch;
	private Double probGrow;
	private Double probLightning;

	public FireGrid(String s) {
		super(s);
	}

	@Override
	protected void init() {
		super.init();
		myParameters = new FireParameters(this.paramPane);
		speed = myParameters.getValue("Speed");
	}

	protected void calculateStates() {
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[row].length; col++) {
				changeState(map[row][col], row, col);
			}
		}
	}

	protected void updateStates() {
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[row].length; col++) {
				map[row][col].changeState();
			}
		}
	}

	private void changeState(Cell cell, int row, int col) {
		String currentState = cell.getCurrentState();
		if (currentState.equals("tree")) {
			catchFire(cell, row, col);
		} else if (currentState.equals("earth")) {
			treeGrowth(cell, row, col);
		} else if (currentState.equals("fire")) {
			cell.setNextState("burnt");
		} else if (currentState.equals("burnt")) {
			cell.setNextState("earth");
		}
	}

	private void treeGrowth(Cell cell, int row, int col) {
		Random rand = new Random();
		if (rand.nextDouble() <= probGrow) {
			cell.setNextState("tree");
		} else {
			cell.setNextState(cell.getCurrentState());
		}
	}

	private void catchFire(Cell cell, int row, int col) {
		int neighborFires = neighborsOnFire(row, col);
		Double fireChance = (1 - Math.pow((1 - probCatch), neighborFires)) + (probLightning);

		Random rand = new Random();
		if (rand.nextDouble() <= fireChance) {
			cell.setNextState("fire");
		} else {
			cell.setNextState(cell.getCurrentState());
		}
	}

	private int neighborsOnFire(int row, int col) {
		// North
		int count = 0;
		if (row - 1 >= 0) {
			if (map[row - 1][col].getCurrentState().equals("fire")
					|| map[row - 1][col].getCurrentState().equals("burnt"))
				count++;
		}
		// South
		if (row + 1 < map.length) {
			if (map[row + 1][col].getCurrentState().equals("fire")
					|| map[row + 1][col].getCurrentState().equals("burnt"))
				count++;
		}
		// West
		if (col - 1 >= 0) {
			if (map[row][col - 1].getCurrentState().equals("fire")
					|| map[row][col - 1].getCurrentState().equals("burnt"))
				count++;
		}
		// East
		if (col + 1 < map[row].length) {
			if (map[row][col + 1].getCurrentState().equals("fire")
					|| map[row][col + 1].getCurrentState().equals("burnt"))
				count++;
		}
		return count;
	}

	protected void reset() {
		speed = myParameters.getValue("Speed");
		probCatch = myParameters.getValue("ProbCatch");
		probLightning = myParameters.getValue("ProbLightning");
		probGrow = myParameters.getValue("ProbGrow");
		super.reset();
	}
}
