package com.gesturemaker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class Compas implements SensorEventListener{

	
	TextView updateView;
	
	public Compas(TextView updateThis)
	{
		updateView = updateThis;
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		
		
		double current = Math.round(arg0.values[0]);
		
		updateView.setText(current+"");
		if(arg0.values[0] >= 0 && arg0.values[0] <= 90)
		{
			System.out.println("north");
		}
		else if(arg0.values[0] >=90 && arg0.values[0] <= 179)
		{
			System.out.println("East");
		}
		else if(arg0.values[0] >=180 && arg0.values[0] <= 269)
		{
			System.out.println("South");
		}
		else
		{
			System.out.println("West");
		}
	}

}
