package cellsociety.grid;

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
	
}
