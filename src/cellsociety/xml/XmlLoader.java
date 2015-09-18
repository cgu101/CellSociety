package cellsociety.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cellsociety.cell.Cell;
import cellsociety.grid.AbstractGrid;
import cellsociety.managers.ConfigManager;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

public class XmlLoader {
	
	private Document doc;
	private int xOffset;
	private int yOffset;
	private int rows;
	private int cols;
	
	public XmlLoader(String file) {
		try {
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();	    
	    dbf.setNamespaceAware(true);
	    DocumentBuilder db = dbf.newDocumentBuilder(); 
	    doc = db.parse(getClass().getClassLoader().getResourceAsStream(file));
		doc.getDocumentElement().normalize();
		setOffsets();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setOffsets() {		
		int width = ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "mapWidth"));
		int height = ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "mapHeight"));
		rows = Integer.parseInt(doc.getElementsByTagName("totRows").item(0).getTextContent());
		cols = Integer.parseInt(doc.getElementsByTagName("totCols").item(0).getTextContent());
		xOffset = Math.min(ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "cellWidth")), width/rows);
		yOffset = Math.min(ConfigManager.getInt(ConfigManager.scope(AbstractGrid.class.getName(), "cellHeight")), height/cols);
	}
	
	public Cell[][] buildGrid(GridPane mapPane) {
		Cell[][] map = new Cell[rows][cols];
		NodeList nList = doc.getElementsByTagName("square");
		
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Element eElement = (Element) nList.item(temp);
			
			int row = Integer.parseInt(eElement.getElementsByTagName("row").item(0).getTextContent());
			int col = Integer.parseInt(eElement.getElementsByTagName("col").item(0).getTextContent());	
			String state = eElement.getElementsByTagName("state").item(0).getTextContent();
			String type = eElement.getElementsByTagName("type").item(0).getTextContent();
			String color = ConfigManager.getString(ConfigManager.scope(Cell.class.getName(), ConfigManager.scope(type, state)));

			if(color.isEmpty()){
				System.out.println("here");
			}
			Cell toAdd = new Cell(row*xOffset, col*yOffset, xOffset, yOffset, Paint.valueOf(color), state, type);
			map[row][col] = toAdd;
			mapPane.add(toAdd, col, row);
		}
		return map;
	}
	
	public NodeList getParams() {
		return doc.getElementsByTagName("params");
	}

}
