package cellsociety.managers;

import cellsociety.screen.AbstractScreen;
import javafx.stage.Stage;

public class RootManager {
	
	private Stage stage;
	private AbstractScreen currentScreen;
	
	public void init(Stage s) {
		stage = s;
	}
	
	public void run() {
		
	}

}
