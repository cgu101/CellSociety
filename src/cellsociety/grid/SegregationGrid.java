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
	
	public SegregationGrid(String s) {
		buildList();
	}
	
	@Override
	protected void init() {
		similar = 0.7;
		satisfied = false;
		makeScene();
		//myParameters = new SegregationParameters();

	}
	
	@Override
	public void run() {
//		if(!satisfied) {
//			gridStatus();
//			gridUpdate();
//		}
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
	
	private void gridStatus() {
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[row].length; col++){ 
				if(map[row][col].getCurrentState().equals("empty")) {
					Map<String, Integer> gridStats = getNeighbors(map[row][col]);
					int val1=0; int val2=0;
					for(String s: gridStats.keySet()) {
						if(s.equals(map[row][col].getCurrentState())) {
							val1 = gridStats.get(s);
						} else {
							val2 = gridStats.get(s);
						}
					}
					
					if((double) val1/(val1+val2) < similar) {
						emptyList.pop().setNextState(map[row][col].getCurrentState());
						map[row][col].setNextState("empty");
					}
				}
			}
		}
	}
	
	private HashMap<String, Integer> getNeighbors(Cell c) {
		HashMap<String, Integer> ret = new HashMap<String, Integer>();
		for(int i=-1; i <2; i++) {
			for(int j=-1; j<2; j++) {
				if(i != 0 && j != 0) {
					placeObject(i, j, ret);
				}
			}
		}
		return ret;
	}
	
	private void placeObject(int i, int j, Map<String, Integer> ret) {
		if(i >= 0 && i < map.length && j >=0 && j < map[0].length) {
			if(!map[i][j].getCurrentState().equals("empty")) {
				if(ret.containsKey(map[i][j].getCurrentState())) {
					ret.put( map[i][j].getCurrentState(), ret.get(map[i][j].getCurrentState())+1);
				} else {
					ret.put(map[i][j].getCurrentState(), 1);
				}
			}
		}
	}
	
	private void gridUpdate() {
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[row].length; col++){ 
				map[row][col].changeState();
			}
		}
	}
}
