package com.example.lockscreen;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class Help extends ActionBarActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.help);
	}
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		Intent prevIntent = new Intent(Help.this, MainActivity.class);

		Bundle bndlanimation = 
				ActivityOptions.makeCustomAnimation(getApplicationContext(), 
						R.anim.startsliding,R.anim.endsliding).toBundle();
		startActivity(prevIntent,bndlanimation);
		Help.this.finish();
	}
}
