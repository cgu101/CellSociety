package cellsociety.xml;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException; 
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.*;

import com.sun.prism.paint.Color;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMTest {
	
    static final String outputEncoding = "UTF-8";
    
    private PrintWriter out;
    private List<Rectangle> myRectList;
    private int indent = 0;
    private final String basicIndent = " ";

    DOMTest() {

    }
	
	public static void usage() {
		
	}
	
	public static void main(String[] args) throws Exception {
	    String filename = null;
	    boolean dtdValidate = false;
	    boolean xsdValidate = false;
	    String schemaSource = null;
	    boolean ignoreWhitespace = false;
	    boolean ignoreComments = false;
	    boolean putCDATAIntoText = false;
	    boolean createEntityRefs = false;

	    for (int i = 0; i < args.length; i++) {
	        if (args[i].equals("-dtd"))  { 
	            dtdValidate = true;
	        } 
	        else if (args[i].equals("-ws")) {
	            ignoreWhitespace = true;
	        } 
	        else if (args[i].startsWith("-co")) {
	            ignoreComments = true;
	        }
	        else if (args[i].startsWith("-cd")) {
	            putCDATAIntoText = true;
	        } 
	        else if (args[i].startsWith("-e")) {
	            createEntityRefs = true;
	        }
	        else if (args[i].equals("-xsd")) {
	            xsdValidate = true;
	        } 
	        else if (args[i].equals("-xsdss")) {
	            if (i == args.length - 1) {
	                usage();
	            }
	            xsdValidate = true;
	            schemaSource = args[++i];
	        }
	        else {
	            filename = args[i];
	            if (i != args.length - 1) {
	                usage();
	            }
	        }
	    }

	    if (filename == null) {
	        usage();
	    }
		
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();	    
	    dbf.setNamespaceAware(true);
	    dbf.setValidating(dtdValidate || xsdValidate);
	    dbf.setIgnoringComments(ignoreComments);
	    dbf.setIgnoringElementContentWhitespace(ignoreWhitespace);
	    dbf.setCoalescing(putCDATAIntoText);
	    dbf.setExpandEntityReferences(!createEntityRefs);
	    
	    DocumentBuilder db = dbf.newDocumentBuilder(); 
	    OutputStreamWriter errorWriter = new OutputStreamWriter(System.err, outputEncoding);
	    db.setErrorHandler(new MyErrorHandler (new PrintWriter(errorWriter, true)));
	    Document doc = db.parse(new File("data/text.xml"));
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("square");
				
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
										
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement0 = (Element) nNode;
				Element eElement1 = (Element) eElement0.getElementsByTagName("characteristic").item(0);				
				
				int row = Integer.parseInt(eElement0.getElementsByTagName("row").item(0).getTextContent());
				int col = Integer.parseInt(eElement0.getElementsByTagName("col").item(0).getTextContent());			
				String color = eElement1.getElementsByTagName("name").item(0).getTextContent();
				
				Rectangle rect = new Rectangle(row*5, col*5, 5, 5);
				//rect.setFill(Color.RED);
			}
		}
	}
	
	private void printlnCommon(Node n) {
	    out.print(" nodeName=\"" + n.getNodeName() + "\"");

	    String val = n.getNamespaceURI();
	    if (val != null) {
	    	out.print(" uri=\"" + val + "\"");
	    }

	    val = n.getPrefix();

	    if (val != null) {
	    	out.print(" pre=\"" + val + "\"");
	    }

	    val = n.getLocalName();
	    if (val != null) {
	    	out.print(" local=\"" + val + "\"");
	    }

	    val = n.getNodeValue();
	    if (val != null) {
	    	out.print(" nodeValue=");
	        if (val.trim().equals("")) {
	            // Whitespace
	        	out.print("[WS]");
	        }
	        else {
	        	out.print("\"" + n.getNodeValue() + "\"");
	        }
	    }
	    out.println();
	}
	
	private void outputIndentation() {
	    for (int i = 0; i < indent; i++) {
	        out.print(basicIndent);
	    }
	}
	
	private void echo(Node n) {
	    outputIndentation();
	    int type = n.getNodeType();

	    switch (type) {
	        case Node.ATTRIBUTE_NODE:
	            out.print("ATTR:");
	            printlnCommon(n);
	            break;

	        case Node.CDATA_SECTION_NODE:
	            out.print("CDATA:");
	            printlnCommon(n);
	            break;

	        case Node.COMMENT_NODE:
	            out.print("COMM:");
	            printlnCommon(n);
	            break;

	        case Node.DOCUMENT_FRAGMENT_NODE:
	            out.print("DOC_FRAG:");
	            printlnCommon(n);
	            break;

	        case Node.DOCUMENT_NODE:
	            out.print("DOC:");
	            printlnCommon(n);
	            break;

	        case Node.DOCUMENT_TYPE_NODE:
	            out.print("DOC_TYPE:");
	            printlnCommon(n);
	            NamedNodeMap nodeMap = ((DocumentType)n).getEntities();
	            indent += 2;
	            for (int i = 0; i < nodeMap.getLength(); i++) {
	                Entity entity = (Entity)nodeMap.item(i);
	                echo(entity);
	            }
	            indent -= 2;
	            break;

	        case Node.ELEMENT_NODE:
	            out.print("ELEM:");
	            printlnCommon(n);

	            NamedNodeMap atts = n.getAttributes();
	            indent += 2;
	            for (int i = 0; i < atts.getLength(); i++) {
	                Node att = atts.item(i);
	                echo(att);
	            }
	            indent -= 2;
	            break;

	        case Node.ENTITY_NODE:
	            out.print("ENT:");
	            printlnCommon(n);
	            break;

	        case Node.ENTITY_REFERENCE_NODE:
	            out.print("ENT_REF:");
	            printlnCommon(n);
	            break;

	        case Node.NOTATION_NODE:
	            out.print("NOTATION:");
	            printlnCommon(n);
	            break;

	        case Node.PROCESSING_INSTRUCTION_NODE:
	            out.print("PROC_INST:");
	            printlnCommon(n);
	            break;

	        case Node.TEXT_NODE:
	            out.print("TEXT:");
	            printlnCommon(n);
	            break;

	        default:
	            out.print("UNSUPPORTED NODE: " + type);
	            printlnCommon(n);
	            break;
	    }

	    indent++;
	    for (Node child = n.getFirstChild(); child != null;
	         child = child.getNextSibling()) {
	        echo(child);
	    }
	    indent--;
	}
//	
//	public Node findSubNode(String name, Node node) {
//	    if (node.getNodeType() != Node.ELEMENT_NODE) {
//	        System.err.println("Error: Search node not of element type");
//	        System.exit(22);
//	    }
//
//	    if (! node.hasChildNodes()) return null;
//
//	    NodeList list = node.getChildNodes();
//	    for (int i=0; i < list.getLength(); i++) {
//	        Node subnode = list.item(i);
//	        if (subnode.getNodeType() == Node.ELEMENT_NODE) {
//	           if (subnode.getNodeName().equals(name)) 
//	               return subnode;
//	        }
//	    }
//	    return null;
//	}
//	
//	public String getText(Node node) {
//	    StringBuffer result = new StringBuffer();
//	    if (! node.hasChildNodes()) return "";
//
//	    NodeList list = node.getChildNodes();
//	    for (int i=0; i < list.getLength(); i++) {
//	        Node subnode = list.item(i);
//	        if (subnode.getNodeType() == Node.TEXT_NODE) {
//	            result.append(subnode.getNodeValue());
//	        }
//	        else if (subnode.getNodeType() == Node.CDATA_SECTION_NODE) {
//	            result.append(subnode.getNodeValue());
//	        }
//	        else if (subnode.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
//	            // Recurse into the subtree for text
//	            // (and ignore comments)
//	            result.append(getText(subnode));
//	        }
//	    }
//
//	    return result.toString();
//	}
//
//	
	private static class MyErrorHandler implements ErrorHandler {
	     
	    private PrintWriter out;

	    MyErrorHandler(PrintWriter out) {
	        this.out = out;
	    }

	    private String getParseExceptionInfo(SAXParseException spe) {
	        String systemId = spe.getSystemId();
	        if (systemId == null) {
	            systemId = "null";
	        }

	        String info = "URI=" + systemId + " Line=" + spe.getLineNumber() +
	                      ": " + spe.getMessage();
	        return info;
	    }

	    public void warning(SAXParseException spe) throws SAXException {
	        out.println("Warning: " + getParseExceptionInfo(spe));
	    }
	        
	    public void error(SAXParseException spe) throws SAXException {
	        String message = "Error: " + getParseExceptionInfo(spe);
	        throw new SAXException(message);
	    }

	    public void fatalError(SAXParseException spe) throws SAXException {
	        String message = "Fatal Error: " + getParseExceptionInfo(spe);
	        throw new SAXException(message);
	    }
	}

}

