package com.example.lockscreen;


//import android.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
public class MyFragment2 extends Fragment {

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.myfragment_layout_two, container, false);
/*
    TextView tv = (TextView) v.findViewById(R.id.textView1);
    tv.setText(getArguments().getString("msg"));*/
    
    Button go = (Button) v.findViewById(R.id.button1);
    
    go.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			Intent in = new Intent(getActivity(),MainActivity.class);
			startActivity(in);
			getActivity().finish();
			// TODO Auto-generated method stub
			
		}
	});

    return v;
}

public static MyFragment2 newInstance(String text)
{

	MyFragment2 f = new MyFragment2();
    Bundle b = new Bundle();
    b.putString("msg", text);

    f.setArguments(b);

    return f;
}
}