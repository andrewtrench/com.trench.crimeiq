package com.trench.crimeiq;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
 
public class CrimeNearby extends Activity {
	
	TableLayout table;
	
	


	
	
	
  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearbycrimes);
		
		Bundle extras = getIntent().getExtras();
		Log.i("extras", extras.toString());
	
		
		TableLayout table = (TableLayout) findViewById(R.id.crimesNearTable);
		
		
 
		
		
		for (String key : extras.keySet()) {
			TableRow row = new TableRow(this);
			 
			// create a new TextView
	        TextView t = new TextView(this);
	        
		    Object value = extras.get(key);
		 // set the text to "text xx"       
	        t.setText(key+" "+value.toString());
	        row.addView(t);
	        
		    Log.d("OTHER AREAS", String.format("%s %s (%s)", key,  
		        value.toString(), value.getClass().getName()));
		    // add the TableRow to the TableLayout
	        table.addView(row,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
	    
		

	}
}