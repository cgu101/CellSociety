package cellsociety.cell;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class AbstractCell {
private Rectangle rect;
	
	public AbstractCell() {
		rect = new Rectangle(100, 100, Color.WHITE);
	}
	
	public Rectangle getRect() {
		return rect;
	}
}
