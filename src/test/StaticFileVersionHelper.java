package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class StaticFileVersionHelper {

	private static boolean isDevMode = false;

	private static String version;
	private static final String ADD_URL = "http://localhost:8080/test/getversion.jsp";
	private static final Timer TIMER = new Timer();

	static {
		version = getCurrentTimeVersion();
		startCronVersionService();
	}

	/**
	 * 系统启动时，应调用该方法,防止每次应用启动均会使版本重置
	 */
	public static void startCronVersionService() {
		TIMER.schedule(new TimerTask() {
			@Override
			public void run() {
				StaticFileVersionHelper.version = StaticFileVersionHelper.syncVersion();
			}
		}, 100, 3000);
	}

	private static String syncVersion() {
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(ADD_URL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);

			connection.connect();

			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lines;
			StringBuffer sb = new StringBuffer("");
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				sb.append(lines);
			}

			return sb.toString();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return getCurrentTimeVersion();
	}

	public static String getVersion() {

		if (isDevMode) {
			return getCurrentTimeVersion();
		}

		return "v-" + version + "-v";

	}

	private static String getCurrentTimeVersion() {
		return new SimpleDateFormat("yyMMddHHmmss").format(new java.util.Date());

	}

}
