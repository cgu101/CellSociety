package cellsociety.grid;

import cellsociety.managers.ConfigManager;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class WatorWorldGrid extends AbstractGrid {
	@Override
	protected void init() {
		WIDTH = ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "width"), 800);
		HEIGHT = ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "height"), 400);
		root = new Group();
		scene = new Scene(root, WIDTH, HEIGHT);
		Button back = new Button("back");
		back.setLayoutX(ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "go-x")));
		back.setLayoutY(ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "go-y")));
		back.setOnAction((event) -> {
			nextScreen = ConfigManager.getObject("startScreen");
		});
		root.getChildren().add(back);
	}
}