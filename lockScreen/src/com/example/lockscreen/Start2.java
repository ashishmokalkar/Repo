package com.example.lockscreen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class Start2 extends Activity
{
	ImageView iv1;
	ImageView iv2;
	ImageView iv3;
	
	public static final int PICK_CONTACT_1    = 1;
	public static final int PICK_CONTACT_2    = 2;
	public static final int PICK_CONTACT_3    = 3;
	
	SharedPreferences Contact_Sett_Pref;
	Editor edit_Contact_Sett_Pref;
	
	TextView tv1;
	TextView tv2;
	TextView tv3;
	
	Button b1;
	Button b2;
	SharedPreferences st2_num_sp;
	
	//SharedPreferences pref;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start2);
		
		/*pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
		Editor ed = pref.edit();
        ed.putBoolean("activity_executed", true);
        ed.commit();*/		
		/*st2sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		final Editor edit = st2sharedPref.edit();*/
		
		/*SharedPreferences */Contact_Sett_Pref = getSharedPreferences("Contact_Sett_Pref", Context.MODE_PRIVATE);
	    /*final Editor*/edit_Contact_Sett_Pref = Contact_Sett_Pref.edit();
		
		//final String past_Acti = getIntent().getExtras().getString("past_Acti");
		/*
		String enum1_name = Contact_Sett_Pref.getString("enum1_name", "1st e.no");
		String enum2_name = Contact_Sett_Pref.getString("enum2_name", "2nd e.no");
		String enum3_name = Contact_Sett_Pref.getString("enum3_name", "3rd e.no");
		*/
		
		
		TextView tv = (TextView) findViewById(R.id.textView1);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "Smush_demo.otf");
		
		tv.setTypeface(font);
		
		tv1 = (TextView) findViewById(R.id.textView4);
		tv2 = (TextView) findViewById(R.id.textView5);
		tv3 = (TextView) findViewById(R.id.textView6);
		/*
		tv1.setText(enum1_name);
		tv2.setText(enum2_name);
		tv3.setText(enum3_name);*/
		
		iv1 = (ImageView) findViewById(R.id.imageView1);
		iv2 = (ImageView) findViewById(R.id.imageView2);
		iv3 = (ImageView) findViewById(R.id.imageView3);
		
		final Animation animAnti = AnimationUtils.loadAnimation(this, R.anim.anti_overshoot);
		final Animation animAnti2 = AnimationUtils.loadAnimation(this, R.anim.anim2);
		
		iv1.setAnimation(animAnti2);
		iv2.setAnimation(animAnti);
		iv3.setAnimation(animAnti);
		
		tv1.setAnimation(animAnti2);
		tv2.setAnimation(animAnti);
		tv3.setAnimation(animAnti);
		
		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.button2);
		//iv.setAnimation(animAnti);
		
		b1.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				Intent prevIntent = new Intent(Start2.this, Start1.class);

				Bundle bndlanimation = 
						ActivityOptions.makeCustomAnimation(getApplicationContext(), 
								R.anim.startsliding,R.anim.endsliding).toBundle();
				startActivity(prevIntent,bndlanimation);
				Start2.this.finish();
				// TODO Auto-generated method stub
				
			}
		});
		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
								
				if(tv1.getText().toString().equals("1st e.no")  || tv2.getText().toString().equals("2nd e.no") || tv3.getText().toString().equals("3rd e.no"))
				{
					
					Toast.makeText(getBaseContext(),"Please select all three contacts",Toast.LENGTH_LONG).show();
				/*Intent nextIntent = new Intent(Start2.this, Start3email_activity.class);
				
				Bundle bndlanimation = 
						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.start1,R.anim.end1).toBundle();

				startActivity(nextIntent,bndlanimation);
				Start2.this.finish();*/
				
				}
				else
				{
					Intent nextIntent = new Intent(Start2.this, /*Start3*/email_activity.class);
					
					Bundle bndlanimation = 
							ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.start1,R.anim.end1).toBundle();

					startActivity(nextIntent,bndlanimation);
					Start2.this.finish();
					//Toast.makeText(getBaseContext(),"Please select all three contacts",Toast.LENGTH_LONG).show();
				}
				// TODO Auto-generated method stub
				//}
			}
		});
		
		
		iv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				
				//Intent intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT_1);
				// TODO Auto-generated method stub
				
			}
		});
		
		iv2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				//Intent intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT_2);
				// TODO Auto-generated method stub
				
			}
		});

		iv3.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {

		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		//Intent intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT_3);
		// TODO Auto-generated method stub
		
	}
});
	}
	
	public void onActivityResult(int reqCode, int resultCode, Intent data) 
	{
		
		/*st2_num_sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		final Editor edit = st2_num_sp.edit();*/
		
/*
	    SharedPreferences Contact_Sett_Pref = getSharedPreferences("Contact_Sett_Pref", Context.MODE_PRIVATE);
	    final Editor edit_Contact_Sett_Pref = Contact_Sett_Pref.edit();*/

		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) 
		{

		case (PICK_CONTACT_1) :

		if (resultCode == Activity.RESULT_OK) 
		{

		Uri contactData = data.getData();
		                Cursor contactCursor = getContentResolver().query(contactData,
		                                new String[] { ContactsContract.Contacts._ID }, null, null,
		                                null);
		                String id = null;
		                if (contactCursor.moveToFirst()) {
		                        id = contactCursor.getString(contactCursor
		                                        .getColumnIndex(ContactsContract.Contacts._ID));
		                }
		                contactCursor.close();
		                String phoneName = null;
		                String phoneNumber1 = null;
		                
		                Cursor c = managedQuery(contactData, null, null, null, null);
		                
		                Cursor phoneCursor = getContentResolver().query(
		                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
		                                new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
		                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ? ",
		                                new String[] { id }, null);
		                if (c.moveToFirst()) {

		                	phoneName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		                	tv1.setText(phoneName);
		                	edit_Contact_Sett_Pref.putString("enum1_name", phoneName);
	                        edit_Contact_Sett_Pref.commit();
		                	Log.e("name", phoneName);
		                	// TODO Whatever you want to do with the selected contact name.

		                	}
		                
		                if (phoneCursor.moveToFirst()) {
		                	
		                	//phoneName = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		                	//phoneName = c.getString(phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		                
		                        phoneNumber1 = phoneCursor
		                                        .getString(phoneCursor
		                                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		                        /*
		                        st2_num_sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        		final Editor edit = st2_num_sp.edit();
                        		*/
		                        edit_Contact_Sett_Pref.putString("enum1", phoneNumber1);
		                        edit_Contact_Sett_Pref.commit();
		                        Log.e("number",phoneNumber1);
		                        //Log.e("number",phoneName);
		                }
		                phoneCursor.close();

		break;
		}

		case (PICK_CONTACT_2) :

			if (resultCode == Activity.RESULT_OK) 
			{

			Uri contactData = data.getData();
			                Cursor contactCursor = getContentResolver().query(contactData,
			                                new String[] { ContactsContract.Contacts._ID }, null, null,
			                                null);
			                String id = null;
			                if (contactCursor.moveToFirst()) {
			                        id = contactCursor.getString(contactCursor
			                                        .getColumnIndex(ContactsContract.Contacts._ID));
			                }
			                contactCursor.close();
			                String phoneName = null;
			                String phoneNumber = null;
			                
			                Cursor c = managedQuery(contactData, null, null, null, null);
			                
			                Cursor phoneCursor = getContentResolver().query(
			                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			                                new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
			                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ? ",
			                                new String[] { id }, null);
			                if (c.moveToFirst()) {

			                	phoneName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			                	tv2.setText(phoneName);
			                	edit_Contact_Sett_Pref.putString("enum2_name", phoneName);
		                        edit_Contact_Sett_Pref.commit();
			                	Log.e("name", phoneName);
			                	// TODO Whatever you want to do with the selected contact name.

			                	}
			                
			                if (phoneCursor.moveToFirst()) {
			                	
			                	//phoneName = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			                	//phoneName = c.getString(phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			                
			                        phoneNumber = phoneCursor
			                                        .getString(phoneCursor
			                                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			                        /*
			                        st2_num_sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	                        		final Editor edit = st2_num2_sp.edit();*/
	                        		
			                        edit_Contact_Sett_Pref.putString("enum2", phoneNumber);
			                        edit_Contact_Sett_Pref.commit();
			                        Log.e("number",phoneNumber);
			                        //Log.e("number",phoneName);
			                }
			                phoneCursor.close();

			break;
			}
		
		case (PICK_CONTACT_3) :

			if (resultCode == Activity.RESULT_OK) 
			{

			Uri contactData = data.getData();
			                Cursor contactCursor = getContentResolver().query(contactData,
			                                new String[] { ContactsContract.Contacts._ID }, null, null,
			                                null);
			                String id = null;
			                if (contactCursor.moveToFirst()) {
			                        id = contactCursor.getString(contactCursor
			                                        .getColumnIndex(ContactsContract.Contacts._ID));
			                }
			                contactCursor.close();
			                String phoneName = null;
			                String phoneNumber = null;
			                
			                Cursor c = managedQuery(contactData, null, null, null, null);
			                
			                Cursor phoneCursor = getContentResolver().query(
			                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			                                new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
			                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ? ",
			                                new String[] { id }, null);
			                if (c.moveToFirst()) {

			                	phoneName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			                	tv3.setText(phoneName);
			                	edit_Contact_Sett_Pref.putString("enum3_name", phoneName);
		                        edit_Contact_Sett_Pref.commit();
			                	Log.e("name", phoneName);
			                	// TODO Whatever you want to do with the selected contact name.

			                	}
			                
			                if (phoneCursor.moveToFirst()) {
			                	
			                	//phoneName = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			                	//phoneName = c.getString(phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			                
			                        phoneNumber = phoneCursor
			                                        .getString(phoneCursor
			                                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			                        /*
			                        st2_num_sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	                        		final Editor edit = st2_num_sp.edit();*/
	                        		
			                        edit_Contact_Sett_Pref.putString("enum3", phoneNumber);
			                        edit_Contact_Sett_Pref.commit();
			                        Log.e("number",phoneNumber);
			                        //Log.e("number",phoneName);
			                }
			                phoneCursor.close();

			break;
			}
		}
		}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent prevIntent = new Intent(Start2.this, Start1.class);

		Bundle bndlanimation = 
				ActivityOptions.makeCustomAnimation(getApplicationContext(), 
						R.anim.startsliding,R.anim.endsliding).toBundle();
		startActivity(prevIntent,bndlanimation);
		Start2.this.finish();
	}
}
