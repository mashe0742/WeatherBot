//report class for parsing giant JSON file from NOAA



import java.util.ArrayList;
import java.util.List;

public class Report {

	//four root objects from json
	private String rawMessage;
	private String textDescription;
	private String temperature;
	private String dewPoint;
	private String windDirection;
	private String windSpeed;
	private String seaLevelPressure;
	private String visibility;
	private String highLast24;
	private String lowLast24;
	private String precipLast24;
	private String precipLast1;
	private String precipLast3;
	private String precipLast6;
	private String relHumidity;
	private String windChill;
	private String heatIndex;
	private String[] clouds;
	
	//empty construct, values set through setter methods
	Report(){
	}
	
	//Setter methods
	
	//set message header
	public void setMsg(String message) {
		this.rawMessage = message;
	}
	
	//set description of weather
	public void setTxt(String desc) {
		this.textDescription = desc;
	}
	
	//set temp and dew point
	public void setTemp(String temp, String dew, String hum) {
		this.temperature = temp;
		this.dewPoint = dew;
		this.relHumidity = hum;
	}
	
	//set all wind variables
	public void setWind(String dir, String speed) {
		this.windDirection = dir;
		this.windSpeed = speed;
	}
	
	//set all pressure variables
	public void setPress(String sea) {
		this.seaLevelPressure = sea;
	}
	
	//set visibility
	public void setVis(String vis) {
		this.visibility = vis;
	}
	
	//set high and low temp previous 24 hours
	public void setHiLo(String high, String low) {
		this.highLast24 = high;
		this.lowLast24 = low;
	}
	
	//set precipitation levels
	public void setPrecip(String l24, String l1, String l3, String l6) {
		this.precipLast24 = l24;
		this.precipLast1 = l1;
		this.precipLast3 = l3;
		this.precipLast6 = l6;
	}
	
	//set heat index or wind chill
	public void setHeatInd(String hInd) {
		this.heatIndex = hInd;
	}
	
	public void setWndChl(String wChil) {
		this.windChill = wChil;
	}
	
	public void setCloud(String[] cloud) {
		this.clouds = cloud;
	}
	
	//Getter methods
	
	//get message header
	public String getMsg() {
		return this.rawMessage;
	}
	
	//get description of weather
	public String getTxt() {
		return this.textDescription;
	}
	
	//get temp, humidity and dew point
	public List<String> getTemp() {
		List<String> tempArr = new ArrayList<String>();
		tempArr.add(this.temperature);
		tempArr.add(this.dewPoint);
		tempArr.add(this.relHumidity);
		return tempArr;
	}
	
	//get all wind variables
	public List<String> getWind() {
		List<String> tempArr = new ArrayList<String>();
		tempArr.add(this.windDirection);
		tempArr.add(this.windSpeed);

		return tempArr;
	}
	
	//get all pressure variables
	public String getPress() {
		return this.seaLevelPressure;
	}
	
	//get visibility
	public String getVis() {
		return this.visibility;
	}
	
	//get high and low temp previous 24 hours
	public List<String> getHiLo() {
		List<String> tempArr = new ArrayList<String>();
		tempArr.add(this.highLast24);
		tempArr.add(this.lowLast24);
		return tempArr;
	}
	
	//get precipitation levels
	public List<String> getPrecip() {
		List<String> tempArr = new ArrayList<String>();
		tempArr.add(this.precipLast24);
		tempArr.add(this.precipLast1);
		tempArr.add(this.precipLast3);
		tempArr.add(this.precipLast6);
		return tempArr;
	}
	
	//get heat index or wind chill
	String getHeInd() {
		return this.heatIndex;
	}
	
	//set heat index or wind chill
	public String getWndChl() {
		return this.windChill;
	}
	
	public String[] getCloud() {
		return this.clouds;
	}
}
