package cellsociety.grid;

import cellsociety.screen.AbstractScreen;
import cellsociety.cell.AbstractCell;
import cellsociety.managers.ConfigManager;
import javafx.scene.Scene;

public class AbstractGrid extends AbstractScreen {

	protected AbstractCell[][] map;
	protected Scene mapScene;
	protected Scene paramScene;

	protected void done() {
		nextScreen = ConfigManager.getObject(AbstractScreen.class,
				ConfigManager.getString(ConfigManager.scope(this.getClass().getName(), "startScreen")));
	}

	protected void makeScene() {

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
