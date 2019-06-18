package base;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.openqa.selenium.remote.RemoteWebDriver;

public class TestBaseWeb extends TestBaseReport {

	protected RemoteWebDriver driver;
	protected String appurl = "";
	protected String quoteurl = "";
	protected String TestName = null;
	protected int dataIndex = 0;
	
	protected static Map<String, String> data = null;
	protected static Map<String, String> dataenv = null;

	@BeforeSuite
	public void setUp() throws BiffException, IOException {

		// Load environment data
		getEnvData();

		// reading url from environment data
		appurl = dataenv.get("url");
		
		quoteurl = dataenv.get("quoteurl");

		// init WebDriver
	    driver = createChromeDriver();

	}

	@BeforeMethod
	public void setUp(Method method) throws Exception {

		// Test Method name would be Test Name and Test Data would be captured
		// based on Test Method Name
		TestName = method.getName();
		// Load test data based on TestName - Test Data is extracted from this file
		getTestData(TestName);


	}

	private RemoteWebDriver createChromeDriver() {
		System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\chromedriver.exe");
		return new ChromeDriver();

	}

	@AfterSuite
	public void tearDown() {

		// To quit web driver
		driver.quit();

	}

	// Method to retrieve test data
	private void getTestData(String testName) throws Exception {

		if (dataIndex == -1) {
			return;
		}

		Sheet dataSheet = null;
		data = new HashMap<String, String>();

		dataSheet = Workbook.getWorkbook(new File(getProperty("data.spreadsheet.name"))).getSheet("Test_Data");

		dataIndex = dataSheet.findCell(testName).getRow();
		for (int i = 1; i < dataSheet.getColumns(); i++) {
			String key = dataSheet.getCell(i, 0).getContents();
			String value = dataSheet.getCell(i, dataIndex).getContents();
			data.put(key, value);
		}

	}

	// Method to retrieve environment test data
	private void getEnvData() throws BiffException, IOException {

		if (dataIndex == -1) {
			return;
		}

		dataenv = new HashMap<String, String>();

		Sheet dataSheet = Workbook.getWorkbook(new File(getProperty("data.spreadsheet.name"))).getSheet("Env");

		for (int row = 0; row < dataSheet.getRows(); row++) {
			String key = dataSheet.getCell(0, row).getContents();
			System.out.println("key::" + key);
			String value = dataSheet.getCell(1, row).getContents();
			System.out.println("value::" + value);
			dataenv.put(key, value);
		}

	}

	public static String getProperty(String property) {
		if (System.getProperty(property) != null) {
			return System.getProperty(property);
		}
		File setupPropFile = new File("setup.properties");
		if (setupPropFile.exists()) {
			Properties prop = new Properties();
			FileReader reader;
			try {
				reader = new FileReader(setupPropFile);
				prop.load(reader);
				reader.close();
				return prop.getProperty(property);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
