package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class Organism {
	protected World world;
	protected Random random = new Random();
	
	//first cell in organism
	protected Cell originCell;
	//container of all cells belonging to this organism
	protected Set<Cell> cells;	
	//container of all cells on the edge of this organism
	protected Set<Cell> edge;
	//container of all cells that can expand this organism (subset of edge)
	protected Set<Cell> expandableCells;

	//map of index of array to an array of int's, stores number of cells at index radius
	protected int[] radiusMap;
	//largest radius with cells contained by this organism
	protected int radius = 1;
	
	protected Color color;
	
	//stores whether the organism has been consumed
	protected boolean consumed = false;
	
	public Organism(World world) {
		this.world = world;
		
		//initializes the radius map
		radiusMap = new int[100];
		for (int i = 0; i < 10; i++) {
			radiusMap[i] = 0;
		}
		
		cells = new HashSet<Cell>();
		edge = new HashSet<Cell>();
		expandableCells = new HashSet<Cell>();
	}
	
	public void addCell(Cell cell) {
		//adds the cells adjacent to this cell to edge IF THEY ARE NOT PART OF ANOTHER CELL
		try {
			for (Cell c : cell.getAdjacentCells()) {
				if (c.getOrganism() == null) {
					edge.add(c);
				}
			}

			//updates the radius map to include this cell
			updateRadiusMap(cell);
			//updates the expandable cell list
			updateExpandableCells();
			//sets the organism for this cell to this organism
			cell.setOrganism(this);
			//adds this cell to the cell list
			cells.add(cell);
			//removes this cell from the edge list
			edge.remove(cell);
			//removes this cell from the expandable cell list
			expandableCells.remove(cell);
			//repaints the frame
			world.repaint();
		} catch (NullPointerException e) {}
	}
	
	public void updateExpandableCells() {
		//
		for (Cell cell : edge) {
			int radius = (int) Math.sqrt(Math.pow(cell.getXGrid() - originCell.getXGrid(), 2) + Math.pow(cell.getYGrid() - originCell.getYGrid(), 2));
			if (radius == 0 || radius == 1) {
				expandableCells.add(cell);
			} else if (radiusMap[radius - 1] > 1.5*(radius - 1)) {
				expandableCells.add(cell);
			} else {
				continue;
			}
		}
	}
	
	public void updateRadiusMap(Cell cell) {
		int index = (int) Math.sqrt(Math.pow(cell.getXGrid() - originCell.getXGrid(), 2) + Math.pow(cell.getYGrid() - originCell.getYGrid(), 2));
		radiusMap[index]++;
		
		int radius = 0;
		for (int i = 0; i < 20; i++) {
			if (radiusMap[i] > 0) {
				radius = i;
			}
		}
		this.radius = radius;
	}
	
	public void beConsumed() {
		consumed = true;
		for (Cell cell : cells) {
			cell.setOrganism(null);
		}
		cells.clear();
		edge.clear();
		expandableCells.clear();
		originCell = null;
	}
	
	public Set<Cell> getCells() {
		return cells;
	}
	
	public Cell getOriginCell() {
		return originCell;
	}
	
	public Set<Cell> getExpandableCells() {
		return expandableCells;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public int getOrganismSize() {
		return cells.size();
	}
	
	public Cell selectFromExpandable() {
		if (expandableCells.size() != 0) {
			int j = random.nextInt(expandableCells.size());
			int k = 0;
			for (Cell cell : expandableCells) {
				if (k++ == j) {
					return cell;
				}
			}
		}
		return null;
	}
	
	public void draw(Graphics g) {
		if (!consumed) {
			for (Cell cell : cells) {
				cell.draw(g, color);
			}
			originCell.drawAsOrigin(g, color);
		}
	}
}
