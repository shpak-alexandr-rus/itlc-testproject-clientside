package ru.itlc.testproject.clientside.utils;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import ru.itlc.testproject.clientside.responses.Book;
import ru.itlc.testproject.clientside.responses.BooleanResponse;
import ru.itlc.testproject.clientside.responses.HealthCheckResponse;

import java.nio.charset.StandardCharsets;

public class HttpWorkUtils {
    private static final String baseApiUrl = "http://localhost:8080";
    private static final String healthCheckPath = "api/health-check";
    private static final String processBooks = "api/books";
    private static final String accessByIdFormat = "api/books/%d";
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
            return getStubStatusCode == HttpStatus.SC_OK && healthCheckResponse.isDbStatus();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Book saveBook(Book book) {
        try {
            URIBuilder uriBuilder = new URIBuilder(baseApiUrl);
            uriBuilder.setPath(processBooks);

            String saveUri = uriBuilder.build().toString();
            HttpPost saveBookMethod = new HttpPost(saveUri);

            Gson gsonEntity = new Gson();
            String bodyValue = gsonEntity.toJson(book, Book.class);
            final StringEntity entity = new StringEntity(bodyValue, StandardCharsets.UTF_8);
            saveBookMethod.setEntity(entity);
            saveBookMethod.setHeader("Accept", "application/json");
            saveBookMethod.setHeader("Content-type", "application/json");

            HttpResponse saveBookResponse = client.execute(saveBookMethod);

            String responseBody = EntityUtils.toString(saveBookResponse.getEntity());

            return gsonEntity.fromJson(responseBody, Book.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Book[] getAllBooks() {
        try {
            URIBuilder uriBuilder = new URIBuilder(baseApiUrl);
            uriBuilder.setPath(processBooks);

            String healthCheckUri = uriBuilder.build().toString();
            HttpGet getHealthCheckMethod = new HttpGet(healthCheckUri);
            HttpResponse getHealthCheckResponse = client.execute(getHealthCheckMethod);

            String responseBody = EntityUtils.toString(getHealthCheckResponse.getEntity());
            Gson gsonEntity = new Gson();
            return gsonEntity.fromJson(responseBody, Book[].class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BooleanResponse updateBookById(long id, Book book) {
        try {
            URIBuilder uriBuilder = new URIBuilder(baseApiUrl);
            uriBuilder.setPath(String.format(accessByIdFormat, id));

            String healthCheckUri = uriBuilder.build().toString();
            HttpPut updateBookMethod = new HttpPut(healthCheckUri);

            Gson gsonEntity = new Gson();
            String bodyValue = gsonEntity.toJson(book, Book.class);
            final StringEntity entity = new StringEntity(bodyValue, StandardCharsets.UTF_8);
            updateBookMethod.setEntity(entity);
            updateBookMethod.setHeader("Accept", "application/json");
            updateBookMethod.setHeader("Content-type", "application/json");

            HttpResponse updateBookByIdResponse = client.execute(updateBookMethod);

            String responseBody = EntityUtils.toString(updateBookByIdResponse.getEntity());

            return gsonEntity.fromJson(responseBody, BooleanResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BooleanResponse deleteBookById(long id) {
        try {
            URIBuilder uriBuilder = new URIBuilder(baseApiUrl);
            uriBuilder.setPath(String.format(accessByIdFormat, id));

            String healthCheckUri = uriBuilder.build().toString();
            HttpDelete getHealthCheckMethod = new HttpDelete(healthCheckUri);
            HttpResponse getHealthCheckResponse = client.execute(getHealthCheckMethod);

            String responseBody = EntityUtils.toString(getHealthCheckResponse.getEntity());
            Gson gsonEntity = new Gson();
            return gsonEntity.fromJson(responseBody, BooleanResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
