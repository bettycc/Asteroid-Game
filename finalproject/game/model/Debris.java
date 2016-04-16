package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by betty on 5/24/15.
 */
public class Debris extends Sprite {

    private int nSpin;

    //radius
    private final int RAD = 10;


    //nSize determines if the Asteroid is Large (0), Medium (1), or Small (2)
    //when you explode a Large asteroid, you should spawn 2 or 3 medium asteroids
    //same for medium asteroid, you should spawn small asteroids
    //small asteroids get blasted into debris
    public Debris(Asteroid astExploded){

        //call Sprite constructor
        super();


        //the spin will be either plus or minus 0-9
        int nSpin = Game.R.nextInt(10);
        setSpin(nSpin);

        //random delta-x
        int nDX = Game.R.nextInt(10);
        if(nDX %2 ==0)
        	nDX = -nDX;
        setDeltaX(nDX);

        //random delta-y
        int nDY = Game.R.nextInt(10);
        if(nDY %2 ==0)
            nDY = -nDY;
        setDeltaY(nDY);

        assignRandomShape();

        setRadius(RAD);
        setExpire( 20 );

        //setDeltaX( astExploded.getDeltaX() +
        //        Math.cos( Math.toRadians(astExploded.getOrientation()) ) );
        //setDeltaY( astExploded.getDeltaY() +
        //        Math.sin( Math.toRadians(astExploded.getOrientation()) ) );
        setCenter(astExploded.getCenter());

        setOrientation(astExploded.getOrientation());

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
        ArrayList<Point> pntCs = new ArrayList<Point>();

        //right points
        pntCs.add(new Point(0, 4));
        pntCs.add(new Point(3, 0));
        pntCs.add(new Point(-3, 0));
        pntCs.add(new Point(0, 4));

        assignPolarPoints(pntCs);

        setColor(Color.BLACK);

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

        if(getSpin() == 1 || getSpin() == 3){
          g.setColor(Color.ORANGE);
          g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        }
        if(getSpin() == 2 || getSpin() == 4){
            g.setColor(Color.PINK);
            g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        }
        if(getSpin() == 5 || getSpin() == 7){
            g.setColor(Color.CYAN);
            g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        }
        if(getSpin() == 6 || getSpin() == 8){
            g.setColor(Color.GRAY);
            g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        }
        if(getSpin() == 9 || getSpin() == 0){
            g.setColor(Color.MAGENTA);
            g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        }
 }

    //override the expire method - once an object expires, then remove it from the arrayList.
    public void expire(){
        if (getExpire() == 0)
            CommandCenter.movDebris.remove(this);
        else
            setExpire(getExpire() - 1);
    }





}
