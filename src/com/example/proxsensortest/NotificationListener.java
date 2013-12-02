package com.example.proxsensortest;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationListener extends NotificationListenerService
{
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.i("NLService", "NLService created");
	}

	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		Log.i("NLService", "Notification posted");
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		Log.i("NLService", "Notification removed");
		
	}

}
