/*Class for connecting and posting to twitter*/


import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class twitterConnect {
	
	//configuration builder
	private static ConfigurationBuilder configurationBuilder = (new ConfigurationBuilder()
	            .setOAuthConsumerKey(Keys.getTwitterConsumer().get(0))
	            .setOAuthConsumerSecret(Keys.getTwitterConsumer().get(1))
	            .setOAuthAccessToken(Keys.getTwitterAccess().get(0))
	            .setOAuthAccessTokenSecret(Keys.getTwitterAccess().get(1)));
	
	//create twitter instance
	private static Twitter mTwitter = new TwitterFactory(configurationBuilder.build()).getInstance();
	
	//method for generating the status
	public static String generateStatus(Report wxRep, String location) {
		//check for heat index or wind chill
		String relativeTempMess = "Blank";
		
		String windChill = wxRep.getWndChl();
		String heatIndex = wxRep.getHeInd();
		
		System.out.println(windChill);
		System.out.println(heatIndex);
		
		if((wxRep.getWndChl() == null || wxRep.getWndChl().equalsIgnoreCase("null")) & (wxRep.getHeInd() == null || wxRep.getHeInd().equalsIgnoreCase("null"))) {
			return String.format("Current conditions at %s are: %s\nTemperature is: %s°F\nDew Point is: %s°F\nRelative Humidity is: %s Percent\nWind Speed is: %s MPH\nBarometric Pressure is: %s Mb",
					location, wxRep.getTxt(),Conversions.cToF(wxRep.getTemp().get(0)),  Conversions.cToF(wxRep.getTemp().get(1)), Conversions.round(wxRep.getTemp().get(2)),Conversions.metPSecToMiPHr((wxRep.getWind().get(1))), Conversions.pascalToMillibar(wxRep.getPress()));
		}else if(wxRep.getWndChl() == null || wxRep.getWndChl().equalsIgnoreCase("null")){
			if(wxRep.getHeInd() != null) {
				relativeTempMess = String.format("Heat Index is: %s°F", Conversions.cToF(wxRep.getHeInd()));
			}
		}else if(wxRep.getHeInd() == null || wxRep.getHeInd().equalsIgnoreCase("null")) {
			if(wxRep.getWndChl() != null) {
				relativeTempMess = String.format("Wind Chill is: %s°F", Conversions.cToF(wxRep.getWndChl()));
			}
		}
		//set relative temperature message in output if relative temperature is known
		return (String.format("Current conditions at %s are: %s\nTemperature is: %s°F\n%s\nDew Point is: %s°F\nRelative Humidity is: %s Percent\nWind Speed is: %s MPH\nBarometric Pressure is: %s Mb",
					location, wxRep.getTxt(),Conversions.cToF(wxRep.getTemp().get(0)), relativeTempMess, Conversions.cToF(wxRep.getTemp().get(1)), Conversions.round(wxRep.getTemp().get(2)),Conversions.metPSecToMiPHr((wxRep.getWind().get(1))), Conversions.pascalToMillibar(wxRep.getPress())));
	}
	
	//method for updating twitter status
    public static void updateStatus(String statusMsg) throws TwitterException {
        //update status
        Status status = mTwitter.updateStatus(statusMsg);
        System.out.println("Successfully updated status to [" + status.getText() + "].");
    }

    //Placeholder for reply method
    public static void replyToTweet(Status status) throws TwitterException {
     	//method tbd
    }
}
