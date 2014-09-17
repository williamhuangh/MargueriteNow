package hacktech.stanford.margueritenow;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayTime extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_display_time);
		
		String busStop = MainActivity.getBusStop();
		Log.d("YEAH", busStop);
		ArrayList<String> times = new ArrayList<String>();
		Log.d("PIVOT3", MainActivity.getMap().get(busStop).toString());
		for(int i = 0; i < 3; i++) {
			times.add(MainActivity.getMap().get(busStop).get(i));
		}
		// Create the text view
	    TextView textView = (TextView) findViewById(R.id.time1);
	    textView.setTextSize(20);
//	    for (int i = 0; i < 3; i++) {
	    	textView.setText(times.get(0));
//	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_time, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
