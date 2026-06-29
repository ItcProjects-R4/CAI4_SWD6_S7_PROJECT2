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

public class ForgotPasswordTest {

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
    public void testForgotPasswordFullJourney() {

        driver.get("https://practice.expandtesting.com/");

        WebElement loginLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#examples > div:nth-child(2) > div:nth-child(2) > div > div.card-body > h3 > a")));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true)", loginLink);

        js.executeScript("arguments[0].click();", loginLink);

        wait.until(ExpectedConditions.urlContains("/login"));
        Assert.assertTrue(
                driver.getCurrentUrl().contains("/login"),
                "Expected to be on the /login page after clicking Login link"
        );

        driver.get("https://practice.expandtesting.com/forgot-password");


        WebElement emailField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("email"))
        );
        emailField.sendKeys("practice@expandtesting.com");

        WebElement retrieveButton = driver.findElement(By.cssSelector("#forgot_password > button"));

        js.executeScript("arguments[0].scrollIntoView(true)", retrieveButton);

        js.executeScript("arguments[0].click();", retrieveButton);



        WebElement confirmationMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("confirmation-alert"))
        );
        Assert.assertTrue(
                confirmationMessage.getText().contains("reset your password"),
                "Expected confirmation message about resetting password to be displayed"
        );


        driver.get("https://practice.expandtesting.com/login");


        Assert.assertTrue(
                driver.getCurrentUrl().contains("/login"),
                "Expected to be back on the /login page at the end of the journey"
        );
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }
}