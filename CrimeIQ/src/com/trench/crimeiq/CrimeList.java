package com.trench.crimeiq;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
 
public class CrimeList extends ListActivity {
	
	CharSequence selected;
	String selected_id;
	Intent intent = new Intent();
	
	Map<String, Integer> crimes = new HashMap<String, Integer>();

	
	
	static final String[] CRIMES = new String[] { "Murder", "Total Sexual Crimes","Attempted murder",
		"Assault with the intent to inflict grievous bodily harm","Common assault","Common robbery","Robbery with aggravating circumstances",
		"Malicious damage to property","Burglary at non-residential premises","Burglary at residential premises",
		"Theft of motor vehicle and motorcycle","Theft out of or from motor vehicle","Stock-theft",
		"Illegal possession of firearms and ammunition","Drug-related crime","Driving under the influence of alcohol or drugs",
		"All theft not mentioned elsewhere","Commercial crime","Shoplifting","Carjacking","Truck hijacking",
		"Robbery at residential premises","Robbery at non-residential premises","Culpable homicide","Public violence",
		"Crimen injuria","Neglect and ill-treatment of children", "Kidnapping"};

  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		putCrimes();
		// no more this
		// setContentView(R.layout.list_fruit);
 
		setListAdapter(new ArrayAdapter<String>(this, R.layout.crimelistview, CRIMES));
 
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
 
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CharSequence selected = ((TextView) view).getText();
				String selected_id = (String) crimes.get(selected).toString();
			    // When clicked, show a toast with the TextView text
			    Toast.makeText(getApplicationContext(),
				(selected+selected_id), Toast.LENGTH_SHORT).show();
				
			    
			    intent.putExtra("crimecatid", selected_id);
		        setResult(RESULT_OK, intent); 
		        finish();
		        };
			
			
			
			
 
		});
		
	
	}
	public void putCrimes() {
		crimes.put("Murder", 1);
		crimes.put("Total Sexual Crimes",2);
		crimes.put("Attempted murder",3);
		crimes.put("Assault with the intent to inflict grievous bodily harm",4);
		crimes.put("Common assault",5);
		crimes.put("Common robbery",6);
		crimes.put("Robbery with aggravating circumstances",7);
		crimes.put("Malicious damage to property",8);
		crimes.put("Burglary at non-residential premises",9);
		crimes.put("Burglary at residential premises",10);
		crimes.put("Theft of motor vehicle and motorcycle",11);
		crimes.put("Theft out of or from motor vehicle",12);
		crimes.put("Stock-theft",13);
		crimes.put("Illegal possession of firearms and ammunition",14);
		crimes.put("Drug-related crime",15);
		crimes.put("Driving under the influence of alcohol or drugs",16);
		crimes.put("All theft not mentioned elsewhere",17);
		crimes.put("Commercial crime",18);
		crimes.put("Shoplifting",19);
		crimes.put("Carjacking",20);
		crimes.put("Truck hijacking",21);
		crimes.put("Robbery at residential premises",22);
		crimes.put("Robbery at non-residential premises",23);
		crimes.put("Culpable homicide",24);
		crimes.put("Public violence",25);
		crimes.put("Crimen injuria", 26);
		crimes.put("Neglect and ill-treatment of children", 27);
		crimes.put("Kidnapping",28);
		};

}