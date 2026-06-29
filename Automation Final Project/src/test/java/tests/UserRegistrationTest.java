package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class UserRegistrationTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testValidRegistrationFullJourney() {


        driver.get("https://practice.expandtesting.com/");

        driver.get("https://practice.expandtesting.com/register");

        String username = "user" + System.currentTimeMillis();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")))
                .sendKeys(username);
        driver.findElement(By.id("password")).sendKeys("Securepass1!");
        driver.findElement(By.id("confirmPassword")).sendKeys("Securepass1!");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        // JS scroll + click: a Google Ads iframe can overlay the button and intercept a native click.
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true)", submitButton);
        js.executeScript("arguments[0].click();", submitButton);

        WebElement flash = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        Assert.assertTrue(flash.getText().toLowerCase().contains("success"), "Expected success message but got: " + flash.getText());

        wait.until(ExpectedConditions.urlContains("/login"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"), "Expected to be redirected to /login after successful registration");
    }

    @Test
    public void testPasswordMismatchFullJourney() {

        driver.get("https://practice.expandtesting.com/");

        driver.get("https://practice.expandtesting.com/register");

        String username = "user" + System.currentTimeMillis();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys("SecurePass1!");
        driver.findElement(By.id("confirmPassword")).sendKeys("DifferentPass1!");
        WebElement submitButton=driver.findElement(By.cssSelector("button[type='submit']"));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true)", submitButton);

        js.executeScript("arguments[0].click();", submitButton);


        WebElement flash = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        Assert.assertTrue(flash.getText().toLowerCase().contains("match"), "Expected password mismatch error but got: " + flash.getText());

        Assert.assertTrue(driver.getCurrentUrl().contains("/register"), "Expected to remain on /register page after failed registration");
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }
}