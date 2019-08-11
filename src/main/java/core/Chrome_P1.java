package core;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Chrome_P1 {
	static Properties p = new Properties();
	static Writer report;
	static String ls = System.getProperty("line.separator");
	static WebDriver driver;
	
	public static boolean isElementPresent(By by) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if(!driver.findElements(by).isEmpty()) 
			return true; 
		else 
			return false;
	}
	
	public static String getSize(By by) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if(!driver.findElements(by).isEmpty() && driver.findElement(by).isDisplayed())
			return driver.findElement(by).getRect().getDimension().toString().replace(", ", "x");
		else
			return "null";
	}
	
	public static String getLocation(By by) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (!driver.findElements(by).isEmpty() && driver.findElement(by).isDisplayed())
			return driver.findElement(by).getRect().getPoint().toString().replace(", ", "x");
		else
			return "null";
	}
	
	public static void setValue(By by, String value) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if(!driver.findElements(by).isEmpty() && driver.findElement(by).isDisplayed())
			driver.findElement(by).sendKeys(p.getProperty(value));
	}
	
	public static String getValue(By by) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// Read value if element present and visible. 
		if(!driver.findElements(by).isEmpty() && driver.findElement(by).isDisplayed()) {
			// Handle dropdown element.
			if(driver.findElement(by).getTagName().toLowerCase().equals("select")) {
				Select select = new Select(driver.findElement(by));
				//select.selectByIndex(2);
				WebElement option = select.getFirstSelectedOption();
				return option.getText();	
			} 
			else {
				if (driver.findElement(by).getText().trim().isEmpty())
					return "null";
				else
					return driver.findElement(by).getAttribute("innerText").replace(",", "").trim();
			}
			
		} 
		else {
			return "null"; 	
		}
	}
	
	private static void writeReport(String line) throws IOException {
		report.write(line);
		System.out.print(line);
	}
	
	private static void writeStaticFieldReport(int id, String browser, String page, String fieldName, String elementId) throws IOException {
		String reportLine = String.format("%02d", id) + "," + browser + "," + page + "," + fieldName + "," + 
				isElementPresent(By.id(p.getProperty(elementId))) + "," + 
				getValue(By.id(p.getProperty(elementId))) + "," + 
				getSize(By.id(p.getProperty(elementId))) + "," +
				getLocation(By.id(p.getProperty(elementId))) + "\n";
		writeReport(reportLine);
	}
	
	private static void writeInputFieldReport(int id, String browser, String page, String fieldName, String elementId, String propertyName) throws IOException {
		String reportLine = String.format("%02d", id) + "," + browser + "," + page + "," + fieldName + "," + 
				isElementPresent(By.id(p.getProperty(elementId))) + "," + 
				p.getProperty(propertyName) + "," + 
				getSize(By.id(p.getProperty(elementId))) + "," +
				getLocation(By.id(p.getProperty(elementId))) + "\n";
		writeReport(reportLine);
	}
	

	public static void main(String[] args) throws Exception {
		Logger.getLogger("").setLevel(Level.OFF);
		p.load(new FileInputStream("./input.properties"));
		report = new FileWriter("./report_chrome.csv", false);
		
		String driverPath = "";
		
		if (System.getProperty("os.name").toUpperCase().contains("MAC"))
			driverPath = "./resources/webdrivers/mac/chromedriver";
		else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS"))
			driverPath = "./resources/webdrivers/pc/chromedriver.exe";
		else if (System.getProperty("os.name").toUpperCase().contains("LINUX"))
			driverPath = "./resources/webdrivers/linux/chromedriver";
		else throw new IllegalArgumentException("Unknown OS");
		
		System.setProperty("webdriver.chrome.driver", driverPath);
		System.setProperty("webdriver.chrome.silentOutput", "true");
		
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		driver.get(p.getProperty("url"));
		
		//HEADER
		
		System.out.println("#,Browser,Page,Field,isPresent,Value,Size,Location");
		report.write("#,Browser,Page,Field,isPresent,Value, Size, Location"); 
		report.write(ls);
		
		// 01 :: Quotes				id="id_quotes"
		writeStaticFieldReport(1, "Chrome", "index.php", "Quotes", "id_quotes");	
		
		// 02 :: Title				id="id_f_title"
		writeStaticFieldReport(2, "Chrome", "index.php", "Title", "id_f_title");
		
		// 03 :: Current Location 	id="id_current_location"
		writeStaticFieldReport(3, "Chrome", "index.php", "Current Location", "id_current_location");
		
		// 04 :: Weather Icon		id="id_weather_icon"
		writeStaticFieldReport(4, "Chrome", "index.php", "Weather Icon", "id_weather_icon");
		
		// 05 :: Temperature		id="id_temperature"
		writeStaticFieldReport(5, "Chrome", "index.php", "Temperature", "id_temperature");
		
		// 06 :: Label First Name	id="id_f_label_fn"
		writeStaticFieldReport(6, "Chrome", "index.php", "Label First Name", "id_f_label_fn");
		
		// 07 :: First Name	
		writeInputFieldReport(7, "Chrome", "index.php", "First Name", "fname_id", "fname_value");
		setValue(By.id(p.getProperty("fname_id")), "fname_value");
		
		// 08 :: First Name Error	id="fname_error"
		writeStaticFieldReport(8, "Chrome", "index.php", "First Name Error", "fname_error");
		
		// 09 :: Label Last Name 	id="id_f_label_ln"
		writeStaticFieldReport(9, "Chrome", "index.php", "Label Last Name", "id_f_label_ln");
		
		// 10 :: Last Name
		writeInputFieldReport(10, "Chrome", "index.php", "Last Name", "lname_id", "lname_value"); 
		setValue(By.id(p.getProperty("lname_id")), "lname_value");
		
		// 11 :: Last Name Error	id="lname_error"
		writeStaticFieldReport(11 , "Chrome" , "index.php" , "Last Name Error", "lname_error");
		
		// 12 :: Label Email 		id="id_f_label_ea"
		writeStaticFieldReport(12, "Chrome", "index.php", "Label Email", "id_f_label_ea");
				
		// 13 :: Email	
		writeInputFieldReport(13, "Chrome", "index.php", "Email", "email_id", "email_value");
		setValue(By.id(p.getProperty("email_id")), "email_value");
		
		// 14 :: Email Error		id="email_error"
		writeStaticFieldReport(14, "Chrome", "index.php", "Email Error", "email_error");
		
		// 15 :: Label Phone		id="id_f_label_pn"
		writeStaticFieldReport(15, "Chrome", "index.php", "Label Phone", "id_f_label_pn");
		
		// 16 :: Phone
		writeInputFieldReport(16, "Chrome", "index.php", "Phone", "phone_id", "phone_value");
		setValue(By.id(p.getProperty("phone_id")), "phone_value");
		
		// 17 :: Phone Error		id="phone_error"
		writeStaticFieldReport(17, "Chrome", "index.php", "Phone Error", "phone_error");
		
		// 18 :: Label Gender		id="id_rb_label_g"
		writeStaticFieldReport(18, "Chrome", "index.php", "Label Gender", "id_rb_label_g");

		// 19 :: Label Gender Male	id="id_rb_label_m"
		writeStaticFieldReport(19, "Chrome", "index.php", "Label Gender Male", "id_rb_label_m");
		
		// 20 :: Gender Male		id="id_gender_male"
		writeStaticFieldReport(20, "Chrome", "index.php", "Gender Male", "id_gender_male");
		
		// 21 :: Label Gender Female	id="id_rb_label_f"
		writeStaticFieldReport(21, "Chrome", "index.php", "Label Gender Female", "id_rb_label_f");
		
		// 22 :: Gender Female		id="id_gender_female"
		writeStaticFieldReport(22, "Chrome", "index.php", "Gender Female", "id_gender_female");
		
		// 23 :: Label State		id="id_f_label_s"
		writeStaticFieldReport(23, "Chrome", "index.php", "Label State", "id_f_label_s");

		// 24 :: State				id="id_state"
		writeStaticFieldReport(24, "Chrome", "index.php", "State", "id_state");
	
		// 25 :: Terms				id="id_terms"
		writeStaticFieldReport(25, "Chrome", "index.php", "Terms", "id_terms");

		// 26 :: Label Terms		id="id_cb_label_t"
		writeStaticFieldReport(26, "Chrome", "index.php", "Label Terms", "id_cb_label_t");
		
		// 27 :: Error Line		id="ErrorLine"
		writeStaticFieldReport(27, "Chrome", "index.php", "ErrorLine", "ErrorLine");
				
		// 28 :: Link Facebook	id="id_link_facebook"
		writeStaticFieldReport(28, "Chrome", "index.php", "Link Facebook", "id_link_facebook");
		
		// 29 :: Img Faceebook	id="id_img_facebook"
		writeStaticFieldReport(29, "Chrome", "index.php", "Img Faceebook", "id_img_facebook");
		
		// 30 :: Link Twitter	id="id_link_twitter"
		writeStaticFieldReport(30, "Chrome", "index.php", "Link Twitter", "id_link_twitter");
		
		// 31 :: Img Twitter	id="id_img_twitter"
		writeStaticFieldReport(31, "Chrome", "index.php", "Img Twitter", "id_img_twitter");
		
		// 32 :: Link Flickr	id="id_link_flickr"
		writeStaticFieldReport(32, "Chrome", "index.php", "Link Flickr", "id_link_flickr");
		
		// 33 :: Img Flickr		id="id_img_flickr"
		writeStaticFieldReport(33, "Chrome", "index.php", "Img Flickr", "id_img_flickr");
		
		// 34 :: Link YouTube	id="id_link_youtube"
		writeStaticFieldReport(34, "Chrome", "index.php", "Link YouTube", "id_link_youtube");
		
		// 35 :: Img YouTube	id="id_img_youtube"
		writeStaticFieldReport(35, "Chrome", "index.php", "Img YouTube", "id_img_youtube");
				
		// 36 :: Reset Button	id="id_reset_button"
		writeStaticFieldReport(36, "Chrome", "index.php", "Reset Button", "id_reset_button");
		
		// 37 :: Submit Button	id="id_submit_button"
		writeStaticFieldReport(37, "Chrome", "index.php", "Submit Button", "id_submit_button");
		
		// 38 :: Timestamp		id="timestamp"
		writeStaticFieldReport(38, "Chrome", "index.php", "Timestamp", "timestamp");
		
		// 39 :: Copyright		id="copyright"
		writeStaticFieldReport(39, "Chrome", "index.php", "Copyright", "copyright");
		
		// 40 :: OS & Browser	id="os_browser"
		writeStaticFieldReport(40, "Chrome", "index.php", "OS & Browser", "os_browser");
		
		//SUBMIT
		driver.findElement(By.id(p.getProperty("submit_id"))).submit();
		
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.titleIs("Confirmation"));
		

		// 41 :: Title				id="id_f_title"
		writeStaticFieldReport(41, "Chrome", "confirmation.php", "Title", "id_f_title");
		
		// 42 :: Label First Name	id="id_f_label_fn"
		writeStaticFieldReport(42, "Chrome", "confirmation.php", "Label First Name", "id_f_label_fn");
		
		// 43 :: First Name	
		writeStaticFieldReport(43, "Chrome", "confirmation.php", "First Name", "fname_id");
		
		// 44 :: Label Last Name 	id="id_f_label_ln"	
		writeStaticFieldReport(44, "Chrome", "confirmation.php", "Label Last Name", "id_f_label_ln");
		
		// 45 :: Last Name
		writeStaticFieldReport(45, "Chrome", "confirmation.php", "Last Name", "lname_id");
		
		// 46 :: Label Email	id="id_f_label_ea"
		writeStaticFieldReport(46, "Chrome", "confirmation.php", "Label Email", "id_f_label_ea");
		
		// 47 :: Email
		writeStaticFieldReport(47, "Chrome", "confirmation.php", "Email", "email_id");
		
		// 48 :: Label Phone	id="id_f_label_pn"
		writeStaticFieldReport(48, "Chrome", "confirmation.php", "Label Phone", "id_f_label_pn");
		
		// 49 :: Phone
		writeStaticFieldReport(49, "Chrome", "confirmation.php", "Phone", "phone_id");

		// 50 :: Label Gender 	id="id_rb_label_g"
		writeStaticFieldReport(50, "Chrome", "confirmation.php", "Label Gender", "id_rb_label_g");
		
		// 51 :: Gender			id="id_gender"
		writeStaticFieldReport(51, "Chrome", "confirmation.php", "Gender", "id_gender");
		
		// 52 :: Label State	id="id_f_label_s"
		writeStaticFieldReport(52, "Chrome", "confirmation.php", "Label State", "id_f_label_s");
		
		// 53 :: State			id="id_state"
		writeStaticFieldReport(53, "Chrome", "confirmation.php", "State", "id_state");
						
		// 54 :: Label Terms	id="id_cb_label_t"
		writeStaticFieldReport(54, "Chrome", "confirmation.php", "Label Terms", "id_cb_label_t");
		
		// 55 :: Terms			id="id_terms"
		writeStaticFieldReport(55, "Chrome", "confirmation.php", "Terms", "id_terms");
		
		// 56 :: Back Button	id="id_back_button"
		writeStaticFieldReport(56, "Chrome", "confirmation.php", "Back Button", "id_back_button");
		
		// 57 :: Copyright		id="copyright"
		writeStaticFieldReport(57, "Chrome", "confirmation.php", "Copyright", "copyright");
		
		report.flush();
		report.close();
		driver.quit();
		

	}

}
