package hacktech.stanford.margueritenow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DisplayLineY extends Activity {

	@SuppressLint("DisplayLineY")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Y LINE ", "CLICKED");
		setContentView(R.layout.activity_display_line_y);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_line_y, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Y LINE1 ", "CLICKED");
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    // Called when user clicks a stop
    public void showTime(View view) {
    	String stopTag = (String) view.getTag();									// get tag of which line
    	Intent intent;
    	if(stopTag.equals("Palo Alto Transit Center (Caltrain platform)")) {
    		Log.v(stopTag, "STOP TAG");
        	MainActivity.setBusStop("Palo Alto Transit Center (Caltrain platform)");					// keeps track of which stop
        	intent = new Intent(this, DisplayTime.class);				
    	} 
    	else if(stopTag.equals("Galvez St. @ El Camino Real")) {
    		Log.v(stopTag, "STOP TAG");
    		MainActivity.setBusStop("Galvez St. @ El Camino Real");				// keeps track of which stop
    		intent = new Intent(this, DisplayTime.class);			
    	}
    	else {//if(stopTag.equals("Schwab Center & Knight Center (Serra St.)")) {			// TEMP: commented
    		MainActivity.setBusStop("Schwab Center & Knight Center (Serra St.)");				// keeps track of which stop
    		intent = new Intent(this, DisplayTime.class);				
    	}
    	startActivity(intent);
    }
    
    public String getLineName() {
    	return "@string/LineY";
    }
}
