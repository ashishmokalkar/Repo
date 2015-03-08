package com.example.lockscreen;

//import com.example.geo2.R;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

//import com.example.audiorec.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//import com.example.lockevents.MyAdminReceiver;

public class MainActivity extends ActionBarActivity implements LocationListener,AnimationListener, OnItemClickListener
{
	
	DrawerLayout drawerLayout; 
	 ListView listView;
	 String mActivityTitle;
	 int selectedposition;
	 public MyAdapter myAdapter;
	 
	 @SuppressWarnings("deprecation")
		ActionBarDrawerToggle mDrawerToggle;
	
	private DevicePolicyManager mDevicePolicyManager; 
    private ComponentName mComponentName;  
	
	EditText et_dia_email_1;
	EditText et_dia_email_2;
	EditText et_dia_email_3;
	
	private MediaRecorder myAudioRecorder;
	   private String outputFile = null;
	   long finaltime; 
	   
	   Session session = null;
	   
	   SharedPreferences AudioSettPref ;/*= getSharedPreferences("AudioSettPref", Context.MODE_PRIVATE);*/
	   Editor editAudioSettPref ;/*= AudioSettPref.edit();*/
	   
	   SharedPreferences Contact_Sett_Pref ;/*= getSharedPreferences("Contact_Sett_Pref", Context.MODE_PRIVATE);*/
	   Editor edit_Contact_Sett_Pref ;/*= Contact_Sett_Pref.edit();*/
	
    LocationManager locationManager ;
    String provider;
	
	AlertDialog levelDialog;
	int button_loc_open = 0;
	int button_audio_open = 0;
	//RadioGroup radioGroup;
	/*//ToggleButton tButton;
	Switch sw1;
	Switch sw2;
	Switch sw3;*/
	
  @SuppressWarnings("deprecation")
@Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    String loc;
/*
    //final SharedPreferences */AudioSettPref = getSharedPreferences("AudioSettPref", Context.MODE_PRIVATE);
    editAudioSettPref = AudioSettPref.edit();
    
    SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
    /*
    final SharedPreferences */Contact_Sett_Pref = getSharedPreferences("Contact_Sett_Pref", Context.MODE_PRIVATE);
    edit_Contact_Sett_Pref = Contact_Sett_Pref.edit();
    
    //===================================Location====================================================================
    
    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    
    // Creating an empty criteria object
    Criteria criteria = new Criteria();

    // Getting the name of the provider that meets the criteria
    provider = locationManager.getBestProvider(criteria, false);

    if(provider!=null && !provider.equals("")){

        // Get the location from the given provider
        Location location = locationManager.getLastKnownLocation(provider);

        locationManager.requestLocationUpdates(provider, 20000, 1, this);

        if(location!=null)
             onLocationChanged(location);
        else
            Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
        
    }
    else
    {
        Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
    }
    
    //======================================================================================================
    
	
    if(pref.getBoolean("activity_executed", false))
    {
        /*Intent intent = new Intent(this, Help.class);
        startActivity(intent);*/
        //finish();
    	
    	DisplayMetrics displaymetrics = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
    	int height = displaymetrics.heightPixels;
    	int width = displaymetrics.widthPixels;
    	
    	setContentView(R.layout.main_);
    	
    	drawerLayout=(DrawerLayout) findViewById(R.id.drawerLayout);
    	listView =(ListView) findViewById(R.id.drawerList);
    	
    	myAdapter =new MyAdapter(this);
    	listView.setAdapter(myAdapter);
    	
    	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    	getSupportActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(
        		this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open , /* "open drawer" description */
                R.string.drawer_close){
			
			 /** Called when a drawer has settled in a completely open state. */
			 @SuppressLint("NewApi") public void onDrawerOpened(View drawerView) 
			 	{
				 	super.onDrawerOpened(drawerView);
				 	getSupportActionBar().setTitle("Navigation!");
				    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			    }
			 @SuppressLint("NewApi") public void onDrawerClosed(View view)
			    {
			    		super.onDrawerClosed(view);
			    		getSupportActionBar().setTitle(mActivityTitle);
			    	    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			    }
			    
			    private void setupDrawer() {
			    	
			    	mDrawerToggle.setDrawerIndicatorEnabled(true);
			    	drawerLayout.setDrawerListener(mDrawerToggle);
			    }
		
		};
		
		listView.setOnItemClickListener(this);
    	
    	/*final ComponentName cn = new ComponentName(getBaseContext(), AdminReceiver.class);
    	
    	Intent intent=
		          new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		      intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
		      intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
		                      getString(R.string.device_admin_explanation));
		      startActivity(intent);*/
    	
    	//===============================Permissions==============================================
    	
    	mDevicePolicyManager = (DevicePolicyManager)getSystemService(  
                Context.DEVICE_POLICY_SERVICE);  
      mComponentName = new ComponentName(this, MyAdminReceiver.class);  
    	//==============================permission end===========================================
    	/*editAudioSettPref.putString("email_id_1","ashishmokalkar79@gmail.com");
    	editAudioSettPref.putString("email_id_2","aishwaryadeshmukh8@gmail.com");
    	editAudioSettPref.commit();*/
    	
    	/*ActionBar bar2 = getSupportActionBar();
		bar2.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3366FF")));*/
		
		final Button alert_button = (Button) findViewById(R.id.button1);
    	
    	final Button bt_edit_msg = (Button) findViewById(R.id.button2);
    	final Button loc_settings = (Button) findViewById(R.id.button4);
    	final Button bt_edit_contact = (Button) findViewById(R.id.button3);
    	final Button bt_locate = (Button) findViewById(R.id.button5);
    	
    	final Button edit_email = (Button) findViewById(R.id.button6);
    	Button audio_sett = (Button) findViewById(R.id.button9);
    	final Button mail_people = (Button) findViewById(R.id.button7);
    	final Button timer_sett = (Button) findViewById(R.id.button8);
    	
    	/*final EditText et_dia_email_1 = (EditText) findViewById(R.id.editText);
    	final EditText et_dia_email_2 = (EditText) findViewById(R.id.editText1);
    	final EditText et_dia_email_3 = (EditText) findViewById(R.id.editText2);*/
    	
		/*String email1 = AudioSettPref.getString("email_id_1", "0");
		//AudioSettPref.getString(, defValue)
		String email2 = AudioSettPref.getString("email_id_2", "0");
		String email3 = AudioSettPref.getString("email_id_3", "0");*/
		
		/*et_dia_email_1.setText(email1);
		et_dia_email_2.setText(email2);
		et_dia_email_3.setText(email3);
    	*/
    	
    	bt_edit_msg.setVisibility(-1);
    	bt_edit_contact.setVisibility(-1);
    	bt_locate.setVisibility(-1);
    	
    	edit_email.setVisibility(-1);
    	timer_sett.setVisibility(-1);
    	mail_people.setVisibility(-1);
    	
    	TextView tv1 = (TextView) findViewById(R.id.textView1);
    	
    	Typeface font = Typeface.createFromAsset(getAssets(), "Smush_demo.otf");
		
		tv1.setTypeface(font);

        Animation animFadein2 = AnimationUtils.loadAnimation(getApplicationContext(),
    			R.anim.zoomin);
    	loc_settings.startAnimation(animFadein2);
    	audio_sett.startAnimation(animFadein2);
    	alert_button.startAnimation(animFadein2);
    	
    	loc_settings.setOnClickListener(new OnClickListener()
    	{
			
			@Override
			public void onClick(View v)
			{
				
				if(button_loc_open == 0)
				{
				
				bt_edit_msg.setVisibility(0);
				bt_edit_contact.setVisibility(0);
		    	bt_locate.setVisibility(0);
		    	
		    	Animation bt_msg_over = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.ant_overshoot_for_msg);
		    	bt_edit_msg.startAnimation(bt_msg_over);
		    	
		    	Animation bt_contactAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.ant_overshoot_for_contact);
		    	bt_edit_contact.startAnimation(bt_contactAnimation);
		    	
		    	Animation bt_locateAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.ant_overshoot_for_locate);
		    	bt_locate.startAnimation(bt_locateAnimation);
		    	
				//loc_settings.startAnimation(bt_loc_sett_rot);
		    	button_loc_open = 1;
		    	
				}
				else
				{
			    	bt_edit_msg.setVisibility(-1);
			    	bt_edit_contact.setVisibility(-1);
			    	bt_locate.setVisibility(-1);
			    	
			    	button_loc_open = 0;
				}
				
			}
		});
    	
    	audio_sett.setOnClickListener(new OnClickListener() 
    	{
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(button_audio_open == 0){

		    	edit_email.setVisibility(0);
		    	timer_sett.setVisibility(0);
		    	mail_people.setVisibility(0);
		    	
		    	Animation edit_email_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.edit_mail_anim);
		    	edit_email.startAnimation(edit_email_anim);
		    	
		    	Animation mailPeople_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.mail_people_anim);
		    	mail_people.startAnimation(mailPeople_anim);
		    	
		    	Animation timer_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.timer_anim);
		    	timer_sett.startAnimation(timer_anim);
				
		    	
		    	button_audio_open = 1;
				}
				
				else
				{

			    	edit_email.setVisibility(-1);
			    	timer_sett.setVisibility(-1);
			    	mail_people.setVisibility(-1);
			    	
			    	button_audio_open = 0;
				}
			}
		});
    	
    	bt_locate.setOnClickListener(new OnClickListener() 
    	{
			
			@Override
			public void onClick(View v) 
			{
				
				AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

				alert.setTitle("Your location is : ");
				//alert.setMessage("");

				// Set an EditText view to get user input 
				final TextView tv_longi = new TextView(MainActivity.this);
				//final TextView tv_latti = new TextView(MainActivity.this);
				
				final float longi = Contact_Sett_Pref.getFloat("longitude", 0f);
				final float latt =  Contact_Sett_Pref.getFloat("lattitude", 0f);
				
				tv_longi.setText("     Lattitude : "+latt+" \n      longitude : "+longi);
				//tv_latti.setText(""+latt);
				
				//alert.setView(tv_latti);
				alert.setView(tv_longi);
				
				//alert.setView(tv_location);

				alert.setPositiveButton("Locate", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				 //String latt_value = tv_latti.getText().toString();
				 String longi_value = tv_longi.getText().toString();
				 
				 String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latt, longi);
				 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				 MainActivity.this.startActivity(intent);
				  // Do something with value!
				 //edit_Contact_Sett_Pref.putString("msg_to_be_sent",value);
				 /*float longi = Contact_Sett_Pref.getFloat("longitude", 0f);
				 float latt = Contact_Sett_Pref.getFloat("lattitude", 0f);
				 
				 edit_Contact_Sett_Pref.putFloat("longitude", longi_value);
				 edit_Contact_Sett_Pref.commit();*/
				  }
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
				  }
				});

				alert.show();
				// TODO Auto-generated method stub
			}
		});
    	
    	bt_edit_msg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
				
				alert.setTitle("Enter message to be sent");
				alert.setMessage("Message");

				// Set an EditText view to get user input 
				final EditText input = new EditText(MainActivity.this);
				alert.setView(input);

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				 String value = input.getText().toString();
				  // Do something with value!
				 edit_Contact_Sett_Pref.putString("msg_to_be_sent",value);
				 edit_Contact_Sett_Pref.commit();
				  }
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
				  }
				});

				alert.show();
				
			}
		});
    	
    	bt_edit_contact.setOnClickListener(new OnClickListener() 
    	{
			
			@Override
			public void onClick(View v) {
				
				Intent start2_intent = new Intent(MainActivity.this,contacts_edit.class);
				//start2_intent.putExtra("past_Acti","1");
				startActivity(start2_intent);
				// TODO Auto-generated method stub
				
			}
		});
    	
    	alert_button.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View v) 
			{
				boolean isAdmin = mDevicePolicyManager.isAdminActive(mComponentName);  
	             

	             if (isAdmin) {  
	                 mDevicePolicyManager.lockNow(); 
	             }
				//alert_button.setBackgroundDrawable( getResources().getDrawable(R.drawable.alert_blue_red));
/*
				Thread t = new Thread(new MainActivity(), "My Thread");
				Log.e("Name of thread", t.getName());
				t.start();*/
				
				float send_long = Contact_Sett_Pref.getFloat("longitude",0f);
				float send_lat = Contact_Sett_Pref.getFloat("lattitude",0f);
				String editted_msg = Contact_Sett_Pref.getString("msg_to_be_sent", "");
				
				String sms_msg = "Emergency lattitude "+send_lat+" longitude "+send_long;
				String final_msg = sms_msg+" "+editted_msg;
				/*
				SharedPreferences Contact_Sett_Pref = getSharedPreferences("Contact_Sett_Pref", Context.MODE_PRIVATE);
			    final Editor edit_Contact_Sett_Pref = Contact_Sett_Pref.edit();*/
				
				String ph_no_1 = Contact_Sett_Pref.getString("enum1","could not find contacts");
				String ph_no_2 = Contact_Sett_Pref.getString("enum2","could not find contacts");
				String ph_no_3 = Contact_Sett_Pref.getString("enum3","could not find contacts");
				
				Log.e("Number1", ph_no_1);
				Log.e("Number2", ph_no_2);
				Log.e("Number3", ph_no_3);
				
				SmsManager smsManager = SmsManager.getDefault();
										
				smsManager.sendTextMessage(ph_no_1, null, final_msg, null, null);
				smsManager.sendTextMessage(ph_no_2, null, sms_msg, null, null);
				smsManager.sendTextMessage(ph_no_3, null, sms_msg, null, null);
				
				Log.e("msg",final_msg);
				
				Toast.makeText(getBaseContext(), final_msg, Toast.LENGTH_LONG).show();
				
				//===============================MSG SENDING END========================================= 
				
				
				/*outputFile = Environment.getExternalStorageDirectory().
					      getAbsolutePath() + "/Emergency/AlertSoundClip.mp3";*/
				
				//File myFile = new File("/sdcard/Emergency/AlertSoundClip.mp3");
				
				outputFile = Environment.getExternalStorageDirectory().
					      getAbsolutePath() + "/AlertSoundClip.mp3";
				
				myAudioRecorder = new MediaRecorder();
			      myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			      myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			      myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
			      myAudioRecorder.setOutputFile(outputFile);
			      
			      try {
					myAudioRecorder.prepare();
				} catch (IllegalStateException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			      myAudioRecorder.start();
			      Log.e("status","recording");
			      
			      long milliTime = System.currentTimeMillis();
					long timesec = milliTime/1000;
					
					//Toast.makeText(getBaseContext(), " "+timesec, Toast.LENGTH_LONG).show();
					Log.e("Starting time", " "+timesec);
					
					for(;;)
					{
						if (((finaltime = System.currentTimeMillis()/1000 )-timesec)>10)
						{
							myAudioRecorder.stop();
						      myAudioRecorder.release();
						      Toast.makeText(getBaseContext(), " "+finaltime, Toast.LENGTH_LONG).show();
						      Log.e("Starting time", " "+finaltime);
						      break;
						}
					}
					
					
					Log.e("status","Mail sending started");
					Properties props = new Properties(); 
			        //props.setProperty("mail.transport.protocol", "smtp");   
			        props.put("mail.smtp.host","smtp.gmail.com");   
			        props.put("mail.smtp.auth", "true");   
			        props.put("mail.smtp.port", "465");   
			        props.put("mail.smtp.socketFactory.port", "465");   
			        props.put("mail.smtp.socketFactory.class",   
			                "javax.net.ssl.SSLSocketFactory");   
			        //props.put("mail.smtp.socketFactory.fallback", "false");
			        
			        final String own_email_id = AudioSettPref.getString("own_email_id", "");
			        final String own_email_pass = AudioSettPref.getString("own_email_pass", "");
			        //final String emer_email_id = AudioSettPref.getString("emer_email_id", "");
			        
			        Log.e("emil",own_email_id);
			        Log.e("pass",own_email_pass);
			        //Log.e("emer",emer_email_id);
			        
			        session = Session.getDefaultInstance(props,new Authenticator() {protected PasswordAuthentication getPasswordAuthentication()
			        {
			        	return new PasswordAuthentication(own_email_id,own_email_pass);
			        }
					});
			        
			        
			        //pd = ProgressDialog.show(getApplicationContext(),"","Sending mail",true);
			        
			        RetrieveFeedTask task = new RetrieveFeedTask();
			        task.execute();
				
			}
		});
    	
    	timer_sett.setOnClickListener(new OnClickListener()
    	{
    		/*AlertDialog levelDialog = null;*/
			@Override
			public void onClick(View v) 
			{
				
				int choice = -1;
				
		    	final CharSequence[] items = {" 10 sec ","30 sec","60 sec"};
		        
		        // Creating and Building the Dialog
		        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		        
		        int sec = AudioSettPref.getInt("timerValue", -1);
		        if(sec == 10)
		        {
		        	choice = 0;
		        }
		        
		        if(sec == 30)
		        {
		        	choice = 1;
		        }
		        
		        if(sec == 60)
		        {
		        	choice = 2;
		        }
		        
		        builder.setTitle("Time of Audio recording");
		        builder.setSingleChoiceItems(items, choice, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int item) {
		        
		            switch(item)
		            {
		                case 0:
		                        // Your code when first option seletced
		                	
		                	editAudioSettPref.putInt("timerValue",10);
		                	editAudioSettPref.commit();
		                	//AudioSettPref.getInt("timerValue", 0);
		                	
		                	Toast.makeText(getApplicationContext(),AudioSettPref.getInt("timerValue", 0)+"",Toast.LENGTH_SHORT).show();
		                         break;
		                case 1:
		                        // Your code when 2nd  option seletced
		                	editAudioSettPref.putInt("timerValue",30);
		                	editAudioSettPref.commit();
		                	Toast.makeText(getApplicationContext(),AudioSettPref.getInt("timerValue", 0)+"",Toast.LENGTH_SHORT).show();
		                	
		                        
		                        break;
		                case 2:
		                       // Your code when 3rd option seletced
		                	editAudioSettPref.putInt("timerValue",60);
		                	editAudioSettPref.commit();
		                	Toast.makeText(getApplicationContext(),AudioSettPref.getInt("timerValue", 0)+"",Toast.LENGTH_SHORT).show();
		                        break;
		                
		            }
		            levelDialog.dismiss();    
		            }
		        });
		        levelDialog = builder.create();
		        levelDialog.show();
		    	
			}
		});
    	
    	edit_email.setOnClickListener(new OnClickListener()
    	{
			
    		@Override
			public void onClick(View v) 
    		{
				AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
				
				alert.setTitle("Enter subject of email");
				alert.setMessage("Subject");

				// Set an EditText view to get user input 
				final EditText input = new EditText(MainActivity.this);
				alert.setView(input);

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				 String value = input.getText().toString();
				  // Do something with value!
				 editAudioSettPref.putString("subject_mail",value);
				 editAudioSettPref.commit();
				  }
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
				  }
				});

				alert.show();
				
			}
		});
    	
    	mail_people.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
								
				LayoutInflater li = LayoutInflater.from(getApplicationContext());
				View promptsView = li.inflate(R.layout.dialog_email, null);
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						MainActivity.this);
				
				// set prompts.xml to alertdialog builder
				alertDialogBuilder.setView(promptsView);
				
				String email1 = AudioSettPref.getString("emer_email_id", "");
				//AudioSettPref.getString(, defValue)
				//String email2 = AudioSettPref.getString("email_id_2", "");
				//String email3 = AudioSettPref.getString("email_id_3", "");
				
				/*final EditText userInput = (EditText) promptsView
						.findViewById(R.id.editText);
				*/
				final EditText et_dia_email_1 = (EditText) promptsView.findViewById(R.id.editText);
		    	//final EditText et_dia_email_2 = (EditText) promptsView.findViewById(R.id.editText1);
		    	//final EditText et_dia_email_3 = (EditText) promptsView.findViewById(R.id.editText2);
		    	
		    	et_dia_email_1.setText(email1);
				//et_dia_email_2.setText(email2);
				//et_dia_email_3.setText(email3);
 
 
				// set dialog message
				alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("OK",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						// get user input and set it to result
						// edit text
					    	
					    	//===========================================================
					    	String email_id_1 = et_dia_email_1.getText().toString();
					    	//String email_id_2 = et_dia_email_2.getText().toString();
					    	//String email_id_3 = et_dia_email_3.getText().toString();
					    	
					    	editAudioSettPref.putString("emer_email_id",email_id_1);
					    	//editAudioSettPref.putString("email_id_2",email_id_2);
					    	//editAudioSettPref.putString("email_id_3",email_id_3);
					    	editAudioSettPref.commit();
					    	//===========================================================
					    	
					    	//et_dia_email_1.getText().toString();
					    	//Toast.makeText(getApplicationContext(), userInput.getText().toString(), Toast.LENGTH_LONG).show();
						//result.setText(userInput.getText());
					    }
					  })
					.setNegativeButton("Cancel",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					    }
					  });
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
 
			}
				// TODO Auto-generated method stub
				
			
		});
    	
    	/*
    	TextView tv = (TextView) findViewById(R.id.textView1);
    	Typeface font = Typeface.createFromAsset(getAssets(), "NamesBrutas.otf");
		
		tv.setTypeface(font);*/

    }
    else
    {/*
        Editor ed = pref.edit();
        ed.putBoolean("activity_executed", true);
        ed.commit();*/
        Intent inte = new Intent(MainActivity.this,Start1.class);
        startActivity(inte);
 
    }

   
  }
  @Override
	public void onBackPressed() 
  {
	  
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
		
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
			AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

			alert.setTitle("Position of evidences ");
			//alert.setMessage("");

			// Set an EditText view to get user input 
			final TextView tv_longi = new TextView(MainActivity.this);
			//final TextView tv_latti = new TextView(MainActivity.this);
			/*
			final float longi = Contact_Sett_Pref.getFloat("longitude", 0f);
			final float latt =  Contact_Sett_Pref.getFloat("lattitude", 0f);*/
			
			tv_longi.setText("     Audio Clip will be saved as AlertSoundClip.mp3 :  \n\n\n      Photo will be saved as offender.png ");
			//tv_latti.setText(""+latt);
			
			//alert.setView(tv_latti);
			alert.setView(tv_longi);
			
			//alert.setView(tv_location);

			alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			 //String latt_value = tv_latti.getText().toString();
			 /*String longi_value = tv_longi.getText().toString();
			 
			 String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latt, longi);
			 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			 MainActivity.this.startActivity(intent);*/
			  // Do something with value!
			 //edit_Contact_Sett_Pref.putString("msg_to_be_sent",value);
			 /*float longi = Contact_Sett_Pref.getFloat("longitude", 0f);
			 float latt = Contact_Sett_Pref.getFloat("lattitude", 0f);
			 
			 edit_Contact_Sett_Pref.putFloat("longitude", longi_value);
			 edit_Contact_Sett_Pref.commit();*/
			  }
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});

			alert.show();
			return true;
		}
		
		if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

		return super.onOptionsItemSelected(item);
	}
  
  @Override
  public void onLocationChanged(Location location)
  {
	  
	  float longitude = (float) location.getLongitude();
	  float lattitude = (float) location.getLatitude();     
	  
	  //Toast.makeText(getBaseContext(), "longitude = "+longitude+"lattitude = "+lattitude, Toast.LENGTH_LONG).show();
	  
	  edit_Contact_Sett_Pref.putFloat("longitude",longitude);
	  edit_Contact_Sett_Pref.putFloat("lattitude",lattitude);
	  
	  edit_Contact_Sett_Pref.commit();
	  
	  //Contact_Sett_Pref.getFloat("longitude",0f);
	  	  
	  /*Toast.makeText(getBaseContext(), "longitude = "+Contact_Sett_Pref.getFloat("longitude",0f)+
			  "lattitude = "+Contact_Sett_Pref.getFloat("lattitude",0f), Toast.LENGTH_LONG).show();*/
	  
	  Log.e("long", " "+Contact_Sett_Pref.getFloat("longitude", 0f));
	  Log.e("long", " "+Contact_Sett_Pref.getFloat("lattitude", 0f));
	  //return (longitude+" "+lattitude);
	  
	  // Getting reference to TextView tv_longitude
      /*TextView tvLongitude = (TextView)findViewById(R.id.tv_longitude);
      // Getting reference to TextView tv_latitude
      TextView tvLatitude = (TextView)findViewById(R.id.tv_latitude);

      // Setting Current Longitude
      tvLongitude.setText("Longitude:" + location.getLongitude());

      // Setting Current Latitude
      tvLatitude.setText("Latitude:" + location.getLatitude() );*/
  }

  @Override
  public void onProviderDisabled(String provider) {
      // TODO Auto-generated method stub
  }

  @Override
  public void onProviderEnabled(String provider) {
      // TODO Auto-generated method stub
  }
  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
      // TODO Auto-generated method stub
  }
  
  private void selectItem(int position) {
  	// TODO Auto-generated method stub
  	
  	listView.setItemChecked(position,true);
//  	setTitle(planets[position]);
  	
  }
  /*@Override
  protected void onPostCreate(Bundle savedInstanceState) {
      super.onPostCreate(savedInstanceState);
      // Sync the toggle state after onRestoreInstanceState has occurred.
      //mDrawerToggle.syncState();
  }*/
  @Override
  public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      mDrawerToggle.onConfigurationChanged(newConfig);
  }
  /*@Override
  public void run() 
  {


	outputFile = Environment.getExternalStorageDirectory().
		      getAbsolutePath() + "/myrec.mp3";
	
	myAudioRecorder = new MediaRecorder();
      myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
      myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
      myAudioRecorder.setOutputFile(outputFile);
      
      try {
		myAudioRecorder.prepare();
	} catch (IllegalStateException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      myAudioRecorder.start();
      Log.e("status","recording");
      
      long milliTime = System.currentTimeMillis();
		long timesec = milliTime/1000;
		
		//Toast.makeText(getBaseContext(), " "+timesec, Toast.LENGTH_LONG).show();
		Log.e("Starting time", " "+timesec);
		
		for(;;)
		{
			if (((finaltime = System.currentTimeMillis()/1000 )-timesec)>10)
			{
				myAudioRecorder.stop();
			      myAudioRecorder.release();
			      //Toast.makeText(getBaseContext(), " "+finaltime, Toast.LENGTH_LONG).show();
			      Log.e("Starting time", " "+finaltime);
			      break;
			}
		}
		
		Log.e("status","Mail sending started");
		Properties props = new Properties(); 
        //props.setProperty("mail.transport.protocol", "smtp");   
        props.put("mail.smtp.host","smtp.gmail.com");   
        props.put("mail.smtp.auth", "true");   
        props.put("mail.smtp.port", "465");   
        props.put("mail.smtp.socketFactory.port", "465");   
        props.put("mail.smtp.socketFactory.class",   
                "javax.net.ssl.SSLSocketFactory");   
        //props.put("mail.smtp.socketFactory.fallback", "false");
        
        session = Session.getDefaultInstance(props,new Authenticator() {protected PasswordAuthentication getPasswordAuthentication()
        {
        	return new PasswordAuthentication("ashishmokalkar79@gmail.com","MyAndroid@79");
        }
		});
        
        
        //pd = ProgressDialog.show(getApplicationContext(),"","Sending mail",true);
        
        RetrieveFeedTask task = new RetrieveFeedTask();
        task.execute();
	
  }*/

  class RetrieveFeedTask extends AsyncTask<String,Void,String>
	{
	 //SharedPreferences AudioSettPref ;/*= getSharedPreferences("AudioSettPref", MainActivity.this.MODE_PRIVATE);
	   // final Editor editAudioSettPref = AudioSettPref.edit();*/
	 
		@Override
		protected String doInBackground(String... params) 
		{
			//AudioSettPref = getSharedPreferences("AudioSettPref", Context.MODE_PRIVATE);
			
			Message msg = new MimeMessage(session);
			try 
			{
				
				String send_email_id_1 = AudioSettPref.getString("emer_email_id","");
				String own_email_id = AudioSettPref.getString("own_email_id","");
				//String send_email_id_2 = AudioSettPref.getString("email_id_2","");
				//String send_email_id_3 = AudioSettPref.getString("email_id_3","");
				
				String subject = AudioSettPref.getString("subject_mail", "Alert");
				
				
				msg.setFrom(new InternetAddress(own_email_id));
				
				msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(send_email_id_1));
				Log.e("EMAIL",send_email_id_1 );
				//msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(send_email_id_2));
				//msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse("ashyagomashya@gmail.com"));
				/*msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse("ashishmokalkar79@gmail.com"));*/
				
				msg.setSubject(subject);
				DataHandler handler = new DataHandler(new javax.mail.util.ByteArrayDataSource("Audio Clip of surrounding".getBytes(), "text/plain"));
				msg.setDataHandler(handler);
				
				BodyPart messageBodyPart1 = new MimeBodyPart();  
			    messageBodyPart1.setText("Audio Clip of surrounding");  
			    
			    //4) create new MimeBodyPart object and set DataHandler object to this object      
			    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
			    
			    String filename = Environment.getExternalStorageDirectory().
					      getAbsolutePath() + "/AlertSoundClip.mp3";
			    
			    //String filename = "image.png";//change accordingly
			    DataSource source = new FileDataSource(filename);
			    messageBodyPart2.setDataHandler(new DataHandler(source));  
			    messageBodyPart2.setFileName(filename);  
			    			    
			    //5) create Multipart object and add MimeBodyPart objects to this object      
			    Multipart multipart = new MimeMultipart();  
			    multipart.addBodyPart(messageBodyPart1);  
			    multipart.addBodyPart(messageBodyPart2);  
			  
			    //6) set the multiplart object to the message object  
			    msg.setContent(multipart );  
			     
				Transport.send(msg);
				
				//Log.e("EMAIL",send_email_id_1 );
				//Log.e("EMAIL",send_email_id_2 );
				
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//pd.dismiss();
			/*mailId.setText("");
			sub.setText("");
			body.setText("");*/
			
			//Toast.makeText(), "Sent", Toast.LENGTH_LONG).show();
			Log.e("status","mail sent");
		}
	}
@Override
public void onAnimationStart(Animation animation) {
	// TODO Auto-generated method stub
	
}
@Override
public void onAnimationEnd(Animation animation) {
	// TODO Auto-generated method stub
	
}
@Override
public void onAnimationRepeat(Animation animation) {
	// TODO Auto-generated method stub
	
}
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@Override
public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	// TODO Auto-generated method stub
	
	selectItem(position);
	if(position == 0)
	{
	Intent i=new Intent(MainActivity.this,Help.class);
	i.putExtra("key",1);
	Bundle bndlanimation = 
			ActivityOptions.makeCustomAnimation(getApplicationContext(), 
					R.anim.start1,R.anim.end1).toBundle();
	startActivity(i,bndlanimation);
	//MainActivity.this.finish();
	}
	
	else if(position == 1)
	{
		Intent i=new Intent(MainActivity.this,About.class);
		i.putExtra("key",1);
		
		Bundle bndlanimation = 
				ActivityOptions.makeCustomAnimation(getApplicationContext(), 
						R.anim.start1,R.anim.end1).toBundle();
		startActivity(i,bndlanimation);
		//MainActivity.this.finish();
		//startActivity(i);
	}
	
	else if(position == 2)
	{
		Toast.makeText(getBaseContext(), "Share app", Toast.LENGTH_LONG).show();
		/*Intent i=new Intent(MainActivity.this,About.class);
		i.putExtra("key",1);
		startActivity(i);*/
	}
	
	else if(position == 3)
	{
		MainActivity.this.finish();
	}
	
}





/*@Override
public void onItemClick(AdapterViewCompat<?> arg0, View arg1, int position,
		long arg3) 
{
	
	selectItem(position);
	if(position == 1)
	{
	Intent i=new Intent(MainActivity.this,About.class);
	i.putExtra("key",1);
	startActivity(i);
	}

	// TODO Auto-generated method stub
	
}

private void selectItem(int position) {
	// TODO Auto-generated method stub
	
	listView.setItemChecked(position,true);
//	setTitle(planets[position]);
	
}
@Override
protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // Sync the toggle state after onRestoreInstanceState has occurred.
    mDrawerToggle.syncState();
}
@Override
public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
}*/

  
  
}

class MyAdapter extends BaseAdapter
{
	private Context context;
	String[] socialSites;// =context.getResources().getStringArray(R.array.social);
	int images[]={R.drawable.ic_action_help_dark,R.drawable.ic_action_about,R.drawable.ic_action_share,
			
			R.drawable.ic_action_cancel};
	
	
	public MyAdapter(Context context) {
		// TODO Auto-generated constructor stub
		super();
		this.context=context;
		socialSites =context.getResources().getStringArray(R.array.social);
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return socialSites.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return socialSites[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row=null;
		if(convertView==null)
		{
			LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.custom_row, parent,false);
		}
		else
		{
			row=convertView;
		}
		TextView titleTextView=(TextView) row.findViewById(R.id.textView1);
		ImageView titleImageView=(ImageView) row.findViewById(R.id.imageView1);
		
		titleTextView.setText(socialSites[position]);
		titleImageView.setImageResource(images[position]);
		return row;
	}
}