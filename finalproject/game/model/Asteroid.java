package edu.uchicago.cs.java.finalproject.game.model;


import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import edu.uchicago.cs.java.finalproject.controller.Game;
import edu.uchicago.cs.java.finalproject.game.view.GamePanel;

public class Asteroid extends Sprite {

	
	private int nSpin;
	
	//radius of a large asteroid
	private final int RAD = 100;

	
	//nSize determines if the Asteroid is Large (0), Medium (1), or Small (2)
	//when you explode a Large asteroid, you should spawn 2 or 3 medium asteroids
	//same for medium asteroid, you should spawn small asteroids
	//small asteroids get blasted into debris
	public Asteroid(int nSize){
		
		//call Sprite constructor
		super();
		
		
		//the spin will be either plus or minus 0-9
		int nSpin = Game.R.nextInt(5)+1;
		if(nSpin %2 ==0)
			nSpin = -nSpin;
		setSpin(nSpin);
			
		//random delta-x
		//int nDX = Game.R.nextInt(10);
		//if(nDX %2 ==0)
		//	nDX = -nDX;
		//setDeltaX(nDX);
		
		//random delta-y
		int nDY = Game.R.nextInt(6)+1;
		if(nDY %2 ==0)
			nDY = nDY;
		setDeltaY(nDY);
			
		assignRandomShape();


        //an nSize of zero is a big asteroid
		//a nSize of 1 or 2 is med or small asteroid respectively
		if (nSize == 0)
			setRadius(RAD);
		else
			setRadius(RAD/(nSize * 2));
	}
	

	

	public Asteroid(Asteroid astExploded){
	

		//call Sprite constructor
		super();
		
		int  nSizeNew =	astExploded.getSize() + 1;
		
		
		//the spin will be either plus or minus 0-9
		int nSpin = Game.R.nextInt(10)+1;
		if(nSpin %2 ==0)
			nSpin = -nSpin;
		setSpin(nSpin);
			
		//random delta-x
		//int nDX = Game.R.nextInt(10 + nSizeNew*2);
		//if(nDX %2 ==0)
		//	nDX = -nDX;
		//setDeltaX(nDX);
		
		//random delta-y
		int nDY = Game.R.nextInt(10)+1;
		if(nDY %2 ==0)
			nDY = nDY;
		setDeltaY(nDY);
			
		assignRandomShape();

		
		//an nSize of zero is a big asteroid
		//a nSize of 1 or 2 is med or small asteroid respectively

		setRadius(RAD / (nSizeNew * 2));
		setCenter(astExploded.getCenter());


	}


	
	public int getSize(){
		
		int nReturn = 0;
		
		switch (getRadius()) {
			case 100:
				nReturn= 0;
				break;
			case 50:
				nReturn= 1;
				break;
			case 25:
				nReturn= 2;
				break;
		}
		return nReturn;
		
	}

	//overridden
	public void move(){

        super.move();
		
		//an asteroid spins, so you need to adjust the orientation at each move()
		setOrientation(getOrientation() + getSpin());
		
	}

	public int getSpin() {
		return this.nSpin;
	}
	

	public void setSpin(int nSpin) {
		this.nSpin = nSpin;
	}
	
	//this is for an asteroid only
	public void assignRandomShape () {
        if(CommandCenter.inLevelNumber() == 1){
        ArrayList<Point> pntCs = new ArrayList<Point>();

        //right points
        pntCs.add(new Point(0, 20));
        pntCs.add(new Point(5, 5));
        pntCs.add(new Point(20,0));
        pntCs.add(new Point(5, -5));

        //left points
        pntCs.add(new Point(0, -20));
        pntCs.add(new Point(-5, -5));
        pntCs.add(new Point(-20, 0));
        pntCs.add(new Point(-5, 5));
        pntCs.add(new Point(0, 20));

        assignPolarPoints(pntCs);

        setColor(Color.BLACK);
        }

        else if(CommandCenter.inLevelNumber()==2 ||CommandCenter.inLevelNumber() == 3){
            ArrayList<Point> pntCs = new ArrayList<Point>();

            //right points
            pntCs.add(new Point(0, 0));
            pntCs.add(new Point(2, 0));
            pntCs.add(new Point(1, 1));
            pntCs.add(new Point(1, 3));
            pntCs.add(new Point(4, 2));
            pntCs.add(new Point(5, 0));
            pntCs.add(new Point(0, -3));
            pntCs.add(new Point(-3, -3));
            pntCs.add(new Point(-5, 3));
            pntCs.add(new Point(-7, 7));
            pntCs.add(new Point(0,10));
            pntCs.add(new Point(5,8 ));
            pntCs.add(new Point(13, 2));
            pntCs.add(new Point(15,0));
            pntCs.add(new Point(12,-6));
            pntCs.add(new Point(3,-12));
            pntCs.add(new Point(0,-15 ));
            pntCs.add(new Point(-7,-13 ));
            pntCs.add(new Point(-13, -5));
            pntCs.add(new Point(-16,0 ));
            pntCs.add(new Point(-12,5 ));
            pntCs.add(new Point(-5,15 ));
            pntCs.add(new Point(0,18));
            pntCs.add(new Point(10,15 ));
            pntCs.add(new Point(17,8 ));
            pntCs.add(new Point(20,0));
            pntCs.add(new Point(17,17 ));
            pntCs.add(new Point(19,7 ));
            pntCs.add(new Point(0,20));
            pntCs.add(new Point(-17,17 ));
            pntCs.add(new Point(-19, 7));
            pntCs.add(new Point(-20,0 ));
            pntCs.add(new Point(-15,-7));
            pntCs.add(new Point(-3,-14));
            pntCs.add(new Point(0,-20));
            pntCs.add(new Point(20,0));
            pntCs.add(new Point(15,3 ));
            pntCs.add(new Point(9, 8));
            pntCs.add(new Point(0,10));

            assignPolarPoints(pntCs);

            setColor(Color.BLACK);


        }

    }

    public void draw(Graphics g) {
        Color colShip;
        colShip = Color.BLACK;

        drawShipWithColor(g, colShip);

    } //end draw()

    public void drawShipWithColor(Graphics g, Color col) {
        super.draw(g);
        g.setColor(col);
        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //填滿黑色
        g.setColor(Color.CYAN);
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }


    public void moveToCruise(Movable movFriend){
        if(movFriend instanceof Cruise){
            Cruise cruise = new Cruise(CommandCenter.getFalcon());

            setDeltaX( cruise.getDeltaX() *25);
            setDeltaY( cruise.getDeltaY() * 25);
            setCenter( cruise.getCenter() );


        }
    }


}
