package cellsociety.grid;

import cellsociety.screen.AbstractScreen;
import cellsociety.xml.XmlLoader;
import cellsociety.cell.Cell;
import cellsociety.managers.ConfigManager;
import cellsociety.parameters.AbstractParameters;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public abstract class AbstractGrid extends AbstractScreen {

	protected Cell[][] map;
	protected GridPane mapPane;
	protected GridPane paramPane;
	protected XmlLoader myLoader;
	protected AbstractParameters myParameters;
	protected double speed;
	protected int timer = 0;

	public AbstractGrid(String input) {
		loadXml(input);
		makeScene();
	}

	public void makeScene() {
		WIDTH = (int) (mapPane.getBoundsInLocal().getWidth()
				+ ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "paramWidth")));
		HEIGHT = (int) (mapPane.getBoundsInLocal().getHeight());
		scene = new Scene(root, WIDTH, HEIGHT);
		root.getChildren().add(mapPane);
		paramPane.setLayoutX(mapPane.getBoundsInLocal().getWidth()
				+ ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "offset")));
		root.getChildren().add(paramPane);
		reset();
	}

	public void loadXml(String file) {
		myLoader = new XmlLoader(file);
		map = myLoader.buildGrid(mapPane);
		myParameters.setValues(myLoader.getParams());
	}

	@Override
	protected void init() {
		root = new Group();
		mapPane = new GridPane();
		paramPane = new GridPane();
	}

	@Override
	public void run() {
		speed = myParameters.getValue("Speed");
		if (timer == 0) {
			if (myParameters.isDone()) {
				nextScreen = ConfigManager.getObject("startScreen");
			} else if (myParameters.isReset()) {
				reset();
			}
			calculateStates();
			timer++;
		} else if (timer >= (int) ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "time"))
				/ this.speed) {
			updateStates();
			timer = 0;
		} else {
			timer++;
		}
	}

	abstract protected void calculateStates();

	abstract protected void updateStates();

	protected void done() {
		nextScreen = ConfigManager.getObject(AbstractScreen.class, ConfigManager.getString("startScreen"));
	}

	protected void reset() {
		map = myLoader.buildGrid(mapPane);
		myParameters.setReset(false);
	}
}
