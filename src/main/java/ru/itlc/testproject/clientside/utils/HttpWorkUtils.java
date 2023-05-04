package ru.itlc.testproject.clientside.utils;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import ru.itlc.testproject.clientside.responses.HealthCheckResponse;

public class HttpWorkUtils {
    private static final String baseApiUrl = "http://localhost:8080";
    private static final String healthCheckPath = "api/health-check";
    private static final HttpClient client = HttpClients.createDefault();
    public static boolean getServerCheckHealth() {
        try {
            URIBuilder uriBuilder = new URIBuilder(baseApiUrl);
            uriBuilder.setPath(healthCheckPath);

            String healthCheckUri = uriBuilder.build().toString();
            HttpGet getHealthCheckMethod = new HttpGet(healthCheckUri);
            HttpResponse getHealthCheckResponse = client.execute(getHealthCheckMethod);
            int getStubStatusCode = getHealthCheckResponse.getStatusLine()
                    .getStatusCode();

            String responseBody = EntityUtils.toString(getHealthCheckResponse.getEntity());
            Gson gsonEntity = new Gson();
            HealthCheckResponse healthCheckResponse = gsonEntity.fromJson(responseBody, HealthCheckResponse.class);
            System.out.println(healthCheckResponse);
            return getStubStatusCode == HttpStatus.SC_OK && healthCheckResponse.isDbStatus();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
