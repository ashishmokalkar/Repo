package com.example.lockscreen;

//import com.example.demo_app.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		TextView tv1 = (TextView) findViewById(R.id.textView1);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "adrip1.ttf");
		
		tv1.setTypeface(font);
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		Intent prevIntent = new Intent(About.this, MainActivity.class);

		Bundle bndlanimation = 
				ActivityOptions.makeCustomAnimation(getApplicationContext(), 
						R.anim.startsliding,R.anim.endsliding).toBundle();
		startActivity(prevIntent,bndlanimation);
		About.this.finish();
	}
}
