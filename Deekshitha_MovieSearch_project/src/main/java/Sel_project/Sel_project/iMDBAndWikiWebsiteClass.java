import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class iMDBAndWikiWebsiteClass {
	static WebDriver driver;
	//WebDriverWait wait = new WebDriverWait(driver,30);
  @Test(dependsOnMethods= {"getvalue"})  
  
  
  public String[] imdb(String movieName) {
	  
	  driver.get("https://www.imdb.com/\n");
	  driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
	  
	  // To get page title
	   System.out.println(driver.getTitle());
	  
	  WebDriverWait wait = new WebDriverWait(driver,30);
	  
	  //Wait of visibility for search bar
	  WebElement searchBar =wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("suggestion-search")));
	  driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	
	  //Enter movie value to search box and click 
	  searchBar.sendKeys(movieName);
	  
	 
	  
	  WebElement search_button = driver.findElement(By.id("suggestion-search-button"));
	  search_button.click();
	  
	  
	  //Click on movie hyperlink
	  String movieHyperlinkXpath = "//a[starts-with(text(),'movieNameToReplace')]";
	  String requiredmovieXpath = movieHyperlinkXpath.replaceAll("movieNameToReplace",movieName);
	
	  
	  WebElement MovieName =wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(requiredmovieXpath)));
	  MovieName.click();
	  // driver.findElement(By.xpath("//a[contains(text(),'Pushpa: The Rise - Part 1')]"));
	  
	  //scroll to get 
	  JavascriptExecutor js= (JavascriptExecutor) driver;
	  WebElement RealiseDate = driver.findElement(By.xpath("//li[@data-testid='title-details-releasedate']/div/descendant::a"));
	  wait.until(ExpectedConditions.visibilityOf(RealiseDate));
	  driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	  js.executeScript("arguments[0].scrollIntoView();", RealiseDate);
	  
	  
	  //To get  RealiseDate
	  String IMDBmovieRealiseDate = RealiseDate.getText().toString();
	 
	  
	//To get  country of origin
	  String countryXpath = "//li[@data-testid='title-details-origin']/div/descendant::a ";
	  String IMDBCountryOfOrigin = driver.findElement(By.xpath(countryXpath)).getText().toString();
	  
	  
	  String IMDBDetails[]= {IMDBmovieRealiseDate,IMDBCountryOfOrigin};
	  
	  return IMDBDetails;
	

		        
  }
  @BeforeMethod
  public void setUp() {
	  
	   System.setProperty("webdriver.chrome.driver", "/Users/deekshitha.s/Documents/chromedriver");
	 
	  driver=new ChromeDriver();
	 
  }
  
  @Parameters({ "movieName" })
  @Test
  public void getvalue(String movieName) {
	  String[] imdb = imdb(movieName);
	  String imdbRealiseDate = imdb[0];
	  String imdbCountryOfOrigin = imdb[1];
	  System.out.println("imdb Realise date -" + imdbRealiseDate);
	  System.out.println("imdb Country Of Origin -" + imdbCountryOfOrigin);
	  
	  
	  String[] wiki = wiki(movieName);
	  String wikiRealiseDate = wiki[0];
	  String wikiCountryOfOrigin = wiki[1];
	  System.out.println("wiki Realise date -" + wikiRealiseDate);
	  System.out.println("wiki Country Of Origin -" + wikiCountryOfOrigin);
	  
	
	  
      DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
      DateTimeFormatter format1 = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH);
	  
	  LocalDate wikiRDate =LocalDate.parse(wikiRealiseDate, format);
	  System.out.println(wikiRDate);
	  
	  LocalDate imdbRDate =LocalDate.parse(wikiRealiseDate, format);
	  System.out.println(imdbRDate);
	  
	  
	  AssertJUnit.assertEquals(imdbCountryOfOrigin,wikiCountryOfOrigin);
	  AssertJUnit.assertEquals(wikiRDate,imdbRDate );
	 
	  
	  	  
  }
  
  @AfterMethod
  public void tearDown() {
	  
	  
	  
	  driver.quit();
  }
  
  @Test(dependsOnMethods= {"getvalue"})  
  
  
  public String[] wiki(String movieName) {
	  
	  driver.get("https://www.wikipedia.org/");
	  driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
	  
	  // To get page title
	   System.out.println(driver.getTitle());
	  
	  WebDriverWait wait = new WebDriverWait(driver,30);
	  WebElement searchBar =wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchInput")));
	 
	  driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	
	  //Enter movie value to search box and click 
	  searchBar.sendKeys(movieName);
	  driver.findElement(By.xpath("//button[@type='submit']")).click();
	 
	  //scroll 
	  JavascriptExecutor js= (JavascriptExecutor) driver;
	  WebElement RealiseDate = driver.findElement(By.xpath("//table[@class='infobox vevent']//tr[12]/td/descendant::li"));
	  wait.until(ExpectedConditions.visibilityOf(RealiseDate));
	  
	  driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	  js.executeScript("arguments[0].scrollIntoView();", RealiseDate);
	 
	  
	  //To get  RealiseDate
	  String wikiMovieRealiseDate = RealiseDate.getText().toString();
	  
	  
	//To get  country of origin
	  String countryXpath = "//table[@class='infobox vevent']//tr[14]/td ";
	  String WikiCountryOfOrigin = driver.findElement(By.xpath(countryXpath)).getText();
	  
	  // int index = WikiCountryOfOrigin.lastIndexOf(" ");
	  
	  String wikiDetails[]= {wikiMovieRealiseDate,WikiCountryOfOrigin};
	  
	  
	  return wikiDetails;
	  
	
  
}
}
