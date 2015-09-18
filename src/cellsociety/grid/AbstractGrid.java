package cellsociety.grid;

import cellsociety.screen.AbstractScreen;
import cellsociety.xml.XmlLoader;
import cellsociety.cell.Cell;
import cellsociety.managers.ConfigManager;
import cellsociety.parameters.AbstractParameters;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public abstract class AbstractGrid extends AbstractScreen {

	protected Cell[][] map;
	protected GridPane mapPane;
	protected StackPane paramPane;
	protected XmlLoader myLoader;
	protected AbstractParameters myParameters;
	
	public AbstractGrid() {
		makeScene();
	}
	
	public void makeScene(){
		root = new Group();
		WIDTH = ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "mapWidth"), 600)
				+ ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "paramWidth"), 300);
		HEIGHT = ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "height"), 600);
		scene = new Scene(root, WIDTH, HEIGHT);	
		mapPane = new GridPane();
		paramPane = new StackPane();
	}
	
	public void loadXml(String file) {
		myLoader = new XmlLoader(file);
		map = myLoader.buildGrid(mapPane);
		root.getChildren().add(mapPane);
	}

	@Override
	protected void init() {
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
	
	protected void done() {
		nextScreen = ConfigManager.getObject(AbstractScreen.class, ConfigManager.getString("startScreen"));
	}
}
