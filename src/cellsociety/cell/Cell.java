package cellsociety.cell;

import cellsociety.managers.ConfigManager;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public abstract class Cell extends Rectangle {
	
	private String currentState;
	private String nextState;
	private String type;
	
	public Cell(double x, double y, double height, double width, Color color, String state, String type) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setFill(color);
		
		currentState = state;
		nextState = null;
		this.type = type;
	}
	
	public void setNextState(String s) {
		nextState = s;
	}
	
	public String getCurrentState() {
		return currentState;
	}
	
	public void changeState() {
		// Config file format cellsociety.cell.Cell."Type of Simulation"."state"="color representing state"
		String newColor = ConfigManager.getString(ConfigManager.scope(this.getClass().getName(), ConfigManager.scope(type, nextState)));
		currentState = nextState;
		nextState = null;
		setFill(Paint.valueOf(newColor));
	}
}
