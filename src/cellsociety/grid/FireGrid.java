package cellsociety.grid;
import java.util.Arrays;
import java.util.Random;

import cellsociety.cell.Cell;
import cellsociety.parameters.FireParameters;


public class FireGrid extends AbstractGrid {
	private Double probCatch;
	private Double probGrow;
	private Double probLightning;
	
	@Override
	protected void init() {
		probCatch = 0.6;
		probLightning = 0.2;
		probGrow = 0.0;
		myParameters = new FireParameters();
	}
	
	@Override
	public void run(){
		updateStates();
		
	}
	
	private void updateStates() {
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[row].length; col++){
				changeState(map[row][col], row,col);
			}
		}
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[row].length; col++){
				System.out.println("\nupdate " + map[row][col].getCurrentState() + " --> " + map[row][col].getNextState());
				if(map[row][col].getCurrentState().equals("fire")){
					System.out.println("here");
				}
				map[row][col].changeState();
				System.out.print("--> " + map[row][col].getCurrentState());
			}
		}
	}

	private void changeState(Cell cell, int row, int col){
		String currentState = cell.getCurrentState();
		if(currentState.equals("tree")){
			catchFire(cell,row, col);
		}
		else if(currentState.equals("earth")){
			treeGrowth(cell,row,col);
		}
		else if(currentState.equals("fire")){
			cell.setNextState("burnt");
		}
		else if(currentState.equals("burnt")){
			cell.setNextState("earth");
		}
	}

	private void treeGrowth(Cell cell, int row, int col) {
		Random rand = new Random();
		if(rand.nextDouble() <= probGrow){
			cell.setNextState("tree");
		}
		else{
			cell.setNextState(cell.getCurrentState());
		}
	}

	private void catchFire(Cell cell, int row, int col) {
		int neighborFires = neighborsOnFire(row, col);
		Double fireChance = (1-Math.pow((1-probCatch), neighborFires)) + (probLightning);
		
		Random rand = new Random();
		if(rand.nextDouble() <= fireChance){
			cell.setNextState("fire");
		}
		else{
			cell.setNextState(cell.getCurrentState());
		}
	}

	private int neighborsOnFire(int row, int col) {
		//North
		int count = 0;
		if(row-1 >= 0){
			if(map[row-1][col].getCurrentState().equals("fire") || map[row-1][col].getCurrentState().equals("burnt") )
				count++;
		}
		//South
		if(row+1 < map.length){
			if(map[row+1][col].getCurrentState().equals("fire") || map[row+1][col].getCurrentState().equals("burnt") )
				count++;
		}
		// West
		if(col-1 >= 0){
			if(map[row][col-1].getCurrentState().equals("fire") || map[row][col-1].getCurrentState().equals("burnt") )
				count++;
		}
		//East
		if(col+1 < map[row].length) {
			if(map[row][col+1].getCurrentState().equals("fire") || map[row][col+1].getCurrentState().equals("burnt") )
				count++;
		}
		return count;
	}
	
}
