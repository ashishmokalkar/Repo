package com.example.lockscreen;

//import com.example.demo_app.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Start1 extends Activity implements AnimationListener{
	Animation rotate_anim;
	Animation decelerate_anim;
	Button b;
	
	private DevicePolicyManager mDevicePolicyManager;
	
	private static final int ADMIN_INTENT = 15;
	  private static final String description = "Lock Screen permission";
	  //private DevicePolicyManager mDevicePolicyManager; 
	  private ComponentName mComponentName;
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start1);
		
//final ComponentName cn = new ComponentName(getBaseContext(), AdminReceiver.class);
		
		/*pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
		*/
		
		/*mDevicePolicyManager = (DevicePolicyManager)getSystemService(  
              Context.DEVICE_POLICY_SERVICE);  */

    mComponentName = new ComponentName(this, MyAdminReceiver.class);
    
    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,description);
    startActivityForResult(intent, ADMIN_INTENT);
    
    
    
    ComponentName cn = new ComponentName(getBaseContext(), AdminReceiver.class);
	
	Intent wrg_intent=
	          new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
	wrg_intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
	wrg_intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
	                      getString(R.string.device_admin_explanation));
	    startActivity(wrg_intent);
		
		
		b = (Button) findViewById(R.id.button1);
		
		rotate_anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
		
		//decelerate_anim = AnimationUtils.loadAnimation(getApplicationContext(),
        //        R.anim.decelerate);
         
        // set animation listener
		rotate_anim.setAnimationListener(this);
        
        b.startAnimation(rotate_anim);
        
        TextView tv1 = (TextView) findViewById(R.id.textView1);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "adrip1.ttf");
		
		tv1.setTypeface(font);
        
        Button b2 = (Button) findViewById(R.id.button2);
        
        b2.setOnClickListener(new OnClickListener()
        {
			
			@Override
			public void onClick(View v) {
				Intent int2 = new Intent(Start1.this, Start2.class);
				int2.putExtra("past_Acti","0");
				Bundle bndlanimation = 
						ActivityOptions.makeCustomAnimation(getApplicationContext(), 
								R.anim.start1,R.anim.end1).toBundle();
				startActivity(int2,bndlanimation);
				Start1.this.finish();
				
				// TODO Auto-generated method stub
				
			}
		});
        //b.startAnimation(decelerate_anim);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		b.setBackgroundDrawable( getResources().getDrawable(R.drawable.logo_1) );
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	 @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	  {
	      if (requestCode == ADMIN_INTENT)
	      {
	          if (resultCode == RESULT_OK) 
	          {
	              Toast.makeText(getApplicationContext(), "Registered As Admin", Toast.LENGTH_SHORT).show();
	          }else
	          {
	              Toast.makeText(getApplicationContext(), "Failed to register as Admin", Toast.LENGTH_SHORT).show();
	          }
	      }
	  }
	 @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
	}
}
