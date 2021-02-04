package com.makemytrip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.makemytrip.utils.ExcelUtils;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TicketBookedTest {
	WebDriver driver = null;

	@BeforeMethod
	public void SetUp() throws InterruptedException {
		WebDriverManager.chromedriver().setup();

		driver = new ChromeDriver();

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		File configFile = new File("./src/test/resources/config/config.properties");
		try {
			FileReader reader = new FileReader(configFile);
			Properties props = new Properties();
			props.load(reader);
			String url = props.getProperty("url");

			driver.get(url);
			Thread.sleep(3000);
			Set<String> windowHandles = driver.getWindowHandles();
			Thread.sleep(3000);
			System.out.println(windowHandles.size());

		} catch (FileNotFoundException ex) {
		} catch (IOException ex) {
		}

	}

	@DataProvider
	public Object[][] getBookedTicketData() {
		Object[][] data = ExcelUtils.getTestData("TicketBooked");
		return data;
	}

	@Test(dataProvider = "getBookedTicketData")
	public void BookingTest(String to) throws InterruptedException {

		driver.findElement(By.xpath("//div[@class='landingContainer']")).click();

		driver.findElement(By.id("toCity")).click();

		driver.findElement(By.xpath("//input[@placeholder='To']")).sendKeys(to);
		Thread.sleep(3000);
		Actions act = new Actions(driver);
		WebElement click = driver.findElement(By.xpath("//p[text()='Kolkata, India']"));
		act.moveToElement(click).click().build().perform();

		driver.findElement(By.xpath("(//p[text()='4'])[2]")).click();
		driver.findElement(By.xpath("//a[text()='Search']")).click();

		driver.findElement(By.xpath("//button[@class='ViewFareBtn  text-uppercase '] ")).click();
		driver.findElement(By.xpath("//button[text()='Book Now']")).click();

		Set<String> windowHandles = driver.getWindowHandles();
		System.out.println(windowHandles.size());
		Iterator<String> iterator = windowHandles.iterator();
		String parent = iterator.next();
		String child = iterator.next();
		System.out.println("Parent window handle name: " + parent);
		System.out.println("Child window handle name: " + child);

		driver.switchTo().window(child);

		String text = driver.findElement(By.xpath("//h4[text()='Review your booking']")).getText();
		System.out.println(text);

	}

	@AfterMethod
	public void tearDown() {
		driver.quit();
	}

}
