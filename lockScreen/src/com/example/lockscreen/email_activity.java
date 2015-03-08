package com.example.lockscreen;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class email_activity extends Activity {
	
	SharedPreferences AudioSettPref ;/*= getSharedPreferences("AudioSettPref", Context.MODE_PRIVATE);*/
	   Editor editAudioSettPref ;
	   
	   SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_activity);
		
		AudioSettPref = getSharedPreferences("AudioSettPref", Context.MODE_PRIVATE);
	    editAudioSettPref = AudioSettPref.edit();
		
		Button bt_next = (Button) findViewById(R.id.button1);
		Button bt_prev = (Button) findViewById(R.id.button2);
		
		final EditText et_own_email_id = (EditText) findViewById(R.id.editText1);
		final EditText et_own_email_pass = (EditText) findViewById(R.id.editText2);
		final EditText et_emer_email_id = (EditText) findViewById(R.id.editText3);
		
		bt_next.setOnClickListener(new OnClickListener() {
			
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onClick(View v) 
			{
				
				
				String own_email_id = et_own_email_id.getText().toString();
				String own_email_pass = et_own_email_pass.getText().toString();
				String emer_email_id = et_emer_email_id.getText().toString();
				
				if(own_email_id.equals("") || own_email_pass.equals("") || emer_email_id.equals(""))
				{
					
					Toast.makeText(getBaseContext(), "Fill all 3 fields", Toast.LENGTH_LONG).show();
					
					
				// TODO Auto-generated method stub
				}
				
				else
				{
					pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
					Editor ed = pref.edit();
			        ed.putBoolean("activity_executed", true);
			        ed.commit();
				
				editAudioSettPref.putString("own_email_id", own_email_id);
				editAudioSettPref.putString("own_email_pass", own_email_pass);
				editAudioSettPref.putString("emer_email_id", emer_email_id);
				
				editAudioSettPref.commit();
				
				Intent nextIntent = new Intent(email_activity.this, /*Start3*/MainActivity.class);
				
				Bundle bndlanimation = 
						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.start1,R.anim.end1).toBundle();

				startActivity(nextIntent,bndlanimation);
				email_activity.this.finish();
					//Toast.makeText(getBaseContext(), "Fill all 3 fields", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		bt_prev.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onClick(View v) {
				Intent prevIntent = new Intent(email_activity.this, Start2.class);

				Bundle bndlanimation = 
						ActivityOptions.makeCustomAnimation(getApplicationContext(), 
								R.anim.startsliding,R.anim.endsliding).toBundle();
				startActivity(prevIntent,bndlanimation);
				email_activity.this.finish();
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent prevIntent = new Intent(email_activity.this, Start2.class);

		Bundle bndlanimation = 
				ActivityOptions.makeCustomAnimation(getApplicationContext(), 
						R.anim.startsliding,R.anim.endsliding).toBundle();
		startActivity(prevIntent,bndlanimation);
		email_activity.this.finish();
	}

}
