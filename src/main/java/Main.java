/*Main class for twitter weatherbot*/



public class Main {

	public static void main(String[] args) throws Exception {
		//perform intial setup
		Keys keys = new Keys("keys.txt");
		keys.parseKeys();
		NOAARequest.setupData();
		
		//coordinates in the format of lat, lon
		//Chicago, IL:  "41.8781° N, 87.6298° W"
		//Baltimore, MD: "39.2904° N, 76.6122° W"
		//Truckee, CA: "39.3280° N, 120.1833° W"
		//STAMPEDE 1: "39.471094° N, 120.086975° W"
		String coords = "39.2904° N, 76.6122° W";
				
		//set up NWS request and retrieve data
		NOAARequest NOAA = new NOAARequest(keys.getNOAA(), coords);
		Report outRep = NOAA.ParseData(NOAA.getObservations());
		
		//test alert retrieval
		//System.out.println(NOAA.getWarnings(NOAARequest.getState("Nevada")));
		//TextToJpg.makeImageFromText(NOAA.getWarnings(NOAARequest.getState("Nevada")));
		
		//twitterConnect.checkWall();
		twitterConnect.updateStatus(twitterConnect.generateStatus(outRep, NOAA.getLoc()));
		//System.out.println(twitterConnect.generateStatus(outRep, NOAA.getLoc()));

	}

}
