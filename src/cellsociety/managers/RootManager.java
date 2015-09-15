package cellsociety.managers;

import cellsociety.screen.AbstractScreen;
import javafx.stage.Stage;

public class RootManager {
	
	private Stage stage;
	private AbstractScreen currentScreen;
	
	public void init(Stage s) {
		stage = s;
		currentScreen = ConfigManager.getObject(AbstractScreen.class, String.format(this.getClass().getName(), "startScreen"));
		stage.setScene(currentScreen.getScene());
	}
	
	public void run() {
		currentScreen.run();
	}

}
