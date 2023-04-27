package com.ttv.at.test.action;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.Map;
//import java.time.Duration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ttv.at.test.*;
import com.ttv.at.test.action.os_actions;
import com.ttv.at.test.action.util;
import com.ttv.at.test.testobjectproperties.ref_object_type;
import com.ttv.at.test.selenium.guielement;
import com.ttv.at.test.selenium.guiinfo;

public class selenium extends testaction {

	guielement selenium_guielement = null;
	
	String downloadPath = "D:/working/ttv_tool/TTV_Automation/TTVAuto/downloads";
	
	public selenium (String name,
			ArrayList<parameter> inputs,
			ArrayList<parameter> returns,
			testobject action_object,
			run_type type) {
		super(name, inputs, returns, action_object, type);
		if (action_object != null)
			selenium_guielement = new guielement(action_object);
	}
	private static final int PAGE_LOAD_SECONDS = 15;
    /** Maximum time to wait for javascript to complete.*/
    private static final int SCRIPT_LOAD_SECONDS = 15;
	@Override
	public result validate() {
		// Set capture image value
		String actname = get_name().toLowerCase();
		if (actname.equals("browser.back") ||
				actname.equals("browser.forward"))
			return new result(status_run.PASSED, error_code.NO_ERROR, "Action OK");
		else if (actname.equals("openbrowser")||
				actname.equals("comparejs")||
				actname.equals("confirmalert")||
				actname.equals("navigate")) {
			set_capture_image(true);
			return validate_has_1_input();
		}
		else if (actname.equals("checkexists") ||
			actname.equals("click") ||
			actname.equals("mouseover") || 
			actname.equals("gettext") ||
			actname.equals("selectanyspanitem") ||
			actname.equals("selectanyitem") ||
			actname.equals("selectanyliitem") ||
			actname.equals("searchintable") ||
			actname.equals("clicktablerow") ||
			actname.equals("clicktablecell") ||
			actname.equals("selectanynotfirst") ||
			actname.equals("getpageurl")) {
			set_capture_image(true);
			return validate_has_object_or_has_1_input();
		}
		else if (actname.equals("settext") || 
			actname.equals("setstate") || 
			actname.equals("settext_submit") || 
			actname.equals("checktext") ||
			actname.equals("checkstate") ||
			actname.equals("select") ||
			actname.equals("clickbackspace") ||
			actname.equals("selectliitem") ||
			actname.equals("selectspanitem") ||
			actname.equals("clickanydescendant") ||
			actname.equals("mouseoveranydescendant") || 
			actname.equals("getproperty") ||
			actname.equals("getdescendantsxpath") ||
			actname.equals("executescript") ||
			actname.equals("executecmd") ||
			actname.equals("checkpageurl")) {
			set_capture_image(true);
			return validate_has_object_1_input_or_has_2_inputs();
		}
		else if (actname.equals("openbrowserproxy")) {
			return validate_has_5_input();
		}
		return new result(status_run.STOP, error_code.NOT_SUPPORT, "Action '" + get_name() + "' is not supported by Selenium runtime");
	}

	result validate_has_1_input() {
		// Check inputs
		if (get_inputs().size() > 0)
			return new result(status_run.PASSED, error_code.NO_ERROR, "Action OK");
		else
			return new result(status_run.STOP, error_code.REQUIRE_INPUT, "Action '" + get_name() + "' is not provide enough inputs.");
	}
	result validate_has_object_or_has_1_input() {
		if (get_action_object() != null || get_inputs().size() > 0)
			return new result(status_run.PASSED, error_code.NO_ERROR, "Action OK");
		return new result(status_run.STOP, error_code.REQUIRED_OBJECT, "Action '" + get_name() + "' not provide test object or xpath input.");
	}
	result validate_has_object_1_input_or_has_2_inputs() {
		if ((get_action_object() != null && get_inputs().size() > 0) ||
				get_inputs().size() > 1)
			return new result(status_run.PASSED, error_code.NO_ERROR, "Action OK");
		return new result(status_run.STOP, error_code.REQUIRE_INPUT, "Action '" + get_name() + "' is not provide enough inputs or action object.");
	}
	result validate_has_5_input() {
		// Check inputs
		if (get_inputs().size() < 5)
			return new result(status_run.STOP, error_code.REQUIRE_INPUT, "Action '" + get_name() + "' is not provide enough inputs.");
		return new result(status_run.PASSED, error_code.NO_ERROR, "Action OK");
	}


	@Override
	public result execute() {
		return execute(testsetting.get_timeout());
	}

	@Override
	public result execute(int timeout) {
		find_object_based_frame_xpath = "";
		stop = false;
		pause = false;
		
		// Reset driver
		if (driver != null)
			try {
				driver.switchTo().defaultContent();
			} catch (Exception e) {
				//if(e instanceof UnhandledAlertException)
					//{
						
					//}
				e.printStackTrace();
			}
		
		try {
			// Capture image
			String file_name = ((Long)((new Date()).getTime())).toString();
			String file_name_before = file_name + "." + get_name() + ".before.png";
			String file_name_after = file_name + "." + get_name() + ".after.png";
			
			if (get_capture_image())
				set_before_action_capture(os_actions.capture_screen());

			String actname = get_name().toLowerCase();
			result run_res = null;
			// execute action
			if (actname.equals("openbrowser"))
				run_res = execute_openbrowser();
			else if (actname.equals("openbrowserproxy"))
				run_res = execute_openbrowserproxy();
			else if (actname.equals("executecmd"))
				run_res = execute_cmd();
			else if (actname.equals("confirmalert"))
				run_res = execute_confirmalert(timeout);
			else if (actname.equals("comparejs"))
				run_res = execute_comparejs(timeout);
			else if (actname.equals("navigate"))
				run_res = execute_navigate();
			else if (actname.equals("checkexists"))
				run_res = execute_checkexists(timeout);
			else if (actname.equals("click"))
				run_res = execute_click(timeout);
			else if (actname.equals("clickbackspace"))
				run_res = execute_clickbackspace(timeout);
			else if (actname.equals("mouseover"))
				run_res = execute_mouseover(timeout);
			else if (actname.equals("settext"))
				run_res = execute_settext(timeout);
			else if (actname.equals("setstate"))
				run_res = execute_setstate(timeout);
			else if (actname.equals("settext_submit"))
				run_res = execute_settext_submit(timeout);
			else if (actname.equals("gettext"))
				run_res = execute_gettext(timeout);
			else if (actname.equals("checktext"))
				run_res = execute_checktext(timeout);
			else if (actname.equals("checkstate"))
				run_res = execute_checkstate(timeout);
			else if (actname.equals("select"))
				run_res = execute_select(timeout);
			else if (actname.equals("selectspanitem"))
				run_res = execute_selectspanitem(timeout);
			else if (actname.equals("selectanyitem"))
				run_res = execute_selectanyitem(timeout);
			else if (actname.equals("selectanyliitem"))
				run_res = execute_selectanyliitem(timeout);
			else if (actname.equals("selectliitem"))
				run_res = execute_selectliitem(timeout);
			else if (actname.equals("selectanyspanitem"))
				run_res = execute_selectanyspanitem(timeout);
			else if (actname.equals("selectanynotfirst"))
				run_res = execute_selectanynotfirst(timeout);
			else if (actname.equals("getdescendantsxpath"))
				run_res = execute_getdescendantsxpath(timeout);
			else if (actname.equals("getproperty"))
				run_res = execute_getproperty(timeout);
			else if (actname.equals("clickanydescendant"))
				run_res = execute_clickanydescendant(timeout);
			else if (actname.equals("mouseoveranydescendant"))
				run_res = execute_mouseoveranydescendant(timeout);
			else if (actname.equals("searchintable"))
				run_res = execute_searchintable(timeout);
			else if (actname.equals("clicktablecell"))
				run_res = execute_clicktablecell(timeout);
			else if (actname.equals("clicktablerow"))
				run_res = execute_clicktablerow(timeout);
			else if (actname.equals("browser.back")) {
				driver.navigate().back();
				String currentURL = driver.getCurrentUrl();
				if (get_Return() != null)
					get_Return().copy_from(currentURL);
				run_res = new result(status_run.PASSED, error_code.NO_ERROR, "Go back one page '"+currentURL+"' successful");
			}
			else if (actname.equals("browser.forward")) {
				driver.navigate().forward();
				String currentURL = driver.getCurrentUrl();
				if (get_Return() != null)
					get_Return().copy_from(currentURL);
				run_res = new result(status_run.PASSED, error_code.NO_ERROR, "Forward one page '"+currentURL+"' successful");
			}
			else if (actname.equals("executescript"))
				run_res = execute_executescript(timeout);
			else if (actname.equals("getpageurl"))
				run_res = execute_getpageurl(timeout);
			else if (actname.equals("checkpageurl"))
				run_res = execute_checkpageurl(timeout);
			else
				run_res = new result(status_run.STOP, error_code.NOT_SUPPORT, "Action '" + get_name() + "' is not supported by Selenium runtime");
			
			return run_res;
		} catch (Exception e) {
			// e.printStackTrace();
			return new result(status_run.STOP, error_code.UNKNOWN_ERROR, "Action '" + get_name() + "' by Selenium runtime exception: " + e.toString());
		}
	}

	result execute_cmd() {
		
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		try {
			 // Execute command
			StringBuilder builder = new StringBuilder();
			String path = get_inputs().get(0).get_value();
			String file = get_inputs().get(1).get_value();
		    builder.append("cmd /c start cmd.exe /K \"cd ");
		    builder.append(path);
		    builder.append(" && ruby ");
		    builder.append(file);
		    builder.append(" && exit");
		    
		    String cmd = builder.toString();
		    Process child = Runtime.getRuntime().exec(cmd);

		    BufferedReader reader =
                    new BufferedReader(new InputStreamReader(child.getInputStream()));
		    
		    String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = child.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

		    // Get output stream to write from it
		    OutputStream out = child.getOutputStream();
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action '"+get_name()+"' failed, exception: " + e);
		}
		return new result(status_run.PASSED, error_code.NO_ERROR, "Action '"+get_name()+"' passed");
	}

	result execute_openbrowser() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		if (testsetting.get_default_browser().toLowerCase().equals("firefox")) {
			if (open_browser_firefox(get_inputs().get(0).get_value()))
				return new result(status_run.PASSED, error_code.NO_ERROR, "Action '"+get_name()+"' passed");
			else
				return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action '"+get_name()+"' failed");
		}
		else if (testsetting.get_default_browser().toLowerCase().equals("ie") ||
				testsetting.get_default_browser().toLowerCase().equals("internet explorer")) {
			if (open_browser_internetexplorer(get_inputs().get(0).get_value()))
				return new result(status_run.PASSED, error_code.NO_ERROR, "Action '"+get_name()+"' passed");
			else
				return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action '"+get_name()+"' failed");
		}
		else if (testsetting.get_default_browser().toLowerCase().equals("chrome")) {
			// if (open_browser_chrome(get_inputs().get(0).get_value()))
			if (open_browser_chrome(get_inputs().get(0).get_value()))
				return new result(status_run.PASSED, error_code.NO_ERROR, "Action '"+get_name()+"' passed");
			else
				return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action '"+get_name()+"' failed");
		}
		else if (testsetting.get_default_browser().toLowerCase().equals("edge")) {
			if (open_browser_edge(get_inputs().get(0).get_value()))
				return new result(status_run.PASSED, error_code.NO_ERROR, "Action '"+get_name()+"' passed");
			else
				return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action '"+get_name()+"' failed");
		}
		else if (testsetting.get_default_browser().toLowerCase().equals("safari")) {

			if (open_browser_safari(get_inputs().get(0).get_value()))
				return new result(status_run.PASSED, error_code.NO_ERROR, "Action '"+get_name()+"' passed");
			else
				return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action '"+get_name()+"' failed");
		}
		//else if (testsetting.get_default_browser().toLowerCase().equals("android")) {

			//if (open_browser_android(get_inputs().get(0).get_value()))
				//return new result(status_run.PASSED, error_code.NO_ERROR, "Action '"+get_name()+"' passed");
			//else
				//return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action '"+get_name()+"' failed");
		//}
		else
			return new result(status_run.STOP, error_code.NOT_SUPPORT, "Action OpenBrowser of action_core not support for (runtime:SELENIUM, browser:" + testsetting.get_default_browser().toString() + ")");
	}
	
	result execute_openbrowserproxy() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		
		else if (testsetting.get_default_browser().toLowerCase().equals("chrome")) {
			// if (open_browser_chrome(get_inputs().get(0).get_value()))
			if (open_browser_chrome_proxy(get_inputs().get(0).get_value(), get_inputs().get(1).get_value(), get_inputs().get(2).get_value(), get_inputs().get(3).get_value(), get_inputs().get(4).get_value()))
				return new result(status_run.PASSED, error_code.NO_ERROR, "Action '"+get_name()+"' passed");
			else
				return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action '"+get_name()+"' failed");
		}

		else
			return new result(status_run.STOP, error_code.NOT_SUPPORT, "Action OpenBrowser of action_core not support for (runtime:SELENIUM, browser:" + testsetting.get_default_browser().toString() + ")");
	}
	
	result execute_navigate() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		if (driver != null) {
			driver.navigate().to(get_inputs().get(0).get_value());
			return new result(status_run.PASSED, error_code.NO_ERROR, "Action '"+get_name()+"' passed");
		}
		else
			return new result(status_run.STOP, error_code.NOT_SUPPORT, "Action OpenBrowser of action_core not support for (runtime:SELENIUM, browser:" + testsetting.get_default_browser().toString() + ")");

	}
	result execute_confirmalert(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		  try {
			    //driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
				Alert alert = driver.switchTo().alert();			
				String strInput = get_inputs().get(0).get_value().toLowerCase();
				if (get_Return() != null)
					get_Return().copy_from(alert.getText());
				if (strInput.equals("ok")) {
					alert.accept();
					return new result(status_run.PASSED, error_code.NO_ERROR,
							"Action Click'" + get_name() + "' passed'");
				}
				else if (strInput.equals("cancel")) {
					alert.dismiss();
					return new result(status_run.PASSED, error_code.NO_ERROR, "Action Click '"+get_name()+"' passed '");
				}
				else
					return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "Action Click '"+get_name()+"' Failed '");
				
			} catch (NoAlertPresentException e) {
				// no alert to dismiss, so return null
				return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action '"+get_name()+"' failed, exception : " + e);
			}
	}
	result execute_comparejs(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		try {
			Alert alert = driver.switchTo().alert();
			// Get the text of the alert or prompt
			String alerttext = alert.getText();
			String strInput = get_inputs().get(0).get_value();
			if (strInput.equals(alerttext)) {
				return new result(status_run.PASSED, error_code.NO_ERROR,
						"Action '" + get_name() + "' passed, value1 = "
								+ strInput + ", value2 = " + alerttext);
			}
			else {
				// Compare regular expression
				if (util.reg_compare(strInput, alerttext))
					return new result(status_run.PASSED, error_code.NO_ERROR, "Action '"+get_name()+"' passed by regular expression, value1 = " + strInput + ", value2 = " + alerttext);
				return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "Action '"+get_name()+"' failed, value1 = " + strInput + ", value2 = " + alerttext);
			}
		} catch (NoAlertPresentException e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action '"+get_name()+"' failed, exception : " + e);
		}
		
	}
	result execute_checkexists(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 0);
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		if (foundElement != null)
			return new result(status_run.PASSED, error_code.NO_ERROR, "Object '"+find_object_name+"' is exists");
		else
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
	}
	result execute_click(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		long startTime = (new Date()).getTime();
		boolean script_click = false;
		// Click
		while (true) {
			try {
				WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 0);
					
				if (stop)
					return new result(status_run.STOP, error_code.NO_ERROR, "Action Click stop by User");
				if (pause)
					return new result(status_run.STOP, error_code.NO_ERROR, "Action Click paused by User");
				
				if (foundElement != null) { // execute click
					if(!script_click) {
						foundElement.click();
					} else {
						JavascriptExecutor executor = (JavascriptExecutor)driver;
						executor.executeScript("arguments[0].click();", foundElement);
					}
					
				}
				else
					return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '" + find_object_name + "' is not found");
				break;
			} catch (Exception e) {
				// Calculate timeout
				if (e instanceof ElementClickInterceptedException) {
					script_click = true;
				} else {
					long currentTime = (new Date()).getTime(); // miliseconds
					if ((currentTime - startTime) > (find_object_timeout * 1000))
						return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action Click exception (object: '" + find_object_name + "'): " + e);
					try { Thread.sleep(500);} catch (Exception sleep_e) { }
				}
				
			}
		}

		try { Thread.sleep(500); } catch (InterruptedException e) {}
		
		if (find_object_name.equals("webchat.btn_csv")) {
			
			deleteAllFilesInFolder();
			
			try { Thread.sleep(5000); } catch (InterruptedException e) {}
			
			if (!downloadFileIsSuccess()) {
				return new result(status_run.FAILED, error_code.ACTION_FAILED, "Click object '" + find_object_name + "' failed");
			}
		}
		
		return new result(status_run.PASSED, error_code.NO_ERROR, "Click object '" + find_object_name + "' passed");
	}
	
	private void deleteAllFilesInFolder() {
		File folder = new File(downloadPath);
		
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			  listOfFiles[i].delete();
		  }
		}
	}
	
	private boolean downloadFileIsSuccess() {
		File folder = new File(downloadPath);
		
		File[] listOfFiles = folder.listFiles();
		
		if (!listOfFiles[0].isFile()) {
			return false;
		}
		
		System.out.println(listOfFiles[0].getName());
		
		return true;
	}
	
	result execute_clickbackspace(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		long startTime = (new Date()).getTime();
		boolean script_click = false;
		// Click
		while (true) {
			try {
				WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 1);
				int first_number =  Integer.parseInt(get_inputs().get(0).get_value());	
				if (stop)
					return new result(status_run.STOP, error_code.NO_ERROR, "Action Clickbackspace stop by User");
				if (pause)
					return new result(status_run.STOP, error_code.NO_ERROR, "Action Clickbackspace paused by User");
				
				if (foundElement != null) { // execute click
					for (int i = 0; i < first_number; i++) {
						foundElement.sendKeys(Keys.BACK_SPACE);
					}
				}
				else
					return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '" + find_object_name + "' is not found");
				break;
			} catch (Exception e) {
				// Calculate timeout
				if (e instanceof ElementClickInterceptedException) {
					script_click = true;
				} else {
					long currentTime = (new Date()).getTime(); // miliseconds
					if ((currentTime - startTime) > (find_object_timeout * 1000))
						return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action Clickbackspace exception (object: '" + find_object_name + "'): " + e);
					try { Thread.sleep(500);} catch (Exception sleep_e) { }
				}
				
			}
		}

		try { Thread.sleep(500); } catch (InterruptedException e) {}
		
		return new result(status_run.PASSED, error_code.NO_ERROR, "Click object '" + find_object_name + "' passed");
	}
	result execute_mouseover(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		long startTime = (new Date()).getTime();
		
		// Click
		while (true) {
			try {
				WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 0);
					
				if (stop)
					return new result(status_run.STOP, error_code.NO_ERROR, "Action Click stop by User");
				if (pause)
					return new result(status_run.STOP, error_code.NO_ERROR, "Action Click paused by User");
				
				if (foundElement != null) { // execute click
					Actions builder = new Actions(driver); // Or maybe seleniumDriver? Not sure which one to use
					builder.moveToElement(foundElement).perform();
				}
				else
					return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '" + find_object_name + "' is not found");
				break;
			} catch (Exception e) {
				// Calculate timeout
				long currentTime = (new Date()).getTime(); // miliseconds
				if ((currentTime - startTime) > (find_object_timeout * 1000))
					return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action Click exception (object: '" + find_object_name + "'): " + e);
				try { Thread.sleep(500);} catch (Exception sleep_e) { }
			}
		}

		try { Thread.sleep(500); } catch (InterruptedException e) {}
		
		return new result(status_run.PASSED, error_code.NO_ERROR, "MouseOver object '" + find_object_name + "' passed");
	}
	result execute_settext(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 1);
		String text_to_set = get_inputs().get(find_object_index + 1).get_value();
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");

		// Click
		try {
			foundElement.clear();
			Thread.sleep(100);
			if (foundElement.isEnabled())
				
				foundElement.sendKeys(text_to_set);
		}
		catch (StaleElementReferenceException e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Set text '"+text_to_set+"' to object '"+find_object_name+"' failed, exception of StaleElementReferenceException");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new result(status_run.PASSED, error_code.NO_ERROR, "Set text '"+text_to_set+"' to object '"+find_object_name+"' passed");
	}
	result execute_settext1(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 1);
		String text_to_set = get_inputs().get(find_object_index + 1).get_value();
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");

		// Click
		
		try {           
			foundElement.clear();
			if (foundElement.isEnabled())
				foundElement.sendKeys(text_to_set);
		}
		catch (StaleElementReferenceException e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Set text '"+text_to_set+"' to object '"+find_object_name+"' failed, exception of StaleElementReferenceException");
		} catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new result(status_run.PASSED, error_code.NO_ERROR, "Set text '"+text_to_set+"' to object '"+find_object_name+"' passed");
	}
	result execute_setstate(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 1);
		String text_state = get_inputs().get(find_object_index + 1).get_value();
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
		
		String tagName = foundElement.getTagName();
		if (!tagName.toLowerCase().equals("input"))
			return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "Object '"+find_object_name+"' tag name is '"+tagName+"', tag name should be input");
		
		String type = foundElement.getAttribute("type");
		if (type.equals("checkbox") ||
				type.equals("radio")) {
			if ((text_state.toLowerCase().equals("true") && !foundElement.isSelected()) ||
				((!text_state.toLowerCase().equals("true")) && foundElement.isSelected()))
					foundElement.click();
			if (get_Return() != null)
				get_Return().copy_from(text_state);
			return new result(status_run.PASSED, error_code.NO_ERROR, "Set state object '"+find_object_name+"' to '"+text_state+"' passed");
		}
		else
			return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "Object tag name '"+find_object_name+"' is '"+tagName+"' but type is '"+type+"', type should be radio or checkbox");		
	}
	result execute_settext_submit(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 1);
		String text_to_set = get_inputs().get(find_object_index + 1).get_value();
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// execute
		if (foundElement != null) {
			foundElement.clear();
			foundElement.sendKeys(text_to_set);
			try { Thread.sleep(500); } catch (InterruptedException e) {}
			return new result(status_run.PASSED, error_code.NO_ERROR, "Set text '"+text_to_set+"' object '"+find_object_name+"' and submit passed");
		}
		else
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
	}
	result execute_gettext(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 0);
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// gettext
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");


		long startTime = (new Date()).getTime();
		while (true) {
			try {
				String tagName = foundElement.getTagName();
				if (tagName.toLowerCase().equals("select")) {
					Select objSel = new Select(foundElement);
					String selected_text = null;
					List<WebElement> options = objSel.getOptions();
					for (int i = 0 ; i < options.size() ; i ++) {
						WebElement objSelelele = options.get(i);
						if (objSelelele.isSelected()) {
							selected_text = objSelelele.getText();
							break;
						}
					}
					if (selected_text == null || selected_text.length() <= 0)
						selected_text = foundElement.getText();
					if (get_Return() != null)
						get_Return().copy_from(selected_text);
					return new result(status_run.PASSED, error_code.NO_ERROR, "Text is '"+selected_text+"' of object '"+find_object_name+"'", selected_text);
				}
				else {
					String text = foundElement.getText();
					if ((text == null || text.length() == 0) && (foundElement.getTagName().toLowerCase().equals("input")))
						text = foundElement.getAttribute("value");
					if (get_Return() != null)
						get_Return().copy_from(text);
					return new result(status_run.PASSED, error_code.NO_ERROR, "Text is '"+text+"' of object '"+find_object_name+"'", text);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			

			
			long currentTime = (new Date()).getTime(); // miliseconds
			if ((currentTime - startTime) > (find_object_timeout * 1000))
				break;
			
			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
		}
		return new result(status_run.FAILED, error_code.ACTION_FAILED, "Trying to get text of object'"+find_object_name+"' failed");
	}
	result execute_checktext(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 1);
		String text_to_check = get_inputs().get(find_object_index + 1).get_value();
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// Check text
		if (foundElement != null) {
			String tagName = foundElement.getTagName();
			String text = get_WebElement_text(foundElement);
			if (util.reg_compare(text, text_to_check)) {
				if (get_Return() != null)
					get_Return().copy_from(text);
				return new result(status_run.PASSED, error_code.NO_ERROR, "Text is '"+text+"' of object '"+find_object_name+"'");
			}
			else
				return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "Expected is '"+ text_to_check+"' but Actual is '"+text+
						"' - Check text object '"+ find_object_name+"' failed");
		}
		else
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
	}
	result execute_checkstate(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 1);
		String text_state = get_inputs().get(find_object_index + 1).get_value();
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// Check text
		if (foundElement != null) {
			// Check if input
			String tagName = foundElement.getTagName();
			if (tagName.toLowerCase().equals("input")) {
				String type = foundElement.getAttribute("type");
				if (type.equals("checkbox") ||
						type.equals("radio")) {
					if ((text_state.toLowerCase().equals("true") && foundElement.isSelected()) ||
						((!text_state.toLowerCase().equals("true")) && (!foundElement.isSelected()))) {

						if (get_Return() != null)
							get_Return().copy_from(text_state);
						return new result(status_run.PASSED, error_code.NO_ERROR, "State is '"+text_state+"', action object '"+find_object_name+"'");
					}
					else {
						String state = null;
						if (foundElement.isSelected())
							state = "true";
						else
							state = "false";
						if (get_Return() != null)	
							get_Return().copy_from(state);
						return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "State is not '"+text_state+"', action object '"+find_object_name+"'");
					}
				}
				else
					return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "Object tag name is '"+tagName+"' but type is '"+type+"', Object '"+find_object_name+"' type should be radio or checkbox");
			}
			
			else
				return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "Object tag name is '"+tagName+"', Object '"+find_object_name+"' tag name should be input");
		}
		else
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
	}
	
	result execute_select(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 1);
		String expected_text = get_inputs().get(find_object_index + 1).get_value();
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// Select
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");

		long startTime = (new Date()).getTime();
		while (true) {
			try {
				String tagName = foundElement.getTagName();
				if (!tagName.toLowerCase().equals("select"))
					return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "Object tag name is '"+tagName+"', Object '"+find_object_name+"' tag name should be select");
				
				// Scan all the options to find the relevant option with regular expression
				Select objSel = new Select(foundElement);
				List<WebElement> options = objSel.getOptions();
				String textFound = null;
				for (int i = 0 ; i < options.size() ; i ++) {
					String optionText = options.get(i).getText();
					if (util.reg_compare(optionText, expected_text)) {
						textFound = optionText;
						break;
					}
				}
				
				if (textFound != null && textFound.length() > 0) {
					objSel.selectByVisibleText(textFound);
					if (get_Return() != null)
						get_Return().copy_from(textFound);
					return new result(status_run.PASSED, error_code.NO_ERROR, "'"+textFound+"' is selected for object '"+find_object_name+"'", textFound);
				}
				else
					return new result(status_run.FAILED, error_code.ACTION_FAILED, "'"+expected_text+"' is not found to select for object '"+find_object_name+"' Failed");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			

			
			long currentTime = (new Date()).getTime(); // miliseconds
			if ((currentTime - startTime) > (find_object_timeout * 1000))
				break;
			
			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
		}
		return new result(status_run.FAILED, error_code.ACTION_FAILED, "Trying to select '"+expected_text+"' for object'"+find_object_name+"' failed");

	}
	result execute_selectspanitem(int timeout) {
		result check_method = validate();
		if (check_method.get_result() 	!= status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 1);
		String expected_text = get_inputs().get(find_object_index + 1).get_value();
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// Select
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");

		long startTime = (new Date()).getTime();
		while (true) {
			try {
				String tagName = foundElement.getTagName();
				
				List<WebElement> liElements = foundElement.findElements(By.tagName("span"));
				WebElement li = null;
				for (int i = 0 ; i < liElements.size() ; i ++) {
					String text_found = liElements.get(i).getText();
					if(util.reg_compare(text_found, expected_text)) {
						li = liElements.get(i);
						break;
					}
				}
				
				if (li != null) { // execute click
					li.click();
					return new result(status_run.PASSED, error_code.NO_ERROR, "'' is selected for object '"+find_object_name+"'", "test");
				} else {
					return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Select Span '"+find_object_name+"' is not found");
				}
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			

			
			long currentTime = (new Date()).getTime(); // miliseconds
			if ((currentTime - startTime) > (find_object_timeout * 1000))
				break;
			
			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
		}
		return new result(status_run.FAILED, error_code.ACTION_FAILED, "Trying to select '"+expected_text+"' for object'"+find_object_name+"' failed");

	}
	
	result execute_selectliitem(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 1);
		String expected_text = get_inputs().get(find_object_index + 1).get_value();
		 // String expected_text = "231";
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// Select
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");

		long startTime = (new Date()).getTime();
		while (true) {
			try {
				String tagName = foundElement.getTagName();
				
				List<WebElement> liElements = foundElement.findElements(By.tagName("li"));
				WebElement li = null;
				for (int i = 0 ; i < liElements.size() ; i ++) {
					String text_found = liElements.get(i).getText();
					if(util.reg_compare(text_found, expected_text)) {
						li = liElements.get(i);
						break;
					}
				}
				
				if (li != null) { // execute click
					li.click();
					return new result(status_run.PASSED, error_code.NO_ERROR, "'' is selected for object '"+find_object_name+"'", "test");
				} else {
					return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Select li '"+find_object_name+"' is not found");
				}
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			

			
			long currentTime = (new Date()).getTime(); // miliseconds
			if ((currentTime - startTime) > (find_object_timeout * 1000))
				break;
			
			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
		}
		return new result(status_run.FAILED, error_code.ACTION_FAILED, "Trying to select '"+expected_text+"' for object'"+find_object_name+"' failed");

	}
	
	result execute_selectanyliitem(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 0);
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// Select
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
		
		long startTime = (new Date()).getTime();
		while (true) {
			try {
				
				List<WebElement> liElements = foundElement.findElements(By.tagName("li"));
				WebElement li = null;
				
				Random randomGen = new Random();
				int span_index = randomGen.nextInt(liElements.size());
				li = liElements.get(span_index);
						
				if (li != null) { // execute click
					li.click();
					return new result(status_run.PASSED, error_code.NO_ERROR, "'' is selected for object '"+find_object_name+"'", "test");
				} else {
					return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Select li '"+find_object_name+"' is not found");
				}
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			long currentTime = (new Date()).getTime(); // miliseconds
			if ((currentTime - startTime) > (find_object_timeout * 1000))
				break;
			
			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
		}
		
		return new result(status_run.FAILED, error_code.ACTION_FAILED, "Unable to convert '"+find_object_name+"' to Select object");

	}
	
	result execute_selectanyspanitem(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 0);
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// Select
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
		
		long startTime = (new Date()).getTime();
		while (true) {
			try {
				
				List<WebElement> liElements = foundElement.findElements(By.className("ng-option-label"));
				WebElement li = null;
				
				Random randomGen = new Random();
				int span_index = randomGen.nextInt(liElements.size());
				li = liElements.get(span_index);
						
				if (li != null) { // execute click
					li.click();
					return new result(status_run.PASSED, error_code.NO_ERROR, "'' is selected for object '"+find_object_name+"'", "test");
				} else {
					return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Select Span '"+find_object_name+"' is not found");
				}
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			long currentTime = (new Date()).getTime(); // miliseconds
			if ((currentTime - startTime) > (find_object_timeout * 1000))
				break;
			
			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
		}
		
		return new result(status_run.FAILED, error_code.ACTION_FAILED, "Unable to convert '"+find_object_name+"' to Select object");

	}
	result execute_selectanyitem(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 0);
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// Select
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
		String tagName = foundElement.getTagName();
		if (!tagName.toLowerCase().equals("select"))return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "Object tag name is '"+tagName+"', Object '"+find_object_name+"' tag name should be select");
		
		// Scan all the options to find the relevant option with regular expression
		Select objSel = null;
		try {
			objSel = new Select(foundElement);
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.ACTION_FAILED, "Unable to convert WebElement to Select object type, Action '" + get_name() + "' object '"+find_object_name+"' failed, exception : " + e);
		}
		
		if (objSel != null) {
		
			List<WebElement> options = objSel.getOptions();
			
			if (options.size() > 0) {
				// random select
				// -- get random number
				Random randomGen = new Random();
				int select_index = randomGen.nextInt(options.size());
				String optionText = options.get(select_index).getText();
				objSel.selectByIndex(select_index);
				if (get_Return() != null)
					get_Return().copy_from(optionText);
				return new result(status_run.PASSED, error_code.NO_ERROR, "'"+optionText+"' is select for '"+find_object_name+"'", optionText);
			}
			else
				return new result(status_run.FAILED, error_code.ACTION_FAILED, "There are no option to select for object '"+find_object_name+"' failed");
		}
		else
			return new result(status_run.FAILED, error_code.ACTION_FAILED, "Unable to convert '"+find_object_name+"' to Select object");
	}
	result execute_selectanynotfirst(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 0);
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// Select
		if (foundElement != null) {
			String tagName = foundElement.getTagName();
			if (tagName.toLowerCase().equals("select")) {
				// Scan all the options to find the relevant option with regular expression
				Select objSel = new Select(foundElement);
				
				List<WebElement> options = objSel.getOptions();
				
				if (options.size() > 1) {
					// random select
					// -- get random number
					Random randomGen = new Random();
					int select_index = randomGen.nextInt(options.size() - 1) + 1;
					String optionText = options.get(select_index).getText();
					objSel.selectByIndex(select_index);
					if (get_Return() != null)
						get_Return().copy_from(optionText);
					return new result(status_run.PASSED, error_code.NO_ERROR, "'"+optionText+"' is selected for '"+find_object_name+"'", optionText);
				}
				else
					return new result(status_run.FAILED, error_code.ACTION_FAILED, "There is '"+options.size()+"' option to select for ' object '"+find_object_name+"'");
			}
			else
				return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "Object tag name is '"+tagName+"', Object '"+find_object_name+"' tag name should be select");
		}
		else
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
	}
	result execute_getdescendantsxpath(int timeout) {
		if (get_Return() != null)
			get_Return().clear();
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		// Find element objectWebElement foundElement = null;
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout);
		int start_inputs_index = find_object_index + 1;
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
		
		ArrayList<com.ttv.at.test.property> properties = new ArrayList<com.ttv.at.test.property>();
		int click_index = -1;
		
		for (int i = start_inputs_index ; i < get_inputs().size() ; i ++) {
			String compare_criteria = get_inputs().get(i).get_value();
			if (compare_criteria != null) {
				int separate_point = compare_criteria.indexOf(":");
				if (separate_point > 0 && separate_point < (compare_criteria.length() - 1)){
					String att_name = compare_criteria.substring(0, separate_point);
					String att_expected_value = compare_criteria.substring(separate_point + 1, compare_criteria.length());
					if (att_name != null && att_name.length() > 0)
					{
						att_name = att_name.toLowerCase();
						if (att_name.equals("indexno"))
							try {
								click_index = Integer.parseInt(att_expected_value);
							} catch (Exception eee) {}
						else
							properties.add(new com.ttv.at.test.property(att_name, att_expected_value));
					}
				}
				else
					return new result(status_run.FAILED, error_code.REQUIRE_INPUT, "Compare criteria '"+compare_criteria+"' is not in right format(should be [key:value]), action object is '"+find_object_name+"'");
			}
		}
		
		
		guielement gelement = new guielement("xpath_search", properties);
		
		long startTime = (new Date()).getTime();
		while (true) {
			int iFoundCount = 0;
			if (get_Return() != null)
				get_Return().clear();
			boolean failed = false;
			try {
				ArrayList<WebElement> match_elements = WebElement_get_descendants(foundElement, gelement, click_index + 1);
				if (match_elements != null && match_elements.size() > 0)
					for (WebElement scan_element : match_elements){
						String xpath = find_object_based_frame_xpath + "/html/" + (String)((JavascriptExecutor)driver).executeScript(get_xpath_jscript, scan_element);
						if (get_Return() != null)	
							get_Return().put_value(xpath);
						iFoundCount ++;
					}
				if (iFoundCount > 0)
				{
					if (get_Return() != null)
						return new result(status_run.PASSED, error_code.NO_ERROR, "Found " + get_Return().get_array_size() + " items descendant of '" + find_object_name + "' ");
					return new result(status_run.PASSED, error_code.NO_ERROR, "Get descendant xpath '" + find_object_name + "' passed");
				}
			}
			catch (UnreachableBrowserException ue) {
				System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
				System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
				ue.printStackTrace();
				try { Thread.sleep(500);}catch (Exception se) {}
				failed = true;
			}
			catch (Exception e) {
				e.printStackTrace();
				failed = true;
			}
			

			
			long currentTime = (new Date()).getTime(); // miliseconds
			if ((currentTime - startTime) > (timeout * 1000))
				if ( !failed )
					return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "Get descendant xpath '" + find_object_name + "' failed, no child is matched with input criteria");
				else
					return new result(status_run.FAILED, error_code.ACTION_FAILED, "Trying to get descendantsxpath of object'"+find_object_name+"' failed");
			
			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
			
			if (get_action_object() != null)
				foundElement = find_object (selenium_guielement, timeout);
			else
				foundElement = find_object(find_object_name, timeout);
			
			if (foundElement == null)
				return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
		}
	}
	result execute_getproperty(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 1);
		int start_input_index = find_object_index + 1;
		
		if (foundElement != null) {
			// String contents = (String)((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", foundElement);
			String attribute_name = get_inputs().get(start_input_index).get_value().toLowerCase();
			if (attribute_name.equals("text")) {
				String text = get_WebElement_text(foundElement);
				if (get_Return() != null)
					get_Return().copy_from(text);
				return new result(status_run.PASSED, error_code.NO_ERROR, "Text is '"+text+"' of '"+find_object_name+"'", text);
			}
			else {
				String att_value = null;
				if (attribute_name.toLowerCase() == "tagname")
					att_value = foundElement.getTagName();
				else if (attribute_name.toLowerCase() == "name")
					att_value = foundElement.getAttribute("name");
				else if (attribute_name.toLowerCase() == "html classname" || attribute_name.toLowerCase() == "classname")
					att_value = foundElement.getAttribute("class");
				else
					att_value = foundElement.getAttribute(attribute_name);
				
				if (att_value != null)
					att_value = att_value.trim();
				if (get_Return() != null)
					get_Return().copy_from(att_value);
				return new result(status_run.PASSED, error_code.NO_ERROR, "Property '" + attribute_name + "' is '"+att_value+"' of '"+find_object_name+"'", att_value);
			}
		}
		else
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
	}
	result execute_clickanydescendant(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout);
		int start_inputs_index = find_object_index + 1;
		
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");

		ArrayList<com.ttv.at.test.property> properties = new ArrayList<com.ttv.at.test.property>();
		int click_index = -1;
		
		for (int i = start_inputs_index ; i < get_inputs().size() ; i ++) {
			String compare_criteria = get_inputs().get(i).get_value();
			if (compare_criteria != null) {
				int separate_point = compare_criteria.indexOf(":");
				if (separate_point > 0 && separate_point < (compare_criteria.length() - 1)){
					String att_name = compare_criteria.substring(0, separate_point);
					String att_expected_value = compare_criteria.substring(separate_point + 1, compare_criteria.length());
					if (att_name != null && att_name.length() > 0)
					{
						att_name = att_name.toLowerCase();
						if (att_name.equals("indexno"))
							try {
								click_index = Integer.parseInt(att_expected_value);
							} catch (Exception eee) {}
						else
							properties.add(new com.ttv.at.test.property(att_name, att_expected_value));
					}
				}
				else
					return new result(status_run.FAILED, error_code.REQUIRE_INPUT, "Compare criteria '"+compare_criteria+"' is not in right format(should be [key:value]), action object is '"+find_object_name+"'");
			}
		}
		
		
		guielement gelement = new guielement("xpath_search", properties);
		
		long startTime = (new Date()).getTime();
		while (true) {
			int iFoundCount = 0;
			if (get_Return() != null)
				get_Return().clear();
			boolean failed = false;
			try {
				ArrayList<WebElement> match_elements = WebElement_get_descendants(foundElement, gelement, click_index + 1);
				if (match_elements != null && match_elements.size() > 0) {
					if (click_index < 0 || click_index >= match_elements.size()){
						Random randomGen = new Random();
						click_index = randomGen.nextInt(match_elements.size());
					}
					String clicked_text = get_WebElement_text(match_elements.get(click_index));
					match_elements.get(click_index).click();
					
					if (get_Return() != null && clicked_text != null)
						get_Return().copy_from(clicked_text);
					
					return new result(status_run.PASSED, error_code.NO_ERROR, "Found " + match_elements.size() + " descendant and click at " + click_index + " child of '" + find_object_name + "'");
				}
			}
			catch (UnreachableBrowserException ue) {
				System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
				System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
				ue.printStackTrace();
				try { Thread.sleep(500);}catch (Exception se) {}
				failed = true;
			}
			catch (Exception e) {
				e.printStackTrace();
				failed = true;
			}
			

			
			long currentTime = (new Date()).getTime(); // miliseconds
			if ((currentTime - startTime) > (timeout * 1000))
				if ( !failed )
					return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "Get descendant xpath '" + find_object_name + "' failed, no child is matched with input criteria");
				else
					return new result(status_run.FAILED, error_code.ACTION_FAILED, "Trying to get descendantsxpath of object'"+find_object_name+"' failed");
			
			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
			
			if (get_action_object() != null)
				foundElement = find_object (selenium_guielement, timeout);
			else
				foundElement = find_object(find_object_name, timeout);
			
			if (foundElement == null)
				return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
		}
	}
	result execute_mouseoveranydescendant(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout);
		int start_inputs_index = find_object_index + 1;
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
		
		ArrayList<com.ttv.at.test.property> properties = new ArrayList<com.ttv.at.test.property>();
		int click_index = -1;
		
		for (int i = start_inputs_index ; i < get_inputs().size() ; i ++) {
			String compare_criteria = get_inputs().get(i).get_value();
			if (compare_criteria != null) {
				int separate_point = compare_criteria.indexOf(":");
				if (separate_point > 0 && separate_point < (compare_criteria.length() - 1)){
					String att_name = compare_criteria.substring(0, separate_point);
					String att_expected_value = compare_criteria.substring(separate_point + 1, compare_criteria.length());
					if (att_name != null && att_name.length() > 0)
					{
						att_name = att_name.toLowerCase();
						if (att_name.equals("indexno"))
							try {
								click_index = Integer.parseInt(att_expected_value);
							} catch (Exception eee) {}
						else
							properties.add(new com.ttv.at.test.property(att_name, att_expected_value));
					}
				}
				else
					return new result(status_run.FAILED, error_code.REQUIRE_INPUT, "Compare criteria '"+compare_criteria+"' is not in right format(should be [key:value]), action object is '"+find_object_name+"'");
			}
		}
		
		
		guielement gelement = new guielement("xpath_search", properties);
		
		long startTime = (new Date()).getTime();
		while (true) {
			int iFoundCount = 0;
			if (get_Return() != null)
				get_Return().clear();
			boolean failed = false;
			try {
				ArrayList<WebElement> match_elements = WebElement_get_descendants(foundElement, gelement, click_index + 1);
				if (match_elements != null && match_elements.size() > 0) {
					if (click_index < 0 || click_index >= match_elements.size()){
						Random randomGen = new Random();
						click_index = randomGen.nextInt(match_elements.size());
					}
					// Execute click with click_index
					String clicked_text = get_WebElement_text(match_elements.get(click_index));
					
					Actions builder = new Actions(driver); // Or maybe seleniumDriver? Not sure which one to use
					builder.moveToElement(match_elements.get(click_index)).perform();
					
					if (get_Return() != null && clicked_text != null)
						get_Return().copy_from(clicked_text);
					
					return new result(status_run.PASSED, error_code.NO_ERROR, "Found " + match_elements.size() + " descendant and click at " + click_index + " child of '" + find_object_name + "'");
				}
			}
			catch (UnreachableBrowserException ue) {
				System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
				System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
				ue.printStackTrace();
				try { Thread.sleep(500);}catch (Exception se) {}
				failed = true;
			}
			catch (Exception e) {
				e.printStackTrace();
				failed = true;
			}
			

			
			long currentTime = (new Date()).getTime(); // miliseconds
			if ((currentTime - startTime) > (timeout * 1000))
				if ( !failed )
					return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "Get descendant xpath '" + find_object_name + "' failed, no child is matched with input criteria");
				else
					return new result(status_run.FAILED, error_code.ACTION_FAILED, "Trying to get descendantsxpath of object'"+find_object_name+"' failed");
			
			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
			
			if (get_action_object() != null)
				foundElement = find_object (selenium_guielement, timeout);
			else
				foundElement = find_object(find_object_name, timeout);
			
			if (foundElement == null)
				return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
		}
	}
	result execute_clicktablecell(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout);
		String text_to_set = get_inputs().get(find_object_index + 1).get_value();
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// Click
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
		
		// Scan in all rows
		int clicked_cell_index = 0;
		if (get_inputs().get(0).get_value() != null && get_inputs().get(0).get_value().length() > 0) {
			clicked_cell_index = Integer.parseInt(get_inputs().get(0).get_value());
			if (clicked_cell_index < 0)
				clicked_cell_index = 0;
		}
		List<WebElement> table_rows = foundElement.findElements(By.tagName("tr"));
		for(WebElement table_row:table_rows) {
			// Get row cells to check
			List<WebElement> row_cells = table_row.findElements(By.tagName("td"));
			if (row_cells.size() >= (get_inputs().size())) {
				boolean pass_check = true;
				for (int i = 0 ; i < (row_cells.size()) ; i ++){
					WebElement cur_cell = row_cells.get(i);
					String expected_cell_value = get_inputs().get(1).get_value();
					if (expected_cell_value != null)
					{
						String actual_text = cur_cell.getText();
						if (actual_text != null)
							actual_text = actual_text.trim();
						if (util.reg_compare(actual_text, expected_cell_value))
						{
							pass_check = true;
							break;
						}
						if (!util.reg_compare(actual_text, expected_cell_value))
						{
							pass_check = false;
						}
					}
				}
				
				if (pass_check)
				{
					// execute click
					if (clicked_cell_index >= row_cells.size())
						clicked_cell_index = row_cells.size() - 1;
					WebElement clicked_cell = row_cells.get(clicked_cell_index);
					clicked_cell.click();
					return new result(status_run.PASSED, error_code.NO_ERROR, "Click a row in table '" + find_object_name + "' passed");
				}
			}
		}
		

		return new result(status_run.FAILED, error_code.ACTION_FAILED, "Click a row in table '" + find_object_name + "' failed, row is not found");
		
	}
	result execute_clicktablerow(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout);
		int start_inputs_index = find_object_index + 1;
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// Click
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
		
		// Scan in all rows
		List<WebElement> table_rows = foundElement.findElements(By.tagName("tr"));
		for(WebElement table_row:table_rows) {
			// Get row cells to check
			List<WebElement> row_cells = table_row.findElements(By.tagName("td"));
			if (row_cells.size() >= (get_inputs().size() - 1)) {
				boolean pass_check = true;
				for (int i = start_inputs_index ; i < (get_inputs().size() - 1) ; i ++){
					WebElement cur_cell = row_cells.get(i);
					String expected_cell_value = get_inputs().get(i).get_value();
					if (expected_cell_value != null)
					{
						String actual_text = cur_cell.getText();
						if (actual_text != null)
							actual_text = actual_text.trim();
						if (!util.reg_compare(actual_text, expected_cell_value))
						{
							pass_check = false;
							break;
						}
					}
				}
				
				if (pass_check)
				{
					// execute click
					table_row.click();
					return new result(status_run.PASSED, error_code.NO_ERROR, "Click a row in table '" + find_object_name + "' passed");
				}
			}
		}
		

		return new result(status_run.FAILED, error_code.ACTION_FAILED, "Click a row in table '" + find_object_name + "' failed, row is not found");
		
	}
	result execute_searchintable(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout);
		int start_inputs_index = find_object_index + 1;
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// Click
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
		
		// Scan in all rows
		List<WebElement> table_rows = foundElement.findElements(By.tagName("tr"));
		for(WebElement table_row:table_rows) {
			// Get row cells to check
			List<WebElement> row_cells = table_row.findElements(By.tagName("td"));
			if (row_cells.size() >= (get_inputs().size() - 1)) {
				boolean pass_check = true;
				for (int i = start_inputs_index ; i < (get_inputs().size() - 1) ; i ++){
					WebElement cur_cell = row_cells.get(i);
					String expected_cell_value = get_inputs().get(i).get_value();
					if (expected_cell_value != null)
					{
						String actual_text = cur_cell.getText();
						if (actual_text != null)
							actual_text = actual_text.trim();
						if (!util.reg_compare(actual_text, expected_cell_value))
						{
							pass_check = false;
							break;
						}
					}
				}
				
				if (pass_check)
				{
					// execute click
					String xpath = find_object_based_frame_xpath + "/html/" + (String)((JavascriptExecutor)driver).executeScript(get_xpath_jscript, table_row);

					if (get_Return() != null)
						get_Return().copy_from(xpath);
					return new result(status_run.PASSED, error_code.NO_ERROR, "Click a row in table '" + find_object_name + "' passed");
				}
			}
		}
		

		return new result(status_run.FAILED, error_code.ACTION_FAILED, "Click a row in table '" + find_object_name + "' failed, row is not found");
		
	}
	result execute_executescript(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 1);
		String script_to_execute = get_inputs().get(find_object_index + 1).get_value();
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		String script_result = null;
		// execute
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
		try {
			script_result = (String)((JavascriptExecutor)driver).executeScript(script_to_execute, foundElement);
		}
		catch (Exception e)
		{
			// Find element object
			foundElement = find_object (selenium_guielement, get_inputs(), timeout, 1);
			
			if (stop)
				return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
			if (pause)
				return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
			
			// execute
			if (foundElement == null)
				return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");
			

			script_result = (String)((JavascriptExecutor)driver).executeScript(script_to_execute);
		}
		
		if (get_Return() != null)
			get_Return().copy_from(script_result);
		
		return new result(status_run.PASSED, error_code.NO_ERROR, "Execute Script '"+script_to_execute+"' for object '"+find_object_name+"' has result '" + script_result + "'");
	}
	result execute_getpageurl(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 0);
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// gettext
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+find_object_name+"' is not found");

		String page_url = driver.getCurrentUrl();
		if (page_url != null && page_url.length() > 0){
			if (get_Return() != null)
				get_Return().copy_from(page_url);
			return new result(status_run.PASSED, error_code.NO_ERROR, "current page url of object'"+find_object_name+"' is '"+page_url+"'");
		}
		return new result(status_run.FAILED, error_code.ACTION_FAILED, "Trying to get current page url of object'"+find_object_name+"' failed");
	}
	result execute_checkpageurl(int timeout) {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		
		// Find element object
		WebElement foundElement = find_object (selenium_guielement, get_inputs(), timeout, 1);
		String object_name = null;
		String text_to_check = get_inputs().get(find_object_index + 1).get_value();
		
		if (stop)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" stop by User");
		if (pause)
			return new result(status_run.STOP, error_code.NO_ERROR, "Action "+get_name()+" paused by User");
		
		// gettext
		if (foundElement == null)
			return new result(status_run.FAILED, error_code.OBJECT_NOT_FOUND, "Object '"+object_name+"' is not found");
		

		String page_url = driver.getCurrentUrl();
		if (util.reg_compare(page_url, text_to_check)) {
			if (get_Return() != null)
				get_Return().copy_from(page_url);
			return new result(status_run.PASSED, error_code.NO_ERROR, "Current page url is '"+page_url+"' of object '"+object_name+"'");
		}

		return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "Expected page url is '"+ text_to_check+"' but Actual is '"+page_url+
				"' - Check current page url of object '"+ object_name+"' failed");
		
	}

	/******** DRIVER AND OPEN BROWSER ********/
	static WebDriver driver = null;
	static public WebDriver get_driver() { return driver; }
	static public void clear_driver() {
		if (driver != null) {
			try { driver.quit(); } catch (Exception e) { }
		}
		driver = null;
	}
	static public boolean open_browser_firefox(String site_address) {
		clear_driver();
//		// Create firefox Profile that anti Telemetry
		String firefox_profile_path = "conf" + com.ttv.at.util.os.os_file_separator + "gecko.selenium.driver";
//		//if (ie_version != null && ie_version.length() > 0)
		File firefoxFile = new File(firefox_profile_path);
//		WebDriverManager.firefoxdriver().cachePath(firefox_profile_path).avoidOutputTree().setup();
		System.setProperty("webdriver.gecko.driver", firefoxFile.getAbsolutePath() + "\\geckodriver.exe");
//		//FirefoxBinary firefoxbin = new FirefoxBinary(firefoxFile);
//		//driver = new FirefoxDriver();

		FirefoxOptions options = new FirefoxOptions();
		Properties conf = testsetting.getProperties();
		String enable_proxy = conf.getProperty("proxy.type");
		if(enable_proxy.equals("1")) {
			 Proxy proxy = conf_proxy_server(conf);
			 options.setCapability(CapabilityType.PROXY, proxy);
		}
		
		driver = new FirefoxDriver(options);
		driver.manage().deleteAllCookies();
		driver.get(site_address);
		return true;
	}
	static public boolean open_browser_edge(String site_address) {
		 clear_driver();
		// Create Edge Profile that anti Telemetry
		// String DriverPath=System.getProperty("user.dir")+"\\"+"Drivers";
	     //System.setProperty("webdriver.edge.driver", DriverPath+"//"+"MicrosoftWebDriver.exe");
		 String edge_profile_path = "conf" + com.ttv.at.util.os.os_file_separator + "edge.selenium.driver";
			//if (ie_version != null && ie_version.length() > 0)
		 WebDriverManager.edgedriver().cachePath(edge_profile_path).avoidOutputTree().setup();
//			System.setProperty("webdriver.edge.driver", firefoxFile.getAbsolutePath() + "\\MicrosoftWebDriver.exe");
//		 WebDriverManager.edgedriver().setup();
		 driver = new EdgeDriver();
		 driver.manage().deleteAllCookies();
		 driver.get(site_address);
		
		return true;
	}
	
	static public boolean open_browser_internetexplorer(String site_address) {
		clear_driver();
		//try
		//{
		//	Runtime.getRuntime().exec("taskkill -f -im iexplore.exe -t");
		//	Thread.sleep(2000);
		//}
		//catch (Exception ioe)
		//{
		//	ioe.printStackTrace();
		//}
		
		/*clear cache*/
		//Runtime rt = Runtime.getRuntime();
		//try {
			// Call command line to clean-up IE
			//Process pr = rt.exec("RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 4351");
		//} catch (IOException e1) {
		//	e1.printStackTrace();
		//}
	
		/*done clear cache*/
/*        DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit().;
        //capabilities.setCapability("ignoreZoomSetting", true);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability("nativeEvents", false);
        capabilities.setCapability("allow-blocked-content", true);
        capabilities.setCapability("disable-popup-blocking", true);
        capabilities.setCapability("allowBlockedContent", true);
        capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        //capabilities.setCapability("requireWindowFocus", true);
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);*/
		String ie_profile_path = "conf" + com.ttv.at.util.os.os_file_separator + "ie.selenium.driver";
		File InternetExplorerProfileFile = new File(ie_profile_path);
		System.setProperty("webdriver.ie.driver", InternetExplorerProfileFile.getAbsolutePath() + "\\IEDriverServer.exe");
		driver = new InternetExplorerDriver();
		driver.manage().deleteAllCookies();
		driver.get(site_address);
		script_click_secure();
		return true;
	}
	
	private static void script_click_secure() {
		List<WebElement> foundElements = driver.findElements(By.id("overridelink"));
		if(foundElements.size() > 0) {
			driver.navigate ().to ("javascript:document.getElementById('overridelink').click()");
		}
	}

	static public boolean open_browser_internetexplorer_1(String site_address) {
		
		clear_driver();
		// Close all ie before launching
		try
		{
			Runtime.getRuntime().exec("taskkill -f -im iexplore.exe -t");
			Thread.sleep(2000);
		}
		catch (Exception ioe)
		{
			ioe.printStackTrace();
		}
		
		/*clear cache*/
		Runtime rt = Runtime.getRuntime();
		try {
			// Call command line to clean-up IE
			Process pr = rt.exec("RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 4351");
			Thread.sleep(2000);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		/*done clear cache*/

		//DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		//ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		//System.setProperty("webdriver.ie.driver", "D:\\Library_FrameWork_MBaseVN\\lib-jar\\IEDriverServer.exe");
		//driver = new InternetExplorerDriver(ieCapabilities);
		driver.manage().deleteAllCookies();
		driver.get(site_address);
		try { Thread.sleep(3000); } catch (InterruptedException e) {}
		return true;
	}
	static public boolean open_browser_chrome(String site_address) {
		clear_driver();
//		// Create chrome Profile that anti Telemetry
		String chrome_profile_path = "conf" + com.ttv.at.util.os.os_file_separator + "chrome.selenium.driver";
		String chrome_download_path = "downloads" + com.ttv.at.util.os.os_file_separator;
//		//if (ie_version != null && ie_version.length() > 0)
//		File chromeFile = new File(chrome_profile_path);
//		System.setProperty("webdriver.chrome.driver", chromeFile.getAbsolutePath() + "\\chromedriver.exe");
		WebDriverManager.chromedriver().cachePath(chrome_profile_path).avoidOutputTree().setup();
//		driver = new ChromeDriver();
		//if (InternetExplorerProfileFile.exists()) {
			//profile.setEnableNativeEvents(true);
			//driver = new InternetExplorerDriver(profile);
		//}
		//else
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("download.default_directory",System.getProperty("user.dir") + File.separator + "downloads");
		Properties conf = testsetting.getProperties();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		options.addArguments("--disable-extensions");
		options.addArguments("--remote-allow-origins=*");
		options.setExperimentalOption("prefs", prefs);
//		options.addArguments("--disable-gpu")
//		options.addArguments("--no-sandbox") # linux only
//		options.addArguments("--headless");
//		options.headless = True # also works

		//Handle Proxy Server
		String enable_proxy = conf.getProperty("proxy.type");
		if(enable_proxy.equals("1")) {
			 Proxy proxy = conf_proxy_server(conf);
			 options.setCapability(CapabilityType.PROXY, proxy);
		}
		options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
		driver = new ChromeDriver(options);
		driver.manage().deleteAllCookies();
		driver.get(site_address);
		
		return true;
	}
	
	static public boolean open_browser_chrome_proxy(String site_address, String server, String port, String username, String password) {
		clear_driver();
		// Create chrome Profile that anti Telemetry
		String chrome_profile_path = "conf" + com.ttv.at.util.os.os_file_separator + "chrome.selenium.driver";
		//if (ie_version != null && ie_version.length() > 0)
		File chromeFile = new File(chrome_profile_path);
		System.setProperty("webdriver.chrome.driver", chromeFile.getAbsolutePath() + "\\chromedriver.exe");
		
        driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
		driver.get(site_address);
		return true;
	}
	
	static public boolean open_browser_safari(String site_address) {
		clear_driver();
		try {
			driver = new SafariDriver();
		}
		catch (Exception eee) {}
		driver.manage().deleteAllCookies();
		driver.get(site_address);
		// try { Thread.sleep(3000); } catch (InterruptedException e) {}
		return true;
	}
	//static public boolean open_browser_android(String site_address) {
		//clear_driver();
		//driver = new AndroidDriver();
		//driver.manage().deleteAllCookies();
		//driver.get(site_address);
		// try { Thread.sleep(3000); } catch (InterruptedException e) {}
		//return true;
	//}

	static private String find_object_name = "";
	static private int find_object_timeout = 15;
	static private int find_object_index = -1;
	static private WebElement find_object (guielement selenium_guielement, ArrayList<parameter> action_inputs, int timeout) {
		find_object_index = -1;
		find_object_timeout = timeout;
		find_object_name = null;
		if (selenium_guielement != null) {
			find_object_name = selenium_guielement.get_key();
			return find_object(selenium_guielement, find_object_timeout);
		}
		else if (action_inputs != null && action_inputs.size() > 0) {
			find_object_index = 0;
			find_object_name = action_inputs.get(find_object_index).get_value();
			if (find_object_name != null && find_object_name.length() > 0) {
				return find_object(find_object_name, find_object_timeout);
			}
		}
		return null;
	}
	static private WebElement find_object (guielement selenium_guielement, ArrayList<parameter> action_inputs, int timeout, int number_of_useful_action_inputs) {
		find_object_index = -1;
		find_object_timeout = timeout;
		find_object_name = null;
		if (selenium_guielement != null) {
			find_object_name = selenium_guielement.get_key();
			if (action_inputs != null && action_inputs.size() > number_of_useful_action_inputs) {
				try{
					find_object_timeout = Integer.parseInt(action_inputs.get(number_of_useful_action_inputs).get_value());
				}
				catch (Exception e) { }
			}
			return find_object(selenium_guielement, find_object_timeout);
		}
		else if (action_inputs != null && action_inputs.size() > number_of_useful_action_inputs) {
			find_object_index = 0;
			find_object_name = action_inputs.get(find_object_index).get_value();
			if (find_object_name != null && find_object_name.length() > 0) {
				if (action_inputs.size() > (number_of_useful_action_inputs + 1)) {
					try{
						find_object_timeout = Integer.parseInt(action_inputs.get(number_of_useful_action_inputs + 1).get_value());
					}
					catch (Exception e) { }
				}
				return find_object(find_object_name, find_object_timeout);
			}
		}
		return null;
	}
	
	static private boolean un_reach_browser_exception = false;
	static private String find_object_based_frame_xpath = ""; 
	static private WebElement find_object (String xpath, int timeout) {
		// Get the xpath list
		ArrayList<String> xpaths = new ArrayList<String>();
		String xpath_temp = xpath;
		int index_of_last_html = xpath_temp.lastIndexOf("/html");
		if (index_of_last_html == 0)
			xpaths.add(xpath_temp);
		else
		{
			while (index_of_last_html > 0) {
				String last_xpath = xpath_temp.substring(index_of_last_html);
				xpaths.add(0, last_xpath);
				xpath_temp = xpath_temp.substring(0, index_of_last_html);
				index_of_last_html = xpath_temp.lastIndexOf("/html");
			}
			if (index_of_last_html == 0)
				xpaths.add(0, xpath_temp);
		}
		
		WebElement last_scan_object = null;
		
		for (int i = 0 ; i < xpaths.size() ; i ++) {
			final String cur_xpath = xpaths.get(i);
			if (last_scan_object != null)
				driver.switchTo().frame(last_scan_object); 
			last_scan_object = (new WebDriverWait(driver, Duration.ofSeconds(timeout)))
			.until(new ExpectedCondition<WebElement>(){ 
				@Override 
				public WebElement apply(WebDriver d) { 
					return d.findElement(By.xpath(cur_xpath)); 
					}}); 
		}
		return last_scan_object;
	}
	
	static private WebElement find_object (guielement target_object, int timeout) {
		if (target_object != null && target_object.get_guiinfos() != null)
			if (target_object.get_guiinfos().size() == 1)
				return find_object_single_guiinfo (target_object.get_guiinfos().get(0), timeout);
			else if (target_object.get_guiinfos().size() > 1)
				return find_object_multi_guiinfos (target_object.get_guiinfos(), timeout);
		return null;
	}
	
	static private WebElement find_object (guielement guiElement)
	{
		if (guiElement != null && guiElement.get_guiinfos() != null)
			if (guiElement.get_guiinfos().size() == 1)
				return find_object_single_guiinfo (guiElement.get_guiinfos().get(0));
			else if (guiElement.get_guiinfos().size() > 1)
				return find_object_multi_guiinfos (guiElement.get_guiinfos());
		return null;
	}
	
	static private WebElement find_object_single_guiinfo (guiinfo single_properties, int timeout) {
		Set<String> hwnds = driver.getWindowHandles();
		Object[] arr_hwnds = hwnds.toArray();
		WebElement foundElement = null;
		
		long startTime = (new Date()).getTime();
		
		while (true) {
			// *** Detect the object  ***
			for (String HWND : hwnds){
				driver.switchTo().window(HWND);
				foundElement = find_object_single_guiinfo(single_properties);
				if (foundElement != null)
					return foundElement;
			}
			
			if (stop || pause)
				return null;
			
			boolean stop = false;
			long currentTime = (new Date()).getTime(); // miliseconds
			if ((currentTime - startTime) > (timeout * 1000))
				stop = true;

			// *** try again after timeout ***
			for (String HWND : hwnds){
				driver.switchTo().window(HWND);
				foundElement = find_object_single_guiinfo(single_properties);
				if (foundElement != null)
					return foundElement;
			}
			
			if (stop)
				break;
			
			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
		}
		
		return foundElement;
	}
	static private WebElement find_object_single_guiinfo (guiinfo single_properties) {
		if (single_properties != null)
		{
			WebElement ref_object = null;
			if (single_properties.get_related_object() == null)
			{
				WebElement foundElement = get_element_by_gui_info(single_properties);
				if (foundElement != null)
					return foundElement;
			}
			else
			{
				ref_object = find_object(single_properties.get_related_object());
				if (ref_object != null)
				{
					WebElement foundElement = get_element_by_gui_info(single_properties, ref_object);
					if (foundElement != null)
						return foundElement;
				}
			}
		}
		return null;
	}
	
	static private WebElement find_object_multi_guiinfos (ArrayList<guiinfo> guiinfos, int timeout) {
		Set<String> hwnds = driver.getWindowHandles();
		Object[] arr_hwnds = hwnds.toArray();
		WebElement foundElement = null;
		
		long startTime = (new Date()).getTime();
		
		while (true) {
			// *** Detect the object  ***
			for (String HWND : hwnds){
				driver.switchTo().window((String)arr_hwnds[0]);
				foundElement = find_object_multi_guiinfos(guiinfos);
				if (foundElement != null)
					return foundElement;
			}
			
			if (stop || pause)
				return null;
			
			boolean stop = false;
			long currentTime = (new Date()).getTime(); // miliseconds
			if ((currentTime - startTime) > (timeout * 1000))
				stop = true;

			// *** try again after timeout ***
			for (String HWND : hwnds){
				driver.switchTo().window((String)arr_hwnds[0]);
				foundElement = find_object_multi_guiinfos(guiinfos);
				if (foundElement != null)
					return foundElement;
			}
			
			if (stop)
				break;
			
			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
		}
		
		return foundElement;
	}

	static private WebElement find_object_multi_guiinfos (ArrayList<guiinfo> guiinfos) {
		if (guiinfos == null && guiinfos.size() <= 1)
			return null;
		
		for (guiinfo scan_properties : guiinfos)
		{
			WebElement ref_object = null;
			if (scan_properties.get_related_object() == null)
			{
				WebElement found_element = get_element_by_gui_info(scan_properties);
				if (found_element != null)
					return found_element;
			}
			else
			{
				ref_object = find_object(scan_properties.get_related_object());
				if (ref_object != null)
				{
					WebElement found_element = get_element_by_gui_info(scan_properties, ref_object);
					if (found_element != null)
						return found_element;
				}
			}
		}
		return null;
	}

	static private WebElement get_element_by_gui_info (guiinfo guiInfo) {
		WebElement searchE = get_element_by_gui_info_quick_search (guiInfo);
		if (searchE != null)
			return searchE;

		searchE = get_element_by_gui_info_detail_search (guiInfo);
		if (searchE != null)
			return searchE;
		return null;
	}
	static private WebElement get_element_by_gui_info (guiinfo guiInfo, WebElement ref_object) {
		if (guiInfo.get_related_type() == ref_object_type.PARENT) {
			int matched_index = 0;
			WebElement cur_Element = ref_object.findElement(By.xpath(".."));
			while (cur_Element != null) {
				if (stop || pause)
					return null;
				if (un_reach_browser_exception) {
					un_reach_browser_exception = false;
					try {
						Thread.sleep(500);
					} catch (Exception se) {}
				}
				boolean passCheck = get_element_by_gui_info_check_properties(cur_Element, guiInfo);

				if (passCheck) {
					if (matched_index == guiInfo.get_htmlIndex())
						return cur_Element;
					else
						matched_index ++;
				}
				cur_Element = cur_Element.findElement(By.xpath(".."));
			}
			return null;
		}
		else {
			WebElement searchE = get_element_by_gui_info_quick_search (guiInfo, ref_object);
			if (searchE != null)
				return searchE;
			else
				return get_element_by_gui_info_detail_search (guiInfo, ref_object);
		}
	}
	
	static private WebElement get_element_by_gui_info_quick_search (guiinfo guiInfo) {
		if (guiInfo.get_htmlIndex() == 0) {
			/******** QUICK SEARCH BY PROPERTIES ********/
			WebElement foundElement = null;
			boolean has_property = false;
			// - ID
			if (guiInfo.get_htmlID() != null && guiInfo.get_htmlID().length() > 0) {
				has_property = true;
				foundElement = get_object_by_ID(guiInfo.get_htmlID());
			}
			
			// - Name
			else if (guiInfo.get_htmlName() != null && guiInfo.get_htmlName().length() > 0) {
				has_property = true;
				foundElement = get_object_by_Name(guiInfo.get_htmlName());
			}
			
			// - tag Name
			else if (guiInfo.get_htmlTagName() != null && guiInfo.get_htmlTagName().length() > 0) {
				has_property = true;
				foundElement = get_object_by_tagName(guiInfo.get_htmlTagName());
			}
			
			// - classname
			else if (guiInfo.get_htmlClassName() != null && guiInfo.get_htmlClassName().length() > 0) {
				has_property = true;
				foundElement = get_object_by_className(guiInfo.get_htmlClassName());
			}
	
			// - xpath
			else if (guiInfo.get_htmlXpath() != null && guiInfo.get_htmlXpath().length() > 0) {
				has_property = true;
				foundElement = get_object_by_xpath(guiInfo.get_htmlXpath());
			}
	
			// - link text
			else if (guiInfo.get_htmlLinkText() != null && guiInfo.get_htmlLinkText().length() > 0) {
				has_property = true;
				foundElement = get_object_by_linkText(guiInfo.get_htmlLinkText());
			}
	
			// - partial link text
			else if (guiInfo.get_htmlPartialLinkText() != null && guiInfo.get_htmlPartialLinkText().length() > 0) {
					has_property = true;
					foundElement = get_object_by_partialLinkText(guiInfo.get_htmlPartialLinkText());
			}
			
			// - css Selector
			else if (guiInfo.get_htmlCssSelector() != null && guiInfo.get_htmlCssSelector().length() > 0) {
				has_property = true;
				foundElement = get_object_by_cssSelector(guiInfo.get_htmlCssSelector());
			}
			
			
			// ****** check the properties ****** //s
			if(has_property && foundElement != null)
			{
				if (get_element_by_gui_info_check_properties(foundElement, guiInfo))
					return foundElement;
			}
		}
		return null;
	}
	static private WebElement get_element_by_gui_info_quick_search (guiinfo guiInfo, WebElement relatedObject) {
		WebElement searchE = get_element_by_gui_info_quick_search (guiInfo);
		if (searchE != null && find_object_check_ref_object (relatedObject, guiInfo.get_related_type(), searchE))
			return searchE;
		return null;
	}
	static private WebElement get_element_by_gui_info_detail_search (guiinfo guiInfo) {
		List<WebElement> foundElements = get_elements_by_gui_info (guiInfo);
		if (foundElements == null || foundElements.size() == 0)
			return null;

		int matched_index = 0;
		for (WebElement scan_E : foundElements) {
			boolean passCheck = get_element_by_gui_info_check_properties(scan_E, guiInfo);
			
			if (passCheck) {
				if (matched_index == guiInfo.get_htmlIndex())
					return scan_E;
				else
					matched_index ++;
			}
		}
		
		return null;
	}
	static private WebElement get_element_by_gui_info_detail_search (guiinfo guiInfo, WebElement relatedObject) {

		if (guiInfo.get_related_type() == ref_object_type.CHILD)
		{
			if (relatedObject == null)
				return null;
			
			List<WebElement> foundElements = get_elements_by_gui_info (guiInfo, relatedObject);
			if (foundElements == null || foundElements.size() == 0)
				return null;
			
			int matched_index = 0;
			for (WebElement scan_E : foundElements) {
				boolean passCheck = get_element_by_gui_info_check_properties(scan_E, guiInfo);
				
				if (passCheck) {
					if (matched_index == guiInfo.get_htmlIndex())
						return scan_E;
					else
						matched_index ++;
				}
			}
		}
		else if (guiInfo.get_related_type() == ref_object_type.UP ||
				guiInfo.get_related_type() == ref_object_type.DOWN ||
				guiInfo.get_related_type() == ref_object_type.LEFT ||
				guiInfo.get_related_type() == ref_object_type.RIGHT) {

			List<WebElement> foundElements = get_elements_by_gui_info (guiInfo);
			if (foundElements == null || foundElements.size() == 0)
				return null;

			int matched_index = 0;
			for (WebElement scan_E : foundElements) {
				boolean passCheck = get_element_by_gui_info_check_properties(scan_E, guiInfo);
				int i = 0;
				if (passCheck &&
						find_object_check_ref_object (relatedObject, guiInfo.get_related_type(), scan_E)) {
					if (matched_index == guiInfo.get_htmlIndex())
						return scan_E;
					else
						matched_index ++;
				}
			}
			
			return null;
		}
		
		return null;
	}
	static private List<WebElement> get_elements_by_gui_info (guiinfo guiInfo) {
		List<WebElement> foundElements = null;
		if (util.check_string_letters_only(guiInfo.get_htmlID()))
			foundElements = get_objects_by_ID(guiInfo.get_htmlID());
		if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlName()))
			foundElements = get_objects_by_Name(guiInfo.get_htmlName());
		if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlClassName()))
			foundElements = get_objects_by_className(guiInfo.get_htmlClassName());
		if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlXpath()))
			foundElements = get_objects_by_xpath(guiInfo.get_htmlXpath());
		if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlLinkText()))
			foundElements = get_objects_by_linkText(guiInfo.get_htmlLinkText());
		if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlTagName()))
			foundElements = get_objects_by_tagName(guiInfo.get_htmlTagName());
		if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlPartialLinkText()))
			foundElements = get_objects_by_partialLinkText(guiInfo.get_htmlPartialLinkText());
		
		if (foundElements == null)
			foundElements = driver.findElements(By.tagName("*"));
		return foundElements;
	}
	static private List<WebElement> get_elements_by_gui_info (guiinfo guiInfo, WebElement parentObject) {
		String parent_tagName = parentObject.getTagName().toLowerCase();
		List<WebElement> foundElements = null;
		if (parent_tagName.equals("iframe") || parent_tagName.equals("frame")) {
			String ref_object_xpath = "/html/" + (String)((JavascriptExecutor)driver).executeScript(get_xpath_jscript, parentObject);
			driver.switchTo().frame(parentObject);
			find_object_based_frame_xpath = find_object_based_frame_xpath + ref_object_xpath;
			if (util.check_string_letters_only(guiInfo.get_htmlID()))
				foundElements = get_objects_by_ID(guiInfo.get_htmlID());
			if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlName()))
				foundElements = get_objects_by_Name(guiInfo.get_htmlName());
			if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlClassName()))
				foundElements = get_objects_by_className(guiInfo.get_htmlClassName());
			if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlXpath()))
				foundElements = get_objects_by_xpath(guiInfo.get_htmlXpath());
			if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlLinkText()))
				foundElements = get_objects_by_linkText(guiInfo.get_htmlLinkText());
			if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlTagName()))
				foundElements = get_objects_by_tagName(guiInfo.get_htmlTagName());
			if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlPartialLinkText()))
				foundElements = get_objects_by_partialLinkText(guiInfo.get_htmlPartialLinkText());
			
			if (foundElements == null)
				foundElements = driver.findElements(By.tagName("*"));
			
			// driver.switchTo().defaultContent();
		}
		else {
			if (util.check_string_letters_only(guiInfo.get_htmlID()))
				foundElements = get_objects_by_ID(guiInfo.get_htmlID(), parentObject);
			if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlName()))
				foundElements = get_objects_by_Name(guiInfo.get_htmlName(), parentObject);
			if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlClassName()))
				foundElements = get_objects_by_className(guiInfo.get_htmlClassName(), parentObject);
			if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlXpath()))
				foundElements = get_objects_by_xpath(guiInfo.get_htmlXpath(), parentObject);
			if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlLinkText()))
				foundElements = get_objects_by_linkText(guiInfo.get_htmlLinkText(), parentObject);
			if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlTagName()))
				foundElements = get_objects_by_tagName(guiInfo.get_htmlTagName(), parentObject);
			if ((foundElements == null || foundElements.size() == 0) && util.check_string_letters_only(guiInfo.get_htmlPartialLinkText()))
				foundElements = get_objects_by_partialLinkText(guiInfo.get_htmlPartialLinkText(), parentObject);
			
			if (foundElements == null)
				foundElements = parentObject.findElements(By.tagName("*"));
		}
		return foundElements;
	}
	
	static private boolean get_element_by_gui_info_check_properties (WebElement checkElement, guiinfo guiInfo) {
		if (checkElement != null) {
			boolean passChecked = true;
			boolean hasProerpties = false;
			if (passChecked && guiInfo.get_htmlID() != null && guiInfo.get_htmlID().length() > 0) {
				if (!find_object_check_ID(checkElement, guiInfo.get_htmlID()))
					passChecked = false;
				hasProerpties = true;
			}
			
			if (passChecked && guiInfo.get_htmlName() != null && guiInfo.get_htmlName().length() > 0) {
				if (!find_object_check_Name(checkElement, guiInfo.get_htmlName()))
					passChecked = false;
				hasProerpties = true;
			}
			
			if (passChecked && guiInfo.get_htmlTagName() != null && guiInfo.get_htmlTagName().length() > 0) {
				if (!find_object_check_tagName(checkElement, guiInfo.get_htmlTagName()))
					passChecked = false;
				hasProerpties = true;
			}
			
			if (passChecked && guiInfo.get_htmlClassName() != null && guiInfo.get_htmlClassName().length() > 0) {
				if (!find_object_check_className(checkElement, guiInfo.get_htmlClassName()))
					passChecked = false;
				hasProerpties = true;
			}
			
			if (passChecked && guiInfo.get_htmlXpath() != null && guiInfo.get_htmlXpath().length() > 0) {
				if (!find_object_check_xpath(checkElement, guiInfo.get_htmlXpath()))
					passChecked = false;
				hasProerpties = true;
			}
			
			if (passChecked && guiInfo.get_htmlLinkText() != null && guiInfo.get_htmlLinkText().length() > 0) {
				if (!find_object_check_linkText(checkElement, guiInfo.get_htmlLinkText()))
					passChecked = false;
				hasProerpties = true;
			}
					
			if (passChecked && guiInfo.get_htmlPartialLinkText() != null && guiInfo.get_htmlPartialLinkText().length() > 0) {
				if (!find_object_check_linkText(checkElement, guiInfo.get_htmlPartialLinkText()))
					passChecked = false;
				hasProerpties = true;
			}
			
			if (passChecked && guiInfo.get_htmlText() != null && guiInfo.get_htmlText().length() > 0) {
				if (!find_object_check_text(checkElement, guiInfo.get_htmlText()))
					passChecked = false;
				hasProerpties = true;
			}
			
			if (passChecked && guiInfo.get_htmlMinWidth() >= 0) {
				if (!find_object_check_minWidth(checkElement, guiInfo.get_htmlMinWidth()))
					passChecked = false;
				hasProerpties = true;
			}
			
			if (passChecked && guiInfo.get_htmlMaxWidth() >= 0) {
				if (!find_object_check_maxWidth(checkElement, guiInfo.get_htmlMaxWidth()))
					passChecked = false;
				hasProerpties = true;
			}
			
			if (passChecked && guiInfo.get_htmlMinHeight() >= 0) {
				if (!find_object_check_minHeight(checkElement, guiInfo.get_htmlMinHeight()))
					passChecked = false;
				hasProerpties = true;
			}
			
			if (passChecked && guiInfo.get_htmlMaxHeight() >= 0) {
				if (!find_object_check_maxHeight(checkElement, guiInfo.get_htmlMaxHeight()))
					passChecked = false;
				hasProerpties = true;
			}
			
			if (passChecked && guiInfo.get_htmlPagePosition() != null && guiInfo.get_htmlPagePosition().length() > 0) {
				if (!find_object_check_PagePosition(checkElement, guiInfo.get_htmlPagePosition()))
					passChecked = false;
				hasProerpties = true;
			}
			
			
			for (property prop : guiInfo.get_properties()) {
				hasProerpties = true;
				if (!find_object_check_property(checkElement, prop) || !passChecked) {
					passChecked = false;
					break;
				}
			}
			return hasProerpties && passChecked;
		}
		return false;
	}
	
	static private ArrayList<WebElement> WebElement_get_descendants(WebElement parentElement, guielement gelement, int maxCount) {
		
		List<WebElement> All = parentElement.findElements(By.cssSelector("*"));

        // Get index before checking
        int[] Indexes = new int[gelement.get_guiinfos().size()];
        int[] Checked_Indexes = new int[gelement.get_guiinfos().size()];
        for (int i = 0; i < gelement.get_guiinfos().size(); i++)
        {
            Indexes[i] = gelement.get_guiinfos().get(i).get_htmlIndex();
            Checked_Indexes[i] = 0;
        }
        int matched_count = 0;
        ArrayList<WebElement> match_elements = new ArrayList<WebElement>();
        for (WebElement dNode : All)
        {
            for (int i = 0; i < gelement.get_guiinfos().size(); i++)
            {
                guiinfo scan_info = gelement.get_guiinfos().get(i);
                if (get_element_by_gui_info_check_properties(dNode, scan_info))
                    // Check index before return
                    if (Checked_Indexes[i] == Indexes[i])
                    {
                    	if (maxCount <= 0 || matched_count < maxCount)
                    		match_elements.add(dNode);
                    	else
                    		break;
                    	matched_count ++;
                    }
                    else
                        Checked_Indexes[i] = Checked_Indexes[i] + 1;
            }
        }
        if (match_elements != null && match_elements.size() > 0)
            return match_elements;
        return null;
    }
	
	/******** QUICK FIND OBJECTS ********/
	static private WebElement get_object_by_ID (String objID) {
		WebElement foundElement = null;
		try {
			foundElement = driver.findElement(By.id(objID));
		} catch (Exception e) {e.printStackTrace();}
		if (foundElement == null)
		{
			List<WebElement> foundElements = null;
			try {
				foundElements = driver.findElements(By.tagName("*"));
			} catch (Exception e) {e.printStackTrace();}
			/*
			if (foundElements != null)
			for (int i = 0 ; i < foundElements.size() ; i ++)
				if (find_object_check_ID(foundElements.get(i), objID))
					return foundElements.get(i);*/
		}
		return foundElement;
	}
	static private WebElement get_object_by_Name (String objName) {
		WebElement foundElement = null;
		try {
			foundElement = driver.findElement(By.name(objName));
		} catch (Exception e) {
			// e.printStackTrace();
		}
		if (foundElement == null)
		{
			List<WebElement> foundElements = null;
			try {
				foundElements = driver.findElements(By.tagName("*"));
			} catch (Exception e) {e.printStackTrace();}
			/*
			if (foundElements != null)
			for (int i = 0 ; i < foundElements.size() ; i ++)
				if (find_object_check_Name(foundElements.get(i), objName))
					return foundElements.get(i);*/
		}
		return foundElement;
	}
	static private WebElement get_object_by_tagName (String tagName) {
		WebElement foundElement = null;
		for (int i = 0 ; i < 10 ; i ++)
			try {
				foundElement = driver.findElement(By.tagName(tagName));
				break;
			} catch (Exception e) {e.printStackTrace(); try { Thread.sleep(100);}catch(Exception se){}}
		if (foundElement == null)
		{
			List<WebElement> foundElements = null;
			try {
				foundElements = driver.findElements(By.tagName("*"));
			} catch (Exception e) {e.printStackTrace();}
			/*
			if (foundElements != null)
			for (int i = 0 ; i < foundElements.size() ; i ++)
				if (find_object_check_tagName(foundElements.get(i), tagName))
					return foundElements.get(i);*/
		}
		return foundElement;
	}
	static private WebElement get_object_by_className (String className) {
		WebElement foundElement = null;
		try {
			foundElement = driver.findElement(By.className(className));
		} catch (Exception e) {e.printStackTrace();}
		if (foundElement == null)
		{
			List<WebElement> foundElements = null;
			try {
				foundElements = driver.findElements(By.tagName("*"));
			} catch (Exception e) {e.printStackTrace();}
			/*
			if (foundElements != null)
			for (int i = 0 ; i < foundElements.size() ; i ++)
				if (find_object_check_className (foundElements.get(i), className))
					return foundElements.get(i);*/
		}
		return foundElement;
	}
	static private WebElement get_object_by_xpath (String xpath) {
		WebElement foundElement = null;
		try {
			foundElement = driver.findElement(By.xpath(xpath));
		} catch (Exception e) {e.printStackTrace();}
		if (foundElement == null)
		{
			List<WebElement> foundElements = null;
			try {
				foundElements = driver.findElements(By.tagName("*"));
			} catch (Exception e) {e.printStackTrace();}
			/*
			if (foundElements != null)
			for (int i = 0 ; i < foundElements.size() ; i ++)
				if (find_object_check_xpath (foundElements.get(i), xpath))
					return foundElements.get(i);*/
		}
		return foundElement;
	}
	static private WebElement get_object_by_linkText (String linkText) {
		WebElement foundElement = null;
		try {
			foundElement = driver.findElement(By.linkText(linkText));
		} catch (Exception e) {e.printStackTrace();}
		if (foundElement == null)
		{
			List<WebElement> foundElements = null;
			try {
				foundElements = driver.findElements(By.tagName("*"));
			} catch (Exception e) {e.printStackTrace();}
			/*
			if (foundElements != null)
			for (int i = 0 ; i < foundElements.size() ; i ++)
				if (find_object_check_linkText (foundElements.get(i), linkText))
					return foundElements.get(i);*/
		}
		return foundElement;
	}
	static private WebElement get_object_by_partialLinkText (String partialLinkText) {
		WebElement foundElement = null;
		try {
			foundElement = driver.findElement(By.partialLinkText(partialLinkText));
		} catch (Exception e) {e.printStackTrace();}
		if (foundElement == null)
		{
			List<WebElement> foundElements = null;
			try {
				foundElements = driver.findElements(By.tagName("*"));
			} catch (Exception e) {e.printStackTrace();}
			/*
			if (foundElements != null)
			for (int i = 0 ; i < foundElements.size() ; i ++)
				if (find_object_check_linkText (foundElements.get(i), partialLinkText))
					return foundElements.get(i);*/
		}
		return foundElement;
	}
	static private WebElement get_object_by_cssSelector (String cssSelector) {
		WebElement foundElement = null;
		try {
			foundElement = driver.findElement(By.cssSelector(cssSelector));
		} catch (Exception e) {e.printStackTrace();}
		/*if (foundElement == null) {
			List<WebElement> foundElements = driver.findElements(By.tagName("*"));
			for (int i = 0 ; i < foundElements.size() ; i ++)
				if (find_object_check_cssSelector (foundElements.get(i), cssSelector))
					return foundElements.get(i);
		}/**/
		return foundElement;
	}
	
	/******** CHECK OBJECT PROPERTIES ********/
	static private boolean find_object_check_ID (WebElement element, String objID) {
		try {
			String actualID = element.getAttribute("id");
			if (actualID != null && actualID.length() > 0){
				actualID = actualID.trim();
				return util.reg_compare(actualID, objID);
			}
		}
		catch (UnreachableBrowserException ue) {
			System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
			try { Thread.sleep(500);}catch (Exception se) {}
			un_reach_browser_exception = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	static private boolean find_object_check_Name (WebElement element, String objName) {
		try {
			String actualName = element.getAttribute("name");
			if (actualName != null && actualName.length() > 0){
				actualName = actualName.trim();
				return util.reg_compare(actualName, objName);
			}
		}
		catch (UnreachableBrowserException ue) {
			System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
			try { Thread.sleep(500);}catch (Exception se) {}
			un_reach_browser_exception = true;
		}
		catch (Exception e) {
		}
		return false;
	}
	static private boolean find_object_check_tagName (WebElement element, String tagName) {
		try {
			String actualTagName = element.getTagName();
			if (actualTagName != null && actualTagName.length() > 0){
				actualTagName = actualTagName.trim();
				return util.reg_compare(actualTagName.toLowerCase(), tagName.toLowerCase());
			}
		}
		catch (UnreachableBrowserException ue) {
			System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
			try { Thread.sleep(500);}catch (Exception se) {}
			un_reach_browser_exception = true;
		}
		catch (Exception e) {
		}
		return false;
	}
	static private boolean find_object_check_className (WebElement element, String className) {
		try {
			String actualClassName = element.getAttribute("class");
			if (actualClassName != null && actualClassName.length() > 0){
				actualClassName = actualClassName.trim();
				return util.reg_compare(actualClassName, className);
			}
		}
		catch (UnreachableBrowserException ue) {
			System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
			try { Thread.sleep(500);}catch (Exception se) {}
			un_reach_browser_exception = true;
		}
		catch (Exception e) {
		}
		return false;
	}
	static private boolean find_object_check_xpath (WebElement element, String xpath) {
		try {
			String actualXpath = element.getAttribute("xpath");
			if (actualXpath != null && actualXpath.length() > 0){
				actualXpath = actualXpath.trim();
				return util.reg_compare(actualXpath, xpath);
			}
		}
		catch (UnreachableBrowserException ue) {
			System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
			try { Thread.sleep(500);}catch (Exception se) {}
			un_reach_browser_exception = true;
		}
		catch (Exception e) {
		}
		return false;
	}
	static private boolean find_object_check_linkText (WebElement element, String linkText) {
		try {
			String actualHref = element.getAttribute("text");
			if (actualHref != null && actualHref.length() > 0){
				actualHref = actualHref.trim();
				return util.reg_compare(actualHref, linkText);
			}
		}
		catch (UnreachableBrowserException ue) {
			System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
			try { Thread.sleep(500);}catch (Exception se) {}
			un_reach_browser_exception = true;
		}
		catch (Exception e) {
		}
		return false;
	}
	static private boolean find_object_check_text (WebElement element, String text) {
		try {
			String actualValue = get_WebElement_text(element);
			if (actualValue != null &&
					actualValue.length() > 0)
				if (util.reg_compare(actualValue, text))
					return true;
		}
		catch (UnreachableBrowserException ue) {
			System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
			try { Thread.sleep(500);}catch (Exception se) {}
			un_reach_browser_exception = true;
		}
		catch (Exception e) {
		}
		return false;
	}
	static private boolean find_object_check_minWidth(WebElement element, int minWidth) {
		try {
			// Determine size
			int width = element.getSize().width;
			// System.out.println("Width = " + width);
			if (width >= minWidth)
				return true;
		}
		catch (UnreachableBrowserException ue) {
			System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
			try { Thread.sleep(500);}catch (Exception se) {}
			un_reach_browser_exception = true;
		}
		catch (Exception e) {
		}
		return false;
	}
	static private boolean find_object_check_maxWidth(WebElement element, int maxWidth) {
		try {
			// Determine size
			int width = element.getSize().width;
			// System.out.println("Width = " + width);
			if (width <= maxWidth)
				return true;
		}
		catch (UnreachableBrowserException ue) {
			System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
			try { Thread.sleep(500);}catch (Exception se) {}
			un_reach_browser_exception = true;
		}
		catch (Exception e) {
		}
		return false;
	}
	static private boolean find_object_check_minHeight(WebElement element, int minHeight) {
		try {
			// Determine size
			int height = element.getSize().height;
			// System.out.println("Height = " + height);
			if (height >= minHeight)
				return true;
		}
		catch (UnreachableBrowserException ue) {
			System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
			try { Thread.sleep(500);}catch (Exception se) {}
			un_reach_browser_exception = true;
		}
		catch (Exception e) {
		}
		return false;
	}
	static private boolean find_object_check_maxHeight(WebElement element, int maxHeight) {
		try {
			// Determine size
			int height = element.getSize().height;
			// System.out.println("Height = " + height);
			if (height <= maxHeight)
				return true;
		}
		catch (UnreachableBrowserException ue) {
			System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
			try { Thread.sleep(500);}catch (Exception se) {}
			un_reach_browser_exception = true;
		}
		catch (Exception e) {
		}
		return false;
	}
	static private boolean find_object_check_PagePosition (WebElement element, String pagePosition) {
		try {

			int obj_X = element.getLocation().getX();
			int obj_Y = element.getLocation().getY();
			int obj_W = element.getSize().getWidth();
			int obj_H = element.getSize().getHeight();
			if (obj_W <= 0 || obj_H <= 0)
				return false;
			
			// Get the root object
			WebElement parent = element.findElement(By.xpath(".."));
			while (parent != null) {
				WebElement tempParent = null;
				try {
					tempParent = parent.findElement(By.xpath(".."));
				} catch (Exception e) {}
				if (tempParent == null || tempParent.getTagName().toLowerCase().equals("html"))
					break;
				else
					parent = tempParent;
			}
			
			if (parent != null) {
				int ref_X = parent.getLocation().getX();
				int ref_Y = parent.getLocation().getY();
				int ref_W = parent.getSize().getWidth();
				int ref_H = parent.getSize().getHeight();
				int ref_center_X = parent.getLocation().getX() + parent.getSize().getWidth()/2;
				int ref_center_Y = parent.getLocation().getY() + parent.getSize().getHeight()/2;
				pagePosition = pagePosition.toLowerCase();
				
				if (pagePosition.equals("absolute.left")) {
					if (obj_X + obj_W < ref_center_X)
						return true;
					else
						return false;
				}
				else if (pagePosition.equals("absolute.right")) {
					if (obj_X > ref_center_X)
						return true;
					else
						return false;
				}
				else if (pagePosition.equals("absolute.top")) {
					if (obj_Y + obj_H < ref_center_Y)
						return true;
					else
						return false;
				}
				else if (pagePosition.equals("absolute.bottom")) {
					if (obj_Y > ref_center_Y)
						return true;
					else
						return false;
				}
				else if (pagePosition.startsWith("center")) {
					int odd = 0;
					if (pagePosition.length() > 6) {
						String str_odd = pagePosition.substring(7);
						try {
							odd = Integer.parseInt(str_odd);
						} catch (Exception eee) {}
					}
					
					if (obj_X < (ref_center_X + odd) &&
						obj_X + obj_W > (ref_center_X - odd) &&
						obj_Y < (ref_center_Y + odd) &&
						obj_Y + obj_H > (ref_center_Y - odd))
						return true;
					else
						return false;
				}
                else if (pagePosition.startsWith("middle"))
                {
					int odd = 0;
					if (pagePosition.length() > 7) {
						String str_odd = pagePosition.substring(7);
						try {
							odd = Integer.parseInt(str_odd);
						} catch (Exception eee) {}
					}

                    if (obj_Y >= (ref_Y + odd) &&
                    	obj_H <= (ref_H - odd))
                        return true;
                    else
                        return false;
                }
			}
		}
		catch (UnreachableBrowserException ue) {
			System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
			try { Thread.sleep(500);}catch (Exception se) {}
			un_reach_browser_exception = true;
		}
		catch (Exception e) {
		}
		return false;
	}
	static private boolean find_object_check_property (WebElement element, property property) {
		if (property.check_key("runtime") || property.check_key("related Object") || property.check_key("related Type"))
			return true;
		
		try {
			String check_key = property.get_key().toLowerCase();
			if (check_key.toLowerCase().equals("innerhtml"))
				check_key = "innerHTML";
			else if (check_key.toLowerCase().equals("outerhtml"))
				check_key = "outerHTML";
			if (check_key.toLowerCase().equals("text")) {
				String actualValue = get_WebElement_text(element);

				if (actualValue != null &&
						actualValue.length() > 0 &&
					util.reg_compare(actualValue, property.get_value()))
					return true;
			}
			String actualValue = element.getAttribute(check_key);
			if (actualValue != null && actualValue.length() > 0){
				actualValue = actualValue.trim();
				return util.reg_compare(actualValue, property.get_value());
			}
		}
		catch (UnreachableBrowserException ue) {
			System.out.println ("\n\n " + (new Date()).toString() + "*** SELENIUM CONNECTION PROBLEM *** \n");
			try { Thread.sleep(500);}catch (Exception se) {}
			un_reach_browser_exception = true;
		}
		catch (Exception e) {
		}
		return false;
	}

	static private boolean find_object_check_ref_object (WebElement ref_object, ref_object_type ref_type, WebElement target_element) {
		try
		{
			if (ref_type == ref_object_type.CHILD || ref_type == ref_object_type.PARENT)
				return true;

			int ref_location_X = ref_object.getLocation().getX();
			int ref_location_Y = ref_object.getLocation().getY();
			int ref_location_W = ref_object.getSize().getWidth();
			int ref_location_H = ref_object.getSize().getHeight();
			
			int target_location_X = target_element.getLocation().getX();
			int target_location_Y = target_element.getLocation().getY();
			int target_location_W = target_element.getSize().getWidth();
			int target_location_H = target_element.getSize().getHeight();
			
			if (ref_type == ref_object_type.UP && 
					(ref_location_X + ref_location_W) > target_location_X &&  // ref_right > iLeft
					(target_location_X + target_location_W) > ref_location_X && // iRight > ref_left
					target_location_Y < ref_location_Y) // at row or Y position, target is upper
				return true;
			else if (ref_type == ref_object_type.DOWN && 
					(ref_location_X + ref_location_W) > target_location_X &&  // ref_right > iLeft
					(target_location_X + target_location_W) > ref_location_X && // iRight > ref_left
					target_location_Y > ref_location_Y) // iTop > ref_top
				return true;
			else if (ref_type == ref_object_type.LEFT && 
					target_location_X < ref_location_X && // iLeft < ref_left
					target_location_Y < (ref_location_Y + ref_location_H) && // iTop < ref_bottom
					ref_location_Y < (target_location_Y + target_location_H)) // iBottom > ref_top
				return true;
			else if (ref_type == ref_object_type.RIGHT &&
					target_location_X > ref_location_X && // iLeft > ref_left
					target_location_Y < (ref_location_Y + ref_location_H) && // iTop < ref_bottom
					ref_location_Y < (target_location_Y + target_location_H)) // iBottom > ref_top
				return true;
		}
		catch (Exception e) { e.printStackTrace(); }
		return false;
	}
	static private String get_WebElement_text(WebElement element) {
		String actualValue = null;

		// Get text to actualValue
		String tagName = element.getTagName();
		if (tagName.toLowerCase().equals("select")) {
			Select objSel = null;
			try {
				objSel = new Select(element);
			} catch (Exception e) {
			}
			
			if (objSel != null) {
				List<WebElement> options = objSel.getOptions();
				for (int i = 0 ; i < options.size() ; i ++) {
					WebElement objSelelele = options.get(i);
					if (objSelelele.isSelected()) {
						actualValue = objSelelele.getText();
						break;
					}
				}
				if (actualValue == null || actualValue.length() <= 0)
					actualValue = element.getText();
			}
		}
		else {
			// String text = element.getText();
			actualValue = element.getAttribute("text");
			if (actualValue == null || actualValue.length() == 0)
				actualValue = element.getText();
			if ((actualValue == null || actualValue.length() == 0) && (element.getTagName().toLowerCase().equals("input")))
				actualValue = element.getAttribute("value");
		}
		if (actualValue != null)
			actualValue = actualValue.trim();
		return actualValue;
	}

	/******** GET OBJECTS FOR SEARCHING ********/

	static private List<WebElement> get_objects_by_ID (String objID) {
			List<WebElement> foundElements = null;
			try {
				foundElements = driver.findElements(By.id(objID));
			} catch (Exception e) {e.printStackTrace();}
			return foundElements;
	}
	static private List<WebElement> get_objects_by_Name (String objName) {
		List<WebElement> foundElements = null;
		try {
			foundElements = driver.findElements(By.name(objName));
		} catch (Exception e) {e.printStackTrace();}
		return foundElements;
	}
	static private List<WebElement> get_objects_by_tagName (String tagName) {
		List<WebElement> foundElements = null;
		try {
			foundElements = driver.findElements(By.tagName(tagName));
		} catch (Exception e) {e.printStackTrace();}
		return foundElements;
	}
	static private List<WebElement> get_objects_by_className (String className) {
		List<WebElement> foundElements = null;
		try {
			foundElements = driver.findElements(By.className(className));
		} catch (Exception e) {e.printStackTrace();}
		return foundElements;
	}
	static private List<WebElement> get_objects_by_xpath (String xpath) {
		List<WebElement> foundElements = null;
		try {
			foundElements = driver.findElements(By.xpath(xpath));
		} catch (Exception e) {e.printStackTrace();}
		return foundElements;
	}
	static private List<WebElement> get_objects_by_linkText (String linkText) {
		List<WebElement> foundElements = null;
		try {
			foundElements = driver.findElements(By.linkText(linkText));
		} catch (Exception e) {e.printStackTrace();}
		return foundElements;
	}
	static private List<WebElement> get_objects_by_partialLinkText (String partialLinkText) {
		List<WebElement> foundElements = null;
		try {
			foundElements = driver.findElements(By.partialLinkText(partialLinkText));
		} catch (Exception e) {e.printStackTrace();}
		return foundElements;
	}


	static private List<WebElement> get_objects_by_ID (String objID, WebElement parent_object) {
			List<WebElement> foundElements = null;
			try {
				foundElements = parent_object.findElements(By.id(objID));
			} catch (Exception e) {e.printStackTrace();}
			return foundElements;
	}
	static private List<WebElement> get_objects_by_Name (String objName, WebElement parent_object) {
		List<WebElement> foundElements = null;
		try {
			foundElements = parent_object.findElements(By.name(objName));
		} catch (Exception e) {e.printStackTrace();}
		return foundElements;
	}
	static private List<WebElement> get_objects_by_tagName (String tagName, WebElement parent_object) {
		List<WebElement> foundElements = null;
		try {
			foundElements = parent_object.findElements(By.tagName(tagName));
		} catch (Exception e) {e.printStackTrace();}
		return foundElements;
	}
	static private List<WebElement> get_objects_by_className (String className, WebElement parent_object) {
		List<WebElement> foundElements = null;
		try {
			foundElements = parent_object.findElements(By.className(className));
		} catch (Exception e) {e.printStackTrace();}
		return foundElements;
	}
	static private List<WebElement> get_objects_by_xpath (String xpath, WebElement parent_object) {
		List<WebElement> foundElements = null;
		try {
			foundElements = parent_object.findElements(By.xpath(xpath));
		} catch (Exception e) {e.printStackTrace();}
		return foundElements;
	}
	static private List<WebElement> get_objects_by_linkText (String linkText, WebElement parent_object) {
		List<WebElement> foundElements = null;
		try {
			foundElements = parent_object.findElements(By.linkText(linkText));
		} catch (Exception e) {e.printStackTrace();}
		return foundElements;
	}
	static private List<WebElement> get_objects_by_partialLinkText (String partialLinkText, WebElement parent_object) {
		List<WebElement> foundElements = null;
		try {
			foundElements = parent_object.findElements(By.partialLinkText(partialLinkText));
		} catch (Exception e) {e.printStackTrace();}
		return foundElements;
	}
	
	//Config Proxy Server
	static public Proxy conf_proxy_server(Properties conf) {
		
		String server =	conf.getProperty("proxy.http");
		String port = conf.getProperty("proxy.port");
		String username = conf.getProperty("proxy.username");
		String password = conf.getProperty("proxy.password");
		
		Proxy proxy = new org.openqa.selenium.Proxy();
		proxy.setHttpProxy(server + ":" + port);
		proxy.setSslProxy(server + ":" + port);
		proxy.setFtpProxy(server + ":" + port);
        proxy.setSocksUsername(username);
        proxy.setSocksPassword(password);
        
		return proxy;
	} 
	
	// Additional functions
	//static private void mouseOver(WebElement element) {
		//Locatable hoverItem = (Locatable)element;
		//int y = hoverItem.getCoordinates().getLocationOnScreen().getY();
		//((JavascriptExecutor)driver).executeScript("window.scrollBy(0,"+y+");");
		
		/*
		// Calculate position of object
		Point p1 = ((Locatable)element).getLocationOnScreenOnceScrolledIntoView();
		
		// Calculation position of browser
		Point p2 = driver.manage().window().getPosition();
		
		// Click point
		Point click_p = new Point(p1.x + p2.x + 35, p1.y + p2.y + 10);
		
		// Call Robot to move mouse
		mouse_move (click_p.x, click_p.y);
		*/
	//}
	static public void mouse_move(int X, int Y)
	{
		try
		{
			Robot rb = new Robot();
			rb.mouseMove(X, Y);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	static public void mouse_lclick(int X, int Y)
	{
		try
		{
			Robot rb = new Robot();
			rb.mouseMove(X, Y);
			rb.mousePress(InputEvent.BUTTON1_MASK);
			rb.mouseRelease(InputEvent.BUTTON1_MASK);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	// static final String get_xpath_jscript = "function getPathTo(node) {  var stack = [];  while(node.parentNode !== null) {	stack.unshift(node.tagName);	node = node.parentNode;  }  return stack.join('/');} return getPathTo(arguments[0]);";
	static final String get_xpath_jscript = "gPt=function(c){if(c===document.body){return c.tagName}var a=0;var e=c.parentNode.children;for(var b=0;b<e.length;b++){var d=e[b];if(d===c){return gPt(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'}if(d.nodeType===1&&d.tagName===c.tagName){a++}}};return gPt(arguments[0]).toLowerCase();";

}
