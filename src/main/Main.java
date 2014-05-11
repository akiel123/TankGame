package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {


	int minimumFieldWidth = 40;
	
	private int players;
	int width;
	int height;
	
	private Map map;
	private int blockSize;
	private int lineWidth;
	
	public Main(int players, int width, int height){
		this.players = players;
		this.width = width - 50;
		this.height = height - 50;
		this.setBackground(new Color(250,250,250));
	}
	
	public void setMap(Map map){
		this.map = map;
		if(width / map.sizex < height / map.sizey)		blockSize = width  / map.sizex;
		else											blockSize = height / map.sizey;
		lineWidth = blockSize / 10;
	}
	

	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(new Color(60,60,60));
		
		//fill bottom edges
		g2d.fillRect(blockSize * map.sizex - lineWidth, 0, lineWidth, blockSize * map.sizey);
		g2d.fillRect(0, blockSize * map.sizey - lineWidth, blockSize * map.sizex, lineWidth);
		
		for(int y = 0; y < map.wall[0].length; y++){
			for(int x = 0; x < map.wall.length; x++){ //horizontal lines
				if(map.wall[x][y]) g2d.fillRect(x * blockSize, y * blockSize, lineWidth + blockSize, lineWidth);
			}
			for(int x = 0; x < map.wall.length; x++){
				if(map.wall[x][y]) g2d.fillRect(x * blockSize, y * blockSize, lineWidth, lineWidth + blockSize);
			}
		}
		
		for(int i = 0; i < players; i++){
			g2d.setColor(new Color(100 * i, 100 * i, 40));
			g2d.fillRect(blockSize * map.startPositions[i][0] + lineWidth, blockSize * map.startPositions[i][1] + lineWidth, blockSize - lineWidth, blockSize - lineWidth);
		}
		
		//if(!running){
		//	paintMenu(g2d);
		//}
	}
	/*public void paintMenu(Graphics2D g2d){
		g2d.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 40));
		g2d.drawString(":", width/2+5, height/8);
		g2d.drawString("" + score2, width/2 + 25, height/8 + 3);
		g2d.drawString("" + score1, width/2 - 16 - 24 * (int)(Math.log10(score1)), height/8 + 3);
		g2d.drawString("Press spacebar to continue", width/2-13*24, height/8*6);
		g2d.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 30));
		if(vsPlayer){
			g2d.setColor(new Color(120,190,80));
			g2d.fillRect(width/2-220, height/8*4, 200, 40);
		} else {
			g2d.setColor(new Color(200,100,80));
			g2d.fillRect(width/2-220, height/8*4, 200, 40);
		}
		if(!vsPlayer){
			g2d.setColor(new Color(120,190,80));
			g2d.fillRect(width/2+20+ballDiameter, height/8*4, 200, 40);
		} else {
			g2d.setColor(new Color(200,100,80));
			g2d.fillRect(width/2+20+ballDiameter, height/8*4, 200, 40);
		}
		g2d.setColor(new Color(0,0,0));
		g2d.drawString("Vs. Player", width/2-210, height/8*4 + 33);
		g2d.drawString("Vs. Enemy", width/2+42+ballDiameter, height/8*4 + 33);

	}*/

	public static void main(String[] args) throws InterruptedException {

		int players = 2;
		JFrame frame = new JFrame("Mini Tennis");
		Main main = new Main(players, 1600, 900);
		Map map = new Map(players);
		System.out.println(map.sizex + "  " + map.sizey);
		main.setMap(map);
		frame.add(main);
		frame.setSize(main.width, main.height);
		frame.setFocusable(true);
		frame.setBackground(new Color(210,205,215));
		frame.setVisible(true);
		frame.requestFocus();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		while (true) {
			//main.moveBall();
			//main.iHandler.update();
			main.repaint();
			map.checkMapValidity();
			Thread.sleep(1000);
		}
	}
	
}
