package com.example.lockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class IncomingSms extends BroadcastReceiver 
{
     
    // Get the object of SmsManager
	LocationManager locationManager ;
	String provider;
	//SharedPreferences Contact_Sett_Pref ;
    final SmsManager sms = SmsManager.getDefault();
    
    public void onReceive(Context context, Intent intent) 
    {
    	SharedPreferences Contact_Sett_Pref ;
    	
    	Contact_Sett_Pref = context.getSharedPreferences("Contact_Sett_Pref", Context.MODE_PRIVATE);
    	float own_longitude = Contact_Sett_Pref.getFloat("longitude",0f);
    	float own_lattitude = Contact_Sett_Pref.getFloat("lattitude",0f);
    	
    	/*locationManager = (LocationManager)getSystemService(context.LOCATION_SERVICE);
        
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
        }*/
    	
     
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try 
        {
             
            if (bundle != null) {
                 
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                 
                for (int i = 0; i < pdusObj.length; i++) {
                     
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    
                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                    
                    String splitted_array[] = message.split(" ");
                    Toast.makeText(context,splitted_array[2]+" , "+splitted_array[4],Toast.LENGTH_LONG).show();
                    
                    if(splitted_array[0].equals("Emergency"))
                    {
                    	
                    	Vibrator vibrator= (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
					
			 			vibrator.vibrate(5000);  
                    
                    //Intent intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?saddr=17.428323,78.412567&daddr=28.6454414,77.0907573"));
                    
    				Intent geo_intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?saddr="+own_lattitude/*17.428323*/+","+own_longitude+/*78.412567*/"&daddr="+splitted_array[2]+","+splitted_array[4]/*77.0907573*/));
    				geo_intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
    				geo_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    				context.startActivity(geo_intent);
    				
                    }
                    /*
                    sendReplySms(senderNum);
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    Boolean tts1 = sharedPrefs.getBoolean("tts", false);
                    
                    if(tts1)
                    tts.speak("You have a message.A reply has been sent.", TextToSpeech.QUEUE_FLUSH, null);
                    //sendMsgNotification(senderNum);
                   // Show Alert
*/                    /*int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, 
                                 "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();
                     */
                } // end for loop
              } // bundle is null
 
        } catch (Exception e)
        {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
        }
        
    }
    
    /*@Override
    public void onLocationChanged(Location location)
    {
  	  
  	  float longitude = (float) location.getLongitude();
  	  float lattitude = (float) location.getLatitude();     
  	  
  	  //Toast.makeText(getBaseContext(), "longitude = "+longitude+"lattitude = "+lattitude, Toast.LENGTH_LONG).show();
  	  
  	  edit_Contact_Sett_Pref.putFloat("longitude",longitude);
  	  edit_Contact_Sett_Pref.putFloat("lattitude",lattitude);
  	  
  	  edit_Contact_Sett_Pref.commit();
  	  
  	  //Contact_Sett_Pref.getFloat("longitude",0f);
  	  
  	  
  	  Toast.makeText(getBaseContext(), "longitude = "+Contact_Sett_Pref.getFloat("longitude",0f)+
  			  "lattitude = "+Contact_Sett_Pref.getFloat("lattitude",0f), Toast.LENGTH_LONG).show();
  	  Log.e("long", " "+Contact_Sett_Pref.getFloat("longitude", 0f));
  	  Log.e("long", " "+Contact_Sett_Pref.getFloat("lattitude", 0f));
  	  //return (longitude+" "+lattitude);
  	  
  	  // Getting reference to TextView tv_longitude
        TextView tvLongitude = (TextView)findViewById(R.id.tv_longitude);
        // Getting reference to TextView tv_latitude
        TextView tvLatitude = (TextView)findViewById(R.id.tv_latitude);

        // Setting Current Longitude
        tvLongitude.setText("Longitude:" + location.getLongitude());

        // Setting Current Latitude
        tvLatitude.setText("Latitude:" + location.getLatitude() );*/
    
}
