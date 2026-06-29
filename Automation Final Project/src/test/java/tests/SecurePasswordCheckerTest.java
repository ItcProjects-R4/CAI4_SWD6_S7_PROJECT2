package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class SecurePasswordCheckerTest {

    WebDriver driver;
    WebDriverWait wait;

    private static final By PASSWORD_FIELD = By.name("password");
    private static final By CONDITIONS_LIST = By.cssSelector("ul.helper-text li");

    @BeforeMethod
    public void setup() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testPasswordCheckerFullJourney() {

       
        driver.get("https://practice.expandtesting.com/");

        driver.get("https://practice.expandtesting.com/secure-password-checker");

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_FIELD));

        List<WebElement> conditions = driver.findElements(CONDITIONS_LIST);
        long visibleCount = conditions.stream().filter(WebElement::isDisplayed).count();
        Assert.assertEquals(visibleCount, 4,"Expected all 4 conditions visible with empty password, but got: " + visibleCount);

        passwordField.sendKeys("abc");
        conditions = driver.findElements(CONDITIONS_LIST);
        visibleCount = conditions.stream().filter(WebElement::isDisplayed).count();
        Assert.assertEquals(visibleCount, 3,"Expected 3 conditions visible after short password, but got: " + visibleCount);

        passwordField.clear();
        passwordField.sendKeys("Abcdefgh");
        conditions = driver.findElements(CONDITIONS_LIST);
        visibleCount = conditions.stream().filter(WebElement::isDisplayed).count();
        Assert.assertEquals(visibleCount, 1,"Expected 1 condition visible for password missing number/symbol, but got: " + visibleCount);

        passwordField.clear();
        passwordField.sendKeys("SecurePass1!");
        conditions = driver.findElements(CONDITIONS_LIST);
        visibleCount = conditions.stream().filter(WebElement::isDisplayed).count();
        Assert.assertEquals(visibleCount, 0,"Expected all conditions to clear for a strong password, but got: " + visibleCount);
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }
}
