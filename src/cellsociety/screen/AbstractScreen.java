package cellsociety.screen;

import javafx.scene.Group;
import javafx.scene.Scene;

public abstract class AbstractScreen {
	protected Group root;
	protected Scene scene;
	protected int WIDTH;
	protected int HEIGHT;
	protected String titleName = "";
	protected AbstractScreen nextScreen = null;

	public AbstractScreen() {
		init();
	}

	abstract public void run();

	abstract protected void init();

	public Scene getScene() {
		return scene;
	}

	public AbstractScreen getNextScreen() {
		return nextScreen;
	}

}
