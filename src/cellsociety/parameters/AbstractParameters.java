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
		for (String curr : sliderNames) {
			List<Double> sliderValues = ConfigManager
					.getDoubleList(ConfigManager.scope(this.getClass().getName(), curr));
			Toggle nextToggle = new Toggle(curr, sliderValues.get(0), sliderValues.get(1), sliderValues.get(2));
			values.put(curr, sliderValues.get(0));
			ToggleList.add(nextToggle);
			ToggleMap.put(curr, nextToggle);
			paneBox.getChildren().add(nextToggle);
		}
		Button reset = new Button("RESET");
		reset.setOnAction((event) -> {
			this.reset = true;
		});
		Button back = new Button("END SIMULATION");
		back.setOnAction((event) -> {
			done = true;
		});
		HBox buttons = new HBox();
		buttons.getChildren().add(reset);
		buttons.getChildren()
				.add(new Rectangle(ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "offset")),
						ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "offset")),
						Color.TRANSPARENT));
		buttons.getChildren().add(back);
		paneBox.getChildren().add(buttons);
		pane.add(paneBox, 0, 0);

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
