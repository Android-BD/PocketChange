package com.example.proxsensortest;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Simple app to adjust phone volume when proximity sensor is engaged/disengaged. The main
 * idea is the phone will be louder when in a pocket or purse.
 * @author Justin Doyle
 * @version 0.1
 * @
 *
 */
public class SensorActivity extends Activity implements SensorEventListener {
	// Layout containers
	private LinearLayout linLayMainContainer;
	private RelativeLayout relLayRinger;
	private RelativeLayout relLayNotification;

	// Ringer card variables
	private TextView tvMaxRinger;
	private TextView tvMinRinger;
	private TextView tvRingerTitle;
	private ImageView ivMaxRinger;
	private ImageView ivMinRinger;
	private Switch ringerSwitch;
	private SeekBar maxRingerSeekBar;
	private SeekBar minRingerSeekBar;

	// Notification card variables
	private TextView tvMaxNotification;
	private TextView tvMinNotification;
	private TextView tvNotificationTitle;
	private ImageView ivMaxNotification;
	private ImageView ivMinNotification;
	private Switch notificationSwitch;
	private SeekBar maxNotificationSeekBar;
	private SeekBar minNotificationSeekBar;


	// Sensor variables
	private SensorManager sensorManager;
	private Sensor sensor;
	ImageView imageView1;

	// Audio variables
	private AudioManager audioManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);

		audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
		additionalUISetup();

		// Sensor setup
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sensor, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO nothing to add here
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// In pocket
		if(event.values[0] == 0)
		{
			if(ringerSwitch.isChecked())
				audioManager.setStreamVolume(AudioManager.STREAM_RING, maxRingerSeekBar.getProgress(),
						AudioManager.FLAG_VIBRATE);

			if(notificationSwitch.isChecked())
				audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, maxNotificationSeekBar.getProgress(),
						AudioManager.FLAG_VIBRATE);
		}
		// Out of pocket
		else
		{
			if(ringerSwitch.isChecked())
				audioManager.setStreamVolume(AudioManager.STREAM_RING, minRingerSeekBar.getProgress(), 
						AudioManager.FLAG_VIBRATE);
			if(notificationSwitch.isChecked())
				audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, maxNotificationSeekBar.getProgress(),
						AudioManager.FLAG_VIBRATE);
		}

	}

	private void additionalUISetup()
	{
		// Get Layout containers
		linLayMainContainer = (LinearLayout)findViewById(R.id.main_container);
		relLayRinger = (RelativeLayout)findViewById(R.id.ringer_card);
		relLayNotification = (RelativeLayout)findViewById(R.id.notification_card);

		// Set up layout container transitions
		LayoutTransition transition = linLayMainContainer.getLayoutTransition();
		transition.enableTransitionType(LayoutTransition.CHANGING);

		// Get Ringer views
		tvRingerTitle = (TextView)findViewById(R.id.ringer_title);
		tvMaxRinger = (TextView)findViewById(R.id.ringer_max_label);
		tvMinRinger = (TextView)findViewById(R.id.ringer_min_label);
		ivMaxRinger = (ImageView)findViewById(R.id.ringer_icon_max);
		ivMinRinger = (ImageView)findViewById(R.id.ringer_icon_min);
		maxRingerSeekBar = (SeekBar)findViewById(R.id.ringer_seekbar_max);
		minRingerSeekBar = (SeekBar)findViewById(R.id.ringer_seekbar_min);
		ringerSwitch = (Switch)findViewById(R.id.ringer_switch);
		ringerSwitch.setChecked(false);
		ringerSwitchCheckedChanged(ringerSwitch, false);


		tvNotificationTitle = (TextView)findViewById(R.id.notification_title);
		tvMaxNotification = (TextView)findViewById(R.id.notification_max_label);
		tvMinNotification = (TextView)findViewById(R.id.notification_min_label);
		ivMaxNotification = (ImageView)findViewById(R.id.notification_icon_max);
		ivMinNotification = (ImageView)findViewById(R.id.notification_icon_min);
		maxNotificationSeekBar = (SeekBar)findViewById(R.id.notification_seekbar_max);
		minNotificationSeekBar = (SeekBar)findViewById(R.id.notification_seekbar_min);
		notificationSwitch = (Switch)findViewById(R.id.notification_switch);
		notificationSwitch.setChecked(false);
		notificationSwitchCheckedChanged(notificationSwitch, false);

		maxRingerSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
		minRingerSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
		maxNotificationSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
		minNotificationSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
		maxRingerSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_RING));
		maxNotificationSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
		setUpSeekBarListeners();

		// Set the appropriate volume icon on initialization
		ringerMaxSeekBarProgressChanged(maxRingerSeekBar,
				maxRingerSeekBar.getProgress(), false);
		ringerMinSeekBarProgressChanged(minRingerSeekBar,
				minRingerSeekBar.getProgress(), false);
		notificationMaxSeekBarProgressChanged(maxNotificationSeekBar,
				maxNotificationSeekBar.getProgress(), false);
		notificationMinSeekBarProgressChanged(minNotificationSeekBar,
				minNotificationSeekBar.getProgress(), false);

		// Set up switch listeners
		ringerSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ringerSwitchCheckedChanged(buttonView, isChecked);	
			}
		});
		notificationSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				notificationSwitchCheckedChanged(buttonView, isChecked);	
			}
		});


	}

	private void setUpSeekBarListeners()
	{
		maxRingerSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// Do nothing
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Do nothing
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				ringerMaxSeekBarProgressChanged(seekBar, progress, fromUser);
			}
		});

		minRingerSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {/*Do nothing*/}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {/*Do nothing*/}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) 
			{
				ringerMinSeekBarProgressChanged(seekBar, progress, fromUser);
			}
		});

		maxNotificationSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// Do nothing
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Do nothing
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				notificationMaxSeekBarProgressChanged(seekBar, progress, fromUser);
			}
		});

		minNotificationSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {/*Do nothing*/}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {/*Do nothing*/}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) 
			{
				notificationMinSeekBarProgressChanged(seekBar, progress, fromUser);
			}
		});
	}
	
	/*###############################################################################################
	 * 
	 * LISTENER TRIGGER METHODS
	 * 
	 ################################################################################################*/

	private void ringerMaxSeekBarProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	{
		if(progress == 0)
			ivMaxRinger.setImageResource(R.drawable.ic_action_volume_muted);
		else
			ivMaxRinger.setImageResource(R.drawable.ic_action_volume_on);
	}

	private void ringerMinSeekBarProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	{
		if(progress == 0)
			ivMinRinger.setImageResource(R.drawable.ic_action_volume_muted);
		else
			ivMinRinger.setImageResource(R.drawable.ic_action_volume_on);
	}

	private void notificationMaxSeekBarProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	{
		if(progress == 0)
			ivMaxNotification.setImageResource(R.drawable.ic_action_volume_muted);
		else
			ivMaxNotification.setImageResource(R.drawable.ic_action_volume_on);
	}

	private void notificationMinSeekBarProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	{
		if(progress == 0)
			ivMinNotification.setImageResource(R.drawable.ic_action_volume_muted);
		else
			ivMinNotification.setImageResource(R.drawable.ic_action_volume_on);
	}

	private void ringerSwitchCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		if(isChecked)
		{
			tvMaxRinger.setVisibility(View.VISIBLE);
			tvMinRinger.setVisibility(View.VISIBLE);
			ivMaxRinger.setVisibility(View.VISIBLE);
			ivMinRinger.setVisibility(View.VISIBLE);
			maxRingerSeekBar.setVisibility(View.VISIBLE);
			minRingerSeekBar.setVisibility(View.VISIBLE);
			tvRingerTitle.setTextColor(Color.BLACK);
		}
		else
		{
			tvMaxRinger.setVisibility(View.GONE);
			tvMinRinger.setVisibility(View.GONE);
			ivMaxRinger.setVisibility(View.GONE);
			ivMinRinger.setVisibility(View.GONE);
			maxRingerSeekBar.setVisibility(View.GONE);
			minRingerSeekBar.setVisibility(View.GONE);
			tvRingerTitle.setTextColor(Color.LTGRAY);
		}
	}

	private void notificationSwitchCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		if(isChecked)
		{
			tvMaxNotification.setVisibility(View.VISIBLE);
			tvMinNotification.setVisibility(View.VISIBLE);
			ivMaxNotification.setVisibility(View.VISIBLE);
			ivMinNotification.setVisibility(View.VISIBLE);
			maxNotificationSeekBar.setVisibility(View.VISIBLE);
			minNotificationSeekBar.setVisibility(View.VISIBLE);
			tvNotificationTitle.setTextColor(Color.BLACK);
		}
		else
		{
			tvMaxNotification.setVisibility(View.GONE);
			tvMinNotification.setVisibility(View.GONE);
			ivMaxNotification.setVisibility(View.GONE);
			ivMinNotification.setVisibility(View.GONE);
			maxNotificationSeekBar.setVisibility(View.GONE);
			minNotificationSeekBar.setVisibility(View.GONE);
			tvNotificationTitle.setTextColor(Color.LTGRAY);
		}
	}

}
