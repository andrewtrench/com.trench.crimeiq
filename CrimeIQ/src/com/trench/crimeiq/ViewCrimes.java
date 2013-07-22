package com.trench.crimeiq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewCrimes extends Activity {

TextView crimeView;
String police_station;
String crimes_2012;
String crime;
String crimes_2011;
String rate_2012;
String population;
double longitude;
double latitude;
String web_url;
String crimecat_id;
Intent extra;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_viewcrimes);
	    
	    final Intent extra = getIntent();
	    police_station = extra.getStringExtra("police_station");
	    crimes_2012 = extra.getStringExtra("crimes_2012");
	    crime = extra.getStringExtra("crime");
	    crimes_2011 = extra.getStringExtra("crimes_2011");
	    rate_2012 = extra.getStringExtra("rate_2012");
	    population = extra.getStringExtra("pop_total");
	    latitude = extra.getDoubleExtra("geo1", 0);
		longitude = extra.getDoubleExtra("geo2",0);
		crimecat_id = extra.getStringExtra("crime2");
	    
	    TextView police_stationView = (TextView) findViewById(R.id.CrimeTablePrecinct);
	    police_stationView.setText(police_station);
	    
	    TextView crimes_2012View = (TextView) findViewById(R.id.Results2012);
	    crimes_2012View.setText(crimes_2012);
	    
	    TextView crimeCatView = (TextView) findViewById(R.id.CrimeTableCategory);
	    crimeCatView.setText(crime);
	    
	    TextView crimes_2011View = (TextView) findViewById(R.id.Results2011);
	    crimes_2011View.setText(crimes_2011);
	    
	    TextView rate_2012View = (TextView) findViewById (R.id.RateResults2012);
	    rate_2012View.setText(rate_2012);
	    
	    TextView populationView =(TextView) findViewById(R.id.PopResults);
	    populationView.setText(population);
	    
	    Button shareButton = (Button) findViewById(R.id.shareButton);
	    shareButton.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		shareIt();
	    		}
	    		});
	    
	    
	    Button viewNearby = (Button) findViewById(R.id.nearbyButton);
	    
	    viewNearby.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				//Log.i("nearby crimes url", web_url);
				new getNearbyCrimes().execute();    

				
			}
		});

	
	}
	
	
	private void shareIt() {
		//sharing implementation here
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "Here is the share content body";
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Crime data "+crime+" for "+ police_station);
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
		}
	
	//*private class to fetch the web data asynchronously      
	private class getNearbyCrimes extends AsyncTask<URL,JSONObject,JSONObject> {
		//this progressDialog does a funky little spinner without having to define it in layout.xml the android.R. stuff makes it translucent
		//TODO: This should be turned into a method as it used several times in the code
		//private ProgressDialog dialog = new ProgressDialog(getApplicationContext()) ;
		private StatusLine statusLine;//To check status of HTTPResponse
		 
				
				
				protected void onPreExecute() {
					//runs dialog spinner on preexecute --> closes it on postExecute
					//this.dialog.setMessage("Getting crime data...");
					//this.dialog.show();
				}
				@Override
				//This is the core code to get json data from the website and check for any errors
				protected JSONObject doInBackground(URL... url) {
					//Concatenates weburl with diff data for web fetch
					web_url = "http://crimeiq.webfactional.com/query/nearby/"+longitude+"+"+latitude+"/"+crimecat_id;
					String uri = web_url;
					Log.i("new web url", uri);
					HttpClient httpclient = new DefaultHttpClient();
				    HttpResponse response;
				    String responseString = null;
				    JSONObject result = null;
				    try {
				    	//try get the json and check status is ok
				        response = httpclient.execute(new HttpGet(uri));
				        StatusLine statusLine = response.getStatusLine();
				        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				            ByteArrayOutputStream out = new ByteArrayOutputStream();
				            response.getEntity().writeTo(out);
				            out.close();
				            responseString = out.toString();
				        } else{
				            //Closes the connection.
				            response.getEntity().getContent().close();
				            
				            
				            throw new IOException(statusLine.getReasonPhrase());
				        }
				    } catch (ClientProtocolException e) {
				        Toast.makeText(getApplicationContext(), "Problem connecting to server. Please retry later", Toast.LENGTH_LONG).show();
				    } catch (IOException e) {
				        if (statusLine.getStatusCode()==404) {
				    //    	RecordProblem(true);
				        }
				    }
				    try {
						result = new JSONObject(responseString);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						throw new RuntimeException(e);
					}
				    if (result!=null){
				    //Log.d("This is the result", result.toString());
				  
				    }
				    return result;
				    
			        
				}

				protected void onPostExecute(JSONObject result) {
					
			//		if (dialog.isShowing()){
				//		dialog.dismiss();
					//}
					
					
					if (result != null) {
						
						//reset web base url
						web_url = "http://crimeiq.webfactional.com/query/nearby/";
						Intent intent = new Intent(getApplicationContext(),CrimeNearby.class);
						Iterator<String> iter = result.keys();
					    while (iter.hasNext()) {
					        String key = iter.next();
					        try {
					            Object value = result.get(key);
					            intent.putExtra(key.toString(), value.toString());
					            Log.i(key.toString(),value.toString());
					            
					        } catch (JSONException e) {
					            // Something went wrong!
					        }
					        startActivity(intent);
					    						}

	 
	    }
				}
	}
}

