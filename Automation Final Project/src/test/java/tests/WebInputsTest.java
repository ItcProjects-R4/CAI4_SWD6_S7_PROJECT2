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

public class WebInputsTest {

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
    public void testWebInputsFullJourney() {
        driver.get("https://practice.expandtesting.com/");

        driver.get("https://practice.expandtesting.com/inputs");

        WebElement numberField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-number")));

        numberField.sendKeys("25");

        WebElement textField = driver.findElement(By.id("input-text"));

        textField.sendKeys("Hello");

        WebElement dateField = driver.findElement(By.id("input-date"));

        dateField.sendKeys("01152025");

        Assert.assertEquals(numberField.getAttribute("value"), "25", "Expected number field to contain 25");

        Assert.assertEquals(textField.getAttribute("value"), "Hello", "Expected text field to contain Hello");

        Assert.assertEquals(dateField.getAttribute("value"), "2025-01-15", "Expected date field to contain the entered date");

        numberField.clear();

        textField.clear();

        dateField.clear();

        Assert.assertEquals(numberField.getAttribute("value"), "", "Expected number field to be empty after clearing");

        Assert.assertEquals(textField.getAttribute("value"), "", "Expected text field to be empty after clearing");

        Assert.assertEquals(dateField.getAttribute("value"), "", "Expected date field to be empty after clearing");
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }
}