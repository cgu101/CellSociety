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

	public AbstractGrid(String input) {
		loadXml(input);
		makeScene();
	}

	public void makeScene() {
		WIDTH = (int) (mapPane.getBoundsInLocal().getWidth() + ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "paramWidth")));
		HEIGHT = (int) (mapPane.getBoundsInLocal().getHeight());
		scene = new Scene(root, WIDTH, HEIGHT);
		root.getChildren().add(mapPane);
		paramPane.setLayoutX(mapPane.getBoundsInLocal().getWidth() +  ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "offset")));
		root.getChildren().add(paramPane);
	}

	public void loadXml(String file) {
		myLoader = new XmlLoader(file);
		map = myLoader.buildGrid(mapPane);
	}

	@Override
	protected void init() {
		root = new Group();
		mapPane = new GridPane();
		paramPane = new GridPane();
	}

	@Override
	public void run() {
		if (myParameters.isDone()){
			nextScreen = ConfigManager.getObject("startScreen");
		}
	}

	protected void done() {
		nextScreen = ConfigManager.getObject(AbstractScreen.class, ConfigManager.getString("startScreen"));
	}
}
