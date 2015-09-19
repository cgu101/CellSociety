package cellsociety.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cellsociety.grid.AbstractGrid;
import cellsociety.managers.ConfigManager;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public abstract class AbstractParameters {

	protected GridPane pane;
	protected List<Toggle> ToggleList;
	protected HashMap<String, Toggle> ToggleMap;
	protected HashMap<String, Double> values;
	protected Font font = Font.loadFont(getClass().getClassLoader().getResourceAsStream("SECRCODE.TTF"),
			ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "FontSize")));
	protected boolean done = false;
	protected boolean reset = false;
	protected boolean pause = true;
	protected boolean step = false;

	protected Button resetButton;
	protected Button pauseButton;
	protected Button backButton;
	protected Button stepButton;
	protected HBox buttons;

	public AbstractParameters(GridPane pane) {
		this.pane = pane;
		this.ToggleList = new ArrayList<Toggle>();
		this.ToggleMap = new HashMap<String, Toggle>();
		this.values = new HashMap<String, Double>();
		init();
	}

	protected void init() {
		List<String> sliderNames = ConfigManager.getStringList(ConfigManager.scope(this.getClass().getName(), "names"));
		VBox paneBox = new VBox();
		int i = 0;
		for (String curr : sliderNames) {
			List<Double> sliderValues = ConfigManager
					.getDoubleList(ConfigManager.scope(this.getClass().getName(), curr));
			Toggle nextToggle = new Toggle(curr, sliderValues.get(0), sliderValues.get(1), sliderValues.get(2));
			values.put(curr, sliderValues.get(0));
			ToggleList.add(nextToggle);
			ToggleMap.put(curr, nextToggle);
			//paneBox.getChildren().add(nextToggle);
			pane.add(nextToggle, 0, i);
			i++;
		}
		resetButton = new Button("RESET");
		resetButton.setOnAction((event) -> {
			this.reset = true;
		});
		stepButton = new Button("STEP");
		stepButton.setOnAction((event) -> {
			this.step = true;
		});
		pauseButton = new Button("PLAY");
		pauseButton.setOnAction((event) -> {
			this.pause = !this.pause;
			if (this.pause) {
				pauseButton.setText("PLAY");
				buttons.getChildren().add(stepButton);
			} else {
				pauseButton.setText("PAUSE");
				buttons.getChildren().remove(stepButton);
			}
		});
		backButton = new Button("MENU");
		backButton.setOnAction((event) -> {
			done = true;
		});
		buttons = new HBox();
		buttons.getChildren().add(backButton);
		buttons.getChildren().add(resetButton);
		buttons.getChildren().add(pauseButton);
		buttons.getChildren().add(stepButton);
	//	paneBox.getChildren().add(buttons);
		pane.add(buttons, 0, i);
//		pane.add(backButton, 0, i);
//		pane.add(resetButton, 1, i);
//		pane.add(pauseButton, 2, i);
//		pane.add(stepButton, 3, i);
		
	}

	public double getValue(String input) {
		return values.get(input);
	}

	public boolean isDone() {
		return done;
	}

	public void setReset(boolean input) {
		reset = input;
	}

	public boolean isReset() {
		return reset;
	}

	public boolean isPause() {
		return pause;
	}

	public boolean isStep() {
		return step;
	}

	public void setStep(boolean step) {
		this.step = step;
	}

	public void resetPause() {
		pause = true;
		pauseButton.setText("PLAY");
		if (!buttons.getChildren().contains(stepButton)) {
			buttons.getChildren().add(stepButton);
		}
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
					if (val >= getMin() && val <= getMax()) {
						setVal(val);
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

		public void setVal(double val) {
			this.slider.setValue(val);
		}

		public double getMax() {
			return this.slider.getMax();
		}

		public double getMin() {
			return this.slider.getMin();
		}
	}

	public void setValues(NodeList params) {
		for (Node child = params.item(0).getFirstChild(); child != null; child = child.getNextSibling()) {
			String name = child.getNodeName();
			if (values.containsKey(name)) {
				try {
					double val = Double.parseDouble(child.getTextContent());
					if (val >= ToggleMap.get(name).getMin() && val <= ToggleMap.get(name).getMax()) {
						ToggleMap.get(name).setVal(val);
						values.put(name, val);
					}
				} catch (NumberFormatException e) {
				}
			}
		}
	}

}
