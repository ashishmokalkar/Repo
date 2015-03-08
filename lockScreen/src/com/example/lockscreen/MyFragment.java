package com.example.lockscreen;

//import com.example.rotate_.R;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
//import android.R;

public class MyFragment extends Fragment 
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.myfragment_layout, container, false);

        TextView tv = (TextView) v.findViewById(R.id.textView);        

		final Animation animAnti = AnimationUtils.loadAnimation(getActivity(), R.anim.anti_overshoot);
		
		ImageView iv = (ImageView) v.findViewById(R.id.imageView1);
		
		ImageView iv2 = (ImageView) v.findViewById(R.id.imageView2);
		
		iv.setAnimation(animAnti);
		iv2.setAnimation(animAnti);
        
        tv.setText(getArguments().getString("msg"));
        
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "NamesBrutas.otf");
		
		tv.setTypeface(font);


        return v;
    }

    public static MyFragment newInstance(String text) {

        MyFragment f = new MyFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}