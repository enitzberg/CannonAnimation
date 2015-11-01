package cs301.cannon;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SeekBar;

/**
 * CannonMainActivity
 * 
 * This is the activity for the cannon animation. It creates a AnimationCanvas
 * containing a particular Animator object
 * 
 * @author Andrew Nuxoll
 * @version September 2012
 *
 * @author G. Emily Nitzberg
 * @version November 2, 2015
 * 
 */
public class CannonMainActivity extends Activity {

	/**
	 * creates an AnimationCanvas containing a TestAnimator.
	 */

	//Instance Variables
	SeekBar Angle;
	CannonAnimator animator;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cannon_main);

		// Create an animation canvas and place it in the main layout
//		Animator testAnim = new TestAnimator();
//		AnimationCanvas myCanvas = new AnimationCanvas(this, testAnim);
//		LinearLayout mainLayout = (LinearLayout) this
//				.findViewById(R.id.topLevelLayout);
//		mainLayout.addView(myCanvas);

		Animator myAnimator = new CannonAnimator();
		AnimationCanvas myCanvas = new AnimationCanvas(this, myAnimator);
		LinearLayout mainLayout = (LinearLayout) this.findViewById(R.id.topLevelLayout);
		mainLayout.addView(myCanvas);

	}

	/**
	 * This is the default behavior (empty menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cannon_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
