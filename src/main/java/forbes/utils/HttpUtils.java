package forbes.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Objects;

public class HttpUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private static String executeHttpRequest(String targetURL, String urlParameters, HttpMethod method) {
        HttpURLConnection connection = null;
        if (Objects.isNull(method)) {
            method = HttpMethod.GET;
        }
        try {
            // Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(String.valueOf(method));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

            if (null != urlParameters) {
                connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            }
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(10000);
            // Send request
            if (null != urlParameters) {
                try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                    wr.write(urlParameters.getBytes());
                }
            }
            return parseResponseData(connection);
        } catch (Exception e) {
            LOGGER.error("ERROR while fetching/sending data for request url : {}, params : {}, request method: {}, Exception : {}", targetURL, urlParameters, method,
                    e);
            try {
                if (connection != null) {
                    return parseResponseData(connection);
                }
                return null;
            } catch (Exception e2) {
                LOGGER.error("ERROR while fetching/sending data second time for request url : {}, params : {}, request method: {}, Exception : {}", targetURL,
                        urlParameters, method, e);
                return null;
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String parseResponseData(HttpURLConnection connection) throws IOException {
        try {
            connection.connect();
            int connectionStatus = connection.getResponseCode();
            if (connectionStatus / 100 != 2) {
                LOGGER.error("Connection returned status code : {}", connectionStatus);
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                    Charset.forName("UTF-8")));
            return reader.readLine();
        } catch (Exception e) {
            LOGGER.error("Exception occurred while parsing Response, Exception : {}", e.getMessage(), e);
            return null;
        }
    }

    public static String post(String targetURL, Object urlParameters) {
        String params = null;
        try {
            params = mapper.writeValueAsString(urlParameters);
            LOGGER.info("Sending http post request for request url : {}, params : {}", targetURL, params);
            return executeHttpRequest(targetURL, params, HttpMethod.POST);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while HTTP POST with arguments: targetUrl: {}, params: {} with message: {}", targetURL, params, e.getMessage(), e);
        }
        return null;
    }

    public static String get(String targetURL) {
        try {
            LOGGER.info("Sending http get request for request url : {}", targetURL);
            return executeHttpRequest(targetURL, null, HttpMethod.GET);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while HTTP GET with arguments: targetUrl: {}, with message: {}", targetURL, e.getMessage(), e);
            return null;
        }
    }

}
