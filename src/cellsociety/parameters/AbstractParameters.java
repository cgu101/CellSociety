package cellsociety.parameters;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Slider;

public abstract class AbstractParameters {

	protected int WIDTH = 500;
	protected int HEIGHT = 500;
	
	
	protected Scene paramScene;
	protected Group root;
	protected List<ParamSlider> sliderList;
	
	public AbstractParameters() {
		this.root = new Group();
		this.paramScene = new Scene(root, WIDTH, HEIGHT);
		this.sliderList = new ArrayList<ParamSlider>();
		init();
	}

	abstract protected void init();
	
	public Scene getScene() {
		return paramScene;
	}
	
	protected class ParamSlider extends Slider {
		private String name;
		
		protected ParamSlider(String n, Double v, Double y, Double double1) {
			name = n;
			setValue(v);
			setMin(y);
			setMax(double1);
		}

		protected String getName(){
			return this.name;
		}
		
		
	}
}
