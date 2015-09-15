package cellsociety.screen;

import java.util.List;

import cellsociety.managers.ConfigManager;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StartScreen extends AbstractScreen {
	private int WIDTH;
	private int HEIGHT;
	private List<Icon> iconList;

	@Override
	protected void init() {
		WIDTH = ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "width"), 800);
		HEIGHT = ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "height"), 400);
		root = new Group();
		scene = new Scene(root, WIDTH, HEIGHT);
		initIconList();
		initTitle();
	}
	
	private void initIconList() {
		List<String> iconNames = ConfigManager.getStringList(ConfigManager.scope(this.getClass().getName(), "iconNames"));
		
		int xPos = 40;
		for(String s: iconNames) {
			Icon toAdd = new Icon(s, xPos, 40);
			iconList.add(toAdd);
			root.getChildren().add(toAdd);
			xPos+=120;
		}
	}
	
	private void initTitle() {
		
	}
	
	@Override
	public void run() {
		for(Icon i: iconList) {
			i.setOnAction(e->i.handleAction());
			i.setOnMouseEntered(e->i.handleHover());
		}
	}
	
	
	private class Icon extends Button {
		private ImageView image;
		private String iconName;
		
		private Icon(String s, int posX, int posY) {
			iconName = s;
			image = new ImageView(new Image(getClass().getResourceAsStream(ConfigManager.scope(s, "image"))));
			image.setX(posX);
			image.setY(posY);
			setGraphic(image);
		}
		
		private void handleAction() {
			System.out.println("Clicking: " + iconName);
		}
		
		private void handleHover() {
			System.out.println("Hovering: " + iconName);
		}
	}
	
	// StartScreen testing
	public static void main(String...args) {
		StartScreen test = new StartScreen();
		System.out.println(String.format(test.getClass().getName(), "width"));
		System.out.println(test.WIDTH);
		System.out.println(test.HEIGHT);

	}
}
