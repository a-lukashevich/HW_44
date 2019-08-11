package core;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import java.util.logging.*;

import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.SilentJavaScriptErrorListener;

public class HtmlUnit_P1 {
	static Properties p = new Properties();
	static Writer report;
	static String ls = System.getProperty("line.separator");
	static WebClient driver;
	
	public static boolean isElementPresentHtmlUnit(HtmlPage page, String by) {
		if(page.getElementsById(by).size()>0)
			return true;
		else
			return false;
	}
	
	public static void setValueHtmlUnit(HtmlPage page, String by, String value) {
		if(isElementPresentHtmlUnit(page, by))
			page.getElementById(by).setTextContent(value);
	}
	
	public static String getValueHtmlUnit(HtmlPage page, String by) {
		if (isElementPresentHtmlUnit(page,by)) {
			if(page.getElementById(by).getTagName().toLowerCase().equals("select")) {
				HtmlSelect select = (HtmlSelect) page.getElementById(by);
				HtmlOption option = select.getOption(select.getSelectedIndex());
				return option.getText();
			}
			else {
				if (page.getElementById(by).getTextContent().trim().isEmpty())
					return "null";
				else
					return page.getElementById(by).getTextContent().replace(",", "").trim();
			}
		}
		else
			return "null";
	}
	
	private static void writeReport(String line) throws IOException {
		report.write(line);
		System.out.print(line);
	}
	
	private static void writeStaticFieldReport(HtmlPage page, int id, String browser, String pageN, String fieldName, String elementId) throws IOException {
		String reportLine = String.format("%02d", id) + "," + browser + "," + pageN + "," + fieldName + "," + 
				isElementPresentHtmlUnit(page, p.getProperty(elementId)) + "," + 
				getValueHtmlUnit(page, p.getProperty(elementId)) + "\n";
				writeReport(reportLine);
	}
	
	private static void writeInputFieldReport(HtmlPage page, int id, String browser, String pageN, String fieldName, String elementId, String propertyName) throws IOException {
		String reportLine = String.format("%02d", id) + "," + browser + "," + pageN + "," + fieldName + "," + 
				isElementPresentHtmlUnit(page, p.getProperty(elementId)) + "," + 
				p.getProperty(propertyName) + "\n";
				writeReport(reportLine);
	}

	public static void main(String[] args) throws Exception {
		Logger.getLogger("").setLevel(Level.OFF);
		p.load(new FileInputStream("./input.properties"));
		report = new FileWriter("./report_htmlunit.csv", false);
		
		driver = new WebClient();
		driver.setCssErrorHandler(new SilentCssErrorHandler());
		driver.setJavaScriptErrorListener(new SilentJavaScriptErrorListener());
		HtmlPage index_page = driver.getPage(p.getProperty("url"));
		
		// HEADER
		System.out.println("#,Browser,Page,Field,isPresent,Value");
		report.write("#,Browser,Page,Field,isPresent,Value"); report.write(ls);
		HtmlForm form = index_page.getFormByName(p.getProperty("form"));
		
		// 01 :: Quotes				id="id_quotes"
		writeStaticFieldReport(index_page, 1, "HtmlUnit", "index.php", "Quotes", "id_quotes");	
		
		// 02 :: Title				id="id_f_title"
		writeStaticFieldReport(index_page, 2, "HtmlUnit", "index.php", "Title", "id_f_title");
		
		// 03 :: Current Location 	id="id_current_location"
		writeStaticFieldReport(index_page, 3, "HtmlUnit", "index.php", "Current Location", "id_current_location");
		
		// 04 :: Weather Icon		id="id_weather_icon"
		writeStaticFieldReport(index_page, 4, "HtmlUnit", "index.php", "Weather Icon", "id_weather_icon");
		
		// 05 :: Temperature		id="id_temperature"
		writeStaticFieldReport(index_page, 5, "HtmlUnit", "index.php", "Temperature", "id_temperature");
		
		// 06 :: Label First Name	id="id_f_label_fn"
		writeStaticFieldReport(index_page, 6, "HtmlUnit", "index.php", "Label First Name", "id_f_label_fn");
		
		// 07 :: First Name	
		writeInputFieldReport(index_page, 7, "HtmlUnit", "index.php", "First Name", "fname_id", "fname_value");
		setValueHtmlUnit(index_page, p.getProperty("fname_id"), p.getProperty("fname_value"));
		
		// 08 :: First Name Error	id="fname_error"
		writeStaticFieldReport(index_page, 8, "HtmlUnit", "index.php", "First Name Error", "fname_error");
		
		// 09 :: Label Last Name 	id="id_f_label_ln"
		writeStaticFieldReport(index_page, 9, "HtmlUnit", "index.php", "Label Last Name", "id_f_label_ln");
		
		// 10 :: Last Name
		writeInputFieldReport(index_page, 10, "HtmlUnit", "index.php", "Last Name", "lname_id", "lname_value"); 
		setValueHtmlUnit(index_page, p.getProperty("lname_id"), p.getProperty("lname_value"));
		
		// 11 :: Last Name Error	id="lname_error"
		writeStaticFieldReport(index_page, 11 , "HtmlUnit" , "index.php" , "Last Name Error", "lname_error");
		
		// 12 :: Label Email 		id="id_f_label_ea"
		writeStaticFieldReport(index_page, 12, "HtmlUnit", "index.php", "Label Email", "id_f_label_ea");
				
		// 13 :: Email	
		writeInputFieldReport(index_page, 13, "HtmlUnit", "index.php", "Email", "email_id", "email_value");
		setValueHtmlUnit(index_page, p.getProperty("email_id"), p.getProperty("email_value"));
		
		// 14 :: Email Error		id="email_error"
		writeStaticFieldReport(index_page, 14, "HtmlUnit", "index.php", "Email Error", "email_error");
		
		// 15 :: Label Phone		id="id_f_label_pn"
		writeStaticFieldReport(index_page, 15, "HtmlUnit", "index.php", "Label Phone", "id_f_label_pn");
		
		// 16 :: Phone
		writeInputFieldReport(index_page, 16, "HtmlUnit", "index.php", "Phone", "phone_id", "phone_value");
		setValueHtmlUnit(index_page, p.getProperty("phone_id"), p.getProperty("phone_value"));
		
		// 17 :: Phone Error		id="phone_error"
		writeStaticFieldReport(index_page, 17, "HtmlUnit", "index.php", "Phone Error", "phone_error");
		
		// 18 :: Label Gender		id="id_rb_label_g"
		writeStaticFieldReport(index_page, 18, "HtmlUnit", "index.php", "Label Gender", "id_rb_label_g");

		// 19 :: Label Gender Male	id="id_rb_label_m"
		writeStaticFieldReport(index_page, 19, "HtmlUnit", "index.php", "Label Gender Male", "id_rb_label_m");
		
		// 20 :: Gender Male		id="id_gender_male"
		writeStaticFieldReport(index_page, 20, "HtmlUnit", "index.php", "Gender Male", "id_gender_male");
		
		// 21 :: Label Gender Female	id="id_rb_label_f"
		writeStaticFieldReport(index_page, 21, "HtmlUnit", "index.php", "Label Gender Female", "id_rb_label_f");
		
		// 22 :: Gender Female		id="id_gender_female"
		writeStaticFieldReport(index_page, 22, "HtmlUnit", "index.php", "Gender Female", "id_gender_female");
		
		// 23 :: Label State		id="id_f_label_s"
		writeStaticFieldReport(index_page, 23, "HtmlUnit", "index.php", "Label State", "id_f_label_s");

		// 24 :: State				id="id_state"
		writeStaticFieldReport(index_page, 24, "HtmlUnit", "index.php", "State", "id_state");
	
		// 25 :: Terms				id="id_terms"
		writeStaticFieldReport(index_page, 25, "HtmlUnit", "index.php", "Terms", "id_terms");

		// 26 :: Label Terms		id="id_cb_label_t"
		writeStaticFieldReport(index_page, 26, "HtmlUnit", "index.php", "Label Terms", "id_cb_label_t");
		
		// 27 :: Error Line		id="ErrorLine"
		writeStaticFieldReport(index_page, 27, "HtmlUnit", "index.php", "ErrorLine", "ErrorLine");
				
		// 28 :: Link Facebook	id="id_link_facebook"
		writeStaticFieldReport(index_page, 28, "HtmlUnit", "index.php", "Link Facebook", "id_link_facebook");
		
		// 29 :: Img Faceebook	id="id_img_facebook"
		writeStaticFieldReport(index_page, 29, "HtmlUnit", "index.php", "Img Faceebook", "id_img_facebook");
		
		// 30 :: Link Twitter	id="id_link_twitter"
		writeStaticFieldReport(index_page, 30, "HtmlUnit", "index.php", "Link Twitter", "id_link_twitter");
		
		// 31 :: Img Twitter	id="id_img_twitter"
		writeStaticFieldReport(index_page, 31, "HtmlUnit", "index.php", "Img Twitter", "id_img_twitter");
		
		// 32 :: Link Flickr	id="id_link_flickr"
		writeStaticFieldReport(index_page, 32, "HtmlUnit", "index.php", "Link Flickr", "id_link_flickr");
		
		// 33 :: Img Flickr		id="id_img_flickr"
		writeStaticFieldReport(index_page, 33, "HtmlUnit", "index.php", "Img Flickr", "id_img_flickr");
		
		// 34 :: Link YouTube	id="id_link_youtube"
		writeStaticFieldReport(index_page, 34, "HtmlUnit", "index.php", "Link YouTube", "id_link_youtube");
		
		// 35 :: Img YouTube	id="id_img_youtube"
		writeStaticFieldReport(index_page, 35, "HtmlUnit", "index.php", "Img YouTube", "id_img_youtube");
				
		// 36 :: Reset Button	id="id_reset_button"
		writeStaticFieldReport(index_page, 36, "HtmlUnit", "index.php", "Reset Button", "id_reset_button");
		
		// 37 :: Submit Button	id="id_submit_button"
		writeStaticFieldReport(index_page, 37, "HtmlUnit", "index.php", "Submit Button", "id_submit_button");
		
		// 38 :: Timestamp		id="timestamp"
		writeStaticFieldReport(index_page, 38, "HtmlUnit", "index.php", "Timestamp", "timestamp");
		
		// 39 :: Copyright		id="copyright"
		writeStaticFieldReport(index_page, 39, "HtmlUnit", "index.php", "Copyright", "copyright");
		
		// 40 :: OS & Browser	id="os_browser"
		writeStaticFieldReport(index_page, 40, "HtmlUnit", "index.php", "OS & Browser", "os_browser");
		
		//SUBMIT
		HtmlSubmitInput button = form.getInputByValue("Submit");
		HtmlPage confirmation_page = button.click();
		Thread.sleep(1000);
		
		// 41 :: Title				id="id_f_title"
		writeStaticFieldReport(confirmation_page, 41, "HtmlUnit", "confirmation.php", "Title", "id_f_title");
		
		// 42 :: Label First Name	id="id_f_label_fn"
		writeStaticFieldReport(confirmation_page, 42, "HtmlUnit", "confirmation.php", "Label First Name", "id_f_label_fn");
		
		// 43 :: First Name	
		writeStaticFieldReport(confirmation_page, 43, "HtmlUnit", "confirmation.php", "First Name", "fname_id");
		
		// 44 :: Label Last Name 	id="id_f_label_ln"	
		writeStaticFieldReport(confirmation_page, 44, "HtmlUnit", "confirmation.php", "Label Last Name", "id_f_label_ln");
		
		// 45 :: Last Name
		writeStaticFieldReport(confirmation_page, 45, "HtmlUnit", "confirmation.php", "Last Name", "lname_id");
		
		// 46 :: Label Email	id="id_f_label_ea"
		writeStaticFieldReport(confirmation_page, 46, "HtmlUnit", "confirmation.php", "Label Email", "id_f_label_ea");
		
		// 47 :: Email
		writeStaticFieldReport(confirmation_page, 47, "HtmlUnit", "confirmation.php", "Email", "email_id");
		
		// 48 :: Label Phone	id="id_f_label_pn"
		writeStaticFieldReport(confirmation_page, 48, "HtmlUnit", "confirmation.php", "Label Phone", "id_f_label_pn");
		
		// 49 :: Phone
		writeStaticFieldReport(confirmation_page, 49, "HtmlUnit", "confirmation.php", "Phone", "phone_id");

		// 50 :: Label Gender 	id="id_rb_label_g"
		writeStaticFieldReport(confirmation_page, 50, "HtmlUnit", "confirmation.php", "Label Gender", "id_rb_label_g");
		
		// 51 :: Gender			id="id_gender"
		writeStaticFieldReport(confirmation_page, 51, "HtmlUnit", "confirmation.php", "Gender", "id_gender");
		
		// 52 :: Label State	id="id_f_label_s"
		writeStaticFieldReport(confirmation_page, 52, "HtmlUnit", "confirmation.php", "Label State", "id_f_label_s");
		
		// 53 :: State			id="id_state"
		writeStaticFieldReport(confirmation_page, 53, "HtmlUnit", "confirmation.php", "State", "id_state");
						
		// 54 :: Label Terms	id="id_cb_label_t"
		writeStaticFieldReport(confirmation_page, 54, "HtmlUnit", "confirmation.php", "Label Terms", "id_cb_label_t");
		
		// 55 :: Terms			id="id_terms"
		writeStaticFieldReport(confirmation_page, 55, "HtmlUnit", "confirmation.php", "Terms", "id_terms");
		
		// 56 :: Back Button	id="id_back_button"
		writeStaticFieldReport(confirmation_page, 56, "HtmlUnit", "confirmation.php", "Back Button", "id_back_button");
		
		// 57 :: Copyright		id="copyright"
		writeStaticFieldReport(confirmation_page, 57, "HtmlUnit", "confirmation.php", "Copyright", "copyright");
		
		report.flush();
		report.close();
		driver.close();

	}

}
