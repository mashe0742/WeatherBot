/*Class for making request to NOAA API*/


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class NOAARequest {

	// store API key read from disk
	private String apiKey;
	// which station to retrieve data from
	private String coords;
	//where to store metadata
	private String[] stationMeta;
	//files for hashmap population of geo reference data
	private static String filePath = new File("").getAbsolutePath();
	private static File stateAbbrsFile = new File (String.format("%s/src/resources/configFileStatesAbbr.txt", filePath));
	private static HashMap<String, String> stateAbrs = new HashMap<String,String>();
	
	// constructor for request to NWS API
	NOAARequest(String key, String inpCoords) throws Exception {
		// build headers for API
		apiKey = key;
		coords = inpCoords;
		
		//array to hold location data
		stationMeta = new String[2];
	}
	
	//getters for station metadata to pretty-up report
	String getStationID() {
		return this.stationMeta[1];
	}
	
	String getLoc() {
		return this.stationMeta[0];
	}
	
	//method for general data setup
	//currently reads hashmap for state names and abbreviations to process requests for named states
	static void setupData() {
		try {
			//parse out each key, with the key being the title containing the type, and value being the actual API key
			FileInputStream fis = new FileInputStream(stateAbbrsFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			
			//read the config file to put all state abbreviations and names into the hashmap
	        String line = reader.readLine();
	        while(line != null){
	            String[] fmt = line.split(",");
	            stateAbrs.put(fmt[0],fmt[1]);
	            line = reader.readLine();
	        }    
	        System.out.println("Geofile Read!");
	        reader.close();
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	static String getState(String stateName) {
		
		//sanitize input, lookup state in hashmap
		stateName = stateName.replaceAll("\\s","");
		stateName = stateName.substring(0,1).toUpperCase() + stateName.substring(1);
		System.out.println(stateName);
		String abbrv = stateAbrs.get(stateName);
		
		return abbrv;
	}
	
	//method to get station id from coordinates (TODO: implement ability to take a user's location from tweet as input)
	String[] getStationID(String coordinates) throws Exception {
		// format coordinate input string
		String[] coordSplit = coordinates.split(",");

		String lat = coordSplit[0].replaceAll("[^\\d.]", "");
		String lon = String.format("-%s", coordSplit[1].replaceAll("[^\\d.]", ""));

		String[] coordCorrected = { lat, lon };

		String coordFmt = String.join("%2C", coordCorrected);
		
		//"https://api.weather.gov/points/%s/stations",coordFmt
		// build URL, then connect and retrieve JSON
		String url = String.format("https://api.weather.gov/points/%s/stations",coordFmt);
		URL latestAPI = new URL(url);
		// url connection builder and custom user-agent string
		URLConnection nwsCon = latestAPI.openConnection();
		nwsCon.setRequestProperty("User-Agent", String.format(
				"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2, API Key:%s",
				this.apiKey));
		// use GSON to parse the returned data
		JsonParser jp = new JsonParser();
		JsonElement root = jp.parse(new InputStreamReader((InputStream) nwsCon.getContent()));
		JsonArray data = root.getAsJsonObject().getAsJsonArray("features");
		
		//parse out required station and location name
		JsonObject properties = (JsonObject) data.get(1);
		JsonObject subProps = properties.getAsJsonObject("properties");
		String locName = subProps.get("name").getAsString();
		String statID = subProps.get("stationIdentifier").getAsString();
		
		//save in list and return
		String[] stationMetadata = new String[]{locName,statID};

		return stationMetadata;
	}
	
	//TODO: method for weekly forecast
	
	//method for retrieving weather warnings for a given area, returns all warnings in array
	//TODO: smart parsing for only warnings that apply to requested area
	//TODO: caching of stored warnings by area, allow for lookup on non-expired warnings and watches
	ArrayList<String> getWarnings(String area) throws Exception{
		// url to API for warnings - warnings can only be pulled from state or marine area level
		String url = String.format("https://api.weather.gov/alerts/active/area/%s",area);
		URL latestAPI = new URL(url);
		// url connection builder and custom user-agent string
		URLConnection nwsCon = latestAPI.openConnection();
		nwsCon.setRequestProperty("User-Agent", String.format(
				"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2, API Key:%s",
				this.apiKey));
		// use GSON to parse the returned data
		JsonParser jp = new JsonParser();
		JsonElement root = jp.parse(new InputStreamReader((InputStream) nwsCon.getContent()));
		JsonArray warningsJsonArr = root.getAsJsonObject().getAsJsonArray("features");
		
		ArrayList<String> warningsComplete = new ArrayList<String>();
		
		for (int i = 0; i < warningsJsonArr.size(); i++) {
				JsonElement warningJsonEl = warningsJsonArr.get(i).getAsJsonObject().get("properties");
				JsonElement warningParams = warningsJsonArr.get(i).getAsJsonObject().get("properties").getAsJsonObject().get("parameters");
	            String warnArea = warningJsonEl.getAsJsonObject().get("areaDesc").getAsString();
	            String warnHeadline;
	            if(warningParams.getAsJsonObject().getAsJsonArray("NWSheadline") != null) {
	            	warnHeadline = warningParams.getAsJsonObject().getAsJsonArray("NWSheadline").getAsString();
	            } else {
	            	warnHeadline = "";
	            }
	            String warnComp = String.format("For the following areas in %s: %s:\n %s\n", "<STATENAME>", warnArea, warnHeadline);
	            warningsComplete.add(warnComp);
	        }
		
		return warningsComplete;
	}

	// method for retrieving observation json data from the API
	JsonObject getObservations() throws Exception {
		//parse coordinates to retrieve station ID, store
		//I know this is hacky right now, fixing it is a priority
		String [] stMeta = getStationID(this.coords);
		String stationID = stMeta[1];
		this.stationMeta = stMeta;
		// build URL, then connect and retrieve JSON
		String url = String.format("https://api.weather.gov/stations/%s/observations/latest", stationID);
		URL latestAPI = new URL(url);
		// url connection builder and custom user-agent string
		URLConnection nwsCon = latestAPI.openConnection();
		nwsCon.setRequestProperty("User-Agent", String.format(
				"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2, API Key:%s",
				this.apiKey));
		// use GSON to parse the returned data
		JsonParser jp = new JsonParser();
		JsonElement root = jp.parse(new InputStreamReader((InputStream) nwsCon.getContent()));
		JsonObject data = root.getAsJsonObject().getAsJsonObject("properties");

		return data;
	}


	// populate wxRep with data from getData
	Report ParseData(JsonObject input) {

		// Make new Report
		Report wxRep = new Report();

		// set values of wxRep
		try {
		wxRep.setMsg(input.getAsJsonPrimitive("rawMessage").getAsString());
		} catch (Exception e) {
			wxRep.setMsg("No Message Available");
		}
		try {
			wxRep.setTxt(input.getAsJsonPrimitive("textDescription").getAsString());
		}catch (Exception e) {
			wxRep.setTxt("No Description Available");
		}
		try {
			wxRep.setTemp(input.getAsJsonObject("temperature").getAsJsonPrimitive("value").getAsString(),
					input.getAsJsonObject("dewpoint").getAsJsonPrimitive("value").getAsString(),
					input.getAsJsonObject("relativeHumidity").getAsJsonPrimitive("value").getAsString());
		} catch (Exception e2) {
			wxRep.setTemp("0", "0", "0");
		}
		try {
			wxRep.setWind(input.getAsJsonObject("windDirection").getAsJsonPrimitive("value").getAsString(),
					input.getAsJsonObject("windSpeed").getAsJsonPrimitive("value").getAsString());
		} catch (Exception e1) {
			wxRep.setWind("0", "0");
		}
		try {
			wxRep.setPress(input.getAsJsonObject("barometricPressure").getAsJsonPrimitive("value").getAsString());
		} catch (Exception e) {
			wxRep.setPress("0");
		}
		try {
			wxRep.setVis(input.getAsJsonObject("visibility").getAsJsonPrimitive("value").getAsString());
		} catch (Exception e) {
			wxRep.setVis("0");
		}
		try {
			wxRep.setHeatInd(input.getAsJsonObject("heatIndex").getAsJsonPrimitive("value").getAsString());
		} catch (Exception e) {
			wxRep.setHeatInd("null");
		}
		try {
			wxRep.setWndChl(input.getAsJsonObject("windChill").getAsJsonPrimitive("value").getAsString());
		} catch (Exception e) {
			wxRep.setWndChl("null");
		}

		return wxRep;
	}

}
