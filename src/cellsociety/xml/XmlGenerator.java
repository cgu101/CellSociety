package cellsociety.xml;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cellsociety.managers.ConfigManager;
import javafx.scene.Group;
import javafx.scene.Scene;

public class XmlGenerator  {
	
	private List<String> myParams;
	private HashMap<String, Integer> myStates;
	private List<String> states;
	private Map<String, String> myValues;
	private int rows = 35;
	private int cols = 35;
	private String type;
	private String className;
	private PrintWriter pw;
	
	public XmlGenerator(String s, String type) {
		try {
			this.type = type;
			pw = new PrintWriter("data/" + s);
			init(type);
		} catch (FileNotFoundException e) {};
	}
	
	protected void init() {
		int width = ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "width"), 800);
		int height = ConfigManager.getInt(ConfigManager.scope(this.getClass().getName(), "height"), 400);
		Group root = new Group();
		Scene scene = new Scene(root, width, height);
	}
 	
	protected void init(String type) {		
		className = ConfigManager.getString(ConfigManager.scope(this.getClass().getName(), type));
		myParams = ConfigManager.getStringList(ConfigManager.scope(className, "names"));
		
		// Build the states
		buildStates();
		
		// Build the values
		buildValues();
		
		// Build the buttons/text boxes
		buildStage();
		// Add the text file button
	}
	
	private void buildStates() {
		states = ConfigManager.getStringList(ConfigManager.scope(ConfigManager.scope(this.getClass().getName(), type), "states"));
		Integer d = rows*cols/states.size()+1;
		myStates = new HashMap<String, Integer>();
		for(String s: states) {
			myStates.put(s, d);
		}
	}
	
	private void buildValues() {
		myValues = new HashMap<String, String>();
		myValues.put("rows", ConfigManager.getString(ConfigManager.scope(this.getClass().getName(), "rows"), "30"));
		myValues.put("cols", ConfigManager.getString(ConfigManager.scope(this.getClass().getName(), "cols"), "30"));
		
		for (String curr : myParams) {
			String sliderValue = ConfigManager.getString(ConfigManager.scope(className, curr));
			myValues.put(curr, sliderValue);
		}
	}
	
	private void buildStage() {
		// Loop through my values and my states building text boxes for each of them
	}
	
	protected void run() {
		
		// Do something in here
		
	}
	
	public void printXml() {
		pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		pw.println("<file>");
		printGlobal();
		printParams();
		printSquares();
		pw.println("</file>");
		pw.close();
	}

	private void printGlobal() {
		pw.println("    <global>");
		pw.println("        <totRows>" + String.valueOf(rows) + "</totRows>");
		pw.println("        <totCols>" + String.valueOf(cols) + "</totCols>");
		pw.println("    </global>");
	}

	private void printParams() {
		pw.println("    <params>");
		for (String s : myValues.keySet()) {
			pw.println("        <" + s + ">" + myValues.get(s) + "</" + s + ">");
		}
		pw.println("    </params>");
	}

	private void printSquares() {
		pw.println("    <squares>");
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				pw.println("        <square>");
				pw.println("            <row>" + i + "</row>");
				pw.println("            <col>" + j + "</col>");
				pw.println("            <state>" + getRandomState() + "</state>");
				pw.println("            <type>" + type + "</type>");
				pw.println("        </square>");
			}
		}
		pw.println("    </squares>");
	}

	private String getRandomState() {
		int rand = (new Random()).nextInt(states.size());
		if(myStates.get(states.get(rand)) > 0) {
			myStates.put(states.get(rand), myStates.get(states.get(rand))-1);
			return states.get(rand);
		} else {
			states.remove(rand);
			return getRandomState();
		}
	}
	
	public static void main(String...args) {	
		XmlGenerator r = new XmlGenerator("Gol1.xml", "GameOfLife");
		r.printXml();
	}
}
