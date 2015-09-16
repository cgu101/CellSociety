package cellsociety.screen;

import java.util.ArrayList;
import java.util.List;

import cellsociety.managers.ConfigManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StartScreen extends AbstractScreen {
	private List<Icon> iconList;
	private Icon selected;
	private Text titleBox;
	private ComboBox<String> xmlLoader;
	private Button goButton;

	@Override
	protected void init() {
		WIDTH = ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "width"), 800);
		HEIGHT = ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "height"), 400);
		root = new Group();
		scene = new Scene(root, WIDTH, HEIGHT);
		initIconList();
		initTitle();
		initXmlList();
		initGoButton();
		click(iconList.get(0));
	}

	private void initIconList() {
		iconList = new ArrayList<Icon>();
		List<String> iconNames = ConfigManager
				.getStringList(ConfigManager.scope(this.getClass().getName(), "iconNames"));

		int offset = WIDTH / iconNames.size() / 2;
		for (String s : iconNames) {
			Icon toAdd = new Icon(s, offset, iconNames.size());
			iconList.add(toAdd);
			root.getChildren().add(toAdd);
			offset = offset + WIDTH / iconNames.size();
		}
	}

	private void initTitle() {
		createText("Cell Society", HEIGHT / 6, 40);
		titleBox = createText("", HEIGHT / 6 + 40, 20);
	}

	private void initXmlList() {
		xmlLoader = new ComboBox<String>();
		xmlLoader.setPromptText("XML File to Load");
		xmlLoader.setEditable(true);
		xmlLoader.setLayoutX(ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "xml-x")));
		xmlLoader.setLayoutY(ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "xml-y")));
		xmlLoader.setVisibleRowCount(3);
		root.getChildren().add(xmlLoader);
	}

	private void initGoButton() {
		goButton = new Button("Go");
		goButton.setLayoutX(ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "go-x")));
		goButton.setLayoutY(ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "go-y")));
		root.getChildren().add(goButton);
	}

	private void handleKeyInput(KeyCode code) {
		int index = iconList.indexOf(selected);
		switch (code) {
		case RIGHT:
			if (index < iconList.size() - 1) {
				index++;
			}
			click(iconList.get(index));
			break;
		case LEFT:
			if (index > 0) {
				index--;
			}
			click(iconList.get(index));
			break;
		case UP:
			click(selected);
			break;
		case DOWN:
			click(selected);
			break;
		default:
		}
	}

	private void click(Icon input) {
		input.handleHover();
		input.handleAction();
	}

	private Text createText(String s, int yPos, int size) {
		Font font = Font.loadFont(getClass().getClassLoader().getResourceAsStream("SECRCODE.TTF"), size);
		Text t = new Text(s);
		t.setFont(font);
		t.setLayoutX((WIDTH - t.getBoundsInLocal().getWidth()) / 2);
		t.setLayoutY(yPos);
		root.getChildren().add(t);
		return t;
	}

	private void formatText(Text t, String s) {
		t.setText(s);
		t.setLayoutX((WIDTH - t.getBoundsInLocal().getWidth()) / 2);
	}

	@Override
	public void run() {
		updateIcons();
		updateButton();
		scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
	}

	private void updateIcons() {
		for (Icon i : iconList) {
			i.setOnAction(e -> i.handleAction());
			i.setOnMouseEntered(e -> i.handleHover());
			i.setOnMouseExited(e -> i.handleExit());
		}
	}

	private void updateButton() {
		goButton.setOnMouseClicked(e -> handleAction());
	}

	private void handleAction() {
		if (xmlLoader.getValue() != null) {
			System.out.println(xmlLoader.getValue());
		}
		nextScreen = ConfigManager.getObject(ConfigManager.scope(this.getClass().getName(), selected.iconName));
	}

	private class Icon extends Button {
		private ImageView image;
		private String iconName;
		private List<String> xmlList;

		private Icon(String s, int xOffset, int numberOfIcons) {
			xmlList = ConfigManager.getStringList(ConfigManager.scope(s, "xmlList"));
			Image img = new Image(getClass().getClassLoader()
					.getResourceAsStream(ConfigManager.getString(ConfigManager.scope(s, "image"))));
			image = new ImageView(img);
			image.setFitWidth(WIDTH / (numberOfIcons * 3 / 2));
			image.setPreserveRatio(true);
			image.setSmooth(true);
			image.setCache(true);
			setGraphic(image);
			setLayoutY(ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "yPos"), HEIGHT / 3));
			setLayoutX(xOffset - (image.getBoundsInParent().getWidth() + 18) / 2);
			iconName = s;
		}

		private void handleAction() {
			if (!xmlLoader.getItems().isEmpty() && !iconName.equals(titleName)) {
				xmlLoader.getItems().clear();
				xmlLoader.getItems().addAll(xmlList);
			} else if (xmlLoader.getItems().isEmpty()) {
				xmlLoader.getItems().addAll(xmlList);
			}
			titleName = iconName;
			selected = this;
		}

		private void handleHover() {
			formatText(titleBox, iconName);
		}

		private void handleExit() {
			formatText(titleBox, titleName);
		}
	}
}
