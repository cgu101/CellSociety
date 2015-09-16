package cellsociety.managers;

import cellsociety.screen.AbstractScreen;
import javafx.stage.Stage;

public class RootManager {

	private Stage stage;
	private AbstractScreen currentScreen;

	public void init(Stage s) {
		stage = s;
		currentScreen = ConfigManager.getObject(AbstractScreen.class,
				ConfigManager.getString(ConfigManager.scope(this.getClass().getName(), "startScreen")));
		stage.setScene(currentScreen.getScene());
		stage.show();
	}

	public void run() {
		currentScreen.run();
		if (currentScreen.getNextScreen() != null) {
			currentScreen = currentScreen.getNextScreen();
			stage.setScene(currentScreen.getScene());
			stage.show();
		}
	}

}
