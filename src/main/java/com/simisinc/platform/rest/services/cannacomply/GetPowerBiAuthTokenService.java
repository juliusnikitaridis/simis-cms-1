package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.application.admin.LoadSitePropertyCommand;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Julius Nikitaridis
 * @created 01/07/23 11:28 AM
 */
public class GetPowerBiAuthTokenService {

    private static Log LOG = LogFactory.getLog(GetPowerBiAuthTokenService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if (!ValidateApiAccessHelper.validateAccess(this.getClass().getName(), context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            ApiResponse apiResponse = getToken();

            ServiceResponse response = new ServiceResponse(200);

            response.setData(apiResponse);
            return response;

        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e, this.getClass());
        }
    }

    public ApiResponse getToken() throws Exception {

        String clientId = LoadSitePropertyCommand.loadByName("powerbi.client.id");
        String clientSecret = LoadSitePropertyCommand.loadByName("powerbi.client.secret");
        String clientUrl = LoadSitePropertyCommand.loadByName("powerbi.client.url");
        String cookie = LoadSitePropertyCommand.loadByName("powerbi.client.cookie");

        HttpClient client = HttpClient.newHttpClient();

        // Construct the form data as a URL-encoded string
        String formData = Map.of(
                        "client_id", clientId,
                        "scope", "https://analysis.windows.net/powerbi/api/.default",
                        "grant_type", "client_credentials",
                        "client_secret", clientSecret
                ).entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        // Build HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(clientUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Cookie", cookie)
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());


        ObjectMapper mapper = new ObjectMapper();
        ApiResponse apiResponse = mapper.readValue(response.body(), ApiResponse.class);
        return apiResponse;
    }

    @Data
    static class ApiResponse {
        String token_type;
        int expires_in;
        int ext_expires_in;
        String access_token;
    }
}