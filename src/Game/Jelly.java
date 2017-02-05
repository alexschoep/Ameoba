package Game;

import java.awt.Color;

public class Jelly extends Organism{
	
	public Jelly(World world) {
		super(world);
		
		originCell = world.getCellAt(random.nextInt(World.numCellRows), random.nextInt(World.numCellRows));
		edge.add(originCell);
		addCell(originCell);
		
		this.color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
		
		if (expandableCells.size() != 0) {
			int numCells = random.nextInt(10) + 5;
			for (int i = 0; i < numCells; i++) {
				addCell(selectFromExpandable());
			}
		}
	}

}
