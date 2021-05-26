package go;

import go.service.StartupService;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.tomcat.util.scan.StandardJarScanner;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spirit {

	public static final String GO_FUTURES = "success";

	public static final String USER_MAINTENANCE = "users:";

	public static final String SUPER_ROLE   = "SUPER_ROLE";
	public static final String DONOR_ROLE   = "DONOR_ROLE";
	public static final String CHARITY_ROLE = "CHARITY_ROLE";

	public static final String SUPER_USERNAME = "croteau.mike@gmail.com";
	public static final String SUPER_PASSWORD = "password";

	public static final String DATE_FORMAT  = "yyyyMMddHHmm";
	public static final String DATE_PRETTY  = "HH:mmaa dd MMM";




	public static boolean isValidMailbox(String str){
		EmailValidator validator = EmailValidator.getInstance();
		return validator.isValid(str);
	}

	public static int getNumber(int max){
		Random r = new Random();
		return r.nextInt(max);
	}

	public static boolean containsSpecialCharacters(String str) {
		Pattern p = Pattern.compile("[^A-Za-z0-9]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		return m.find();
	}

	private static String getExtension(final String path) {
		String result = null;
		if (path != null) {
			result = "";
			if (path.lastIndexOf('.') != -1) {
				result = path.substring(path.lastIndexOf('.'));
				if (result.startsWith(".")) {
					result = result.substring(1);
				}
			}
		}
		return result;
	}

	public static String getString(int n) {
		String CHARS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
		StringBuilder uuid = new StringBuilder();
		Random rnd = new Random();
		while (uuid.length() < n) {
			int index = (int) (rnd.nextFloat() * CHARS.length());
			uuid.append(CHARS.charAt(index));
		}
		return uuid.toString();
	}

	public static long getDate(){
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Spirit.DATE_FORMAT);
		String date = dtf.format(ldt);
		return Long.parseLong(date);
	}

	public static long getDateTimezone(String timezone){
		LocalDateTime ldt = LocalDateTime.now();
		ZoneId zone = ZoneId.systemDefault();
		ZoneOffset zoneOffset = zone.getRules().getOffset(ldt);
		ZonedDateTime zdt = ldt.atOffset(zoneOffset)
							.atZoneSameInstant(ZoneId.of(timezone));
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Spirit.DATE_FORMAT);
		String date = dtf.format(zdt);
		return Long.parseLong(date);
	}

	public static long getDateTimezoneMins(int mins, String timezone){
		LocalDateTime ldt = LocalDateTime.now().plusMinutes(mins);
		ZoneId zone = ZoneId.systemDefault();
		ZoneOffset zoneOffset = zone.getRules().getOffset(ldt);
		ZonedDateTime zdt = ldt.atOffset(zoneOffset)
				.atZoneSameInstant(ZoneId.of(timezone));
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Spirit.DATE_FORMAT);
		String date = dtf.format(zdt);
		return Long.parseLong(date);
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static String getPretty(Long date){
		String dateString = "";
		try {
			SimpleDateFormat parser = new SimpleDateFormat(Spirit.DATE_FORMAT);
			Date d = parser.parse(Long.toString(date));

			SimpleDateFormat sdf2 = new SimpleDateFormat(Spirit.DATE_PRETTY);
			dateString = sdf2.format(d);
		}catch(Exception ex){}
		return dateString;
	}

	public static String pad(String value, int places, String character){
		if(value.length() < places){
			value = character.concat(value);
			pad(value, places, character);
		}
		return value;
	}

	public static Map<String, String[][]> getNations() {
		Map<String, String[][]> locations = new HashMap<>();
		String[][] usStates = {
			{
				"Alabama", "Alaska", "American Samoa", "Arizona", "Arkansas",
				"California", "Colorado", "Connecticut",
				"Delaware", "District of Columbia",
				"Florida",
				"Georgia", "Guam",
				"Hawaii",
				"Idaho", "Illinois", "Indiana", "Iowa",
				"Kansas", "Kentucky",
				"Louisiana",
				"Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana",
				"Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Northern Mariana Islands",
				"Ohio", "Oklahoma", "Oregon",
				"Pennsylvania", "Puerto Rico",
				"Rhode Island",
				"South Carolina", "South Dakota",
				"Tennessee", "Texas",
				"Utah",
				"Vermont", "Virgin Islands", "Virginia",
				"Washington", "West Virginia", "Wisconsin", "Wyoming"
			},
			{
				"AL", "AK", "AS", "AZ", "AR",
				"CA", "CO", "CT",
				"DE", "DC",
				"FL",
				"GA", "GU",
				"HI",
				"ID", "IL", "IN", "IA",
				"KS", "KY",
				"LA",
				"ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT",
				"NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "MP",
				"OH", "OK", "OR",
				"PA", "PR",
				"RI",
				"SC", "SD",
				"TN", "TX",
				"UT",
				"VT", "VI", "VA",
				"WA", "WV", "WI", "WY"
			}
		};
		locations.put("USA", usStates);

		return locations;
	}

}