package com.trench.crimeiq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;




public class UserLocation extends Activity  {
    //Button addressButton;
    //Button getRiskButon;
    //Button getPhoneLocation;
    //ImageButton mainNavigation;
    EditText addressText;
    private GoogleMap map;
    //LatLng p1;
    Double lat = new Double(-30.89);
	Double longitude = new Double(24.26);
	//GPSTracker gps;
	//String data = null;
	final String web_url = "http://crimeiq.webfactional.com/query/";
	String final_web_url = "";
	Spinner crimespin;
	String selected="";//Frekin hell! To get this variable to be read from OnItemSelected
						///Declare it here and DONT make it PUBLIC or BUGGER ALL
	
	String selected_id;
	Dialog menuDialog;
	Dialog addressDialog;
	
	Map<String, Integer> crimes = new HashMap<String, Integer>();
	//static InputStream is = null;
	//JSONObject result = null;
	//static String json ="";
	String cleanAddress;
	

	//JSON Node names
	
	private static final String TAG_ID = "id";
	private static final String TAG_STATION_ID = "station_id";
	private static final String TAG_POP_TOTAL = "pop_total";
	private static final String TAG_STATION_NAME = "station_name";
	private static final String TAG_CRIME = "crime";
	private static final String TAG_NUMBER_2011 = "number_2011";
	private static final String TAG_NUMBER_2012 = "number_2012";
	private static final String TAG_RATE_2012_10000 = "rate_2012_10000";
	private static final String TAG_RATE_2012_1000 = "rate_2012_1000";
	private static final String TAG_RATE_2011_10000 = "rate_2011_10000";
	private static final String TAG_RATE_2011_1000 = "rate_2011_1000";
	private static final String TAG_COORDINATES = "coordinates";
	
	// contacts JSONArray
	JSONArray crime_record = null;
	
	
	
	
	//**On create method 
		
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_location);
		
		popCrimeHashMap();
		
		
	
		
		//Set up initial map fragment view
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		//LatLng initialPoint= new LatLng(lat,longitude);
		LatLng startPoint = new LatLng(-30.89,24.26);
		
		//call animateMap method
		animateMapCamera(startPoint, 6);
		
			   
		
		
		
		//END OF onCREATE
		}
	
	 public void animateMapCamera(LatLng locationPoint, Integer zoom) {
		 CameraPosition cameraPosition = new CameraPosition.Builder().target(locationPoint).tilt(60).zoom(zoom).bearing(0).
		            build();
		    map.addMarker(new MarkerOptions().position(locationPoint));
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	 }
	
	
	//launch search dialog for address
	public void OnClick_Load_LocateAddress (View v) {
		
		
		addressDialog = new Dialog(this);
		addressDialog.setContentView(R.layout.activity_custom_dialog);
		
		//menuDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		//menuDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		addressDialog.setContentView(getLayoutInflater().inflate(R.layout.activity_locate_address,null));
		
		addressDialog.show();
		
	}
		
	//GeoCoder for entered Address
	public void OnClick_LocateAddress(View v){
		
			
		
		if (UserLocation.this.addressDialog.isShowing()==true) {
			UserLocation.this.addressDialog.dismiss();
		}
		map.clear();
		
		addressText = (EditText) addressDialog.findViewById(R.id.editText1);		
	//	addressText = (EditText)findViewById(R.id.editText1);
			
		cleanAddress = addressText.getText().toString()+" South Africa";
		Log.i("Clean address", cleanAddress.toString());
		new GetAddress().execute();
	}
	


	//Calls private class to get policing precinct boundaries
	
	public void OnAddressLocates (Boolean result) {
		if (result == true) {
			new GetPoly().execute();
		}
	}
	
	
	//Method to show a problem retrieving a record
	
	public void RecordProblem(Boolean result) {
		if (result == true) {
		
		//Toast.makeText(getApplicationContext(), "Problem locating record. Please try another", Toast.LENGTH_LONG).show();
	}
	}
	

public void OnClick_GetMyRisk (View v) {
		
	Intent crimelistintent = new Intent(getApplicationContext(),CrimeList.class);
	//Log.i("passing variables to crime list view", lat.toString());
	
	
	startActivityForResult(crimelistintent, 1);
	
}
	
	@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if(requestCode == 1 && resultCode == RESULT_OK){
	        selected_id = data.getStringExtra("crimecatid");
	        new GetData().execute();
	    }
	}
	public void popCrimeHashMap() {
		//HashMap for crimes and category numbers
		
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

	}


	
	
		
				private class GetAddress extends AsyncTask<String,LatLng, LatLng> {
	private ProgressDialog dialog = new ProgressDialog(UserLocation.this) ;
	// android.R.style.Theme_Translucent);
	Geocoder coder = new Geocoder(UserLocation.this, Locale.getDefault()); 
			
			
			protected void onPreExecute() {
								
			this.dialog.setMessage("Locating address...");
			this.dialog.show();
			}
			
			
			@Override
						
			protected LatLng doInBackground(String... arg0) {
				try {
					
					List<Address> address = coder.getFromLocationName(cleanAddress,1);
					Log.i("Address item", address.toString());
					if (address!=null){
						lat = (address.get(0).getLatitude());
						
						longitude = (address.get(0).getLongitude());
						Log.i("Latitude", lat.toString());
						Log.i("Longitude", longitude.toString());
						
					} 
				}catch (IOException e){
					RecordProblem(true);
						e.printStackTrace();
					}
				
				LatLng p1 = new LatLng(lat,longitude);
				
				return p1;
			}
			
			@Override
			protected void onPostExecute(LatLng p1) {
				//Stops the dialog progress spinner
				if (dialog.isShowing()){
					dialog.dismiss();
				}
				
				//updates camera position 
				
				animateMapCamera(p1, 14);
				
				UserLocation.this.OnAddressLocates(true);

					
		}
	}


	//Get geo-cords of phone;
	public void OnClick_LocatePhone(View v) {
		
		map.clear();
		GPSTracker gps = new GPSTracker(this);
				
		if (gps.canGetLocation()){
			lat = gps.getLatitude();
			longitude = gps.getLongitude();
			
			LatLng phoneLocation = new LatLng(lat,longitude);
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			//LatLng initialPoint= new LatLng(lat,longitude);
			
			//call inner class for getting precinct boundaries
			new GetPoly().execute();
			
			animateMapCamera(phoneLocation, 14);

					
		
		}
		if (gps.canGetLocation==false){
			gps.showSettingsAlert();
			lat = gps.getLatitude();
			longitude = gps.getLongitude();
			
			LatLng phoneLocation = new LatLng(lat,longitude);
			gps.stopUsingGPS();
			
			
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			//LatLng initialPoint= new LatLng(lat,longitude);
				
			animateMapCamera(phoneLocation, 14);
						
		
		}
		
		
	}

		
	
	//*private class to fetch the web data asynchronously      
private class GetData extends AsyncTask<URL,JSONObject,JSONObject> {
	//this progressDialog does a funky little spinner without having to define it in layout.xml the android.R. stuff makes it translucent
	//TODO: This should be turned into a method as it used several times in the code
	private ProgressDialog dialog = new ProgressDialog(UserLocation.this) ;
	private StatusLine statusLine;//To check status of HTTPResponse
	 
			
			
			protected void onPreExecute() {
				//runs dialog spinner on preexecute --> closes it on postExecute
				this.dialog.setMessage("Getting crime data...");
				this.dialog.show();
			}
			@Override
			//This is the core code to get json data from the website and check for any errors
			protected JSONObject doInBackground(URL... url) {
				//Concatenates weburl with diff data for web fetch
				final_web_url = web_url+longitude+"+"+lat+"/"+selected_id;
				String uri = final_web_url;
				Log.d("Web url =", uri);
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
			        	RecordProblem(true);
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
				
				if (dialog.isShowing()){
					dialog.dismiss();
				}
				
				
				if (result != null) {
					
					//reset web base url
					final_web_url = "http://crimeiq.webfactional.com/query/";
					Intent intent = new Intent(getApplicationContext(),ViewCrimes.class);
					
					try {
						String police_station = result.getString(TAG_STATION_NAME);
						Log.d("Cop station name sending on ", police_station);
						intent.putExtra("police_station", police_station);
						
						String crimes_2012 = result.getString(TAG_NUMBER_2012);
						intent.putExtra("crimes_2012", crimes_2012);
						
						String crimes_2011 = result.getString(TAG_NUMBER_2011);
						intent.putExtra("crimes_2011", crimes_2011);
						
						String rate_2012 = result.getString(TAG_RATE_2012_10000);
						intent.putExtra("rate_2012", rate_2012);
						
						String rate_2011 = result.getString(TAG_RATE_2011_10000);
						intent.putExtra("rate_2011", rate_2011);
						
						String crime = result.getString(TAG_CRIME);
						intent.putExtra("crime", crime);
						
						String total_pop = result.getString(TAG_POP_TOTAL);
						intent.putExtra("pop_total", total_pop);
						
						String rate_2011_1000 = result.getString(TAG_RATE_2011_1000);
						intent.putExtra("rate_2011_1000",rate_2011_1000);
						
						String rate_2012_1000 = result.getString(TAG_RATE_2012_1000);
						intent.putExtra("rate_2012_1000", rate_2012_1000);
						//next three put lat and long crime cat values for the nearby search function
						intent.putExtra("geo1", lat);
						intent.putExtra("geo2", longitude);
						intent.putExtra("crime2", selected_id);
						
						JSONObject co_ords = result.getJSONObject(TAG_COORDINATES);
						
						//Log.d("Results",result.toString());
						
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					
					
					
					startActivity(intent);
					//finish();
				
			
				}
			}
}
//Private class to get precinct polygon coords

private class GetPoly extends AsyncTask<URL,JSONObject,JSONObject> {
	
	private StatusLine statusLine;//To check status of HTTPResponse
	 
			
			
			protected void onPreExecute() {
				//runs dialog spinner on preexecute --> closes it on postExecute
	//			this.dialog.setMessage("Getting data");
		//		this.dialog.show();
			}
			@Override
			//This is the core code to get json data from the website and check for any errors
			protected JSONObject doInBackground(URL... url) {
				//Concatenates weburl with diff data for web fetch
				final_web_url = web_url+longitude+"+"+lat;
				String uri = final_web_url;
				Log.d("Web url =", uri);
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
			        	//Toast.makeText(getApplicationContext(), "Sorry. No record for that query. Try another", Toast.LENGTH_LONG).show();
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
				
				
				if (result != null) {
					crunchPoly(result);
					}
			}
				
					public void crunchPoly(JSONObject result) {

						//reset web base url
						final_web_url = "http://crimeiq.webfactional.com/query/";
						List<LatLng> points = new ArrayList<LatLng>();
						try {
							JSONArray cords = result.getJSONArray("coordinates");
							//Log.i("Coords from fetch",cords.toString());
							Integer ab = cords.length();
							//Log.i("Array length ", ""+ab);
							for (int i=0; i< cords.length();i++){
								//hacky extraction of lat and long values
								String avalue = (String) cords.get(i).toString().split(",")[0].replace("[", "");
								String bvalue = (String) cords.get(i).toString().split(",")[1].replace("]", "");
								Double lat_val = Double.parseDouble(avalue);
								Double long_val = Double.parseDouble(bvalue);
								
								points.add(new LatLng(long_val,lat_val)); 
								//Log.i("String avalue", avalue.toString());
								//Log.i("Double value", bvalue.toString());
											
							}
								} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
								}
						
						//Log.i("Points parsed", points.toString());
						 
						    PolygonOptions options = new PolygonOptions();
						    options.addAll(points);
						    options.strokeWidth(2);
						    options.fillColor(0x55FF6600);
						    map.addPolygon(options);
					}
				}

				}
			






	
	
					



	
	
    
			