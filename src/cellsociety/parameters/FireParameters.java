package cellsociety.parameters;

import java.util.List;

import cellsociety.managers.ConfigManager;
import javafx.scene.layout.Pane;

public class FireParameters extends AbstractParameters {
	

	@Override
	protected void init() {
		List<String> sliderNames = ConfigManager.getStringList(ConfigManager.scope(this.getClass().getName(),  "names"));
		
		for(String curr: sliderNames){
			List<Double> values = ConfigManager.getDoubleList(ConfigManager.scope(this.getClass().getName(),  curr));
			ParamSlider nextSlider = new ParamSlider(curr, values.get(0), values.get(1), values.get(2)); 
			sliderList.add(nextSlider);
		}
		Pane parametersPane = new Pane();
		parametersPane.getChildren().addAll(sliderList);
		root.getChildren().add(parametersPane);
	}

}
