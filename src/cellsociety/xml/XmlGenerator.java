package cellsociety.xml;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class XmlGenerator {

	private int rows = 30;
	private int cols = 30;
	private String type = "Fire";
	private PrintWriter pw;
	
	public XmlGenerator(String s) throws Exception {
		pw = new PrintWriter(new File(s));
	}
	
	public void printXml() {
		Map<String, String> vals = new HashMap<String, String>();
		
		pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		pw.println("<file>");
		printGlobal();
		printParams(vals);
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
	
	private void printParams(Map<String, String> vals) {
		 pw.println("    <params>");
		for(String s: vals.keySet()) {
			 pw.println("        <" + s + ">" + vals.get(s) + "</" + s + ">");
		}
		 pw.println("    </params>");
	}
	
	private void printSquares() {
		 pw.println("    <squares>");
		for(int i=0; i < rows; i++) {
			for(int j=0; j < cols; j++) {
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
		double rand = Math.random();
		if(rand > .95) {
			return "fire";
		} else if (rand > .5){
			return "earth";
		} else {
			return "tree";
		}
	}
	
	public static void main(String...args) throws Exception {	
		XmlGenerator r = new XmlGenerator("data/test1.xml");
		r.printXml();
	}
}
