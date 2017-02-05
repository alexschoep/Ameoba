package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JOptionPane;

public class Amoeba extends Organism{
	private int energy;
	private HashSet<Cell> headCells;
	private HashSet<Cell> headExpandableCells;
	private HashSet<Cell> consumableCells;
		
	public Amoeba(World world) {
		super(world);
				
		energy = 10;
		
		headCells = new HashSet<Cell>();
		
		originCell = world.getCellAt(50, 50);
		edge.add(originCell);
		addCell(originCell);
		
		headExpandableCells = new HashSet<Cell>();
		consumableCells = new HashSet<Cell>();
		
		this.color = new Color(100, 100, 255);
	}
	
	public void addHead() {
		Cell headCell = null;
		headCell = selectFromExpandable();
		energy+= 100;
		addCell(headCell);
		headCells.add(headCell);
		updateHeadExpandableCells();
		
		String splashMessage;
		splashMessage = new String("This organism has evolved and gained a new head. The head\n"
				+ "can be extended indefinitely but costs more to expand.\n"
				+ "Hint: Expand out to find other organisms.");
		JOptionPane.showMessageDialog(world, splashMessage, "New head grown", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void photosynthesis() {
		energy += cells.size();
	}
	
	public Integer getEnergy() {
		return energy;
	}
	
	public void updateVisibleCells(Cell cell) {
		for (Cell cell1 : cell.getAdjacentCells()) {
			for (Cell cell2 : cell1.getAdjacentCells()) {
				cell2.makeVisible(true);
			}
		}
	}
	
	public void consumeOrganism(Cell cell) {
		for (Cell cell1 : cell.getOrganism().getCells()) {
			cell1.makeVisible(true);
			energy += cell1.getEdgeCost();
		}
		cell.getOrganism().beConsumed();
		consumableCells.remove(cell);
		updateExpandableCells();
		updateHeadExpandableCells();
		world.repaint();
	}
	
	public void updateHeadExpandableCells() {
		headExpandableCells.clear();
		consumableCells.clear();
		for (Cell cell : headCells) {
			for (Cell cell1 : cell.getAdjacentCells()) {
				if (cell1.getOrganism() == null) {
					headExpandableCells.add(cell1);
				} else if (cell1.getOrganism() != null && cell1.getOrganism() != this) {
					for (Cell cell2 : consumableCells) {
						if (cell1.getOrganism() == cell2.getOrganism()) {
							return;
						}
					}
					consumableCells.add(cell1);
				}
			} 
		}
	}
	
	public HashSet<Cell> getHeadExpandableCells() {
		return headExpandableCells;
	}
	
	public HashSet<Cell> getConsumableCells() {
		return consumableCells;
	}
	
	@Override
	public void addCell(Cell cell) {
		Cell head = null;
		
		for (Cell cell1 : headCells) {
			if (cell1.getAdjacentCells().contains(cell)) {
				head = cell1;
			}
		}
		
		if (head != null) {
			if (energy >= cell.getHeadEdgeCost()) {
				energy -= cell.getHeadEdgeCost();
				updateVisibleCells(cell);
				super.addCell(cell);
				headCells.remove(head);
				headCells.add(cell);
				updateHeadExpandableCells();
			}
			
		} else {
			if (energy >= cell.getEdgeCost()) {
				energy -= cell.getEdgeCost();
				updateVisibleCells(cell);
				super.addCell(cell);
			}
		}
		
		if (cells.size() == 5 || cells.size() == 20 || cells.size() == 50) {
			addHead();
		}
	}
	
	@Override
	public void draw(Graphics g) {
		for (Cell cell : cells) {
			cell.draw(g, color);
		}
		
		for (Cell cell : expandableCells) {
			if (!headExpandableCells.contains(cell)) {
				cell.drawAsEdge(g);
			}
		}
		
		for (Cell cell : headExpandableCells) {
			cell.drawAsHeadEdge(g);
		}
		
		for (Cell cell : consumableCells) {
			cell.drawAsConsumable(g);
		}
		
		for (Cell cell : headCells) {
			cell.drawAsHead(g, color);
		}
		
		originCell.drawAsOrigin(g, color);
	}
}
