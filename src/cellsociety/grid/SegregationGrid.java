package cellsociety.grid;

import cellsociety.parameters.FireParameters;

public class SegregationGrid extends AbstractGrid {
	
	private int similar;
	private boolean satisfied;
	
	public SegregationGrid(String s) {
		
	}
	
	@Override
	protected void init() {
		makeScene();
		myParameters = new SegregationParameters();
	}
	
	@Override
	public void run() {
		
	}
	
}
