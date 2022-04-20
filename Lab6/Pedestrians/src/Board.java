import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
	private static final long serialVersionUID = 1L;
	private Point[][] points;
	private int size = 10;
	public int editType=0;
	private boolean moore_neigh = true;
///
	public static String[] neighbourhoods = {"Moore", "VonNeumann"};
///
	public Board(int length, int height) {
		addMouseListener(this);
		addComponentListener(this);
		addMouseMotionListener(this);
		setBackground(Color.WHITE);
		setOpaque(true);
	}
	private void move_points_in_random_order(){
		ArrayList<ArrayList<Integer>> points_coords = new ArrayList<>();
		for (int x = 1; x < points.length - 1; ++x)
			for (int y = 1; y < points[x].length - 1; ++y)
				points_coords.add(new ArrayList<>(Arrays.asList(x,y)));

		Random rand = new Random();
		while (!points_coords.isEmpty()){
			int rand_index = rand.nextInt(points_coords.size());
			ArrayList<Integer> point_to_move = points_coords.get(rand_index);
			points[point_to_move.get(0)][point_to_move.get(1)].move();
			points_coords.remove(rand_index);
		}
	}
	public void iteration() {
		for (int x = 1; x < points.length - 1; ++x)
			for (int y = 1; y < points[x].length - 1; ++y)
				points[x][y].setBlocked(false);
		move_points_in_random_order();
		this.repaint();
	}

	public void clear() {
		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y) {
				points[x][y].clear();
			}
		calculateField();
		this.repaint();
	}

	private void initialize_Moore(int length, int height) {
		points = new Point[length][height];

		for (int x = 0; x < points.length; ++x) {
			for (int y = 0; y < points[x].length; ++y) {
				points[x][y] = new Point();
			}
		}
		for (int x = 1; x < points.length-1; ++x) {
			for (int y = 1; y < points[x].length-1; ++y) {
				points[x][y].addNeighbor(points[x][y+1]);
				points[x][y].addNeighbor(points[x][y-1]);
				points[x][y].addNeighbor(points[x+1][y]);
				points[x][y].addNeighbor(points[x-1][y]);
				points[x][y].addNeighbor(points[x+1][y+1]);
				points[x][y].addNeighbor(points[x-1][y-1]);
				points[x][y].addNeighbor(points[x-1][y+1]);
				points[x][y].addNeighbor(points[x+1][y-1]);
			}
		}	
	}

	private void initialize_Von_Neumann(int length, int height) {
		points = new Point[length][height];

		for (int x = 0; x < points.length; ++x) {
			for (int y = 0; y < points[x].length; ++y) {
				points[x][y] = new Point();
			}
		}
		for (int x = 1; x < points.length-1; ++x) {
			for (int y = 1; y < points[x].length-1; ++y) {
				points[x][y].addNeighbor(points[x][y+1]);
				points[x][y].addNeighbor(points[x][y-1]);
				points[x][y].addNeighbor(points[x+1][y]);
				points[x][y].addNeighbor(points[x-1][y]);
			}
		}
	}
	
	private void calculateField(){
		ArrayList<Point> toCheck = new ArrayList<Point>();
		for (int x = 1; x < points.length-1; ++x) {
			for (int y = 1; y < points[x].length-1; ++y) {
				if (points[x][y].getType() == 2) {
					points[x][y].setStaticField(0);
					toCheck.addAll(points[x][y].getNeighbuors());
				}
			}
		}

		while (!toCheck.isEmpty()){
			Point firstPoint = toCheck.get(0);
			if (firstPoint.calcStaticField()){
				toCheck.addAll(firstPoint.getNeighbuors());
			}
			toCheck.remove(firstPoint);
		}
	}

	protected void paintComponent(Graphics g) {
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		g.setColor(Color.GRAY);
		drawNetting(g, size);
	}

	private void drawNetting(Graphics g, int gridSpace) {
		Insets insets = getInsets();
		int firstX = insets.left;
		int firstY = insets.top;
		int lastX = this.getWidth() - insets.right;
		int lastY = this.getHeight() - insets.bottom;

		int x = firstX;
		while (x < lastX) {
			g.drawLine(x, firstY, x, lastY);
			x += gridSpace;
		}

		int y = firstY;
		while (y < lastY) {
			g.drawLine(firstX, y, lastX, y);
			y += gridSpace;
		}

		for (x = 1; x < points.length-1; ++x) {
			for (y = 1; y < points[x].length-1; ++y) {
				if(points[x][y].type==0){
					float staticField = points[x][y].staticField;
					float intensity = staticField/100;
					if (intensity > 1.0) {
						intensity = 1.0f;
					}
					if(intensity < 0){
						intensity = 0.0f;
					}
					g.setColor(new Color(intensity, intensity, intensity));
				}
				else if (points[x][y].type==1){
					g.setColor(new Color(1.0f, 0.0f, 0.0f, 0.7f));
				}
				else if (points[x][y].type==2){
					g.setColor(new Color(0.0f, 1.0f, 0.0f, 0.7f));
				}
				if (points[x][y].isPedestrian){
					g.setColor(new Color(0.0f, 0.0f, 1.0f, 0.7f));
				}
				g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
			}
		}

	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			if(editType==3){
				if(!points[x][y].isPedestrian) points[x][y].becomePedestrian();
			}
			else{
				points[x][y].type= editType;
			}
			this.repaint();
		}
	}

	public void componentResized(ComponentEvent e) {
		int dlugosc = (this.getWidth() / size) + 1;
		int wysokosc = (this.getHeight() / size) + 1;

		if(this.moore_neigh) initialize_Moore(dlugosc, wysokosc);
		else initialize_Von_Neumann(dlugosc,wysokosc);
	}

	public void mouseDragged(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			if(editType==3){
				if(!points[x][y].isPedestrian) points[x][y].becomePedestrian();
			}
			else{
				points[x][y].type= editType;
			}
			this.repaint();
		}
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public String[] getNeighbourhoods() {
		return neighbourhoods;
	}

	public void changeNeigbourhoodType(String newNeighbourhoodType){
		int length = (this.getWidth() / size) + 1;
		int height = (this.getHeight() / size) + 1;
		switch (newNeighbourhoodType){
			case "Moore":
				moore_neigh = true;
				initialize_Moore(length, height);
			case "VonNeumann":
				moore_neigh = false;
				initialize_Von_Neumann(length, height);
		}
	}

}
