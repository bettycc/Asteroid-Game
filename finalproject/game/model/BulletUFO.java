package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by betty on 5/28/15.
 */
public class BulletUFO extends Sprite {

    private final double FIRE_POWER = 20.0;

    public BulletUFO(UFO ufo){
        super();

        //defined the points on a cartesean grid
        ArrayList<Point> pntCs = new ArrayList<Point>();

        pntCs.add(new Point(0,3)); //top point

        pntCs.add(new Point(1,-1));
        pntCs.add(new Point(0,-2));
        pntCs.add(new Point(-1,-1));

        assignPolarPoints(pntCs);

        setColor(Color.BLACK);

        //a bullet expires after 20 frames
        setExpire(50);
        setRadius(6);


        //everything is relative to the falcon ship that fired the bullet
        setDeltaX( (ufo.getDeltaX() +
                Math.cos( Math.toRadians( -ufo.getOrientation() ) ) * FIRE_POWER ));
        setDeltaY( (ufo.getDeltaY() +
                Math.sin( Math.toRadians( -ufo.getOrientation() ) ) * FIRE_POWER ));
        setCenter(ufo.getCenter());



        //set the bullet orientation to the falcon (ship) orientation
        setOrientation(ufo.getOrientation());

    }

    public void expire(){
        if (getExpire() == 0)
            CommandCenter.movFoes.remove(this);
        else
            setExpire(getExpire() - 1);
    }




    @Override
    public void move() {


            Point pnt = getCenter();
            double dX = pnt.x + getDeltaX();
            double dY = pnt.y + getDeltaY();

            //this just keeps the sprite inside the bounds of the frame
            if (pnt.x > getDim().width) {
                setCenter(new Point(1, pnt.y));

            } else if (pnt.x < 0) {
                setCenter(new Point(getDim().width - 1, pnt.y));
            } else if (pnt.y > getDim().height) {
                setExpire(0);

            } else if (pnt.y < 0) {
                setCenter(new Point(pnt.x, getDim().height - 1));
            } else {

                setCenter(new Point((int) dX, (int) dY));
            }


    }
}

