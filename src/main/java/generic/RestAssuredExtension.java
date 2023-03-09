package generic;

import configreader.FileReaderManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;


public class RestAssuredExtension {
    private RequestSpecBuilder builder = new RequestSpecBuilder();
    private String method;
    private String url;
    private static String jsonBody;
    private static boolean isDemo;
    private RequestSpecification requestSpecs;



    public static String createJsonBody(String jsonFile) {
        String jsonFilePath =System.getProperty("user.dir")+"\\src\\test\\resources\\jsonFiles\\"+jsonFile+".json";
        //jsonFilePath = FileReaderManager.getInstance().getConfigReader().jasonFilePath(jsonFile);

        try {
            jsonBody = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonBody;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * RestAssuredExtension constructor to pass the initial settings for the the
     * following method
     *
     * @param uri
     * @param method
     * @param token
     */
    public RestAssuredExtension(String uri, String method, String token) {
        String baseurl = FileReaderManager.getInstance().getConfigReader().baseURLPath();
        String subjectAPI = uri;
        String SubcriptionKey= "";

        //this is required for not to fail at hook start
        if(subjectAPI!=null) {

            try {
                subjectAPI = subjectAPI.substring(0, subjectAPI.indexOf('/'));
                SubcriptionKey = FileReaderManager.getInstance().getConfigReader().GetString(subjectAPI);
                //Put here your subscription Key
                builder.addHeader("YOUR SUBSCRIPTION KEY", SubcriptionKey );

                System.out.println("Try Block ");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                System.out.println("No Subscription Needed");

            }

        }
        // Formulate the API url
        this.url = baseurl + uri;
        this.method = method;
        System.out.println(baseurl + uri);
        if (token != null)
            builder.addHeader("Authorization", "Bearer " + token);
    }

    /**
     * RestAssuredExtension constructor to pass the initial settings for the the
     * following method
     *
     * @param uri
     * @param method
     * @param token
     */
    public RestAssuredExtension(String uri, String method, String token, boolean isDemo) {
        String baseURLPathDemo = FileReaderManager.getInstance().getConfigReader().baseURLPathDemo();
        // Formulate the API url
        if (isDemo) {
            this.url = baseURLPathDemo + uri;
            this.method = method;

            if (token != null)
                builder.addHeader("Authorization", "Bearer " + token);
        }
    }

    /**
     * RestAssuredExtension constructor for config independet calls
     *
     * @param uri
     * @param method
     * @param token
     */
    public RestAssuredExtension(boolean isIndependet, String uri, String method, String token, Map<String, String> header) {
        // Formulate the API url
        if (isIndependet) {
            this.url = uri;
            this.method = method;

            if (token != null) {
                builder.addHeader("Authorization", "Bearer " + token);
            }

            if (header != null) {
                for(String key : header.keySet()) {
                    builder.addHeader(key, header.get(key) );
                }
            }
        }
    }
    /**
     * ExecuteAPI to execute the API for GET/POST/DELETE
     *
     * @return ResponseOptions<Response>
     */
    private ResponseOptions<Response> ExecuteAPI() {
        RequestSpecification requestSpecification = builder.build();
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON); // check for framework
        request.spec(requestSpecification);

        if (this.method.equalsIgnoreCase("POST"))
            return request.post(this.url);
        else if (this.method.equalsIgnoreCase("DELETE"))
            return request.delete(this.url);
        else if (this.method.equalsIgnoreCase("GET"))
            return request.get(this.url);
        else if (this.method.equalsIgnoreCase("PUT"))
            return request.put(this.url);
        return null;
    }

    /**
     * Authenticate to get the token variable
     *
     * @param body
     * @return string token
     */
    public String Authenticate(Object body) {
        builder.setBody(body);
        return ExecuteAPI().getBody().jsonPath().get("access_token");
    }

    /**
     * Executing API with query params being passed as the input of it
     *
     * @param queryPath
     * @return Reponse
     */
    public ResponseOptions<Response> ExecuteWithQueryParams(Map<String, String> queryPath) {
        builder.addQueryParams(queryPath);
        return ExecuteAPI();
    }

    /**
     * ExecuteWithPathParams
     *
     * @param pathParams
     * @return
     */
    public ResponseOptions<Response> ExecuteWithPathParams(Map<String, String> pathParams) {
        builder.addPathParams(pathParams);
        return ExecuteAPI();
    }

    /**
     * ExecuteWithPathParamsAndMapBody
     *
     * @param pathParams
     * @param body
     * @return
     */
    public ResponseOptions<Response> ExecuteWithPathParamsAndBody(Map<String, String> pathParams,
                                                                  Map<String, String> body) {
        builder.setBody(body);
        builder.addPathParams(pathParams);
        return ExecuteAPI();
    }

    /**
     * ExecuteWithPathParamsAndJSONBody
     *
     * @param pathParams
     * @param jsonFile
     * @return
     */
    public ResponseOptions<Response> ExecuteWithPathParamsAndJsonBody(Map<String, String> pathParams, String jsonFile) {
        createJsonBody(jsonFile);

        builder.setBody(jsonBody);
        builder.addPathParams(pathParams);
        return ExecuteAPI();
    }

    /**
     * ExecuteWithJSONBody
     *
     * @return
     */
    public ResponseOptions<Response> ExecuteWithJsonBody(String jsonFile) {
        createJsonBody(jsonFile);

        builder.setBody(jsonBody);
        return ExecuteAPI();
    }


    public ResponseOptions<Response> ExecuteWithJsonBodyIndependent(String jsonFile) {

        builder.setBody(jsonFile);
        return ExecuteAPI();
    }
    /**
     * ExecuteWithOutPathParamsAndBody
     *
     * @return
     */
    public ResponseOptions<Response> ExecuteWithOutBodyAndParams() {

        return ExecuteAPI();
    }

    public ResponseOptions<Response> ExecuteRequest(String jsonFile) {
        createJsonBody(jsonFile);

        builder.setBody(jsonBody);
        return ExecuteAPI();
    }

    /**
     *
     *
     * @return
     */
    public RequestSpecification genericBuilder (Map headers, Map queryParams, String body) {
        RequestSpecification requestSpecs = builder.build();
        RequestSpecification request = RestAssured.given();
        builder.addHeaders(headers);
        builder.addQueryParams(queryParams);
        if(body != null) {
            builder.setBody(body);
        }
        request = request.spec(requestSpecs);

        return request;
    }

}
