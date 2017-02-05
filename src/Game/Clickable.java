package Game;

import java.util.ArrayList;

public abstract class Clickable {
	private static ArrayList<Clickable> clickables = new ArrayList<Clickable>();
	
	public Clickable() {
		clickables.add(this);
	}
	
	public static ArrayList<Clickable> getClickables() {
		return clickables;
	}
	
	public boolean containsClick(int mouseX, int mouseY) {
		return false;
	}
	
	public void clicked(Amoeba organism) {
		
	}

}
