//package com.aims.util;
//
//import java.awt.image.BufferedImage;
//import java.awt.image.Raster;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.net.URL;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Properties;
//import java.util.concurrent.TimeUnit;
//
//import javax.imageio.ImageIO;
//
//import org.apache.log4j.Logger;
//import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.NoSuchElementException;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Action;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.Select;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.SkipException;
//
//import com.aims.Controller;
//import com.aims.model.TestCaseData;
//
//
//public class Functions extends Controller {
//
//	public Functions(TestCaseData testCaseData) {
//		super(testCaseData);
//		// TODO Auto-generated constructor stub
//	}
//
//	public static void skipModule() throws SkipException {
//		throw new SkipException("Skipping this module.");
//	}
//
//	public static void dragTo(WebDriver driver, WebElement element, int pixelsToClick) {
//
//		Actions actions = new Actions(driver);
//		actions.dragAndDropBy(element, 0, pixelsToClick).perform();
//	}
//
//	public static void dragAndFindSavedSearch(WebDriver driver,WebElement element,String searchName,List<WebElement> rows,Logger log) throws InterruptedException{
//
//		Actions actions = new Actions(driver);
//		int i=0;
//		int j=0;
//		for(int m=1;m<rows.size();m++){
//			actions.dragAndDropBy(element, i, j).perform();
//			log.debug("Saved search is" + driver.findElement(By.xpath("//div[@id='savesearch-wrap']/descendant::ul[@class='dk_options_inner']/descendant::li["+m+"]/a")).getAttribute("title").trim());
//			if(driver.findElement(By.xpath("//div[@id='savesearch-wrap']/descendant::ul[@class='dk_options_inner']/descendant::li["+m+"]/a")).getAttribute("title").trim().equals(searchName)){
//				log.debug("Saved search is" + driver.findElement(By.xpath("//div[@id='savesearch-wrap']/descendant::ul[@class='dk_options_inner']/descendant::li["+m+"]/a")).getAttribute("title").trim());
//				driver.findElement(By.xpath("//div[@id='savesearch-wrap']/descendant::ul[@class='dk_options_inner']/descendant::li["+m+"]/a")).click();
//				Thread.sleep(1000);
//				break;
//			}
//			i=j;
//			j+=6;
//		}
//
//	}	
//
//	public static void dragAndDeleteSavedSearch(WebDriver driver,WebElement element,String searchName,List<WebElement> rows,Logger log) throws InterruptedException{
//
//		Actions actions = new Actions(driver);
//		int i=0;
//		int j=0;
//		for(int m=1;m<rows.size();m++){
//			actions.dragAndDropBy(element, i, j).perform();
//			if (driver.findElement(By.xpath("//div[@id='savesearch-wrap']/descendant::ul[@class='dk_options_inner']/descendant::li["+m+"]/a")).getAttribute("title").trim().equals(searchName)){
//				WebElement webElement = driver.findElement(By.xpath("//span[text()='"+searchName+"']/ancestor::li/span[@class='icon-delete delete']"));
//				if(webElement!=null) {
//					webElement.click();
//					Thread.sleep(1000);
//					break;
//				}
//
//			}
//			i=j;
//			j+=6;
//		}	
//
//	}
//
//	public static void dragTillAllRowsLoaded(WebDriver driver, Logger log, String rowsLocator, String dragThumbLocator, int totalRowCount, int numberOfPixelsToDragTheScrollbarDown) {
//
//		int i=0, previousCount=0, currentCount;
//		boolean exit=true;
//
//		Actions actions = new Actions(driver);
//
//		WebElement draggablePartOfScrollbar = driver.findElement(By.xpath(dragThumbLocator));
//
//		previousCount= driver.findElements(By.xpath(rowsLocator)).size();
//		log.debug("Previous Count: "+previousCount);
//
//		while(exit && (i<60)) {
//			// drag downwards
//			++i;
//			//    	   int numberOfPixelsToDragTheScrollbarDown = 100;
//			try{
//				actions.moveToElement(draggablePartOfScrollbar).clickAndHold().moveByOffset(0,numberOfPixelsToDragTheScrollbarDown).release().perform();
//				//	    			   Thread.sleep(1000L);
//
//				currentCount= driver.findElements(By.xpath(rowsLocator)).size();
//				log.debug("Scroll:"+i+" Previous Count: "+previousCount+" Current Count: "+currentCount+"\n");
//
//				if((currentCount>=previousCount) && (currentCount<totalRowCount)) {
//					previousCount=currentCount;
//					continue;
//				}else {
//					exit=false;
//					break;
//				}
//
//			}catch(Throwable t){
//				log.debug("An error has occurred while executing function dragTillAllRowsLoaded");
//			}
//		}
//	}
//
//	public static int pixelVal(String style, String splitByVal) {
//
//		int pixelCount;
//		String arrSplitByColon[] = style.split(splitByVal);
//		String arrSplitByPX[] = arrSplitByColon[1].split("px");
//		pixelCount = Integer.parseInt(arrSplitByPX[0].trim());
//
//		return pixelCount;
//	}
//	public static double pixelValDouble(String style, String splitByVal) {
//
//		double pixelCount;
//		String arrSplitByColon[] = style.split(splitByVal);
//		String arrSplitByPX[] = arrSplitByColon[1].split("px");
//		pixelCount = Double.parseDouble(arrSplitByPX[0].trim());
//
//		return pixelCount;
//	}
//
//	public static int pixelVal(String style) {
//
//		int pixelCount;
//
//		String arrSplitByPX[] = style.split("px");
//		pixelCount = Integer.parseInt(arrSplitByPX[0].trim());
//
//		return pixelCount;
//	}
//
//	public static double pixelValDouble(String style) {
//
//		double pixelCount;
//
//		String arrSplitByPX[] = style.split("px");
//		pixelCount = Double.parseDouble(arrSplitByPX[0].trim());
//
//		return pixelCount;
//	}
//	public static boolean sortAum( List<String> assetValue, int sizeArr, String type) {
//
//		boolean result = true;
//
//		for(int k=0; k<sizeArr-1; k++){
//
//			System.out.println("assetValue :" + assetValue.get(k));
//			String s = assetValue.get(k).toString().trim().toLowerCase();
//			String s2 = assetValue.get(k+1).toString().trim().toLowerCase();
//			double current = Functions.getDoubleAUMval(s);
//			double next = Functions.getDoubleAUMval(s2);
//			if(!(current<=next) && type.equals("asc")){
//				System.out.println("current : "+ current + "  next : "+ next );
//				result=false;
//				break;
//			}
//			else if(!(current>=next) && type.equals("desc")){
//				System.out.println("current : "+ current + "  next : "+ next );
//				result=false;
//				break;
//			}
//			else{
//				System.out.println("current : "+ current + "  next : "+ next );
//				result=true;
//			}
//		}
//
//		return result;
//	}
//
//	public static Double getDoubleAUMval(String aum) {
//
//		if(aum.contains("mn")){
//			aum = aum.replaceAll("[^\\d.]", "");
//			return Double.parseDouble(aum) * 1000000;
//		}
//		if(aum.contains("bn")){
//			aum = aum.replaceAll("[^\\d.]", "");
//			return Double.parseDouble(aum) * 1000000000;
//		}
//		if(aum.contains("tn")){
//			aum = aum.replaceAll("[^\\d.]", "");
//			return Double.parseDouble(aum) * 1000000000000.00;
//		}else
//			return (double) 0;
//
//
//	}
//	public static String replaceAll(String text,String regexExp, String replacementExp) {
//
//		text = text.replaceAll(regexExp,replacementExp);
//		return text;
//	}
//	public static Properties loadConfigFile(String folderName, String fileName) throws IOException {
//		// load the config property file
//		FileInputStream fs = null;
//		Properties prop = new Properties();
//
//		if(folderName.equals("objectRepo")) {
//			folderName = System.getProperty("user.dir") + "/src/com/aims/objectRepo";
//		}else if(folderName.equals("config")) {
//			folderName = System.getProperty("user.dir") + "/config";
//		}
//
//		fs = new FileInputStream(folderName+ "/"+fileName+".properties");
//		prop.load(fs);
//		return prop;
//
//	}
//
//	public static void highlighter(WebDriver driver, WebElement element) {
//		//		JavascriptExecutor js = (JavascriptExecutor) driver;
//		//		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "border: 4px solid red; "); 
//		//		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, ""); 
//	}
//
//	public static void handleExceptionHandlingPopUp(WebDriver driver, Logger log, Properties CONFIG) throws InterruptedException
//	{
//		log.debug("=============================");
//		log.debug("Executing function handleExceptionHandlingPopUp");
//		try{
//			if(driver.findElement(By.xpath("//div[@id='cboxContent']/descendant::div[@class='modal_dialog warning_message']")).isDisplayed()) {
//				driver.findElement(By.xpath("//div[@id='colorbox']/descendant::button[text()='OK']")).click();
//				Thread.sleep(3000);
//			}
//		}catch(Throwable t) {
//		}
//	}
//
//	public static void handleHelpOverlay(WebDriver driver, Logger log, Properties CONFIG) {
//		log.debug("=============================");
//		log.debug("Executing function handleHelpOverlay");
//		try {
//			WebElement checkboxButton = driver.findElement(By.xpath(OR.getProperty("aims.global.HelpCheckbox.button.xpath")));
//			checkboxButton.click();
//			WebElement closeButton = driver.findElement(By.xpath(OR.getProperty("aims.global.HelpClose.button.xpath")));
//			closeButton.click();
//			Thread.sleep(5000);
//		} catch (Throwable t) {
//			// report error
//			log.debug("Help Overlay Not Present");
//		}
//	}
//
//
//	public static int getNumberWithinBrackets(String input){
//		String temp[] = input.split("\\(|\\)");
//		return Integer.parseInt(temp[1].trim());
//
//	}
//
//	public static void handleDataUnavailablePopUp(WebDriver driver, Logger log, Properties CONFIG) throws InterruptedException {
//		log.debug("=============================");
//		log.debug("Executing function handleDataUnavailablePopUp");
//		try{
//			//if((driver.getCurrentUrl().endsWith("myworkspace.html"))||(driver.getCurrentUrl().endsWith("mydropbox.html")||(driver.getCurrentUrl().endsWith("portfolio.html")) || (driver.getCurrentUrl().endsWith("managerfundlanding.html?section=fund")))) {
//			if((driver.getCurrentUrl().endsWith("myworkspace.html"))||(driver.getCurrentUrl().endsWith("mydropbox.html")||(driver.getCurrentUrl().endsWith("portfolio.html")) || (driver.getCurrentUrl().endsWith("managerfundlanding.html#/overview/funds")) || (driver.getCurrentUrl().endsWith("managerfundlanding.html#/overview/managers")))) {
//				driver.findElement(By.xpath(OR.getProperty("aims.global.DataUnavailable.popUp.xpath"))).isDisplayed();
//				Thread.sleep(3000);
//				driver.findElement(By.xpath(OR.getProperty("aims.global.DataUnavailablePopUpClose.button.xpath"))).click();
//			}
//
//		}catch(Throwable t) {
//			log.debug("Data Unavailable pop up was not present to handle.");
//		}
//	}
//
//
//	public static boolean compareActualExpected(WebDriver driver, Logger log, String actualValue, String expectedvalue, Properties CONFIG) {
//		log.debug("=============================");
//		log.debug("Executing function compareActualExpected");
//		try {
//			boolean flag = false;
//			String fetchActualVal[] = actualValue.split("\\.");
//			String fetchExpectedVal[] = expectedvalue.split("\\.");
//
//			int actualValLength = fetchActualVal[0].length();
//			int expectedValLength = fetchExpectedVal[0].length();
//
//			if(expectedValLength > actualValLength) {
//				if(fetchExpectedVal[0].contains(fetchActualVal[0]))
//					flag = true;
//			}
//			else if(expectedValLength < actualValLength) {
//				if(fetchActualVal[0].contains(fetchExpectedVal[0]))
//					flag = true;
//			}
//			else{
//				if(fetchActualVal[0].equals(fetchExpectedVal[0]))
//					flag = true;
//			}
//
//			log.debug("Actual value :" +actualValue);
//			log.debug("Expected value:" +expectedvalue);	
//
//			if(flag)
//				return true;
//			else
//				return false;
//
//		} catch (Throwable t) {
//			// report error
//			log.debug("An error has occurred while executing the function compareActualExpected");
//			return false;
//		}
//	}
//
//	public static void dragAndDropElement(WebDriver driver, Logger log, WebElement dragElement, WebElement dropElement) throws InterruptedException {
//		log.debug("=============================");
//		log.debug("Executing function dragAndDropElement");
//
//		Actions builder = new Actions(driver);
//
//		try {
//			Action dragAndDrop = builder.clickAndHold(dragElement)  
//					.moveToElement(dropElement)  
//					.release(dropElement)  
//					.build();  // Get the action  
//			Thread.sleep(5000);
//			dragAndDrop.perform(); // Execute the Action  
//		} catch (Throwable t) {
//			// report error
//			log.debug("An error has occurred while dragging and dropping element.");
//		}
//	}
//
//	public static boolean handleCustomPlaylistPopUp(WebDriver driver, Logger log, Properties CONFIG)
//	{
//		log.debug("=============================");
//		log.debug("Executing function handleCustomPlaylistPopUp");
//		try {
//			if(driver.findElement(By.xpath(OR.getProperty("aims.global.PlaylistWarningDialogueBox.link.xpath"))).isDisplayed()) {
//				driver.findElement(By.xpath(OR.getProperty("aims.global.PlaylistWarningOk.button.xpath"))).click();
//				System.out.println("Cant be added to lib as it is a part of custom playlist, hence the case will pass");
//				//				driver.findElement(By.xpath(OR.getProperty("aims.global.PlaylistWarningClose.button.xpath"))).click();
//				return true;
//			}
//			else
//				return false;
//		}catch (NoSuchElementException e) {
//			log.debug("The item type which is to be found is not present here");
//			//			log.debug("Failure : " + e.getMessage());
//			return false;
//		}catch (Throwable t){
//			// report error
//			log.debug("An error has occurred while handling custom playlist pop up");
//			//			log.debug("Failure : " + t.getMessage());
//			return false;
//		}
//	}	
//
//	public static void handleTnCPopUp(WebDriver driver, Logger log, Properties OR) {
//		log.debug("=============================");
//		log.debug("Executing function handleTnCPopUp");
//
//		try {
//			driver.manage().timeouts().implicitlyWait(50, TimeUnit.MILLISECONDS);
//			if(driver.findElements(By.xpath((OR.getProperty("aims.global.TnCWarningDialogueBox.popUp.xpath")))).size()>0){
//				WebElement checkboxButton = driver.findElement(By.xpath(OR.getProperty("aims.global.TnCWarningCheckbox.button.xpath")));
//				checkboxButton.click();
//				WebElement continueButton = driver.findElement(By.xpath(OR.getProperty("aims.global.TnCWarningContinue.button.xpath")));
//				continueButton.click();
//				log.debug("TnC handled ");
//				Thread.sleep(5000);
//			}else {
//				log.debug("TnC did not come");
//			}
//		} catch (Throwable t) {
//			log.debug("in catch. No tnc pop up.");
//		}finally{
//			driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
//		}
//	}
//
//	public static boolean verifyElementPresenceOrAbsence(WebDriver driver,Logger log,String xpath,String verifyOverlay) {
//		log.debug("=============================");
//		log.debug("Executing verifyElementPresenceOrAbsence");
//
//		WebElement webElement = null;
//		boolean result = false;
//		try {
//			webElement = driver.findElement(By.xpath(OR.getProperty(xpath)));
//			String check=webElement.getCssValue("display");
//			log.debug("Display: "+check);
//			if(check.contains("none") && verifyOverlay.equalsIgnoreCase("n"))
//				result = true;
//			else if(check.contains("none") && verifyOverlay.equalsIgnoreCase("y"))
//				result = false;
//			else if(check.contains("block") && verifyOverlay.equalsIgnoreCase("y"))
//				result = true;
//			else if(check.contains("block") && verifyOverlay.equalsIgnoreCase("n"))
//				result = false;
//		} catch (Throwable t) {
//			//do nothing
//			if (webElement==null && verifyOverlay.equalsIgnoreCase("n")) 
//				result = true;
//			else if(webElement==null  && verifyOverlay.equalsIgnoreCase("y")) 
//				result = false;
//			else if(webElement!=null  && verifyOverlay.equalsIgnoreCase("y"))
//				result = true;
//			else if(webElement!=null  && verifyOverlay.equalsIgnoreCase("n"))
//				result = false;	
//		}	
//		return result;
//	}
//
//
//	public static void selectDateFromCalender(WebDriver driver, int dayInput, String monthInput, String yearInput, Logger log) throws InterruptedException{
//		//pick year as per input from data sheet
//
//		WebElement yearPicker = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxYearPicker")));
//		yearPicker = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxYearPicker")));
//		yearPicker.click();
//		Thread.sleep(1000);
//		Select year = new Select(yearPicker);
//		year.selectByVisibleText(yearInput);
//		yearPicker = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxYearPicker")));
//		yearPicker.sendKeys(Keys.ENTER);
//		Thread.sleep(2000);
//
//		//pick month as per input from data sheet
//		WebElement monthPicker = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxMonthPicker")));
//		monthPicker.click();
//		Thread.sleep(1000);
//		Select month = new Select(monthPicker);
//		month.selectByVisibleText(monthInput);
//		monthPicker = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxMonthPicker")));
//		monthPicker.sendKeys(Keys.ENTER);
//		Thread.sleep(2000);
//
//		//pick day as per input from data sheet
//		List<WebElement> days = driver.findElements(By.xpath("//*[@id='ui-datepicker-div']/descendant::a[contains(@class,'ui-state-default')]"));
//		Iterator<WebElement> i2= days.iterator();
//		while(i2.hasNext()){
//			System.out.println("inside days");
//			WebElement day = i2.next();
//			String actualDay = day.getText();
//			System.out.println(actualDay);
//			int actualDayInt = Integer.parseInt(actualDay);
//			Thread.sleep(1000);
//			if(dayInput == actualDayInt){
//				System.out.println(dayInput + " " + actualDay);
//				day.click();
//				Thread.sleep(2000);
//				break;
//			}
//		}
//
//	}
//
//	public static void waitForElementClickable(WebDriver driver, Logger log, String locator){
//
//		log.debug("=============================");
//		log.debug("Executing waitForElementClickable");
//
//		try {
//
//			WebDriverWait wait = new WebDriverWait(driver, 60);
//			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OR.getProperty(locator))));
//
//		}catch(Throwable t) {
//			log.debug("An error has occurred while executing waitForElementClickable keyword "+t.getMessage());
//		}
//
//	}
//
//	public static void dragTillAllRowsLoadedWithWait(WebDriver driver, Logger log, String rowsLocator, String dragThumbLocator, int totalRowCount, int numberOfPixelsToDragTheScrollbarDown) {
//
//		int i=0, previousCount=0, currentCount;
//		boolean exit=true;
//
//		Actions actions = new Actions(driver);
//
//		WebElement draggablePartOfScrollbar = driver.findElement(By.xpath(dragThumbLocator));
//
//		previousCount= driver.findElements(By.xpath(rowsLocator)).size();
//		log.debug("Previous Count: "+previousCount);
//		System.out.println("Previous Count: "+previousCount);
//
//		while(exit && (i<60)) {
//			// drag downwards
//			++i;
//			//    	   int numberOfPixelsToDragTheScrollbarDown = 100;
//			try{
//				actions.moveToElement(draggablePartOfScrollbar).clickAndHold().moveByOffset(0,numberOfPixelsToDragTheScrollbarDown).release().perform();
//				Thread.sleep(2000L);
//
//				currentCount= driver.findElements(By.xpath(rowsLocator)).size();
//				log.debug("Scroll:"+i+" Previous Count: "+previousCount+" Current Count: "+currentCount+"\n");
//				System.out.println("Scroll:"+i+" Previous Count: "+previousCount+" Current Count: "+currentCount+"\n");
//
//				if((currentCount>=previousCount) && (currentCount<totalRowCount)) {
//					previousCount=currentCount;
//					continue;
//				}else {
//					exit=false;
//					break;
//				}
//
//			}catch(Throwable t){
//				log.debug("An error has occurred while executing function dragTillAllRowsLoaded");
//			}
//		}
//	}
//	public static void downloadImage(WebDriver driver, Logger log, WebElement element, String imageName, String imageExtn){
//		URL url;
//		try {
//			String src = element.getAttribute("src");
//			url = new URL(src);
//			log.debug("Image URL:"+ url);
//			BufferedImage bufImgOne = ImageIO.read(url);
//			String imagePath = System.getProperty("user.dir")+"/images/"+imageName;
//			log.debug("Downloaded Image Path:"+ imagePath);
//			ImageIO.write(bufImgOne, imageExtn, new File(imagePath));
//
//		}catch(Throwable t){
//			log.debug("An error has occurred while executing function downloadImage "+"\n StackTrace"+t.getMessage());
//		}
//	}
//
//	
//	public static boolean compareTwoImages(BufferedImage originalImage, BufferedImage inputImage, Logger log) {
//		try{
//			boolean ret1 = false;
//			boolean ret2 = true;
//			Raster ras1 = originalImage.getData();
//			log.debug("Raster for Image1: "+ras1);
//			Raster ras2 = inputImage.getData();
//			log.debug("Raster for Image2: "+ras2);
//
//			//Comparing the the two images for number of bands,width & height.
//			if (ras1.getNumBands() == ras2.getNumBands()
//					&& ras1.getWidth() == ras2.getWidth()
//					&& ras1.getHeight() == ras2.getHeight()) {
//				ret1=true;
//			}
//			// Once the band ,width & height matches, comparing the images.
//			search: for (int i = 0; i < ras1.getNumBands(); ++i) {
//				for (int x = 0; x < ras1.getWidth(); ++x) {
//					for (int y = 0; y < ras1.getHeight(); ++y) {
//						if (ras1.getSample(x, y, i) != ras2.getSample(x, y, i)) {
//							ret2 = false;
//							break search;
//						}
//					}
//				}
//			}
//
//			log.debug("Net Result Value: "+ret1);
//			if (ret1 && ret2) 
//				return true;
//			else{
//				log.debug("Images are not Same.");
//				return false;
//			}
//		}catch(Throwable t){
//			log.debug("Error while executing function compareTwoImages" + t.getMessage());
//			return false;
//		}
//	}
//
//	public static void renameFile(String dirPath, String newFileName, File f, Logger log) {
//		try{
//			File tmpFile = null;
//			tmpFile = new File(dirPath+"/"+newFileName);
//			log.debug("File Renamed at: "+tmpFile);
//			f.renameTo(tmpFile);
//		}catch(Throwable t){
//			log.debug("Error while executing function renameFile" + t.getMessage());
//		}
//	}
//}