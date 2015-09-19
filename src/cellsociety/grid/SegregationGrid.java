package cellsociety.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cellsociety.cell.Cell;
import cellsociety.parameters.SegregationParameters;

public class SegregationGrid extends AbstractGrid {
	
	private double similar;
	private LinkedList<Cell> emptyList;
	private List<Cell> emptyList1;
	
	public SegregationGrid(String input) {
		super(input);
	}
	
	@Override
	protected void init() {
		super.init();
		myParameters = new SegregationParameters(this.paramPane);
	}
	
	private void buildList() {
		emptyList = new LinkedList<Cell>();
		emptyList1 = new ArrayList<Cell>();
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[row].length; col++){ 
				if(map[row][col].getCurrentState().equals("empty")) {
					emptyList.add(map[row][col]);
					emptyList1.add(map[row][col]);
				}
			}
		}
	}
	
	@Override
	protected void calculateStates() {
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[0].length; col++){ 
				if(!map[row][col].getCurrentState().equals("empty")) {
					changeState(map[row][col], row, col);
				}
			}
		}
	}
	
	private void changeState(Cell c, int row, int col) {
		Map<String, Integer> gridStats = getNeighbors(row, col);
		int val1 = gridStats.get(map[row][col].getCurrentState());
		int val2=0;
		for(String s: gridStats.keySet()) {
			if(!s.equals(map[row][col].getCurrentState())) {
				val2 = gridStats.get(s);
			}
		}
		
		if((double) val1/(val1+val2) < similar) {
			int rand = (int) (Math.random()*emptyList1.size());
			if(emptyList1.get(rand).getNextState() == null) {
				emptyList1.get(rand).setNextState(map[row][col].getCurrentState());
				emptyList1.remove(rand);
				map[row][col].setNextState("empty");
				emptyList1.add(map[row][col]);
			}
//			if(emptyList.getFirst().getNextState() == null) {							
//				emptyList.pop().setNextState(map[row][col].getCurrentState());
//				map[row][col].setNextState("empty");
//				emptyList.addLast(map[row][col]);
//			}
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
	
	@Override
	protected void updateStates() {
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[row].length; col++){ 
				if(map[row][col].getNextState() != null) {
					map[row][col].changeState();
				}
			}
		}
	}
	
	@Override
	protected void reset() {
		speed = myParameters.getValue("Speed");
		similar = myParameters.getValue("Similar");
		//System.out.printf("The speed is: %s, the Similarity is: %s\n", speed, similar);
		super.reset();
		buildList();
	}
}
