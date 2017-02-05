package Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.HashSet;
import java.util.Random;

public class Cell extends Clickable{
	private static int cellSpace;
	private int xGrid;
	private int yGrid;
	private int xPos;
	private int yPos;
	private int edgeCost;
	private int headEdgeCost;
	private Polygon polygon;
	private Organism organism = null;
	private Random random = new Random();
	private HashSet<Cell> adjacentCells;
	private boolean visible = false;
	
	public Cell(int xGrid, int yGrid) {
		super();
		
		int radius = (int) Math.sqrt(Math.pow(xGrid - 50, 2) + Math.pow(yGrid - 50, 2));
		edgeCost = 6*radius*radius;
		headEdgeCost = 20*radius;
		
		adjacentCells = new HashSet<Cell>();
		this.xGrid = xGrid;
		this.yGrid = yGrid;
	}
	
	public void setAdjacentCells(HashSet<Cell> adjacentCells) {
		this.adjacentCells = adjacentCells;
	}
	
	public HashSet<Cell> getAdjacentCells() {
		return adjacentCells;
	}
	
	public int getXGrid() {
		return xGrid;
	}
	
	public int getYGrid() {
		return yGrid;
	}
	
	public int getEdgeCost() {
		return edgeCost;
	}
	
	public int getHeadEdgeCost() {
		return headEdgeCost;
	}
	
	public Organism getOrganism() {
		return organism;
	}
	
	public void setOrganism(Organism organism) {
		this.organism = organism;
	}
	
	public void makeVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void createPolygon(int[] xpoints, int[] ypoints, int cellSpace, int deltaX, int deltaY) {
		Cell.cellSpace = cellSpace;
		
		if (xGrid % 2 == 0) {
			yPos = (yGrid * cellSpace) + deltaY;
		} else {
			yPos = (yGrid * cellSpace + cellSpace/2) + deltaY;
		}
		//xPos = xGrid * (cellSpace - (cellSpace/10));
		xPos = (xGrid * cellSpace) + deltaX;

		
		polygon = new Polygon(xpoints, ypoints, 6);
		polygon.translate(xPos, yPos);
	}

	public void draw(Graphics g, Color color) {
		if (!visible) {
			return;
		}
		
		g.setColor(color);
		g.fillPolygon(polygon);
		g.setColor(Color.BLACK);
	}
	
	public void drawAsOrigin(Graphics g, Color color) {
		if (!visible) {
			return;
		}
		
		g.setColor(color);
		g.fillPolygon(polygon);
		g.setColor(Color.BLACK);
		int eyeSize = cellSpace;
		g.fillOval(xPos - eyeSize/4, yPos - eyeSize/4, eyeSize/2, eyeSize/2);
		g.setColor(color);
		g.fillOval(xPos - eyeSize/8, yPos - eyeSize/8, eyeSize/4, eyeSize/4);
	}
	
	public void drawAsEdge(Graphics g) {
		if (!visible) {
			return;
		}
		
		g.setColor(new Color(200, 200, 255));
		g.drawPolygon(polygon);
		g.setColor(Color.GRAY);
		Integer cost = new Integer(edgeCost);
		g.drawString(cost.toString(), xPos - 5, yPos);
	}
	
	public void drawAsHeadEdge(Graphics g) {
		if (!visible) {
			return;
		}
		
		g.setColor(Color.ORANGE);
		g.drawPolygon(polygon);
		g.setColor(Color.GRAY);
		Integer cost = new Integer(headEdgeCost);
		g.drawString(cost.toString(), xPos - 5, yPos);
	}
	
	public void drawAsHead(Graphics g, Color color) {
		if (!visible) {
			return;
		}
		
		g.setColor(color);
		g.fillPolygon(polygon);
		g.setColor(Color.BLACK);
		int eyeSize = cellSpace;
		g.fillOval(xPos - eyeSize/4, yPos - eyeSize/4, eyeSize/2, eyeSize/2);
		g.setColor(Color.ORANGE);
		g.fillOval(xPos - eyeSize/8, yPos - eyeSize/8, eyeSize/4, eyeSize/4);
	}
	
	public void drawAsConsumable(Graphics g) {
		if (!visible) {
			return;
		}
		
		g.setColor(Color.BLACK);
		g.drawRect(xPos - 35, yPos - 15, 80, 20);
		g.drawString("consume", xPos - 20, yPos);
	}
	
	public void drawAsInvisible(Graphics g) {
		if (!visible) {
			g.setColor(Color.GRAY);
			g.fillPolygon(polygon);
		}
	}
	
	@Override
	public boolean containsClick(int mouseX, int mouseY) {
		return (polygon.contains(new Point(mouseX, mouseY)));
	}
	
	@Override
	public void clicked(Amoeba ameoba) {
		if (ameoba.getExpandableCells().contains(this) || ameoba.getHeadExpandableCells().contains(this)) {
			ameoba.addCell(this);
			return;
		}
		
		if(ameoba.getConsumableCells().contains(this)) {
			ameoba.consumeOrganism(this);
		}
	}
}
