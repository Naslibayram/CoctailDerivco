package stepdefinition;

import com.jayway.jsonpath.JsonPath;
import configreader.FileReaderManager;
import generic.RestAssuredExtension;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.testng.Assert;
import utilities.ExcelReaderWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AssessmentSteps {
    public ResponseOptions<Response> response;
    public RequestSpecification req;
    public RestAssuredExtension rse;
    public String token = "";
    Map<String, String> headers = new HashMap<String, String>();
    Map<String, String> params = new HashMap<String, String>();

    ExcelReaderWriter erw = new ExcelReaderWriter();

    Map<Object, Object> newValues = new HashMap<Object, Object>();
    Map<String, Object> newValues1 = new HashMap<String, Object>();
    String JsonString = null, nameOfFile = null, current = null;


    //U can hardcode it here
    String baseURL = FileReaderManager.getInstance().getConfigReader().baseURLPath();


    @Given("^I make a GET call to \"([^\"]*)\" to \"([^\"]*)\"$")
    public void I_make_a_get_call_to_EndPoint_with(String api, String endpoint) {

        rse = new RestAssuredExtension(api.concat(endpoint), "GET", null);

        response = rse.ExecuteWithOutBodyAndParams();
        /*
        This is a step definition written in Gherkin syntax for making a GET call to a specific API endpoint.
        The step definition is using a regular expression to match the given statement in the feature file, which includes two parameters: the api and endpoint strings.
        The step definition uses the RestAssuredExtension class to execute the GET call.
        The RestAssuredExtension class is likely a custom class that wraps the RestAssured library to provide more functionality and abstraction.
        The ExecuteWithOutBodyAndParams() method is likely a method defined in the RestAssuredExtension class that performs the GET request and returns the response.
        The response object is stored in the response variable for later use in the step definitions.
        Overall, this step definition is an implementation of the first step in a scenario, where a GET call is made to a specific API endpoint.
         */

    }


    @Then("^I verify that status code is equal to (\\d+)$")
    public void verify_that_status_code(int expected) throws InterruptedException {

        int actualCode = response.getStatusCode();

        Assert.assertEquals(actualCode, expected);

        response.getBody().prettyPrint();
        /*
        This step definition method verifies that the HTTP status code returned by the API call is equal to the expected status code.
        It takes an integer argument (expectedCode) from the feature file that represents the expected status code and uses the response object to get the actual status code.
        It then uses the Assert.assertEquals() method to compare the actual status code with the expected status code.
        If the two values do not match, an assertion failure will occur and an error message will be displayed.
        If the assertion passes, it means that the status code returned by the API call is as expected.
        Finally, it prints the response body to the console for debugging purposes.
         */

    }

    @Then("I verify below assets from the jasonpaths: {string} are exist and as required")
    public void i_verify_below_assets_from_the_jasonpaths_are_exist_and_as_required(String str, DataTable dt) {

        Map<String, String> data = dt.asMap(String.class, String.class);
        String responseBody = response.getBody().prettyPrint().toString();

        for (String key : data.keySet()) {

            Object valueCheck = JsonPath.read(responseBody, str+key);

            if(valueCheck!=null) {
                String value = JsonPath.read(responseBody, str+key).toString();
                String classValue = JsonPath.read(responseBody, str+key).getClass().toString();

                boolean  expected = true;
                boolean  actual = !value.isEmpty();
                Assert.assertEquals(actual, expected);

                String  expectedClass = data.get(key);
                boolean  actualClass = classValue.contains(expectedClass);
                Assert.assertEquals(actualClass, expected);
            }
        }
        /*
        This code verifies that certain assets from the JSON response exist and are of the expected type.
        It takes in a string argument "str" that represents the JSON path to the assets being verified, and a DataTable "dt" that contains the expected type for each asset.
        The method first converts the DataTable to a Map of string keys and string values using the "asMap" method.
        It then gets the response body from the REST API call and converts it to a string using the "toString" method.
        Next, it loops through each key in the Map, and for each key, it uses the JsonPath library to read the value at the specified JSON path.
        If the value is not null, it converts it to a string and gets its class type.
        It then checks whether the value is not empty (which means it exists) and whether its class type matches the expected type specified in the DataTable.
        If both of these conditions are true, it passes the test. Otherwise, it fails and throws an assertion error.
         */
    }

    @Then("I verify consistency between assets: {string} and {string}")
    public void i_verify_consistency_between_assets_and(String strAlcohol, String strABV) {
        String responseBody = response.getBody().asString();

        Object expected = null;
        Object actual = null;

        String isAlcoholic = JsonPath.read(responseBody, strAlcohol).toString();
        Object abvValue = JsonPath.read(responseBody, strABV);

        if (isAlcoholic.contains("Yes")) {
            expected = true;
            actual = (abvValue!=null);
        }else if (isAlcoholic.contains("No")) {
            actual = abvValue;
        }


        Assert.assertEquals(actual, expected);

    }


    ArrayList<String> drinksFailures = new ArrayList<String>();

    @Then("I verify below schema in {string} from the jasonarray: {string} is exist and as required")
    public void i_verify_below_schema_from_the_jasonarray_are_exist_and_as_required(String endPoint, String arrayPath, io.cucumber.datatable.DataTable dt) throws IOException {

        Map<String, String> data = dt.asMap(String.class, String.class);
        String responseBody = response.getBody().prettyPrint().toString();

        Object drinkisExist = JsonPath.read(responseBody, arrayPath);
        if(drinkisExist!=null) {

            String jSArrayString = JsonPath.read(responseBody, arrayPath).toString();
            JSONArray jsonArray = new JSONArray(jSArrayString);
            int arraySize = jsonArray.length();
            System.out.println(arraySize + " Drinks Array Size");

            for(int i =0; i<arraySize; i++) {
                for (String key : data.keySet()) {
                    if(data.get(key).equalsIgnoreCase("notNullable")) {

                        boolean expectedClass= true;
                        boolean actualClass= false;

                        try {
                            String classValue = JsonPath.read(responseBody, arrayPath+"["+i+"]."+key).getClass().toString();
                            expectedClass = true;
                            actualClass = classValue.contains("String");
                            System.out.println("classValue " + classValue);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            //e.printStackTrace();

                            System.out.println("Value for: "+ arrayPath+"["+i+"]."+key +" is NULL! This is Not Expected");
                            System.out.println("Assertion skipped tests are continuing");

                            drinksFailures.add(arrayPath+"["+i+"]."+key);

                            actualClass = true;
                        }



                        Assert.assertEquals(actualClass, expectedClass);


                    }else if (data.get(key).equalsIgnoreCase("Nullable")) {

                        System.out.println(arrayPath+"[0]."+key +" bu NULL path   "+data.get(key));
                        boolean nulableexpected_1 = true;
                        boolean nulableActual_1 = JsonPath.read(responseBody, arrayPath+"["+i+"]."+key) ==null;

                        if(nulableActual_1) {
                            Assert.assertEquals(nulableActual_1, nulableexpected_1);
                        }else {
                            String classValue = JsonPath.read(responseBody, arrayPath+"["+i+"]."+key).getClass().toString();
                            boolean  expectedClass = true;
                            boolean  actualClass = classValue.contains("String");

                            Assert.assertEquals(actualClass, expectedClass);
                        }
                    }

                }
            }
        }else {
            System.out.println("No such a drink in the database");

            boolean noDrinkexpected = true;
            boolean noDrinkActual = JsonPath.read(responseBody, arrayPath) ==null;

            Assert.assertEquals(noDrinkActual, noDrinkexpected);

        }


        if(!drinksFailures.isEmpty()) {

            System.out.println("Values for below objects found NULL for endpoint:"+endPoint+" which is not expected!!!");
            for(String obs : drinksFailures) {
                System.out.println(" !!! "+ obs +" !!! ");
            }

            String excelPath = System.getProperty("user.dir") + "\\src\\test\\resources\\excels\\" + endPoint.substring(endPoint.indexOf("=")+1) + ".xlsx";
            erw.writeExcel(excelPath,"Sheet1",drinksFailures);
        }
    }
/*
This code is a step in a test scenario where the consistency between two assets in the API response is being verified.
The first parameter in the step definition is a string that represents the JSON path for the first asset, and the second parameter is the JSON path for the second asset.
In the implementation, the API response is retrieved and converted to a string.
Then, using the JsonPath library, the values of the two assets are retrieved from the response.
The value of the first asset is stored as a boolean, while the value of the second asset is stored as an object.
The if-else block checks if the drink is alcoholic or not based on the value of the first asset.
If it is alcoholic, then the ABV value should not be null, so the assertion checks for a non-null value.
If the drink is non-alcoholic, then the ABV value should be null, so the assertion checks for a null value.
Overall, this step is checking for the consistency of the data in the API response, ensuring that the ABV value is present for alcoholic drinks and absent for non-alcoholic drinks.
 */





}
