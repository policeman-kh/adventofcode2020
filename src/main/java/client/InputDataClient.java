package client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class InputDataClient {
    public static List<String> getInputDataList(String url) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                                                 .uri(URI.create(url))
                                                 .GET()
                                                 .build();
            HttpClient httpClient = HttpClient.newBuilder()
                                              .build();
            String responseBody = httpClient.send(httpRequest,
                                                  HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
                                            .body();
            return Arrays.asList(responseBody.split("\n"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
