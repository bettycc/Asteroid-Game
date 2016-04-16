package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by betty on 5/24/15.
 */
public class UFO extends Sprite {

    private int nSpin;
    private Game mGame;
    private int mHit;


    public UFO(Game game) {


        super();
        mHit = 5;
        mGame = game;
        ArrayList<Point> pntCs = new ArrayList<Point>();
        // top of ship
        pntCs.add(new Point(0, 20));
        pntCs.add(new Point(10,15));
        pntCs.add(new Point(25,0));
        pntCs.add(new Point(23,3));
        pntCs.add(new Point(26,5));
        pntCs.add(new Point(28,4));
        pntCs.add(new Point(26,0));
        pntCs.add(new Point(25,0));

        pntCs.add(new Point(26, -10));
        pntCs.add(new Point(20,-15));
        pntCs.add(new Point(15,-10));
        pntCs.add(new Point(12,-15));
        pntCs.add(new Point(10,-10));
        pntCs.add(new Point(8,-15));
        pntCs.add(new Point(5,-10));
        pntCs.add(new Point(2,-15));
        pntCs.add(new Point(0, -10));


        pntCs.add(new Point(-2,-15));
        pntCs.add(new Point(-5,-10));
        pntCs.add(new Point(-8,-15));
        pntCs.add(new Point(-10,-10));
        pntCs.add(new Point(-12,-15));
        pntCs.add(new Point(-15,-10));
        pntCs.add(new Point(-20,-15));
        pntCs.add(new Point(-26, -10));

        pntCs.add(new Point(-25,0));
        pntCs.add(new Point(-26,0));
        pntCs.add(new Point(-28,4));
        pntCs.add(new Point(-26,5));
        pntCs.add(new Point(-23,3));
        pntCs.add(new Point(-25,0));
        pntCs.add(new Point(-10,15));
        pntCs.add(new Point(0,20));

/**
        pntCs.add(new Point(-2,18));
         pntCs.add(new Point(-8,13));
         pntCs.add(new Point(-23,-2));
         pntCs.add(new Point(-21,1));
         pntCs.add(new Point(-24,3));
         pntCs.add(new Point(-26,2));
         pntCs.add(new Point(-24,-2));
         pntCs.add(new Point(-23,-2));

         pntCs.add(new Point(-26, -10));
         pntCs.add(new Point(-20,-15));
         pntCs.add(new Point(-15,-10));
         pntCs.add(new Point(-12,-15));
         pntCs.add(new Point(-10,-10));
         pntCs.add(new Point(-8,-15));
         pntCs.add(new Point(-5,-10));
         pntCs.add(new Point(-2,-15));


         pntCs.add(new Point(-2, 8));
         pntCs.add(new Point(0,-13));
         pntCs.add(new Point(3,-8));
         pntCs.add(new Point(6,-12));
         pntCs.add(new Point(12,-8));
         pntCs.add(new Point(10,-12));
         pntCs.add(new Point(13,-8));
         pntCs.add(new Point(18,-13));
         pntCs.add(new Point(24, -8));
         pntCs.add(new Point(23,-2));
         pntCs.add(new Point(24,-2));
         pntCs.add(new Point(26,2));
         pntCs.add(new Point(24,3));
         pntCs.add(new Point(21,1));
         pntCs.add(new Point(23,-2));
         pntCs.add(new Point(8,12));
         pntCs.add(new Point(0, 10));
**/
        assignPolarPoints(pntCs);

        setExpire(200);
        setRadius(50);
        setColor(Color.BLUE);


        //int nX = Game.R.nextInt(10)+5;
        int nX = 50;
        int nY = Game.R.nextInt(3);
        //int nS = Game.R.nextInt(5);

        //set random DeltaX
        if (nX % 2 == 0)
            setDeltaX(nX);
        else
            setDeltaX(-nX);

        //set random DeltaY
        if (nY % 2 == 0)
            setDeltaX(nY);
        else
            setDeltaX(-nY);


        setCenter(new Point(Game.R.nextInt(Game.DIM.width),60));

        //random orientation
        setOrientation(-90);



    }

    public void move() {
        super.move();

        if (mHit <= 0){
            CommandCenter.getMovFoes().remove(this);
        }

      //  setOrientation(getOrientation() + getSpin());

        if (mGame.getTick() % 11 == 0){
            CommandCenter.getMovFoes().add(new BulletUFO(this));
        }



    }

    public int getSpin() {
        return this.nSpin;
    }

    public void setSpin(int nSpin) {
        this.nSpin = nSpin;
    }

    //override the expire method - once an object expires, then remove it from the arrayList.
    @Override
    public void expire() {
        if (getExpire() == 0)
            CommandCenter.movFoes.remove(this);
        else
            setExpire(getExpire() - 1);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        //fill this polygon (with whatever color it has)
        g.setColor(new Color(230- (mHit*46), (230 -mHit*46), (255-mHit*46)));
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //now draw a white border
        g.setColor(Color.black);

        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }

    public int getmHit() {
        return mHit;
    }

    public void setmHit(int mHit) {
        this.mHit = mHit;
    }
}

