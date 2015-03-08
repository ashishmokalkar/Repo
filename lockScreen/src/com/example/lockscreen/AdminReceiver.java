package com.example.lockscreen;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class AdminReceiver extends DeviceAdminReceiver {
	
	static CameraActivity camAct;
	
	int failedLoginCnt = 0;
  @Override
  public void onEnabled(Context ctxt, Intent intent) {
    ComponentName cn=new ComponentName(ctxt, AdminReceiver.class);
    DevicePolicyManager mgr=
        (DevicePolicyManager)ctxt.getSystemService(Context.DEVICE_POLICY_SERVICE);

    mgr.setPasswordQuality(cn,
                           DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC);
    onPasswordChanged(ctxt, intent);
  }

  @Override
  public void onPasswordChanged(Context ctxt, Intent intent) {
    DevicePolicyManager mgr=
        (DevicePolicyManager)ctxt.getSystemService(Context.DEVICE_POLICY_SERVICE);
    int msgId;
    if (mgr.isActivePasswordSufficient()) {
      msgId=R.string.compliant;
    }
    else {
      msgId=R.string.not_compliant;
    }

    Toast.makeText(ctxt, msgId, Toast.LENGTH_LONG).show();
  }

  @Override
  public void onPasswordFailed(Context ctxt, Intent intent) 
  {
	  SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctxt);
	  int cnt = sp.getInt("failedLoginCnt", 0);
	  cnt++;
	  
	  Editor ed = sp.edit();
      ed.putInt("failedLoginCnt", cnt);
      ed.commit();
      
      Intent i = new Intent(ctxt.getApplicationContext(), CameraActivity.class);
      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      ctxt.getApplicationContext().startActivity(i);
      
	  //failedLoginCnt++;
	  Log.e("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa", cnt +"failed attempts.... ");
    Toast.makeText(ctxt,failedLoginCnt +"failed attempts.... ", Toast.LENGTH_LONG)
         .show();
  }
  @Override
  public void onPasswordSucceeded(Context ctxt, Intent intent) 
  {
	  SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctxt);
	  Editor ed = sp.edit();
      ed.putInt("failedLoginCnt", 0);
      ed.commit();
      /*camAct = new CameraActivity();
      camAct.finish();*/
      /*
      Intent i = new Intent(ctxt, LCam.class);
      ctxt.startActivity(i);*/
      
     /* LCam lcam = new LCam();
      lcam.onCreate(new Bundle());*/
	  Log.e("zzzzzzzzzzzzzzzzzzzzzz", "Congratssssssssssssssssssssss ");
    Toast.makeText(ctxt, R.string.password_success, Toast.LENGTH_LONG)
         .show();
  }
}