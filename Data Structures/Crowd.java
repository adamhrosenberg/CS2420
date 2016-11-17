package homework10;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.ListIterator;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;
import javax.swing.JApplet;
import com.jogamp.opengl.util.FPSAnimator;
import homework10.Kdsearchtree;


/**	
 * 
 * Author: Adam Rosenberg
 * 
 * December 1, 2014.
 */

public class Crowd extends JApplet implements GLEventListener, MouseListener 
{
    public class Boid {
        double posx, posy, velx, vely, frcx, frcy;
        long lastTime;
        public void update() {
            long dt = System.currentTimeMillis() - lastTime;
            lastTime = System.currentTimeMillis();

            // limit forces
            if (frcx > 1) frcx = 0.1;
            if (frcx < -1) frcx = -0.1;
            if (frcy > 1) frcy = 0.1;
            if (frcy < -1) frcy = -0.1;
            
            // limit velocities
            if (velx > 5) velx = 5;
            if (velx < -5) velx = -5;
            if (vely > 5) vely = 5;
            if (vely < -5) vely = -5;
            
            // integrate forces and velocities
            velx = velx + frcx*dt;
            vely = vely + frcy*dt;
            posx = posx + velx*dt/50.0;
            posy = posy + vely*dt/50.0;
            
            // check if we've left the window
            if (posx < size) {
                posx = size;
                velx = -velx;
            }
            if (posx > winWidth-size) {
                posx = winWidth-size;
                velx = -velx;
            }                
            if (posy < size) {
                posy = size;
                vely = -vely;
            } 
            if (posy > winHeight-size) {
                posy = winHeight-size;
                vely = -vely;
            }
        }
        
        public void draw(GLAutoDrawable gld) {
            final GL2 gl = gld.getGL().getGL2(); 
            // set color
        	if (repulsoid != null && this.equals(repulsoid))	{	//Color our repulsoid red.
        		gl.glColor3d(0.9, 0.0, 0.1);
        	}
        	else if	(this != null) {	//Color the common trash boids blue.
        		gl.glColor3d(0.1, 0.0, 0.9);
        	}
            // draw the object
            gl.glBegin(GL2.GL_QUADS);
            // x, y, z coordinates of the corners
            gl.glVertex3d(posx-size, posy-size, 0);
            gl.glVertex3d(posx+size, posy-size, 0);
            gl.glVertex3d(posx+size, posy+size, 0);
            gl.glVertex3d(posx-size, posy+size, 0);
            gl.glEnd();
        }
        
        Boid(double posx, double posy, double velx, double vely) {	//Boids!
            lastTime = System.currentTimeMillis();
            this.posx = posx;
            this.posy = posy;
            this.velx = velx;
            this.vely = vely;
        }
    }
    
    // square a number
    private double sqr(double x) {return x*x;}
    
    
    // this is the main interesting method...
    public synchronized void update() {
    	//Create the tree we use to get all the points within the interaction radius.
    	Kdsearchtree KDtree = new Kdsearchtree(boids);
        // loop over all the boids
        ListIterator<Boid> bi = boids.listIterator(); 
        while (bi.hasNext()) {
            Boid i = bi.next(); 
            // initialize forces to repulsoid force and 0's to everything else.
            double frcx;
            double frcy;
            if (repulsoid != null)	{
            	frcx=fweight*15/(i.posx - repulsoid.posx);
            	frcy=fweight*15/(i.posy - repulsoid.posy);
            }
            else	{
            	frcx=0;
            	frcy=0;
            }
            double centerx = 0;
            double centery = 0;
            int n = 0;
            double velx = 0;
            double vely = 0;
            
            // loop over all boids within interaction radius.
            ArrayList<Crowd.Boid> inRadius = KDtree.pointsInCircle(i.posx, i.posy, rad); 
            for (int k = 0; k < inRadius.size(); k++) {
                Boid j = inRadius.get(k);
                // boids don't affect themselves
                if (i==j) continue;
                // compute the distance between the boids
                double r = sqr(i.posx - j.posx) + sqr(i.posy - j.posy);
                // careful about dividing by zero
                if (r > 0.1) {
                    // compute a force to push boids apart (avoidance)
                    frcx += (i.posx - j.posx)/sqr(r);
                    frcy += (i.posy - j.posy)/sqr(r);
                }
                // compute the center of nearby boids
                centerx += j.posx;
                centery += j.posy;
                // compute the average velocity of nearby boids
                velx += j.velx;
                vely += j.vely;
                // we're averaging... keep track of howmany nearby boids there are
                n++;
            }
            // if there were nearby boids
            if (n > 0) {
                // compute the difference between our position and the average of nearby boids
                centerx = (centerx/n) - i.posx;
                centery = (centery/n) - i.posy;
                // compute the difference between our velocity and the average of nearby boids
                velx = (velx/n) - i.velx;
                vely = (vely/n) - i.vely;
                // compute a weighted force on this boid
                i.frcx = fweight*frcx + cweight*centerx + vweight*velx + rweight*Math.random();
                i.frcy = fweight*frcy + cweight*centery + vweight*vely + rweight*Math.random(); 
            }
        }
        
        // iterate over the boids updating their positions/velocities
        bi = boids.listIterator(); 
        while (bi.hasNext()) {
            bi.next().update();
        }
    }
    
    GLU glu;
    int winWidth=600, winHeight=800;
    FPSAnimator animator;
    Thread updateThread;
    ArrayList<Boid> boids;
    Boid repulsoid = new Boid(winWidth/2, winHeight/2, 0, 0);	//Our repulsoid.  Repels objects based on their difference in x and y values.  Divides the screen into 4 regions where crossing from one to the other is nearly impossible.
    double rad, fweight, cweight, vweight, rweight;
    int size;

    public Crowd() {
    	
    	
        boids = new ArrayList<Boid>();
        
        size = 10;
        rad = 100; // interaction radius
        fweight = 0.1; // avoidance weight
        cweight = 0.00001; // centering weight
        vweight = 0.00001; // velocity matching weight
        rweight = 0.000001; // randomization weight
        
        // create 20 random boids
        for (int i=0; i<20; i++) {
            double posx = Math.random()*winWidth;
            double posy = Math.random()*winHeight;
            double velx = Math.random()-0.5;
            double vely = Math.random()-0.5;
            boids.add(new Boid(posx, posy, velx, vely));
        }
    }

    public synchronized void display (GLAutoDrawable gld)
    {
        final GL2 gl = gld.getGL().getGL2();
        
        // clearing the window
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        // setting up the camera 
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0.0, winWidth, 0.0, winHeight);
        
        // initializing world transformations
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        // draw the objects
        ListIterator<Boid> bi = boids.listIterator();
        while (bi.hasNext()) {
            Boid b = bi.next();
            b.draw(gld);
        }
        if (repulsoid != null) repulsoid.draw(gld);
    }

    public void displayChanged (GLAutoDrawable arg0, boolean arg1, boolean arg2) {}

    public void init (GLAutoDrawable gld)
    {
        glu = new GLU();
    }
    
    public void init() {
        setLayout(new FlowLayout());
        // create a gl drawing canvas
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);
        canvas.setPreferredSize(new Dimension(winWidth, winHeight));

        // add gl event listener
        canvas.addGLEventListener(this);
        canvas.addMouseListener(this);
        add(canvas);
        setSize(winWidth, winHeight);
        // add the canvas to the frame
        animator = new FPSAnimator(canvas,30);
        updateThread = new Thread(new Runnable() {
		public void run() {
		    while(true) {
			update();
		    }
		}
	    });
    }

    public void reshape (GLAutoDrawable gld, int x, int y, int width, int height)
    {
        GL gl = gld.getGL();
        winWidth = width;
        winHeight = height;
        // sets the mapping from camera space to the window
        gl.glViewport(0,0, width, height);
    }

    public void start() {
        animator.start();
        updateThread.start();
    } 

    public void stop() {
        animator.stop();
        updateThread.interrupt();
    }

    public synchronized void mousePressed (MouseEvent e)
    {
    	if (e.getButton() == MouseEvent.BUTTON1)	{	//Place a repulsoid with a left click.
        	repulsoid = new Boid(e.getX(), winHeight-e.getY(), 0, 0);
    	}
    	else if (e.getButton() == MouseEvent.BUTTON3)	{	//Remove the repulsiod with a right click.
    		repulsoid = null;
    	}
    }

    public void mouseClicked (MouseEvent e) {}
    public void mouseEntered (MouseEvent e) {}
    public void mouseExited (MouseEvent e){}
    public void mouseReleased (MouseEvent e) {}


    /* (non-Javadoc)
     * @see javax.media.opengl.GLEventListener#dispose(javax.media.opengl.GLAutoDrawable)
     */
    public void dispose (GLAutoDrawable arg0)
    {
        // TODO Auto-generated method stub
        
    }
}