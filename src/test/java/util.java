package test.java;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;


public class util {

	public static String REPORT_LIB = "/Users/uzie/Documents/PMRepos/reports/";
	public static String SCREENSHOTS_LIB = "/Users/uzie/Documents/PMRepos/reports/";


	public static void closeTest(RemoteWebDriver driver)
	{
		System.out.println("CloseTest");
		driver.quit();
	}

	



	public static RemoteWebDriver getRWD(String deviceId,String browser,String platform)   {

		RemoteWebDriver  webdriver = null;

		if (deviceId.equals("local"))
		{
			DesiredCapabilities capabilities =  DesiredCapabilities.firefox();
			capabilities.setCapability("platformName",  platform);


			try {
				webdriver = new RemoteWebDriver(new URL("http://54.68.7.220:4444//wd/hub"), DesiredCapabilities.firefox());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block

				capabilities.setCapability("platformName",  platform);

				e.printStackTrace();
			}

		}
		else
		{
			DesiredCapabilities capabilities = new DesiredCapabilities(browser, "", Platform.ANY);

			capabilities.setCapability("user", "uzie@perfectomobile.com");
			capabilities.setCapability("password", "@Perfecto1");
			capabilities.setCapability("deviceName",  deviceId);
			capabilities.setCapability("platformName",  platform);
			

			//capabilities.setCapability("takesScreenShot", false);
			//capabilities.setCapability("automationName", "PerfectoMobile");
			try {
				webdriver = new AndroidDriver(new URL("https://demo.perfectomobile.com/nexperience/perfectomobile/wd/hub") , capabilities);
			} catch (Exception e) {
				String ErrToRep = e.getMessage().substring(0,e.getMessage().indexOf("Command duration")-1);
				System.out.println(ErrToRep);
				return (null);


			}
		}
		return webdriver;

	}

	//public cloud
	public static AppiumDriver getAppiumDriver(String deviceId,String app,String platform,String persona,String appLocation,HTMLReporter rep)   {
		return getAppiumDriver(deviceId,app,platform,"https://mobilecloud.perfectomobile.com","uzie@perfectomobile.com","@Perfecto1",persona,appLocation,rep);
	}
	public static AppiumDriver getAppiumDriver(String deviceId,String app,String platform,String Cloud,String user,String password,String persona,String appLocation,HTMLReporter rep)   {

		AppiumDriver webdriver= null;

		DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);


		// get local Appium webDriver
		if (platform=="local")
		{
			capabilities.setCapability("deviceName",  deviceId);
			capabilities.setCapability("app-activity","com.thehartford.hignavigator.MainActivity");
			capabilities.setCapability("appPackage","com.thehartford.hignavigator");
			try {
				webdriver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub/"), capabilities);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return webdriver;

		}

		// PerfectoDriver
		if (platform.equalsIgnoreCase("ios"))
		{
			capabilities.setCapability("bundleId", app);
			capabilities.setCapability("automationName", "appium");


		}else
		{
			capabilities.setCapability("app-activity",app);
			capabilities.setCapability("appPackage",app);

		}

		if (persona != null)
		{
			capabilities.setCapability("windTunnelPersona", persona);
		}
		if (appLocation!= null)
		{
			capabilities.setCapability("app", appLocation);
		}
		capabilities.setCapability("user",  user);
		capabilities.setCapability("password", password);
		capabilities.setCapability("deviceName",  deviceId);
		//capabilities.setCapability("platformName",  platform);


		//capabilities.setCapability("takesScreenShot", false);
		//capabilities.setCapability("automationName", "PerfectoMobile");
		try {
			webdriver = new AndroidDriver(new URL(Cloud+"/nexperience/perfectomobile/wd/hub") , capabilities);
		} catch (Exception e) {
			String ErrToRep = e.getMessage().substring(0,e.getMessage().indexOf("Command duration")-1);
			System.out.println(ErrToRep);
			if (rep !=null)
			{
				rep.addline(deviceId, ErrToRep, null);
			}
			return (null);


		}

		return webdriver;

	}

	public static String getScreenShot(RemoteWebDriver driver,String name,String deviceID )
	{
		String screenShotName = SCREENSHOTS_LIB+name+"_"+deviceID+".png";
		driver   = (RemoteWebDriver) new Augmenter().augment( driver );
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

		try {
			FileUtils.copyFile(scrFile, new File(screenShotName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return screenShotName;
	}

	public static void startApp(String appName,RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", appName);
		d.executeScript("mobile:application:open", params);
	}


	public static void stoptApp(String appName,RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", appName);
		d.executeScript("mobile:application:close", params);
	}

	public static void setLocation(String address,RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("address", address);
		d.executeScript("mobile:location:set", params);
	}
	 
	public static void setLocationCoordinates(String latlong,RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("coordinates", latlong);
		d.executeScript("mobile:location:set", params);
	}

	public static void pressKey(String key,RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("keySequence", key);
		d.executeScript("mobile:presskey:", params);
	}
	 

	public static void longtouch(String key,RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("location", key);  // 145,449
		params.put("duration", key);  
		d.executeScript("mobile:touch:tap", params);
	}

	 
	public static void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}
	public static void swipe(String start,String end,RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("start", start);  //50%,50%
		params.put("end", end);  //50%,50%

		d.executeScript("mobile:touch:swipe", params);

	}

	public static void rotateDevice (String stat,WebDriver d )
	{
		// operation - next or reset
		Map<String,String> params = new HashMap<String,String>();
		params.put("operation", stat);
		((RemoteWebDriver) d).executeScript("mobile:handset:rotate", params);
	}



	public static void downloadReport(RemoteWebDriver driver, String type, String fileName) throws IOException {
		try { 
			String command = "mobile:report:download"; 
			Map<String, Object> params = new HashMap<>(); 
			params.put("type", "html"); 
			String report = (String)driver.executeScript(command, params); 
			File reportFile = new File(getReprtName(fileName, true) ); 
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(reportFile)); 
			byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report); 
			output.write(reportBytes); output.close(); 
		} catch (Exception ex) { 
			System.out.println("Got exception " + ex); }
	}

	public static String getReprtName(String repID,boolean withPath) {
		if (withPath)
		{
			return REPORT_LIB+"/rep_"+repID+".html";
		}
		else
		{
			return  "/rep_"+repID+".html";
		}

	}
	public static void pointOfInterest(RemoteWebDriver d ,String poiName, String poiStatus) {
		String command;
		Map<String,Object> params = new HashMap<String,Object>();
		command = "mobile:status:event";
		params.put("description", poiName);
		params.put("status", poiStatus);
		Object result = d.executeScript(command, params);
	}
	public static long timerGet(RemoteWebDriver d,String timerType) {
		String command = "mobile:timer:info";
		Map<String,String> params = new HashMap<String,String>();
		params.put("type", timerType);
		long result = (long)d.executeScript(command, params);
		return result;
	}
	public static void timerReport(RemoteWebDriver d,long timerResult, int threashold, String description, String name) {
		String command;
		Map<String,Object> params = new HashMap<String,Object>();
		command = "mobile:status:timer";
		params.put("result", timerResult);
		params.put("threshold", threashold);
		params.put("description", description);
		//params.put("status", status);
		params.put("name", name);
        Object result = d.executeScript(command, params);

		
	}

		public static void sleep(long millis) {
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
			}
		}
	}
	
