package cellsociety.grid;

import cellsociety.screen.AbstractScreen;
import cellsociety.xml.XmlLoader;
import cellsociety.cell.Cell;
import cellsociety.managers.ConfigManager;
import cellsociety.parameters.AbstractParameters;
import javafx.geometry.Pos;
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
	protected boolean calculated = false;

	public AbstractGrid(String input) {
		loadXml(input);
		makeScene();
	}

	public void makeScene() {
		WIDTH = (int) (mapPane.getBoundsInLocal().getWidth()
				+ ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "paramWidth")));
		HEIGHT = (int) (Math.max(mapPane.getBoundsInLocal().getHeight(), 300));
		mapPane.setAlignment(Pos.CENTER_LEFT);
		paramPane.setAlignment(Pos.CENTER_RIGHT);
		GridPane temp = new GridPane();
		temp.setHgap(25.0);
		temp.add(mapPane, 0, 0);
		temp.add(paramPane, 1, 0);
		temp.setAlignment(Pos.CENTER);
		scene = new Scene(temp, WIDTH, HEIGHT);
	//	root.getChildren().add(mapPane);
	//	paramPane.setLayoutX(mapPane.getBoundsInLocal().getWidth()
	//			+ ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "offset")));
	//	root.getChildren().add(paramPane);
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
		if (!calculated) {
			calculateStates();
			calculated = true;
		}
		speed = myParameters.getValue("Speed");
		if (timer == 0) {
			if (myParameters.isDone()) {
				nextScreen = ConfigManager.getObject("startScreen");
			} else if (myParameters.isReset()) {
				reset();
			} else if (myParameters.isStep()) {
				timer = ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "time"));
				myParameters.setStep(false);
			} else {
				if (!myParameters.isPause()) {
					timer++;
				}
			}
		} else
			if (timer >= ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "time")) / this.speed) {
			updateStates();
			calculated = false;
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
		myParameters.setStep(false);
		myParameters.resetPause();
		timer = 0;
		calculated = false;
	}
}
