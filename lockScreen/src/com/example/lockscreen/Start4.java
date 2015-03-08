package com.example.lockscreen;

//import com.example.lockevents.MyAdminReceiver;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Start4 extends Activity{
	Button b4;
	Button bt_wr_ptrn;
	
	SharedPreferences pref;
	//SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
	
	private static final int ADMIN_INTENT = 15;
  private static final String description = "Some Description About Your Admin";
  private DevicePolicyManager mDevicePolicyManager; 
  private ComponentName mComponentName;  
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start4);
		
		final ComponentName cn = new ComponentName(getBaseContext(), AdminReceiver.class);
		
		pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
		
		
		mDevicePolicyManager = (DevicePolicyManager)getSystemService(  
              Context.DEVICE_POLICY_SERVICE);  
    mComponentName = new ComponentName(this, MyAdminReceiver.class);
    
		Button bt_lock = (Button) findViewById(R.id.button2);
		
		bt_lock.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
	            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
	            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,description);
	            startActivityForResult(intent, ADMIN_INTENT);
				// TODO Auto-generated method stub
				
			}
		});
		
		bt_wr_ptrn = (Button) findViewById(R.id.button1);
		
		bt_wr_ptrn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

			      Intent intent=
			          new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			      intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
			      intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
			                      getString(R.string.device_admin_explanation));
			      startActivity(intent);
				
			}
		});
		
		b4 = (Button) findViewById(R.id.button3);
		
		b4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				

		        Editor ed = pref.edit();
		        ed.putBoolean("activity_executed", true);
		        ed.commit();

				Intent in = new Intent(Start4.this,MainActivity.class);
				startActivity(in);
				// TODO Auto-generated method stub
				
			}
		});
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

}