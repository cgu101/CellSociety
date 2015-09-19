package cellsociety.grid;

import cellsociety.cell.Cell;
import cellsociety.parameters.GameOfLifeParameters;

public class GameOfLifeGrid extends AbstractGrid {

	//Any live cell with fewer than two live neighbours dies, as if caused by under-population.
		//Any live cell with two or three live neighbours lives on to the next generation.
		//Any live cell with more than three live neighbours dies, as if by overcrowding.
		//Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
	
	public GameOfLifeGrid(String input) {
		super(input);
	}

	@Override
	protected void init() {
		super.init();
		myParameters = new GameOfLifeParameters(this.paramPane);
	}
	
	@Override
	protected void calculateStates() {
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[row].length; col++){
				changeState(map[row][col], row,col);
			}
		}
		
	}

	@Override
	protected void updateStates() {
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[row].length; col++){
				map[row][col].changeState();
			}
		}	
	}

	
	private void changeState(Cell cell, int row, int col){
		String currentState = cell.getCurrentState();
		int liveNeighbors = getLiveNeighbors(row, col);
		
		if(currentState.equals("live")){
			if(liveNeighbors < 2){
				cell.setNextState("dead");
			}	
			else if(liveNeighbors == 2 || liveNeighbors == 3){
				cell.setNextState("live");
			}
			else if(liveNeighbors >= 3){
				cell.setNextState("dead");
			}
		}
		else{
			if(liveNeighbors == 3){
				cell.setNextState("live");
			}
			else{
				cell.setNextState("dead");
			}
		}
	}

	private int getLiveNeighbors(int row, int col) {
		int neighbors[][] = {{row-1, col+1}, {row-1, col}, {row-1, col-1}, {row, col+1}, 
							{row, col-1}, {row+1, col+1}, {row+1, col}, {row+1, col-1}};
	
		int liveCount = 0;
		for(int[] curr : neighbors){
			if(curr[0] >= 0 && curr[0]<map.length && curr[1] >= 0 && curr[1] < map[curr[0]].length){
				Cell cell = map[curr[0]][curr[1]];
				if(cell.getCurrentState().equals("live")){
					liveCount++;
				}
			}
		}
		
		return liveCount;
	}
	
	@Override
	protected void reset() {
		speed = myParameters.getValue("Speed");
		super.reset();
		
	}
}


