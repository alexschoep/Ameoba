package Game;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Controls extends JPanel {
	private static final long serialVersionUID = 1L;
	private Frame frame;
	
	JButton left;
	JButton right;
	JButton up;
	JButton down;
	JButton in;
	JButton out;
	JButton center;
	
	int xGrid;
	int yGrid;
	int scale;
	
	public Controls(Frame frame) {
		this.frame = frame;
		
		xGrid = 50;
		yGrid = 50;
		scale = 80;
		setScale();
		
		setLayout(new GridLayout(1, 6));
		
		right = new JButton("<");
		add(right);
		right.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	xGrid -= 150 / scale + 1;
		    	setScale();
		    }
		});		
		
		left = new JButton(">");
		add(left);
		left.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	xGrid += 150 / scale + 1;
		    	setScale();
		    }
		});
		
		up = new JButton("/\\");
		add(up);
		up.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	yGrid -= 150 / scale + 1;
		    	setScale();
		    }
		});
		
		down = new JButton("\\/");
		add(down);
		down.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	yGrid += 150 / scale + 1;
		    	setScale();
		    }
		});
		
		in = new JButton("zoom in");
		add(in);
		in.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	scale += 10;
		    	setScale();
		    }
		});
		
		out = new JButton("zoom out");
		add(out);
		out.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	scale -= 10;
		    	setScale();
		    }
		});
		
		setVisible(true);
		
		center = new JButton("center view");
		add(center);
		center.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	frame.getWorld().setScale(scale, frame.getWorld().getCellAt(50, 50));
		    }
		});
		
		setVisible(true);
	}
	
	public void setScale() {
		frame.getWorld().setScale(scale, frame.getWorld().getCellAt(xGrid, yGrid));
	}
}
