package cellsociety.grid;

import cellsociety.screen.AbstractScreen;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cellsociety.cell.Cell;
import cellsociety.managers.ConfigManager;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

public abstract class AbstractGrid extends AbstractScreen {

	protected Cell[][] map;
	protected GridPane mapPane;
	protected StackPane paramPane;

	public AbstractGrid() {
		makeScene();
		try {
			loadXml();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void makeScene(){
		root = new Group();
		WIDTH = ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "mapWidth"), 600)
				+ ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "paramWidth"), 300);
		HEIGHT = ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "height"), 600);
		scene = new Scene(root, WIDTH, HEIGHT);	
		mapPane = new GridPane();
		paramPane = new StackPane();
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
	
	protected void done() {
		nextScreen = ConfigManager.getObject(AbstractScreen.class, ConfigManager.getString("startScreen"));
	}
	
	protected void loadXml() throws ParserConfigurationException, SAXException, IOException {
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();	    
	    dbf.setNamespaceAware(true);
	    
	    DocumentBuilder db = dbf.newDocumentBuilder(); 
	    Document doc = db.parse(new File("data/test1.xml"));
		doc.getDocumentElement().normalize();
		
		int rows = Integer.parseInt(doc.getElementsByTagName("totRows").item(0).getTextContent());
		int cols = Integer.parseInt(doc.getElementsByTagName("totCols").item(0).getTextContent());
		int width = ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "mapWidth"));
		int height = ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "height"));
		int xOffset = Math.min(ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "cellWidth")), width/rows);
		int yOffset = Math.min(ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "cellHeight")), height/cols);
		map = new Cell[rows][cols];
		buildGrid(doc.getElementsByTagName("square"), xOffset, yOffset);
		buildParams(doc.getElementsByTagName("parameter"));
	}
	
	private void buildGrid(NodeList nList, int width, int height) {
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Element eElement = (Element) nList.item(temp);
			
			int row = Integer.parseInt(eElement.getElementsByTagName("row").item(0).getTextContent());
			int col = Integer.parseInt(eElement.getElementsByTagName("col").item(0).getTextContent());	
			String state = eElement.getElementsByTagName("state").item(0).getTextContent();
			String type = eElement.getElementsByTagName("type").item(0).getTextContent();
			String color = ConfigManager.getString(ConfigManager.scope(Cell.class.getName(), ConfigManager.scope(type, state)));

			Cell toAdd = new Cell(row*width, col*height, width, height, Paint.valueOf(color), state, type);
			map[row][col] = toAdd;
			mapPane.add(toAdd, col, row);
		}
		mapPane.setAlignment(Pos.CENTER_LEFT);
		root.getChildren().add(mapPane);
	}
	
	private void buildParams(NodeList nList) {
		for (int temp = 0; temp < nList.getLength(); temp++) {			
		    for (Node child = nList.item(temp).getFirstChild(); child != null; child = child.getNextSibling()) {
		    	setParams(child);
		   	}
	    }	
	}

	protected void setParams(Node n) {
		
	}
}
