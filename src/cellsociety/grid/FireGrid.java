package cellsociety.grid;
import java.util.Arrays;
import java.util.Random;

import cellsociety.cell.Cell;
import cellsociety.parameters.FireParameters;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class FireGrid extends AbstractGrid {
	private Double probCatch;
	private Double probGrow;
	private Double probLightning;
	private boolean stillAFire;
	
	@Override
	protected void init() {
		makeScene();
		probCatch = 0.6;
		probLightning = 0.2;
		probGrow = 0.2;
		myParameters = new FireParameters();
	}
	
	@Override
	public void run(){
		stillAFire = false;
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[row].length; col++){
				changeState(map[row][col], row,col);
			}
		}
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[row].length; col++){
				map[row][col].setToNextState();
				if(map[row][col].getCurrentState() == "BURNING1"){
					map[row][col].setFill(Color.RED);
				}
			}
		}
	}
	
	private void changeState(Cell cell, int row, int col){
		String currentState = cell.getCurrentState();
		if(currentState == "TREE"){
			catchFire(cell,row, col);
		}
		else if(currentState == "EMPTY"){
			treeGrowth(row,col);
		}
		else if(currentState == "BURNING1"){
			cell.setNextState("BURNING2");
			stillAFire = true;
		}
		else if(currentState == "BURNING2"){
			cell.setNextState("EMPTY");
		}
	}

	private void treeGrowth(int row, int col) {
		Random rand = new Random();
		if(rand.nextDouble() <= probGrow){
			map[row][col].setNextState("TREE");
		}
	}

	private void catchFire(Cell cell, int row, int col) {
		int neighborFires = neighborsOnFire(row, col);
		Double fireChance = (1-Math.pow((1-probCatch), neighborFires)) + (probLightning);
		
		Random rand = new Random();
		if(cell.getCurrentState() == "TREE" && (rand.nextDouble() <= fireChance)){
			System.out.println(neighborFires + "neighbors" + fireChance + "chance");
			cell.setNextState("BURNING1");
			stillAFire = true;
		}
	}

	private int neighborsOnFire(int row, int col) {
		//North
		int count = 0;
		if(row-1 >= 0){
			if(map[row-1][col].getCurrentState() == "BURNING1" || map[row-1][col].getCurrentState() == "BURNING2" )
				count++;
		}
		//South
		if(row+1 < map.length){
			if(map[row+1][col].getCurrentState() == "BURNING1" || map[row+1][col].getCurrentState() == "BURNING2" )
				count++;
		}
		// West
		if(col-1 >= 0){
			if(map[row][col-1].getCurrentState() == "BURNING1" || map[row][col-1].getCurrentState() == "BURNING2" )
				count++;
		}
		//East
		if(col+1 < map[row].length) {
			if(map[row][col+1].getCurrentState() == "BURNING1" || map[row][col+1].getCurrentState() == "BURNING2" )
				count++;
		}
		return count;
	}
	
}
