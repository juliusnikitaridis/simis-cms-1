package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.application.admin.LoadSitePropertyCommand;
import com.simisinc.platform.domain.model.cannacomply.Hygiene;
import com.simisinc.platform.infrastructure.persistence.cannacomply.HygieneRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import lombok.Data;
import okhttp3.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.UUID;

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

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("client_id", clientId)
                .addFormDataPart("scope", "https://analysis.windows.net/powerbi/api/.default")
                .addFormDataPart("grant_type", "client_credentials")
                .addFormDataPart("client_secret", clientSecret)
                .build();
        Request request = new Request.Builder()
                .url(clientUrl)
                .method("POST", body)
                .addHeader("Cookie", cookie)
                .build();
        Response response = client.newCall(request).execute();

        ObjectMapper mapper = new ObjectMapper();
        ApiResponse apiResponse = mapper.readValue(response.body().string(),ApiResponse.class);
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