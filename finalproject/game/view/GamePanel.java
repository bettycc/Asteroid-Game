package edu.uchicago.cs.java.finalproject.game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;


import edu.uchicago.cs.java.finalproject.controller.Game;
import edu.uchicago.cs.java.finalproject.game.model.CommandCenter;
import edu.uchicago.cs.java.finalproject.game.model.Falcon;
import edu.uchicago.cs.java.finalproject.game.model.Movable;


 public class GamePanel extends Panel {
	
	// ==============================================================
	// FIELDS 
	// ============================================================== 
	 
	// The following "off" vars are used for the off-screen double-bufferred image. 
	private Dimension dimOff;
	private Image imgOff;
	private Graphics grpOff;
	
	private GameFrame gmf;
	private Font fnt = new Font("SansSerif", Font.BOLD, 12);
	private Font fntBig = new Font("SansSerif", Font.BOLD + Font.ITALIC, 36);
	private FontMetrics fmt; 
	private int nFontWidth;
	private int nFontHeight;
	private String strDisplay = "";
	

	// ==============================================================
	// CONSTRUCTOR 
	// ==============================================================
	
	public GamePanel(Dimension dim){
	    gmf = new GameFrame();
		gmf.getContentPane().add(this);
		gmf.pack();
		initView();
		
		gmf.setSize(dim);
		gmf.setTitle("Game Base");
		gmf.setResizable(false);
		gmf.setVisible(true);
		this.setFocusable(true);
	}
	
	
	// ==============================================================
	// METHODS 
	// ==============================================================
	
	private void drawScore(Graphics g) {
		g.setColor(Color.BLACK);
		g.setFont(fnt);
		if (CommandCenter.getScore() != 0) {
			g.drawString("SCORE :  " + CommandCenter.getScore(), nFontWidth, nFontHeight);
		} else {
			g.drawString("NO SCORE", nFontWidth, nFontHeight);
		}
	}


	
	@SuppressWarnings("unchecked")
	public void update(Graphics g) {
		if (grpOff == null || Game.DIM.width != dimOff.width
				|| Game.DIM.height != dimOff.height) {
			dimOff = Game.DIM;
			imgOff = createImage(Game.DIM.width, Game.DIM.height);
			grpOff = imgOff.getGraphics();
		}
		// Fill in background with black.
        //被我改成白色背景
		grpOff.setColor(Color.LIGHT_GRAY);
		grpOff.fillRect(0, 0, Game.DIM.width, Game.DIM.height);

		drawScore(grpOff);


		if (!CommandCenter.isPlaying() ) {
			displayTextOnScreen();
            drawTitle(grpOff);
		} else if (CommandCenter.isPaused()) {
			strDisplay = "Game Paused";
			grpOff.drawString(strDisplay,
					(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4);
		}
		
		//playing and not paused!
		else {
			
			//draw them in decreasing level of importance
			//friends will be on top layer and debris on the bottom
			iterateMovables(grpOff, 
					   CommandCenter.movDebris,
			           CommandCenter.movFloaters, 
			           CommandCenter.movFoes,
			           CommandCenter.movFriends);
			
			
			drawNumberShipsLeft(grpOff);
            drawLevel(grpOff);
            drawShieldLeft(grpOff);
            drawWeapon1Left(grpOff);
            drawWeapon2Left(grpOff);
            drawBackground(grpOff);

			if (CommandCenter.isGameOver()) {
				CommandCenter.setPlaying(false);
				//bPlaying = false;
			}
		}


        //if(CommandCenter.isGameOver()){
        //     displayAfterGame();
        //}
		//draw the double-Buffered Image to the graphics context of the panel
		g.drawImage(imgOff, 0, 0, this);
	} 


	
	//for each movable array, process it.
	private void iterateMovables(Graphics g, CopyOnWriteArrayList<Movable>...movMovz){
		
		for (CopyOnWriteArrayList<Movable> movMovs : movMovz) {
			for (Movable mov : movMovs) {

				mov.move();
				mov.draw(g);
				mov.fadeInOut();
				mov.expire();
			}
		}
		
	}
	

     private void drawBackground(Graphics g){
         for(int i =0; i<=Game.DIM.getHeight(); i++){
             for(int j=0; j<=Game.DIM.getWidth(); j++){
                 i=i+5;
                 j=j+10;
                 g.setColor(Color.BLACK);
                 g.drawOval(100+i,0+j,50,50);
                 g.setColor(Color.WHITE);
                 g.fillOval(100+i,0+j,12,12);
                 //g.setColor(Color.BLACK);
                 //g.fillOval(100+i,100+j,25,25);
                 //g.setColor(Color.WHITE);
                 //g.fillOval(125+i,125+j,12,12);
             }
         }

     }

     private void drawTitle(Graphics g){
         g.setColor(Color.BLACK);
         g.fillOval(50,50,100,100);
         g.setColor(Color.WHITE);
         g.fillOval(75,75,50,50);
         g.setColor(Color.BLACK);
         g.fillOval(100,100,25,25);
         g.setColor(Color.WHITE);
         g.fillOval(125,125,12,12);

         g.setColor(Color.BLACK);
         g.fillOval(1100-50,50,100,100);
         g.setColor(Color.WHITE);
         g.fillOval(1100-75,75,50,50);
         g.setColor(Color.BLACK);
         g.fillOval(1100-100,100,25,25);
         g.setColor(Color.WHITE);
         g.fillOval(1100-125,125,12,12);

     
     }

	// Draw the number of falcons left on the bottom-right of the screen. 
	private void drawNumberShipsLeft(Graphics g) {
		Falcon fal = CommandCenter.getFalcon();
		double[] dLens = fal.getLengths();
		int nLen = fal.getDegrees().length;
		Point[] pntMs = new Point[nLen];
		int[] nXs = new int[nLen];
		int[] nYs = new int[nLen];
	
		//convert to cartesean points
		for (int nC = 0; nC < nLen; nC++) {
			pntMs[nC] = new Point((int) (10 * dLens[nC] * Math.sin(Math
					.toRadians(90) + fal.getDegrees()[nC])),
					(int) (10 * dLens[nC] * Math.cos(Math.toRadians(90)
							+ fal.getDegrees()[nC])));
		}
		
		//set the color to white
		g.setColor(Color.BLACK);
		//for each falcon left (not including the one that is playing)
		for (int nD = 1; nD < CommandCenter.getNumFalcons(); nD++) {
			//create x and y values for the objects to the bottom right using cartesean points again
			for (int nC = 0; nC < fal.getDegrees().length; nC++) {
				//nXs[nC] = pntMs[nC].x + Game.DIM.width - (20 * nD);
				//nYs[nC] = pntMs[nC].y + Game.DIM.height - 40;
                nXs[nC] = pntMs[nC].x + Game.DIM.width - (20*nD);
                nYs[nC] = pntMs[nC].y + Game.DIM.height - 680;
			}
			g.drawPolygon(nXs, nYs, nLen);
            g.fillPolygon(nXs, nYs, nLen);
		} 
	}

     private void drawLevel(Graphics g){
         g.setColor(Color.BLACK);
         g.setFont(fnt);
         /**
         if(CommandCenter.getScore() <100){
             g.drawString("Level ", nFontWidth, nFontHeight*3);
             g.setColor(Color.white);
             g.drawRect(nFontWidth, nFontHeight*4, nFontWidth, nFontHeight );
             g.drawString("1", nFontWidth+5, nFontHeight*5);
             g.drawRect(nFontWidth*2, nFontHeight*4, nFontWidth, nFontHeight );
             g.drawString("2", nFontWidth+30, nFontHeight*5);
             g.drawRect(nFontWidth*3, nFontHeight*4, nFontWidth, nFontHeight );
             g.drawString("3", nFontWidth+50, nFontHeight*5);
         }
          **/
         if(CommandCenter.getScore()<= 100){
             g.drawString("Level ", nFontWidth, nFontHeight*3);
             g.setColor(Color.BLACK);
             g.drawRect(nFontWidth, nFontHeight*4, nFontWidth, nFontHeight );
             g.drawString("1", nFontWidth+5, nFontHeight*5);
             g.setColor(Color.green);
             g.fillRect(nFontWidth, nFontHeight*4, nFontWidth, nFontHeight );
             g.setColor(Color.BLACK);
             g.drawString("1", nFontWidth+5, nFontHeight*5);

             g.drawRect(nFontWidth*2, nFontHeight*4, nFontWidth, nFontHeight );
             g.drawString("2", nFontWidth+30, nFontHeight*5);
             g.drawRect(nFontWidth*3, nFontHeight*4, nFontWidth, nFontHeight );
             g.drawString("3", nFontWidth+50, nFontHeight*5);
         }
         else if(CommandCenter.getScore()>100 && CommandCenter.getScore()<=200){
             g.drawString("Level ", nFontWidth, nFontHeight*3);
             g.setColor(Color.BLACK);
             g.drawRect(nFontWidth, nFontHeight*4, nFontWidth, nFontHeight );
             g.drawRect(nFontWidth*2, nFontHeight*4, nFontWidth, nFontHeight );

             g.setColor(Color.green);
             g.fillRect(nFontWidth, nFontHeight*4, nFontWidth, nFontHeight );
             g.fillRect(nFontWidth*2, nFontHeight*4, nFontWidth, nFontHeight );

             g.setColor(Color.BLACK);
             g.drawString("1", nFontWidth+5, nFontHeight*5);
             g.drawString("2", nFontWidth+30, nFontHeight*5);

             g.drawRect(nFontWidth*3, nFontHeight*4, nFontWidth, nFontHeight );
             g.drawString("3", nFontWidth+50, nFontHeight*5);
         }
         else if(CommandCenter.getScore()>200){
             g.drawString("Level ", nFontWidth, nFontHeight*3);
             g.setColor(Color.BLACK);
             g.drawRect(nFontWidth, nFontHeight*4, nFontWidth, nFontHeight );
             g.drawRect(nFontWidth*2, nFontHeight*4, nFontWidth, nFontHeight );
             g.drawRect(nFontWidth*3, nFontHeight*4, nFontWidth, nFontHeight );

             g.setColor(Color.green);
             g.fillRect(nFontWidth, nFontHeight*4, nFontWidth, nFontHeight );
             g.fillRect(nFontWidth*2, nFontHeight*4, nFontWidth, nFontHeight );
             g.fillRect(nFontWidth*3, nFontHeight*4, nFontWidth, nFontHeight );

             g.setColor(Color.BLACK);
             g.drawString("1", nFontWidth+5, nFontHeight*5);
             g.drawString("2", nFontWidth+30, nFontHeight*5);
             g.drawString("3", nFontWidth+50, nFontHeight*5);

         }

     }
     private void drawWeapon1Left(Graphics g){}
     private void drawWeapon2Left(Graphics g){}
     private void drawShieldLeft(Graphics g){}
	
	private void initView() {
		Graphics g = getGraphics();			// get the graphics context for the panel
		g.setFont(fnt);						// take care of some simple font stuff
		fmt = g.getFontMetrics();
		nFontWidth = fmt.getMaxAdvance();
		nFontHeight = fmt.getHeight();
		g.setFont(fntBig);					// set font info
	}
	
	// This method draws some text to the middle of the screen before/after a game
	private void displayTextOnScreen() {



		strDisplay = "GAME";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4);

		strDisplay = "use the arrow keys to turn and thrust";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 40);

		strDisplay = "use the space bar to fire";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 80);

		strDisplay = "'S' to Start";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 120);

		strDisplay = "'P' to Pause";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 160);

		strDisplay = "'Q' to Quit";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 200);

        //shield
		strDisplay = "left pinkie on 'A' for Shield";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 240);

        //special weapon
		strDisplay = "left index finger on 'F' for Guided Missile";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ nFontHeight + 280);


	}




    // public void displayAfterGame(){

    //     strDisplay = "GAME OVER";
    //     grpOff.drawString(strDisplay,
    //             (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4);

    //     strDisplay = "'S' to Start";
    //     grpOff.drawString(strDisplay,
    //             (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
    //                     + nFontHeight + 120);

     //}


	public GameFrame getFrm() {return this.gmf;}
	public void setFrm(GameFrame frm) {this.gmf = frm;}	
}