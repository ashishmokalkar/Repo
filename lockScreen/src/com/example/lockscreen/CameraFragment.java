package com.example.lockscreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
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

//import com.example.send3.MainActivity.RetrieveFeedTask;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

//import com.androidzeitgeist.mustache.listener.CameraFragmentListener;
//import com.androidzeitgeist.mustache.view.CameraPreview;

/**
 * Fragment for displaying the camera preview.
 *
 * @author Sebastian Kaspari <sebastian@androidzeitgeist.com>
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CameraFragment extends Fragment implements SurfaceHolder.Callback {
	
	SharedPreferences AudioSettPref ;
	Editor editAudioSettPref ;
	
    public static final String TAG = "Mustache/CameraFragment";
    Session session = null;
    private static final int PICTURE_SIZE_MAX_WIDTH = 1280;
    private static final int PREVIEW_SIZE_MAX_WIDTH = 640;
    Parameters parameters;
    Bitmap bmp;
    private int cameraId;
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private CameraFragmentListener listener;

    /**
     * On activity getting attached.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof CameraFragmentListener)) {
            throw new IllegalArgumentException(
                "Activity has to implement CameraFragmentListener interface"
            );
        }

        listener = (CameraFragmentListener) activity;
    }

    /**
     * On creating view for fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CameraPreview previewView = new CameraPreview(getActivity());

        previewView.getHolder().addCallback(this);

        return previewView;
    }

    /**
     * On fragment getting resumed.
     */
    @Override
    public void onResume() 
    {
        super.onResume();

        /*try {
        	
        	for (int camNo = 0; camNo < Camera.getNumberOfCameras(); camNo++) {
                CameraInfo camInfo = new CameraInfo();
                Camera.getCameraInfo(camNo, camInfo);
               
                if (camInfo.facing==(Camera.CameraInfo.CAMERA_FACING_FRONT)) {
                	cameraId = camNo;
                    camera = Camera.open(camNo);
                }
            }
        	
            //camera = Camera.open(cameraId);
        } catch (Exception exception) {
            Log.e(TAG, "Can't open camera with id " + cameraId, exception);

            listener.onCameraError();
            return;
        }*/
    }

    /**
     * On fragment getting paused.
     */
    @Override
    public void onPause() {
        super.onPause();
/*
        stopCameraPreview();
        camera.release();*/
    }

    /**
     * Start the camera preview.
     */
    private synchronized void startCameraPreview() {
        determineDisplayOrientation();
        setupCamera();

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException exception) {
            Log.e(TAG, "Can't start camera preview due to IOException", exception);

            listener.onCameraError();
        }
    }

    /**
     * Stop the camera preview.
     */
    private synchronized void stopCameraPreview() {
        try {
            camera.stopPreview();
        } catch (Exception exception) {
            Log.i(TAG, "Exception during stopping camera preview");
        }
    }

    /**
     * Determine the current display orientation and rotate the camera preview
     * accordingly.
     */
    public void determineDisplayOrientation() {
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);

        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees  = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int displayOrientation;

        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            displayOrientation = (cameraInfo.orientation + degrees) % 360;
            displayOrientation = (360 - displayOrientation) % 360;
        } else {
            displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        }

        camera.setDisplayOrientation(displayOrientation);
    }

    /**
     * Setup the camera parameters.
     */
    public void setupCamera() {
        Camera.Parameters parameters = camera.getParameters();

        Size bestPreviewSize = determineBestPreviewSize(parameters);
        Size bestPictureSize = determineBestPictureSize(parameters);

        parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
        parameters.setPictureSize(bestPictureSize.width, bestPictureSize.height);

        camera.setParameters(parameters);
    }

    private Size determineBestPreviewSize(Camera.Parameters parameters) {
        List<Size> sizes = parameters.getSupportedPreviewSizes();

        return determineBestSize(sizes, PREVIEW_SIZE_MAX_WIDTH);
    }

    private Size determineBestPictureSize(Camera.Parameters parameters) {
        List<Size> sizes = parameters.getSupportedPictureSizes();

        return determineBestSize(sizes, PICTURE_SIZE_MAX_WIDTH);
    }

    protected Size determineBestSize(List<Size> sizes, int widthThreshold) {
        Size bestSize = null;

        for (Size currentSize : sizes) {
            boolean isDesiredRatio = (currentSize.width / 4) == (currentSize.height / 3);
            boolean isBetterSize = (bestSize == null || currentSize.width > bestSize.width);
            boolean isInBounds = currentSize.width <= PICTURE_SIZE_MAX_WIDTH;

            if (isDesiredRatio && isInBounds && isBetterSize) {
                bestSize = currentSize;
            }
        }

        if (bestSize == null) {
            listener.onCameraError();

            return sizes.get(0);
        }

        return bestSize;
    }

    /**
     * On camera preview surface created.
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.surfaceHolder = holder;
        
try {
        	
        	for (int camNo = 0; camNo < Camera.getNumberOfCameras(); camNo++) {
                CameraInfo camInfo = new CameraInfo();
                Camera.getCameraInfo(camNo, camInfo);
               
                if (camInfo.facing==(Camera.CameraInfo.CAMERA_FACING_FRONT)) {
                	cameraId = camNo;
                    camera = Camera.open(camNo);
                }
            }
        	
            //camera = Camera.open(cameraId);
        } catch (Exception exception) {
            Log.e(TAG, "Can't open camera with id " + cameraId, exception);

            listener.onCameraError();
            return;
        }

        startCameraPreview();
    }

    /**
     * On camera preview surface changed.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
    {
    	
    	AudioSettPref = getActivity().getSharedPreferences("AudioSettPref", Context.MODE_PRIVATE);
        //editAudioSettPref = AudioSettPref.edit();
        
        final String own_email_id = AudioSettPref.getString("own_email_id", "");
        final String own_email_pass = AudioSettPref.getString("own_email_pass", "");
        
        Log.e("emil",own_email_id);
        Log.e("pass",own_email_pass);
        // The interface forces us to have this method but we don't need it
        // up to now.
    	

		 //get camera parameters
		 parameters = camera.getParameters();
		 
		 //set camera parameters
	     camera.setParameters(parameters);
	     camera.startPreview();
	     
	     
	     //sets what code should be executed after the picture is taken
	     Camera.PictureCallback mCall = new Camera.PictureCallback() 
	     {
	    	 @Override
	    	 public void onPictureTaken(byte[] data, Camera camera) 
	    	 {
	    		//decode the data obtained by the camera into a Bitmap
	    		 
	    		 bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
	    		 
	    //============================================Store bitmap in sdcard============================================================================		 
	    		 String sdcard = Environment.getExternalStorageDirectory()+File.separator + "Offender.png";
	    		 File f = new File(sdcard);
	    		 FileOutputStream out = null;
				try {
					out = new FileOutputStream(f);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		 bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
	    		 
	    		 Log.e("status","photo saved into sd card");
	    		 
	    		 /*SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
	    		 
	    		 boolean play = sp1.getBoolean("sw2",false);*/
	    		 
	    		 //Log.e("play value"," "+play);
	    		 
	    		 //if(play)
	    		 //{
	    		 
	    		 /*Intent i = new Intent(getActivity(), PlayAlarm.class);
	    		 getActivity().startService(i);*/
	    		 //}
//========================================================Send Email===========================================================================================
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
	    	        
	    	        session = Session.getDefaultInstance(props,new Authenticator() {
	    	        	protected PasswordAuthentication getPasswordAuthentication()
	    	        {
	    	        	return new PasswordAuthentication(own_email_id,own_email_pass);
	    	        }
	    			});
	    	        
	    	        
	    	        //pd = ProgressDialog.show(getApplicationContext(),"","Sending mail",true);
	    	        
	    	        RetrieveFeedTask task = new RetrieveFeedTask();
	    	        task.execute();	    		 
	    	 }
	     };
	     
	     camera.takePicture(null, null, mCall);
	     //getActivity().finish();
	     //CameraActivity.this.finish();
	     
	     //camera.release();
    }
    
    
    
    class RetrieveFeedTask extends AsyncTask<String,Void,String>
	{

		@Override
		protected String doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
			Message msg = new MimeMessage(session);
			try {
				
				String send_email_id_1 = AudioSettPref.getString("emer_email_id","");
				String own_email_id = AudioSettPref.getString("own_email_id","");
				
				msg.setFrom(new InternetAddress(own_email_id));
				msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(send_email_id_1));
				msg.setSubject("Alert");
				DataHandler handler = new DataHandler(new javax.mail.util.ByteArrayDataSource("Someone tried to unlock the phone".getBytes(), "text/plain"));
				msg.setDataHandler(handler);
				
				BodyPart messageBodyPart1 = new MimeBodyPart();  
			    messageBodyPart1.setText("Offenders photo.");  
			    
			    //4) create new MimeBodyPart object and set DataHandler object to this object      
			    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
			    
			    String filename = Environment.getExternalStorageDirectory()+File.separator + "Offender.png";
			    
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

    /**
     * On camera preview surface getting destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) 
    {
        // We don't need to handle this case as the fragment takes care of
        // releasing the camera when needed.
    	
    	holder.removeCallback(this);
        camera.stopPreview();
        camera.release();
    }
}
