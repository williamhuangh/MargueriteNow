package hacktech.stanford.margueritenow;

import hacktech.stanford.margueritenow.MainActivity.ResponseReceiver;

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
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


// broadcasts HashMap<String, ArrayList<String>> map
@SuppressLint("SimpleDateFormat")
public class DownloadTask extends IntentService {

	public DownloadTask() {
		super("DownloadTask");
	}

	protected void onHandleIntent (Intent workIntent) {
		ArrayList<String> listOfTimes = new ArrayList<String>();
		ArrayList<String> listOfStops = new ArrayList<String>();
		HashMap<String, ArrayList<String>> timesMap = new HashMap<String,ArrayList<String>>();
		String currentText;
		Document doc;

		// get input
		Bundle input = workIntent.getExtras();
		String url = input.getString("url");
		Log.d("BEFORETRY", "CLICKED");

		try{
			doc = Jsoup.connect("http://transportation.stanford.edu/marguerite/y/").get();
			Log.d("PIVOT1STARTED", "CLICKED");
			Elements times = doc.getElementsByTag("td");
			Elements stops = doc.getElementsByTag("th");

			for (Element link : times){
				currentText = link.text();
				while(currentText.contains("" + '*')){
					currentText = currentText.substring(1).trim();
				}
				listOfTimes.add(currentText);
			}


			for (Element link: stops){
				if(!listOfStops.contains(link.text())){
					listOfStops.add(link.text());
				}
			}
			//					Log.e(MainActivity.getMap().toString(), "SMC");
			listOfStops.add(stops.get(0).text()+" Arrival");
			convertTimes(listOfTimes);
			//Log.v(listOfTimes.get(0), listOfTimes.get(1));
			mapTimes(listOfStops, listOfTimes, timesMap);
			orderTimes(listOfStops, timesMap);

			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
			broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
			broadcastIntent.putExtra("Bus Times", timesMap);
			sendBroadcast(broadcastIntent);
//			Log.d("PIVOT1DONE", timesMap.toString());
		} 
		catch (IOException e) {
			Log.e("Catch Thread", "Catch Balls");
		}
	}


	/* Convert to military time */ 
	private void convertTimes(ArrayList<String> listOfTimes){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();
		String milTime;
		String difference;
		int i = 0;

		for(String time : listOfTimes) {
			if (time.contains(""+'p') && !time.contains("12:")){
				milTime=""+ (Integer.parseInt(time.substring(0,time.indexOf(":"))+time.substring(time.indexOf(":")+1, time.length()-2))+1200);
				difference=calculateDifferences(dateFormat.format(cal.getTime()), milTime);
			} 
			else if (time.contains(""+'a') || time.contains("12:")){
				milTime = "" + Integer.parseInt(time.substring(0,time.indexOf(":"))+time.substring(time.indexOf(":")+1, time.length()-2));
				difference=calculateDifferences(dateFormat.format(cal.getTime()), milTime);
			} 
			else {
				difference="9999";							// Never reaches case, placeholder needs to be at end
			}
			listOfTimes.set(i, difference);
			i++;
		}
	}

	/* Calculate differences between times */
	private String calculateDifferences(String currentTime, String milTime){
		int hours;
		int minutes;

		if (milTime.length() == 4){
			hours=Integer.parseInt(milTime.substring(0,2)) - Integer.parseInt(currentTime.substring(0,2));
			minutes=Integer.parseInt(milTime.substring(2)) - Integer.parseInt(currentTime.substring(3));
			if (minutes < 0){
				minutes += 60;
				hours--;
			}
			if (hours < 0){
				hours += 24;
			}
		} 
		else {
			hours = Integer.parseInt(milTime.substring(0,1)) - Integer.parseInt(currentTime.substring(0,2));
			minutes = Integer.parseInt(milTime.substring(1)) - Integer.parseInt(currentTime.substring(3));
			if (minutes < 0){
				minutes += 60;
				hours--;
			}
			if (hours < 0){
				hours += 24;
			}
		}
		if (minutes < 10) {											// add 0s in front
			if (hours < 10) {
				return "0" + hours + "0"+minutes;
			}
			return hours + "0" + minutes;
		}
		if (hours < 10) {
			return "0" + hours + minutes;
		}
		return hours + "" + minutes;
	}

	/* Loads entire hashmap of stops and times */ 
	private void mapTimes (ArrayList<String> listOfStops, ArrayList<String> listOfTimes, HashMap<String,ArrayList<String>> timesMap){
		int index;

		for(int i = 0; i < listOfStops.size(); i++){
			timesMap.put(listOfStops.get(i), new ArrayList<String>());
		}

		for (int j = 0; j < listOfTimes.size(); j++){
			index = j % listOfStops.size();
			timesMap.get(listOfStops.get(index)).add(listOfTimes.get(j));
		}
	}

	/* Order times so shortest is at top */
	private void orderTimes(ArrayList<String> listOfStops, HashMap<String,ArrayList<String>> timesMap){
		for(String key: timesMap.keySet()){
			ArrayList list = timesMap.get(key);
			Collections.sort(list);
			timesMap.put(key, list);
		}
	}
};

