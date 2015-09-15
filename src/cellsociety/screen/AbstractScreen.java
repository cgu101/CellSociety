package cellsociety.screen;

import javafx.scene.Group;
import javafx.scene.Scene;

public abstract class AbstractScreen {
	protected Group root;
	protected Scene scene;

	public AbstractScreen() {
		init();
	}
	
	abstract public void run();
	abstract protected void init();
	
	public Scene getScene() {
		return scene;
	}

}
