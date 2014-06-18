package com.gesturemaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.gesture.GestureOverlayView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GestureMaster extends Activity implements SensorEventListener{

	GestureOverlayView gesture;
	SensorManager manager;
	Sensor compass;
	TextView north;
	float value;
	
	private ImageView compassImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_master);
		
		Button clearButton = (Button) findViewById(R.id.clear);
		Button getAngleButton = (Button) findViewById(R.id.getangle);
		gesture = (GestureOverlayView) findViewById(R.id.drawing);
		
		compassImage = (ImageView) findViewById(R.id.compassImage);

		
		getAngleButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				try
				{
				
					float x = gesture.getGesture().getBoundingBox().centerX();
					float y = gesture.getGesture().getBoundingBox().centerY();
					System.out.println("The drawn angle is : " + Math.atan(y/x));
					
					double angleDifference = Math.abs(value - Math.atan(y/x));
					
					System.out.println("Therefore our difference in angle issss : " + angleDifference);
					
					//launch the popup that tells the stats and such
					
					LayoutInflater li = LayoutInflater.from(arg0.getContext());
	 				View promptsView = li.inflate(R.layout.informationpopup, null);
	 				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					arg0.getContext());	 				
	 				
					// set prompts.xml to alertdialog builder
					alertDialogBuilder.setView(promptsView);
					
					// create alert dialog
					final AlertDialog alertDialog = alertDialogBuilder.create();
					
					// show it
					alertDialog.show(); 
					
					TextView information = (TextView) alertDialog.findViewById(R.id.information);
					information.setText("The difference between your current heading and swipe is : " + angleDifference+" degrees");
					
					Button ok = (Button) alertDialog.findViewById(R.id.ok);
					
					ok.setOnClickListener(new OnClickListener(){
	
						@Override
						public void onClick(View arg0) {
							alertDialog.dismiss();
						}	
					});
				}
			catch(Exception e)
			{
				Toast toast = Toast.makeText(getApplicationContext(), "Please draw a line before trying to get an angle", Toast.LENGTH_SHORT);
	      	 	toast.show();
			}
			}
		});
		
		
		clearButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gesture.cancelClearAnimation();
				gesture.clear(true);
			}
		});
		
		//north = (TextView) findViewById(R.id.north);
		
		manager = (SensorManager)getSystemService(SENSOR_SERVICE);
	    compass = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
      
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gesture_master, menu);
		return true;
	}




	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		value = Math.round(event.values[0]);
		//north.setText(Float.toString(value));
		
		RotateAnimation ra = new RotateAnimation(value,value,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		ra.setDuration(200);
		ra.setFillAfter(true);
		compassImage.startAnimation(ra);
		
		
	}
	protected void onResume() {
	    super.onResume();
	    manager.registerListener(this, compass, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onPause() {
	    // Unregister the listener on the onPause() event to preserve battery life;
	    super.onPause();
	    manager.unregisterListener(this);
	}
	
}
