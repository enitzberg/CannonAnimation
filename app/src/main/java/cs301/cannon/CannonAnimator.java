package cs301.cannon;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.Random;

import static android.graphics.Color.WHITE;
import static java.lang.Math.*;

/**
 * Created by nitzberg16 on 10/30/2015.
 *
 * Color reference: http://www.rapidtables.com/web/color/RGB_Color.htm
 *
 * Instructions:
 *
 * An application that allows the user to shoot a ball out of a cannon at whichever angle
 * the user desires by simply clicking on the screen.
 * When a ball hits one of the targets in the background, an "explosion" appears.
 *
 */
public class CannonAnimator implements Animator {

    //Constant Variables
    //The frame interval of the animation in ms.
    private static final int FrameInterval = 20;
    //Background will be set to blue.
    //private static final int backgroundColor = Color.BLUE;
    //Radius of the ball being shot out.
    private static final int radius = 20;

    //Instance Variables
    protected float angle;
    protected boolean direction;
    protected PointF position;
    protected PointF velocity;
    protected int initialVelocity;
    protected int gravity;
    protected PointF accelerationXY;
    protected int expNumber;
    protected float expX;
    protected float expY;
    private int count;
    private int ballCount;

    //Paint colors for the explosion of the cannon
    private Paint pBlack;
    private Paint pWhite;
    private Paint color1;
    private Paint color2;
    private Paint color3;
    private Paint color4;

    //Target location and colors
    protected PointF targetCenter1;
    protected PointF targetCenter2;
    protected PointF TC1Collision;
    protected PointF TC2Collision;
    private Paint colorT1;
    private Paint colorT2;


    //Constructor for the CannonAnimator class.
    public CannonAnimator() {
        //Initialize tick count variable.
        count = 0;
        ballCount = 0;
        //Initialize the parameters of the cannon.
        angle = 0;
        direction = true;

        //Initialize the parameters of the ball being shot out.
        position = null;
        velocity = null;
        //Need to give the ball some sort of initial velocity in order to work.
        initialVelocity = 35;

        //Initialize the parameters for the gravitational forces.
        accelerationXY = new PointF(0, 1);
        //Need to start the ball with an initial gravitational force 10m/s^2.
        gravity = 1;

        //No explosions have occurred yet, so the explosion counter is set to zero.
        expNumber = 0;

        //Initialize the colors used for the explosion.
        pBlack = new Paint();
        pBlack.setColor(Color.BLACK);
        pWhite = new Paint();
        pWhite.setColor(Color.WHITE);
        color1 = new Paint();
        color1.setColor(Color.rgb(233, 40, 40));
        color2 = new Paint();
        color2.setColor(Color.rgb(255, 128, 0));
        color3 = new Paint();
        color3.setColor(Color.rgb(255, 255, 0));
        color4 = new Paint();
        color4.setColor(Color.rgb(102, 0, 0));

        //Initialize colors used for targets
        colorT1 = new Paint();
        colorT1.setColor(Color.rgb(240, 0, 204));
        colorT2 = new Paint();
        colorT2.setColor(Color.rgb(76, 0, 153));

        //Initialize targets
        Random rand = new Random();
        Random rand2 = new Random();
        targetCenter1 = new PointF(rand.nextInt(100), rand.nextInt(100));
        targetCenter2 = new PointF(rand2.nextInt(100), rand.nextInt(100));
    }


    @Override
    //This is the interval between the different frames in ms
    public int interval()

    {
        return 30;
    }

    @Override
    //This is the set background color
    public int backgroundColor() {
        return Color.rgb(180, 200, 255);
    }

    @Override
    //Never stop the animator.
    public boolean doPause() {
        return false;
    }

    @Override
    //Method that quits the animation.
    //Indicates that the animation is never stopped.
    public boolean doQuit() {
        return false;
    }

    @Override
    //Action that will be performed on tick.
    public void tick(Canvas canvas) {
        //Increment the number of ticks.
        count = count + 1;

        //Obtain the height and width of the canvas.
        int height = canvas.getHeight();
        int width = canvas.getWidth();

        //Draw the Cannon and save the frame
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(angle);
        Rect cannon = new Rect(0, 0, 100, 40);
        canvas.drawRect(cannon, pBlack);
        canvas.restore();

        //Cannon should rotate for shooting
        if ((direction && angle == 85) || (!direction && angle == 0)) {
            direction = !(direction);
        }
        if (direction) {
            angle = angle + 1;
        } else {
            angle = angle - 1;
        }

        //Draw the two targets
        canvas.drawCircle(((targetCenter1.x) * width / 100), ((targetCenter1.y) * height / 100), 80, colorT1);
        canvas.drawCircle(((targetCenter2.x) * width / 100), ((targetCenter2.y) * height / 100), 80, colorT2);
        TC1Collision = new PointF(((targetCenter1.x) * width / 100), ((targetCenter1.y) * height / 100));
        TC2Collision = new PointF(((targetCenter2.x) * width / 100), ((targetCenter2.y) * height / 100));

        //If there is an explosion, paint it.
        //Explosions consist of various amounts of different colored circles.
        if (expNumber > 0) {
            if (expNumber > 10) {
                canvas.drawCircle(expX, expY, 5, color1);
            } else if (expNumber > 8) {
                canvas.drawCircle(expX, expY, 10, color2);
            } else if (expNumber > 6) {
                canvas.drawCircle(expX, expY, 15, color3);
            } else {
                canvas.drawCircle(expX, expY, 20, color4);
            }

            expNumber = expNumber - 1;

        }

        //Update the position of the ball at each frame and draw it on the canvas.
        if (!(position == null)) {
            ballCount++;
           // velocity = new PointF((initialVelocity * ((float) cos(Math.toRadians(angle)))), (initialVelocity * ((float) sin(Math.toRadians(angle)))));
            position = new PointF((position.x + velocity.x), (float) (position.y + velocity.y));
            velocity = new PointF(velocity.x, velocity.y + gravity);
            canvas.drawCircle(position.x, position.y, radius, pWhite);

            if (position.x + radius < 0 || position.y + radius < 0 || position.y > height || position.x > width || Collision()) {
                position = null;
                ballCount = 0;
            }

        }

    }

    @Override
    //Method that gets called whenever the surface is touched
    //pressed, or clicked on.
    public void onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (position == null) {

                ballCount = 0;
                position = new PointF((float) (cos((Math.toRadians(angle)) - 10)), (float) (sin((Math.toRadians(angle)) + 10 )));
                velocity = new PointF((initialVelocity * (float) cos(Math.toRadians(angle))), initialVelocity * (float) sin(Math.toRadians(angle)));

            }
        }
    }
    public boolean Collision()
    {
        if((position.x + radius) > (TC1Collision.x - 40) && (position.x - radius) < (TC1Collision.x + 40)
                && (position.y + radius) > (TC1Collision.y - 40) && (position.y -radius) < (TC1Collision.y + 40))
        {
            expNumber = 20;
            return true;
        }
        else
        {
            return false;
        }
    }
}
