package cellsociety.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import cellsociety.cell.Cell;
import cellsociety.parameters.WatorWorldParameters;

public class WatorWorldGrid extends AbstractGrid {
	private Double FishBreed;
	private Double SharkBreed;
	private Double SharkStarve;
	private HashMap<String, Integer> fishTimes;
	private HashMap<String, Integer> sharkTimes;
	private HashMap<String, Integer> sharkEnergy;
	private HashMap<String, Integer> nextFishTimes;
	private HashMap<String, Integer> nextSharkTimes;
	private HashMap<String, Integer> nextSharkEnergy;

	public WatorWorldGrid(String s) {
		super(s);
	}

	@Override
	protected void init() {
		super.init();
		myParameters = new WatorWorldParameters(this.paramPane);
	}

	@Override
	protected void calculateStates() {
		for (String shark : sharkTimes.keySet()) {
			if (sharkEnergy.get(shark) != 0) {
				if (!eatFish(shark)) {
					moveShark(shark);
				}
			}
		}
		for (String fish : fishTimes.keySet()) {
			moveFish(fish);
		}
	}

	private void breedShark(String shark) {
		ArrayList<String> validPositions = getValid(shark);
		if (!validPositions.isEmpty()) {
			Random random = new Random();
			int next = random.nextInt(validPositions.size());
			String newShark = validPositions.get(next);
			find(newShark).setNextState("shark");
			nextSharkTimes.put(newShark, 0);
			nextSharkEnergy.put(newShark, (int) Math.round(SharkStarve));
			nextSharkTimes.put(shark, 0);
		}
	}

	private void moveShark(String oldPosition) {
		ArrayList<String> validPositions = getValid(oldPosition);
		if (validPositions.isEmpty()) {
			find(oldPosition).setNextState("shark");
			nextSharkTimes.put(oldPosition, sharkTimes.get(oldPosition) + 1);
			nextSharkEnergy.put(oldPosition, sharkEnergy.get(oldPosition) - 1);
		} else {
			Random random = new Random();
			int next = random.nextInt(validPositions.size());
			String newPosition = validPositions.get(next);
			find(oldPosition).setNextState("water");
			find(newPosition).setNextState("shark");
			nextSharkTimes.put(newPosition, sharkTimes.get(oldPosition) + 1);
			nextSharkEnergy.put(newPosition, sharkEnergy.get(oldPosition) - 1);
			if (nextSharkTimes.get(newPosition) >= (int) Math.round(SharkBreed)) {
				breedShark(newPosition);
			}
		}
	}

	private void moveFish(String oldPosition) {
		ArrayList<String> validPositions = getValid(oldPosition);
		if (validPositions.isEmpty()) {
			find(oldPosition).setNextState("fish");
			nextFishTimes.put(oldPosition, fishTimes.get(oldPosition) + 1);
		} else {
			Random random = new Random();
			int next = random.nextInt(validPositions.size());
			String newPosition = validPositions.get(next);
			find(oldPosition).setNextState("water");
			find(newPosition).setNextState("fish");
			nextFishTimes.put(newPosition, fishTimes.get(oldPosition) + 1);
			if (nextFishTimes.get(newPosition) >= (int) Math.round(FishBreed)) {
				breedFish(newPosition);
			}
		}
	}

	private void breedFish(String fish) {
		ArrayList<String> validPositions = getValid(fish);
		if (!validPositions.isEmpty()) {
			Random random = new Random();
			int next = random.nextInt(validPositions.size());
			String newFish = validPositions.get(next);
			find(newFish).setNextState("fish");
			nextFishTimes.put(newFish, 0);
			nextFishTimes.put(fish, 0);
		}
	}

	private Cell find(String coordinates) {
		int x = Integer.parseInt(coordinates.split(" ")[0]);
		int y = Integer.parseInt(coordinates.split(" ")[1]);
		return map[x][y];
	}

	private boolean eatFish(String shark) {
		ArrayList<String> food = getAdjacentFish(shark);
		if (food.isEmpty()) {
			return false;
		} else {
			Random random = new Random();
			int unluckyNumber = random.nextInt(food.size());
			String unluckyFish = food.get(unluckyNumber);
			fishTimes.remove(unluckyFish);
			find(shark).setNextState("shark");
			nextSharkTimes.put(shark, sharkTimes.get(shark) + 1);
			nextSharkEnergy.put(shark, (int) Math.round(SharkStarve));
			return true;
		}
	}

	private ArrayList<String> getAdjacentFish(String shark) {
		int x = Integer.parseInt(shark.split(" ")[0]);
		int y = Integer.parseInt(shark.split(" ")[1]);
		int x1 = (x + 1) % map.length;
		int y1 = (y + 1) % map[0].length;
		int x2 = (x - 1);
		if (x2 < 0) {
			x2 += map.length;
		}
		int y2 = (y - 1);
		if (y2 < 0) {
			y2 += map[0].length;
		}
		ArrayList<String> output = new ArrayList<String>();
		checkForFish(y, x1, output);
		checkForFish(y, x2, output);
		checkForFish(y1, x, output);
		checkForFish(y2, x, output);
		return output;
	}

	public void checkForFish(int y, int x, ArrayList<String> output) {
		if (map[x][y].getCurrentState().equals("fish")) {
			String coord = x + " " + y;
			output.add(coord);
		}
	}

	private ArrayList<String> getValid(String oldPosition) {
		int x = Integer.parseInt(oldPosition.split(" ")[0]);
		int y = Integer.parseInt(oldPosition.split(" ")[1]);
		int x1 = (x + 1) % map.length;
		int y1 = (y + 1) % map[0].length;
		int x2 = (x - 1);
		if (x2 < 0) {
			x2 += map.length;
		}
		int y2 = (y - 1);
		if (y2 < 0) {
			y2 += map[0].length;
		}
		ArrayList<String> output = new ArrayList<String>();
		checkForValid(y, x1, output);
		checkForValid(y, x2, output);
		checkForValid(y1, x, output);
		checkForValid(y2, x, output);
		return output;
	}

	public void checkForValid(int y, int x, ArrayList<String> output) {
		if ((map[x][y].getCurrentState().equals("water") && map[x][y].getNextState() == null)
				|| map[x][y].getNextState() == "water") {
			String coord = x + " " + y;
			output.add(coord);
		}
	}

	@Override
	protected void updateStates() {
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[row].length; col++) {
				if (map[row][col].getNextState() == null || map[row][col].getNextState() == "water") {
					map[row][col].setNextState("water");
				}
				map[row][col].changeState();
			}
		}
		fishTimes = nextFishTimes;
		sharkTimes = nextSharkTimes;
		sharkEnergy = nextSharkEnergy;
		nextFishTimes = new HashMap<String, Integer>();
		nextSharkTimes = new HashMap<String, Integer>();
		nextSharkEnergy = new HashMap<String, Integer>();
	}

	@Override
	protected void reset() {
		speed = myParameters.getValue("Speed");
		FishBreed = myParameters.getValue("FishBreed");
		SharkBreed = myParameters.getValue("SharkBreed");
		SharkStarve = myParameters.getValue("SharkStarve");
		super.reset();
		fishTimes = new HashMap<String, Integer>();
		sharkTimes = new HashMap<String, Integer>();
		sharkEnergy = new HashMap<String, Integer>();
		nextFishTimes = new HashMap<String, Integer>();
		nextSharkTimes = new HashMap<String, Integer>();
		nextSharkEnergy = new HashMap<String, Integer>();
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				String coord = i + " " + j;
				String currentState = map[i][j].getCurrentState();
				if (currentState.equals("fish")) {
					fishTimes.put(coord, 0);
				} else if (currentState.equals("shark")) {
					sharkTimes.put(coord, 0);
					sharkEnergy.put(coord, (int) Math.round(SharkStarve));
				}
			}
		}
	}
}
