package edu.uchicago.cs.java.finalproject.controller;

import sun.audio.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.Clip;

import edu.uchicago.cs.java.finalproject.game.model.*;
import edu.uchicago.cs.java.finalproject.game.view.*;
import edu.uchicago.cs.java.finalproject.sounds.Sound;

// ===============================================
// == This Game class is the CONTROLLER
// ===============================================

public class Game implements Runnable, KeyListener {

	// ===============================================
	// FIELDS
	// ===============================================

	public static final Dimension DIM = new Dimension(1100, 700); //the dimension of the game.
	private GamePanel gmpPanel;          //gamepanel設在view
	public static Random R = new Random();
	public final static int ANI_DELAY = 45; // milliseconds between screen
											// updates (animation)
	private Thread thrAnim;
	private int nLevel = 1;
	private int nTick = 0;
	private ArrayList<Tuple> tupMarkForRemovals; //儲存被打掉之後的FOE,為了datasurcture的完整性
	private ArrayList<Tuple> tupMarkForAdds;//當有新的物件，加入，為了datastructure的完整性
	private boolean bMuted = true;
	

	private final int PAUSE = 80, // p key
			QUIT = 81, // q key
			LEFT = 37, // rotate left; left arrow
			RIGHT = 39, // rotate right; right arrow
			UP = 38, // thrust; up arrow
			START = 83, // s key
			FIRE = 32, // space key
			MUTE = 77, // m-key mute

	// for possible future use
	// HYPER = 68, 					// d key
	   SHIELD = 65, 				// a key arrow
	// NUM_ENTER = 10, 				// hyp
	 SPECIAL = 70; 					// fire special weapon;  F key

	private Clip clpThrust;
	private Clip clpMusicBackground;

	private static final int SPAWN_NEW_SHIP_FLOATER = 1200;



	// ===============================================
	// ==CONSTRUCTOR
	// ===============================================

	public Game() {

		gmpPanel = new GamePanel(DIM);
		gmpPanel.addKeyListener(this);

		clpThrust = Sound.clipForLoopFactory("whitenoise.wav");
		clpMusicBackground = Sound.clipForLoopFactory("music-background.wav");
	

	}

	// ===============================================
	// ==METHODS
	// ===============================================

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() { // uses the Event dispatch thread from Java 5 (refactored)
					public void run() {
						try {
							Game game = new Game(); // construct itself
							game.fireUpAnimThread();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void fireUpAnimThread() { // called initially
		if (thrAnim == null) {
			thrAnim = new Thread(this); // pass the thread a runnable object (this) //this implement the run class
			thrAnim.start();
		}
	}

	// implements runnable - must have run method
	public void run() {

		// lower this thread's priority; let the "main" aka 'Event Dispatch'
		// thread do what it needs to do first
		thrAnim.setPriority(Thread.MIN_PRIORITY);

		// and get the current time
		long lStartTime = System.currentTimeMillis();

		// this thread animates the scene
		while (Thread.currentThread() == thrAnim) {
			tick();
			//spawnNewShipFloater();

            if (CommandCenter.getScore()>100 && nTick % 120 == 0)
                spawnUFO();

			gmpPanel.update(gmpPanel.getGraphics()); // update takes the graphics context we must 
														// surround the sleep() in a try/catch block
														// this simply controls delay time between 
														// the frames of the animation

			//this might be a good place to check for collisions
			checkCollisions();
			//this might be a god place to check if the level is clear (no more foes)
			//if the level is clear then spawn some big asteroids -- the number of asteroids 
			//should increase with the level. 
			checkNewLevel();

			try {
				// The total amount of time is guaranteed to be at least ANI_DELAY long.  If processing (update) 
				// between frames takes longer than ANI_DELAY, then the difference between lStartTime - 
				// System.currentTimeMillis() will be negative, then zero will be the sleep time
				lStartTime += ANI_DELAY;
				Thread.sleep(Math.max(0,
						lStartTime - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				// just skip this frame -- no big deal
				continue;
			}
		} // end while
	} // end run



    //這個method很重要！
	private void checkCollisions() {

		
		//@formatter:off
		//for each friend in movFriends
			//for each foe in movFoes
				//if the distance between the two centers is less than the sum of their radii
					//mark it for removal
		
		//for each mark-for-removal
			//remove it
		//for each mark-for-add
			//add it
		//@formatter:on
		
		//we use this ArrayList to keep pairs of movMovables/movTarget for either
		//removal or insertion into our arrayLists later on
		tupMarkForRemovals = new ArrayList<Tuple>();
		tupMarkForAdds = new ArrayList<Tuple>();

		Point pntFriendCenter, pntFoeCenter;
		int nFriendRadiux, nFoeRadiux;

        //O(n*2)
		for (Movable movFriend : CommandCenter.movFriends) {
			for (Movable movFoe : CommandCenter.movFoes) {

				pntFriendCenter = movFriend.getCenter();
				pntFoeCenter = movFoe.getCenter();
				nFriendRadiux = movFriend.getRadius();
				nFoeRadiux = movFoe.getRadius();

				//detect collision
				if (pntFriendCenter.distance(pntFoeCenter) < (nFriendRadiux + nFoeRadiux)) {

					//falcon
					if ((movFriend instanceof Falcon) ){
                        //if the falcon is not protected
						if (CommandCenter.getFalcon().isbShield() == false){
							tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));
							CommandCenter.spawnFalcon(false);
							//killFoe(movFoe);
						}

                        //if the falcon is protected
                        if(CommandCenter.getFalcon().isbShield() == true ){
                            CommandCenter.setNumFalcons(CommandCenter.getNumFalcons());
                        }
					}
					//not the falcon
                     //falcon's bullets
                      //falcon's bullets kill asteroids
					else {

                        if (movFoe instanceof UFO){
                            UFO ufo = ((UFO) movFoe);
                            ufo.setmHit(ufo.getmHit()-1);
                            CommandCenter.calculateScore(movFoe);

                        } else {
                            killFoe(movFoe);
                        }

						//tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));
					//
					}//end else 



					//explode/remove foe
					
					

				}//end if 
			}//end inner for
		}//end outer for

		//check for collisions between falcon/FALCON BULLETS and floaters/UFO/BULLETS UFO
		if (CommandCenter.getFalcon() != null){

            Point pntFalCenter = CommandCenter.getFalcon().getCenter();
			int nFalRadiux = CommandCenter.getFalcon().getRadius();
			Point pntFloaterCenter;
			int nFloaterRadiux;
			
			for (Movable movFloater : CommandCenter.movFloaters) {
                for (Movable movFriend : CommandCenter.movFriends){

                    pntFloaterCenter = movFloater.getCenter();
				    nFloaterRadiux = movFloater.getRadius();
                    pntFriendCenter = movFriend.getCenter();
                    nFriendRadiux = movFriend.getRadius();
                    int counter =0;

	
				//detect collision
				if (pntFalCenter.distance(pntFloaterCenter) < (nFalRadiux + nFloaterRadiux)) {

                    if ((movFloater instanceof BulletUFO) ){
                        //if the falcon is not protected

                        if (CommandCenter.getFalcon().isbShield() == false){
                            tupMarkForRemovals.add(new Tuple(CommandCenter.movFloaters, movFloater));
                            CommandCenter.spawnFalcon(false);
                            Sound.playSound("pacman_eatghost.wav");
                            //killFloater(movFloater);
                        }

                        //if the falcon is protected
                        if(CommandCenter.getFalcon().isbShield() == true ){
                            CommandCenter.setNumFalcons(CommandCenter.getNumFalcons());
                        }
                    }

					//tupMarkForRemovals.add(new Tuple(CommandCenter.movFloaters, movFloater));
					//Sound.playSound("pacman_eatghost.wav");
	
				}//end if


                if(pntFriendCenter.distance(pntFloaterCenter) < (nFriendRadiux + nFloaterRadiux)){

                    if(movFloater instanceof UFO && movFriend instanceof Bullet){
                            //counter++;

                    }

                    if(counter >= 5){
                            tupMarkForRemovals.add(new Tuple(CommandCenter.movFloaters, movFloater));
                            killFloater(movFloater);
                            }
                    }
                }

            }

	    }//end inner for
		 //end if not null






		//remove these objects from their appropriate ArrayLists
		//this happens after the above iterations are done
		for (Tuple tup : tupMarkForRemovals) 
			tup.removeMovable();
		
		//add these objects to their appropriate ArrayLists
		//this happens after the above iterations are done
		for (Tuple tup : tupMarkForAdds) 
			tup.addMovable();

		//call garbage collection
		System.gc();
		
	}//end meth


    //這個method很重要，一旦FOE被打掉後要移走（用TUPLEREMOVAL），再加回去(用tupleADD)
	private void killFoe(Movable movFoe) {


		if (movFoe instanceof Asteroid){

			//we know this is an Asteroid, so we can cast without threat of ClassCastException
			Asteroid astExploded = (Asteroid)movFoe;
			//big asteroid 
			if(astExploded.getSize() == 0){
				//spawn two medium Asteroids
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				
			} 
			//medium size aseroid exploded
			else if(astExploded.getSize() == 1 ){
				//spawn three small Asteroids
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes, new Asteroid(astExploded)));
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
			}
			//remove the original Foe	
			tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));
            spawnDebris(astExploded);
		
			
		} 
		//not an asteroid
		else {
			//remove the original Foe
			tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));

		}

	CommandCenter.calculateScore(movFoe);
	}

    public void killFloater(Movable movFloater){

        if(movFloater instanceof UFO){
            Asteroid astExploded = new Asteroid(1);
            tupMarkForAdds.add(new Tuple(CommandCenter.movFoes, new Asteroid(astExploded)));
            tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
            tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
        }


    }



	//some methods for timing events in the game,
	//such as the appearance of UFOs, floaters (power-ups), etc.
    //例如說UFO出現持續的時間
	public void tick() {
		if (nTick == Integer.MAX_VALUE)
			nTick = 0;
		else
			nTick++;
	}

	public int getTick() {
		return nTick;
	}

    //重要！ 要使用shipFloater時，要先加到MOVFOES 或是 MOVEFRIENDS
	private void spawnNewShipFloater() {
		//make the appearance of power-up dependent upon ticks and levels
		//the higher the level the more frequent the appearance
		if (nTick % (SPAWN_NEW_SHIP_FLOATER - nLevel * 7) == 0) {
			CommandCenter.movFloaters.add(new NewShipFloater());
		}
	}


    private void spawnUFO() {
        if (CommandCenter.getScore() >100 ) {
            UFO ufo = new UFO(this);
            CommandCenter.movFoes.add(ufo);

        }
    }

    // Called when user presses 's'
	private void startGame() {
		CommandCenter.clearAll();
		CommandCenter.initGame();
		CommandCenter.setLevel(0);
		CommandCenter.setPlaying(true);
		CommandCenter.setPaused(false);
		//if (!bMuted)
		   // clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
	}

	//this method spawns new asteroids
    //重要！ 要使用ASTEROIDS時，要先加到MOVFOES 或是 MOVEFRIENDS
	private void spawnAsteroids(int nNum) {
		for (int nC = 0; nC < nNum; nC++) {
			//Asteroids with size of zero are big
			CommandCenter.movFoes.add(new Asteroid(1));
            CommandCenter.movFoes.add(new Asteroid(1));
            CommandCenter.movFoes.add(new Asteroid(1));
		}
	}

    private void spawnDebris(Asteroid astExploded){

        for(int i = 0; i<20;i++){
            CommandCenter.movDebris.add(new Debris(astExploded));
        }
    }

	
	private boolean isLevelClear(){
		//if there are no more Asteroids on the screen
		
		boolean bAsteroidFree = true;
		for (Movable movFoe : CommandCenter.movFoes) {
			if (movFoe instanceof Asteroid){
				bAsteroidFree = false;
				break;
			}
		}
		
		return bAsteroidFree;

		
	}


	
	private void checkNewLevel(){
		
		 if (isLevelClear() ){
			if (CommandCenter.getFalcon() !=null)
				CommandCenter.getFalcon().setProtected(true);
			
			 spawnAsteroids(CommandCenter.getLevel() + 1);
			 CommandCenter.setLevel(CommandCenter.getLevel() + 1);

		    }

	}
	
	
	

	// Varargs for stopping looping-music-clips
	private static void stopLoopingSounds(Clip... clpClips) {
		for (Clip clp : clpClips) {
			clp.stop();
		}
	}

	// ===============================================
	// KEYLISTENER METHODS
	// ===============================================

	@Override
	public void keyPressed(KeyEvent e) {
		Falcon fal = CommandCenter.getFalcon();
		int nKey = e.getKeyCode();
		// System.out.println(nKey);
        int counter = 3;

		if (nKey == START && !CommandCenter.isPlaying())
			startGame();

		if (fal != null) {

			switch (nKey) {
			case PAUSE:
				CommandCenter.setPaused(!CommandCenter.isPaused());
				if (CommandCenter.isPaused())
					stopLoopingSounds(clpMusicBackground, clpThrust);
				else
					clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
				break;
			case QUIT:
				System.exit(0);
				break;
			case UP:
				fal.thrustOn();
				if (!CommandCenter.isPaused())
					clpThrust.loop(Clip.LOOP_CONTINUOUSLY);
				break;
			case LEFT:
				fal.rotateLeft();
				break;
			case RIGHT:
				fal.rotateRight();
				break;

			// possible future use
			// case KILL:

			// case SHIELD:
            case SHIELD:

                if(counter>=1){
                fal.setShield(true);
                Graphics g = gmpPanel.getGraphics();
                fal.draw(g);
                counter--;
                }
                else {
                fal.setShield(false);
                }

                break;

			// case NUM_ENTER:

			default:
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Falcon fal = CommandCenter.getFalcon();
		int nKey = e.getKeyCode();
		 System.out.println(nKey);

		if (fal != null) {
			switch (nKey) {
			case FIRE:
				CommandCenter.movFriends.add(new Bullet(fal));
				Sound.playSound("laser.wav");
				break;
				
			//special is a special weapon, current it just fires the cruise missile. 
			case SPECIAL:
				CommandCenter.movFriends.add(new Cruise(fal));
				//Sound.playSound("laser.wav");
				break;
				
			case LEFT:
				fal.stopRotating();
				break;
			case RIGHT:
				fal.stopRotating();
				break;
			case UP:
				fal.thrustOff();
				clpThrust.stop();
				break;
            case SHIELD:
                fal.setShield(false);

                break;

				
			case MUTE:
				if (!bMuted){
					stopLoopingSounds(clpMusicBackground);
					bMuted = !bMuted;
				} 
				else {
					clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
					bMuted = !bMuted;
				}
				break;
				
				
			default:
				break;
			}
		}
	}

	@Override
	// Just need it b/c of KeyListener implementation
	public void keyTyped(KeyEvent e) {
	}
	

	
}



// ===============================================
// ==A tuple takes a reference to an ArrayList and a reference to a Movable
//This class is used in the collision detection method, to avoid mutating the array list while we are iterating
// it has two public methods that either remove or add the movable from the appropriate ArrayList 
// ===============================================

class  Tuple{


	//this can be any one of several CopyOnWriteArrayList<Movable>
	private CopyOnWriteArrayList<Movable> movMovs;
	//this is the target movable object to remove
	private Movable movTarget;
	
	public Tuple(CopyOnWriteArrayList<Movable> movMovs, Movable movTarget) {
		this.movMovs = movMovs;
		this.movTarget = movTarget;
	}
	
	public void removeMovable(){
		movMovs.remove(movTarget);
	}
	
	public void addMovable(){
		movMovs.add(movTarget);
	}

}
