package cellsociety.grid;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import cellsociety.cell.Cell;
import cellsociety.parameters.SegregationParameters;

public class SegregationGrid extends AbstractGrid {
	
	private double similar;
	private boolean satisfied;
	private LinkedList<Cell> emptyList;
	
	public SegregationGrid(String input) {
		super(input);
		buildList();
	}
	
	@Override
	protected void init() {
		super.init();
		similar = 0.5;
		satisfied = false;
		//myParameters = new SegregationParameters();

	}
	
	@Override
	public void run() {
		calculateStates();
		updateStates();
	}
	
	private void buildList() {
		emptyList = new LinkedList<Cell>();
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[row].length; col++){ 
				if(map[row][col].getCurrentState().equals("empty")) {
					emptyList.add(map[row][col]);
				}
			}
		}
	}
	
	private void calculateStates() {
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[0].length; col++){ 
				if(!map[row][col].getCurrentState().equals("empty")) {
					Map<String, Integer> gridStats = getNeighbors(row, col);
					int val1 = gridStats.get(map[row][col].getCurrentState());
					int val2=0;
					for(String s: gridStats.keySet()) {
						if(!s.equals(map[row][col].getCurrentState())) {
							val2 = gridStats.get(s);
						}
					}
					
					if((double) val1/(val1+val2) < similar) {
						if(emptyList.getFirst().getNextState() == null) {
							emptyList.pop().setNextState(map[row][col].getCurrentState());
							map[row][col].setNextState("empty");
							emptyList.addLast(map[row][col]);
						}
					}
				}
			}
		}
	}
	
	private HashMap<String, Integer> getNeighbors(int row, int col) {
		HashMap<String, Integer> ret = new HashMap<String, Integer>();
		ret.put("red", 0);
		ret.put("blue", 0);
		for(int i=-1; i <2; i++) {
			for(int j=-1; j<2; j++) {
				if(i != 0 || j != 0) {
					placeObject(i+row, j+col, ret);
				}
			}
		}
		return ret;
	}
	
	private void placeObject(int i, int j, Map<String, Integer> ret) {
		if(i >= 0 && i < map.length && j >=0 && j < map[0].length) {
			if(!map[i][j].getCurrentState().equals("empty")) {
				ret.put( map[i][j].getCurrentState(), ret.get(map[i][j].getCurrentState())+1);
			}
		}
	}
	
	private void updateStates() {
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[row].length; col++){ 
				if(map[row][col].getNextState() != null) {
					map[row][col].changeState();
				}
			}
		}
	}
}
