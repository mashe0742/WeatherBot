//conversions and small data processing



public class Conversions {

	public static String cToF(String celsius) {
		String result;
		try {
			double tempF = (9.0/5.0)*(Double.valueOf(celsius)) + 32;
			result = String.format("%.2f", tempF);
		} catch (NumberFormatException e) {
			return "null";
		}
		return result;
	}
	
	public static String round(String num) {
		String result = String.format("%.2f", Double.parseDouble(num));
		return result;
	}
	
	public static String pascalToMillibar(String num) {
		double conv = Double.parseDouble(num) / 100;
		String result = String.format("%.2f", conv);
		return result;
	}
	
	public static String kilometerToMile(String num) {
		double conv = (Double.parseDouble(num) / 1000) * 0.6213711922;
		String result = String.format("%.2f", conv);
		return result;
	}
	
	public static String metPSecToMiPHr(String num) {
		double conv = Double.parseDouble(num) * 2.236936;
		String result = String.format("%.2f", conv);
		return result;
	}
}
