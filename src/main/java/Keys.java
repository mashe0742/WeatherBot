/*Parse API keys from a given text file*/


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Keys {

	//store name of keyfile, allows for saving at arbitrary location
	private String fileName;
	private String filePath = new File("").getAbsolutePath();
	private String NOAA;
	private String twitterConsumerAPI;
	private String twitterConsumerAPISecret;
	private String twitterAccessToken;
	private String twitterAccessTokenSecret;
	private static List<String> consKeysList = new ArrayList<String>();
	private static List<String> accKeysList = new ArrayList<String>();


	//constructor for keyring
	Keys(String file){
		fileName = file;
	}
	
	//set NOAA key
	public void setNOAA(String input) {
		this.NOAA = input;
	}
	
	//set Twitter Consumer keys
	public void setTwitterConsumer(String consumerAPI, String consumerAPISecret) {
		this.twitterConsumerAPI = consumerAPI;
		this.twitterConsumerAPISecret = consumerAPISecret;
	}
	
	//set Twitter Access keys
	public void setTwitterAccess(String accessToken, String accessTokenSecret) {
		this.twitterAccessToken = accessToken;
		this.twitterAccessTokenSecret = accessTokenSecret;
	}

	//getter methods for NOAA Key
	public String getNOAA() {
		return this.NOAA;
	}
	
	//getter methods for Twitter Consumer keys
	public static List<String> getTwitterConsumer() {
		return consKeysList;
	}
	
	//getter methods for Twitter Access keys
	public static List<String> getTwitterAccess() {
		return accKeysList;
	}
	
	//parse file for keys
	public void parseKeys(){
		//use properties to read the keys.txt file
		Properties prop = new Properties();
		try {
			//parse out each key, with the key being the title containing the type, and value being the actual API key
			FileInputStream fis = new FileInputStream(filePath + "/src/keys/" + this.fileName);
			prop.load(fis);
			//set the keys
			this.setNOAA(prop.getProperty("key.NOAA"));
			this.setTwitterAccess(prop.getProperty("key.TwitterAccessToken"), prop.getProperty("key.TwitterAccessTokenSecret"));
			this.setTwitterConsumer(prop.getProperty("key.TwitterConsumerAPI"), prop.getProperty("key.TwitterConsumerAPISecret"));
			consKeysList.add(this.twitterConsumerAPI);
			consKeysList.add(this.twitterConsumerAPISecret);
			accKeysList.add(this.twitterAccessToken);
			accKeysList.add(this.twitterAccessTokenSecret);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
