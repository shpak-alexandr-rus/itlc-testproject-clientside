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
import ru.itlc.testproject.clientside.components.BookTableView;
import ru.itlc.testproject.clientside.responses.Book;
import ru.itlc.testproject.clientside.responses.BookPaginationResponse;
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

    public static BookPaginationResponse getAllBooks(int page, int pageSize, String sortingColumn, String sortingDirection) {
        try {
            URIBuilder uriBuilder = new URIBuilder(baseApiUrl);
            uriBuilder.setPath(processBooks);

            String getAllBooksUri = uriBuilder
                    .addParameter("page", String.valueOf(page))
                    .addParameter("pageSize", String.valueOf(pageSize))
                    .addParameter("sortingColumn", sortingColumn)
                    .addParameter("sortingDirection", sortingDirection)
                    .build().toString();
            HttpGet getAllBooksMethod = new HttpGet(getAllBooksUri);

            HttpResponse getHealthCheckResponse = client.execute(getAllBooksMethod);

            String responseBody = EntityUtils.toString(getHealthCheckResponse.getEntity());
            Gson gsonEntity = new Gson();
            return gsonEntity.fromJson(responseBody, BookPaginationResponse.class);
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

    public static void refreshBookTable(BookTableView table, int page, int pageSize, String sortingColumn, String sortingDirection) {
        // Очистка содержимого таблицы
        table.clear();

        // Получение данных от сервера
        BookPaginationResponse booksResponse = HttpWorkUtils.getAllBooks(page, pageSize, sortingColumn, sortingDirection);

        // Если данные получены
        if (booksResponse != null) {
            // Данные помещаются в таблицу
            booksResponse.getBooks().forEach(b -> table.add(b));
            table.setPageNumbers(booksResponse.getTotalPagesCount());
        }
    }

    public static String mapTextToColumnName(String text) {
        String result = "";
        switch (text) {
            case "Номер в каталоге":
                result = "book_id";
                break;
            case "Авторы":
                result = "book_author";
                break;
            case "Название":
                result = "book_title";
                break;
            case "Издательство":
                result = "book_publisher";
                break;
            case "Адрес издательства":
                result = "book_publisher_address";
                break;
            case "Дата публикации":
                result = "book_publishing_date";
                break;
        }
        return result;
    }

    public static String mapSortTypeToDirection(String sortType) {
        String result = "";
        switch (sortType) {
            case "ASCENDING":
                result = "ASC";
                break;
            case "DESCENDING":
                result = "DESC";
                break;
        }
        return result;
    }
}
