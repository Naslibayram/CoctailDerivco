package configreader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileReader {
    public Properties prop = null;

    @SuppressWarnings("unused")
    public PropertyFileReader() {
        prop = new Properties();
        try {
            InputStream input = new FileInputStream("src/main/resources/configfile/config.properties");
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            prop.load(input);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return prop.getProperty("Username");
    }

    public String getPassword() {
        return prop.getProperty("Password");
    }

    public String getWebsite() {
        return prop.getProperty("Website");
    }

    public String getWebsite(String type) {
        return prop.getProperty(type);
    }

    public int getPageLoadTimeOut() {
        return Integer.parseInt(prop.getProperty("PageLoadTimeOut"));
    }

    public int getImplicitWait() {
        return Integer.parseInt(prop.getProperty("ImplcitWait"));
    }

    public int getExplicitWait() {
        return Integer.parseInt(prop.getProperty("ExplicitWait"));
    }

    public String extentConfigFilePath() {
        return prop.getProperty("extentReportPathConfig");
    }

    public String baseURLPath() {
        return prop.getProperty("BaseAPI");
    }

    public String baseURLPathDemo() {
        return prop.getProperty("BaseAPIDemo");
    }

    public String endpoint1() {
        return prop.getProperty("APITestEndPoint");
    }

    public String log4jConfigFilePath() {
        return prop.getProperty("log4jPathConfig");
    }

    public String jasonFilePath(String fileName) {
        return prop.getProperty(fileName);
    }

    public String GetString(String configKey) {
        return prop.getProperty(configKey);
    }

    public String getSessionID() {
        return prop.getProperty("sessionID");
    }

    public void setSessionID(String SessionID) {
        prop.setProperty("SessionID", SessionID);
    }


    // ***************


    public String getJsonDataFilePath() {
        return prop.getProperty("jsonDataFileReader");
    }


    public String getReportFilePath(){
        return prop.getProperty("reportFilePath");
    }




}
