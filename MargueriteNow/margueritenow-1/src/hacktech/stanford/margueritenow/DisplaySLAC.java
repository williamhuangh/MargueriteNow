package hacktech.stanford.margueritenow;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class DisplaySLAC extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_slac);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_slac, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    // Called when user clicks a stop
    public void showTime(View view) {
    	String stopTag = (String) view.getTag();									// get tag of which line
    	Intent intent;
//    	if(stopTag.equals("@string/Palo_Alto_Transit_Center")) {
        	MainActivity.setBusStop("@string/Palo_Alto_Transit_Center");					// keeps track of which stop
        	intent = new Intent(this, DisplayTime.class);				// TODO
//    	} 
//    	else if(stopTag.equals("@string/Galvez_St._at_El_Camino_Real")) {
//    		MainActivity.setBusStop("@string/Galvez_St._at_El_Camino_Real");				// keeps track of which stop
//    		intent = new Intent(this, DisplayTime.class);				// TODO
//    	}
//    	else {//if(stopTag.equals("@string/Schwab_Center_and_Knight_Center")) {			// TEMP: commented
//    		MainActivity.setBusStop("@string/Schwab_Center_and_Knight_Center");				// keeps track of which stop
//    		intent = new Intent(this, DisplayTime.class);				// TODO
//    	}
    	startActivity(intent);
    }
    
    public String getLineName() {
    	return "@string/SLAC";
    }
}
