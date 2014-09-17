package hacktech.stanford.margueritenow;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;


@SuppressLint("SimpleDateFormat")
public class MainActivity extends Activity {

	public final static String MAP = "hacktech.stanford.margueritenow.TIMEMAP";
	private static HashMap<String, ArrayList<String>> timesMap;
	private static String busLine, busStop, url;

	private ResponseReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);				// register receiver
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new ResponseReceiver();
		registerReceiver(receiver, filter);
		
		timesMap = new HashMap<String,ArrayList<String>>();
		ArrayList<String> test = new ArrayList<String>();
		test.add("3");
		timesMap.put("test", test);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// Called when user clicks a line
	public void showLine(View view) {
		String line = (String) view.getTag();									// get tag of which line
		Intent intent;
		if(line.equals("Line Y")) {												// compare with 
			intent = new Intent(this, DisplayLineY.class);		
			setBusLine("@string/LineY");
			setUrl("http://transportation.stanford.edu/marguerite/y/");
		} 
		else if(line.equals("Line X")) {
			intent = new Intent(this, DisplayLineX.class);	
			setBusLine("@string/LineX");
			setUrl("http://transportation.stanford.edu/marguerite/x/");
		}
		else {//if(line.equals("SLAC")) {										// TEMP: commented
			intent = new Intent(this, DisplaySLAC.class);
			setBusLine("@string/SLAC");
			setUrl("http://transportation.stanford.edu/marguerite/slac/");
		}
		startActivity(intent);
		Log.d("PIVOT0", "CLICKED");
		Intent mServiceIntent = new Intent(this, DownloadTask.class);								// Can't call Jsoup in main	
		mServiceIntent.putExtra("url", url);														// pass in url string
		startService(mServiceIntent); 
	}

	public static HashMap<String, ArrayList<String>> getMap() {							// switch to getBusTimes
		return timesMap;
	}

	public static void setMap(HashMap<String,ArrayList<String>> map) {
		timesMap = map;
	}
	
	public static void setBusLine(String line) {
		busLine = line;
	}

	public static String getBusLine() {
		return busLine;
	}

	public static void setBusStop(String stop) {
		busStop = stop;
	}

	public static String getBusStop() {
		return busStop;
	}

	public static void setUrl(String urlString) {
		url = urlString;
	}

	public static String getUrl() {
		return url;
	}

	// Receiving the broadcast 
	public class ResponseReceiver extends BroadcastReceiver {
		public static final String ACTION_RESP = "com.hacktech.intent.action.MESSAGE_PROCESSED";

		@Override
		public void onReceive(Context context, Intent intent) {
			HashMap<String, ArrayList<String>> map = (HashMap<String, ArrayList<String> >) intent.getSerializableExtra("Bus Times");
			MainActivity.setMap(map);
			Log.d("PIVOT2", getMap().toString());
		}
	}
}
