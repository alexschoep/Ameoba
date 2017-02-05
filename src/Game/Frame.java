package Game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;
	protected static final int FRAME_WIDTH = 800;
	protected static final int FRAME_HEIGHT = 800;
	private String frameTitle = "Game";
	
	private Controls controls;
	private World world;
	
	public Frame() {
		//build the frame
		setSize(FRAME_HEIGHT, FRAME_WIDTH);
		setTitle(frameTitle);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(new BorderLayout());
	    
		//make a new world and set it as the content pane
		world = new World(this);
		add(world, BorderLayout.CENTER);
		
		controls = new Controls(this);
		add(controls, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	//delays for x milliseconds
	public void delay(int milliseconds) {
		try {
		    Thread.sleep(milliseconds);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
	
	public World getWorld() {
		return world;
	}
	
	public static void main(String[] args) {
		Frame frame = new Frame();
		
		while(true) {
			frame.delay(1000);
			frame.getWorld().getAmeoba().photosynthesis();
			frame.repaint();
		}
	}
}
