package cellsociety.main;

import cellsociety.managers.RootManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	
	private static final int FRAMES_PER_SECOND = 60;
	private static final double MILLISECOND_DELAY = 1000/FRAMES_PER_SECOND;
	private RootManager manager;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		manager = new RootManager();
		manager.init(primaryStage);
		primaryStage.setTitle("Cell Society");
		
	    KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                e -> manager.run());
	    
	    Timeline animation = new Timeline();
	    animation.setCycleCount(Timeline.INDEFINITE);
	    animation.getKeyFrames().add(frame);
	    animation.play();
	}
	
    public static void main (String[] args) {
        launch(args);
    }
}
