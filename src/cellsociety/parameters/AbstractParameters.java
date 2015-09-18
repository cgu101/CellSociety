package cellsociety.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cellsociety.managers.ConfigManager;
import cellsociety.parameters.AbstractParameters.Toggle;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public abstract class AbstractParameters {

	protected int WIDTH = 250;
	protected int HEIGHT = 500;

	protected Scene paramScene;
	protected Group root;
	protected List<Toggle> sliderList;
	protected HashMap<String, Double> values;
	protected Font font = Font.loadFont(getClass().getClassLoader().getResourceAsStream("SECRCODE.TTF"),
			ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "FontSize")));

	public AbstractParameters() {
		this.root = new Group();
		this.paramScene = new Scene(root, WIDTH, HEIGHT);
		this.sliderList = new ArrayList<Toggle>();
		this.values = new HashMap<String, Double>();
		init();
	}

	protected void init() {
		List<String> sliderNames = ConfigManager.getStringList(ConfigManager.scope(this.getClass().getName(), "names"));
		double yOffset = 0;
		for (String curr : sliderNames) {
			List<Double> sliderValues = ConfigManager
					.getDoubleList(ConfigManager.scope(this.getClass().getName(), curr));
			Toggle nextToggle = new Toggle(curr, sliderValues.get(0), sliderValues.get(1), sliderValues.get(2));
			values.put(curr, sliderValues.get(0));
			nextToggle.setLayoutX(ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "x")));
			yOffset += ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "y"));
			nextToggle.setLayoutY(yOffset);
			sliderList.add(nextToggle);
			root.getChildren().add(nextToggle);
		}
	}

	public Scene getScene() {
		return paramScene;
	}

	public double getValue(String input) {
		return values.get(input);
	}

	protected class Toggle extends VBox {
		private String name;
		private Label nameLabel;
		private Slider slider;
		private TextField valueField;

		protected Toggle(String n, Double x, Double y, Double z) {
			name = n;
			slider = new Slider();
			slider.setValue(x);
			slider.setMin(y);
			slider.setMax(z);
			slider.setShowTickMarks(true);
			slider.setShowTickLabels(true);
			slider.setMajorTickUnit((slider.getMax() - slider.getMin()));
			nameLabel = new Label(name + ":");
			nameLabel.setFont(font);
			valueField = new TextField(String.format("%.2f", slider.getValue()));
			valueField.setPrefColumnCount(valueField.getText().length());
			valueField.setAlignment(Pos.CENTER);
			valueField.textProperty()
					.addListener((ObservableValue<? extends String> ob, String oldVal, String newVal) -> {
						valueField.setPrefColumnCount(valueField.getText().length());
					});
			valueField.setOnAction(e -> {
				try {
					String current = this.valueField.getCharacters().toString();
					double val = Double.parseDouble(current);
					if (val >= this.slider.getMin() && val <= this.slider.getMax()) {
						this.slider.setValue(val);
						values.put(name, val);
					} else {
						valueField.setText(String.format("%.2f", this.slider.getValue()));
					}
				} catch (NumberFormatException d) {
					valueField.setText(String.format("%.2f", this.slider.getValue()));
				}
			});
			HBox lowerBox = new HBox();
			lowerBox.getChildren().addAll(slider, valueField);
			this.getChildren().addAll(nameLabel, lowerBox);
			slider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
				values.put(name, (Double) newVal);
				valueField.setText(String.format("%.2f", newVal));
			});
		}

		protected String getName() {
			return this.name;
		}

	}
}
