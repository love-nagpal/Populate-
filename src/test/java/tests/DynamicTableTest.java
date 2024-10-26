package tests;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import config.WebDriverSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DynamicTableTest {
	private WebDriver driver;

	@BeforeClass
	public void setUp() {
		driver = WebDriverSetup.initializeDriver();
		driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
		driver.manage().window().maximize();  
	}

	@Test
	public void testPopulateInputAndRefresh() {
		String jsonString = "[{\"name\": \"Bob\", \"age\": 20, \"gender\": \"male\"}," +
				"{\"name\": \"George\", \"age\": 42, \"gender\": \"male\"}," +
				"{\"name\": \"Sara\", \"age\": 42, \"gender\": \"female\"}," +
				"{\"name\": \"Conor\", \"age\": 40, \"gender\": \"male\"}," +
				"{\"name\": \"Jennifer\", \"age\": 42, \"gender\": \"female\"}]";

		// Locate the input box and populate it with JSON data
		WebElement tableDataSummary = driver.findElement(By.xpath("//summary[contains(text(), 'Table Data')]"));
		tableDataSummary.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement inputBox = wait.until(ExpectedConditions.elementToBeClickable(By.id("jsondata")));
		inputBox.clear();
		inputBox.sendKeys(jsonString);

		// Click the Refresh button
		WebElement refreshButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("refreshtable")));
		refreshButton.click();

		// Wait for the table to be populated
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[contains(@id, 'dynamictable')]//tr")));

		// Extract data from the table
		List<List<String>> actualData = new ArrayList<>();
		List<WebElement> rows = driver.findElements(By.xpath("//table[contains(@id, 'dynamictable')]//tr"));

		// Loop through the rows to extract cell data
		for (WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.tagName("td")); // Extract cell elements
			List<String> cellData = new ArrayList<>();
			for (WebElement cell : cells) {
				cellData.add(cell.getText()); // Add cell text to the list
			}
			if (!cellData.isEmpty()) { // Only add non-empty rows (to skip header if necessary)
				actualData.add(cellData);
			}
		}

		// Define expected data
		List<List<String>> expectedData = Arrays.asList(
				Arrays.asList("Bob", "20", "male"),
				Arrays.asList("George", "42", "male"),
				Arrays.asList("Sara", "42", "female"),
				Arrays.asList("Conor", "40", "male"),
				Arrays.asList("Jennifer", "42", "female")
				);

		// Assert the size of the actual data matches expected data
		Assert.assertEquals(actualData.size(), expectedData.size(), "Row count does not match.");

		// Loop through each row and validate the data
		for (int i = 0; i < actualData.size(); i++) {
			Assert.assertEquals(actualData.get(i), expectedData.get(i), "Data mismatch at row " + (i + 1));
		}
	}

	    @AfterClass
	    public void tearDown() {
	        if (driver != null) {
	            driver.quit();
	        }
	    }
}
