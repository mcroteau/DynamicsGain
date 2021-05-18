package go;

import go.service.StartupService;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
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
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spirit {

	public static final String USER_MAINTENANCE = "users:";

	public static final String SUPER_ROLE  = "SUPER_ROLE";
	public static final String USER_ROLE   = "USER_ROLE";

	public static final String SUPER_USERNAME = "croteau.mike@gmail.com";
	public static final String SUPER_PASSWORD = "password";

	public static final String PROSPECT_STATUS = "Prospect";
	public static final String WORKING_STATUS  = "Working";
	public static final String CUSTOMER_STATUS = "Customer";
	public static final String IDLE_STATUS     = "Idle";

	public static final String DATE_TIME  = "yyyyMMddHHmm";
	public static final String ZERO_TIME_FORMAT  = "yyyyMMdd";
	public static final String DATE_FORMAT  = "yyyyMMddHHmm";
	public static final String DATE_PRETTY  = "HH:mmaa dd MMM";

	public static final int    NOTIFICATION_JOB_DURATION = 60;
	public static final String NOTIFICATION_GROUP = "Spirit";
	public static final String NOTIFICATION_JOB = "Notification Job";
	public static final String NOTIFICATION_TRIGGER = "Trigger1";

	public static final String SMS_SERVICE_KEY   = "smsService";
	public static final String PROSPECT_REPO_KEY = "prospectRepo";



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

	public static void main(String[] args) throws Exception {

		String baseDir = ".";
		String webappDirLocation = "src/main/webapp/";
		Tomcat tomcat = new Tomcat();

		tomcat.setPort(8080);

		tomcat.setBaseDir("tomcat.8080");
		tomcat.getHost().setAppBase(baseDir);
		tomcat.enableNaming();

		StandardContext ctx = (StandardContext) tomcat.addWebapp("/a", new File(webappDirLocation).getAbsolutePath());
		ctx.setJarScanner(new StandardJarScanner());

		ClassLoader classLoader = Spirit.class.getClassLoader();
		ctx.setParentClassLoader(classLoader);

		File additionWebInfClasses = new File("output/classes");
		WebResourceRoot resources = new StandardRoot(ctx);
		resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
				additionWebInfClasses.getAbsolutePath(), "/"));
		ctx.setResources(resources);

		File webXml = new File(webappDirLocation + "WEB-INF/web.xml");
		ctx.setDefaultWebXml(webXml.getAbsolutePath());

		tomcat.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				new StartupService().shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));

		tomcat.getServer().await();

	}

}