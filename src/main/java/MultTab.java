import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;

import java.util.concurrent.TimeUnit;

public class MultTab {
    public static void openUrl() {
        WebDriver driver;
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("https://amazon.com");
        ((ChromeDriver) driver).executeScript("window.open('');");
        driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString());
        driver.get("https://facebook.com");
        WebElement contactLink = driver.findElement(By.xpath("//*[@id=\"reg_pages_msg\"]/a"));
        Actions actions = new Actions(driver);
        String platformName = System.getProperty("os.name").toLowerCase();
        if (platformName.contains("mac")) {
            actions.keyDown(Keys.COMMAND).click(contactLink).keyUp(Keys.COMMAND).perform();
        }
        driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString());
        driver.quit();
    }

    public static void main(String[] args) {
        openUrl();
    }
}
