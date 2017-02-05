package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.swing.JPanel;

public class World extends JPanel implements MouseListener{
	private static final long serialVersionUID = 1L;
	
	//points of a hexagon with diameter 1.0
	private static final double[] xpoints_double = {1.1, 0.6, -0.6, -1.1, -0.6, 0.6};
	private static final double[] ypoints_double = {0, 0.866, 0.866, 0, -0.866, -0.866};
	
	//number of cells in a row, cells are created as a square
	protected static final int numCellRows = 100;
	
	//2D array of all the cells on the board
	private Cell[][] cells;
	
	private Amoeba amoeba;
	private ArrayList<Jelly> jellies;
	
	public World(Frame frame) {
		addMouseListener(this);
		
		//populates cells, passes in the row and column into cell constructor
		cells = new Cell[numCellRows][numCellRows];
		for (int i = 0; i < numCellRows; i++) {
			for (int j = 0; j < numCellRows; j++) {
				cells[i][j] = new Cell(i, j);
			}
		}
		
		//set adjacent cells for each cell
		for (int i = 0; i < numCellRows; i++) {
			for (int j = 0; j < numCellRows; j++) {
				cells[i][j].setAdjacentCells(findAdjacentCells(cells[i][j]));
			}
		}
		
		//creates an amoeba
		amoeba = new Amoeba(this);
		setScale(40, amoeba.getOriginCell());
		
		//creates 50 jellies
		jellies = new ArrayList<Jelly>();
		for (int i = 0; i < 50; i++) {
			jellies.add(new Jelly(this));
		}
	}
	
	//returns cells adjacent to passed in cell
	//array is out of bounds at edges
	public HashSet<Cell> findAdjacentCells(Cell cell) {
		HashSet<Cell> foundAdjacentCells = new HashSet<Cell>();
		if (cell.getXGrid() % 2 == 0) {
			try {
				foundAdjacentCells.add(cells[cell.getXGrid() - 1][cell.getYGrid() - 1]);
			} catch(ArrayIndexOutOfBoundsException e) {}
			
			try {
				foundAdjacentCells.add(cells[cell.getXGrid() - 1][cell.getYGrid()]);
			} catch(ArrayIndexOutOfBoundsException e) {}
			
			try {
				foundAdjacentCells.add(cells[cell.getXGrid()][cell.getYGrid() - 1]);
			} catch(ArrayIndexOutOfBoundsException e) {}
			
			try {
				foundAdjacentCells.add(cells[cell.getXGrid()][cell.getYGrid() + 1]);
			} catch(ArrayIndexOutOfBoundsException e) {}
			
			try {
				foundAdjacentCells.add(cells[cell.getXGrid() + 1][cell.getYGrid() - 1]);
			} catch(ArrayIndexOutOfBoundsException e) {}
			
			try {
				foundAdjacentCells.add(cells[cell.getXGrid() + 1][cell.getYGrid()]);
			} catch(ArrayIndexOutOfBoundsException e) {}
			
		} else {
			try {
				foundAdjacentCells.add(cells[cell.getXGrid() - 1][cell.getYGrid() + 1]);
			} catch(ArrayIndexOutOfBoundsException e) {}
			
			try {
				foundAdjacentCells.add(cells[cell.getXGrid() - 1][cell.getYGrid()]);
			} catch(ArrayIndexOutOfBoundsException e) {}
			
			try {
				foundAdjacentCells.add(cells[cell.getXGrid()][cell.getYGrid() - 1]);
			} catch(ArrayIndexOutOfBoundsException e) {}
			
			try {
				foundAdjacentCells.add(cells[cell.getXGrid()][cell.getYGrid() + 1]);
			} catch(ArrayIndexOutOfBoundsException e) {}
			
			try {
				foundAdjacentCells.add(cells[cell.getXGrid() + 1][cell.getYGrid() + 1]);
			} catch(ArrayIndexOutOfBoundsException e) {}
			
			try {
				foundAdjacentCells.add(cells[cell.getXGrid() + 1][cell.getYGrid()]);
			} catch(ArrayIndexOutOfBoundsException e) {}
		}
		
		return foundAdjacentCells;
	}
	
	//returns cell with grid position x and y
	public Cell getCellAt(int xGrid, int yGrid) {
		return cells[xGrid][yGrid];
	}
	
	public Amoeba getAmeoba() {
		return amoeba;
	}
	
	//scales how the board is drawn and centers it at a passed in cell
	public void setScale(int cellSpace, Cell centerCell) {
		//create a hexagon with diameter cellSpace
		int[] xpoints = new int[6];
		int[] ypoints = new int[6];
		for (int i = 0; i < 6; i++) {
			xpoints[i] = (int) (xpoints_double[i] * (cellSpace / 1.75)); //1.75 affects how large the gap between cells is
			ypoints[i] = (int) (ypoints_double[i] * (cellSpace / 1.75));
		}
		
		//translate the board so passed in cell is in center
		int deltaX = (Frame.FRAME_WIDTH / 2) - (centerCell.getXGrid() * cellSpace);
		int deltaY = (Frame.FRAME_HEIGHT / 2) - (centerCell.getYGrid() * cellSpace);
		
		//update the cell for each polygon with these parameters
		for (int i = 0; i < numCellRows; i++) {
			for (int j = 0; j < numCellRows; j++) {
				cells[i][j].createPolygon(xpoints, ypoints, cellSpace, deltaX, deltaY);
			}
		}
	}
	
	//draws all of the jellies then draws the amoeba and finally says how much energy
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Jelly jelly : jellies) {
			jelly.draw(g);
		}
		amoeba.draw(g);
		
		for (int i = 0; i < numCellRows; i++) {
			for (int j = 0; j < numCellRows; j++) {
				cells[i][j].drawAsInvisible(g);
			}
		}
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Helvetica Bold", 0, 20));
		g.drawString("Energy: " + amoeba.getEnergy().toString(), Frame.FRAME_WIDTH - 130, 40);
	}

	public void mousePressed (MouseEvent event) {}
	public void mouseReleased (MouseEvent event) {}
	public void mouseEntered (MouseEvent event) {}
	public void mouseExited (MouseEvent event) {}
	
	//on mouse click, iterates through clickable to find what was clicked, then calls click for that object
	public void mouseClicked (MouseEvent event) {
		for (Clickable clickable : Clickable.getClickables()) {
			if (clickable.containsClick(event.getX(), event.getY())) {
				clickable.clicked(amoeba);
			}
		}
	}
}