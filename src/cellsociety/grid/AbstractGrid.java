package cellsociety.grid;

import cellsociety.screen.AbstractScreen;
import cellsociety.cell.Cell;
import cellsociety.managers.ConfigManager;
import cellsociety.parameters.AbstractParameters;
import javafx.scene.Group;
import javafx.scene.Scene;

public class AbstractGrid extends AbstractScreen {

	protected Cell[][] map;
	protected Scene mapScene;
	protected Scene paramScene;
	protected AbstractParameters myParameters;
	protected void done() {
		nextScreen = ConfigManager.getObject(AbstractScreen.class,
				ConfigManager.getString(ConfigManager.scope(this.getClass().getName(), "startScreen")));
	}

	protected void makeScene(){
		WIDTH = ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "mapWidth"), 800)
				+ ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "paramWidth"), 200);
		HEIGHT = ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "height"), 800);
		
		root = new Group();
		scene = new Scene(root, WIDTH, HEIGHT);	
		
		
	}

	@Override
	protected void init() {
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
