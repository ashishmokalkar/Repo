package com.example.lockscreen;

//import com.example.media_player.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class PlayAlarm extends Service{
	
	MediaPlayer mp;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		mp=MediaPlayer.create(this,R.raw.abc );
		mp.start();
		return super.onStartCommand(intent, flags, startId);
		
		
	}

}
