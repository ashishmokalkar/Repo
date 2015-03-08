package com.example.lockscreen;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.Log;
import android.view.View;

public class ZoomOutPageTransformer implements PageTransformer {
    private static final float MIN_SCALE = 1.0f;
    private static final float MIN_ALP = 0.5f;
    private static final float MIN_ALP_for_tv = 0.2f;
    
    private static final float MIN_ALPHA = 0.2f;
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") 
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.w
            view.setAlpha(1);

        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            
            float alpFactor = Math.max(MIN_ALP, 1 - Math.abs(position));
/*            float alpFactor_for_tv = Math.max(MIN_ALP, 1 - Math.abs(position));
*/            
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
           	
            	//view.findViewById(R.id.button1).setTranslationY(vertMargin-horzMargin);
                //view.setTranslationX(-(vertMargin-horzMargin));
            	//view.setTranslationY(vertMargin);
            	//view.setTranslationX(-position * (pageWidth / 2));
            	//view.setTranslationX(-horzMargin);
            	//view.findViewById(R.id.button1).setRotation(vertMargin);
            	Log.e("vertexarging"," "+vertMargin);
            } else {
               // view.setTranslationX(-horzMargin + vertMargin / 8);
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA +
                    (alpFactor - MIN_ALP) /
                    (1 - MIN_ALP) * (1 - MIN_ALPHA));
            

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(1);
        }
    }
}