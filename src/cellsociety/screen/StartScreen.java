package cellsociety.screen;

import java.util.ArrayList;
import java.util.List;

import com.sun.prism.paint.Color;

import cellsociety.managers.ConfigManager;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class StartScreen extends AbstractScreen {
	private int WIDTH;
	private int HEIGHT;
	private List<Icon> iconList;
	private Text titleBox;

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
		iconList = new ArrayList<Icon>();
		List<String> iconNames = ConfigManager.getStringList(ConfigManager.scope(this.getClass().getName(), "iconNames"));
		
		for(String s: iconNames) {
			Icon toAdd = new Icon(s);
			iconList.add(toAdd);
			root.getChildren().add(toAdd);
		}
	}
	
	private void initTitle() {
		Font f = Font.loadFont(getClass().getClassLoader().getResourceAsStream("SECRCODE.TTF"), 40);
		Text t = new Text(400, 50, "Cell Society");
		t.setFont(f);
		
		titleBox = new Text(400, 100, "");
		titleBox.setFont(f);
		
		root.getChildren().add(t);
		root.getChildren().add(titleBox);
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
		
		private Icon(String s) {
			iconName = s;
			Image img = new Image(getClass().getClassLoader().getResourceAsStream(ConfigManager.getString(ConfigManager.scope(s, "image"))));
			image = new ImageView(img);
			setGraphic(image);	
		}
		
		private void handleAction() {
			System.out.println("Clicking: " + iconName);
		}
		
		private void handleHover() {
			System.out.println("Hovering: " + iconName);
			titleBox.setText(iconName);
		}
	}
	
	// StartScreen testing
//	public static void main(String...args) {
//		StartScreen test = new StartScreen();
//		System.out.println(String.format(test.getClass().getName(), "width"));
//		System.out.println(test.WIDTH);
//		System.out.println(test.HEIGHT);
//
//	}
}
