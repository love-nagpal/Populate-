package config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverSetup {
    public static WebDriver initializeDriver() {
        System.setProperty("webdriver.chrome.driver", "C://chromedriver//chromedriver.exe");
        return new ChromeDriver();
    }
}

