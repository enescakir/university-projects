package visualization;

import java.util.ArrayList;

import acm.graphics.GObject;

public interface BoardIntf {
	ArrayList<GObject> objects = new ArrayList<GObject>();
	
	public void setCanvas(String boardName, int width, int height);
	public void setBackground();
	public void addKeyBoardListener();
	public void addGameInfoLabels();
	public void addObject(GObject g);
	public void waitFor(long millisecs);
}
