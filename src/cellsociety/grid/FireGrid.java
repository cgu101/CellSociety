package cellsociety.grid;

import cellsociety.cell.AbstractCell;
import cellsociety.managers.ConfigManager;
import cellsociety.parameters.FireParameters;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class FireGrid extends AbstractGrid {
	@Override
	protected void init() {
		makeScene();
		myParameters = new FireParameters();
		this.scene = myParameters.getScene();
	}
	
	@Override
	public void run(){
		//for each cell in map 
			//check if cell is on fire
				//(Set cell's next state to empty)
		
				//if cell is on fire... grab neighbors and put in set
				//for each neighbor... if (probCatch)
				//nextState = fire 
		
		AbstractCell cell = null;
		for(int row = 0; row < map.length; row++){
			for(int col = 0; col < map[row].length; col++){
				cell = map[row][col];
				if(cell.toString() == "onFire"){
					spreadFire(row,col);
				}
			}
		}
		
	}

	private void spreadFire(int row, int col) {
		AbstractCell cell = map[row][col];
		//cell.changeState();
		//No
	}
	
}
