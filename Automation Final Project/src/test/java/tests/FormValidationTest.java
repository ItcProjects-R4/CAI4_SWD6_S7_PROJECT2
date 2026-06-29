package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class FormValidationTest {

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
    public void testFormValidationFullJourney() {

        driver.get("https://practice.expandtesting.com/");
        driver.get("https://practice.expandtesting.com/form-validation");

        WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#core > div > div > div > div > div > form > div.col-12 > button")));
        WebElement nameField = driver.findElement(By.id("validationCustom01"));

        nameField.clear();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true)", submitButton);
        js.executeScript("arguments[0].click();", submitButton);

        WebElement nameError = driver.findElement(By.cssSelector("#core > div > div > div > div > div > form > div:nth-child(1) > div.invalid-feedback"));
        Assert.assertTrue(nameError.isDisplayed(), "Expected name field validation error to be displayed");

        WebElement contactError = driver.findElement(By.cssSelector("#core > div > div > div > div > div > form > div:nth-child(2) > div"));
        Assert.assertTrue(contactError.isDisplayed(), "Expected contact number validation error to be displayed");

        WebElement pickupDateError = driver.findElement(By.cssSelector("#core > div > div > div > div > div > form > div:nth-child(3) > div"));
        Assert.assertTrue(pickupDateError.isDisplayed(), "Expected pickup date validation error to be displayed");

        WebElement paymentError = driver.findElement(By.cssSelector("#core > div > div > div > div > div > form > div:nth-child(4) > div"));
        Assert.assertTrue(paymentError.isDisplayed(), "Expected payment method validation error to be displayed");

        nameField.sendKeys("Abdelrahman");

        WebElement contactField = driver.findElement(By.name("contactnumber"));
        contactField.sendKeys("012-3456789");

        WebElement pickupDateField = driver.findElement(By.name("pickupdate"));
        pickupDateField.sendKeys("01152025");

        Select paymentDropdown = new Select(driver.findElement(By.id("validationCustom04")));
        paymentDropdown.selectByVisibleText("cash on delivery");




        js.executeScript("arguments[0].click();", submitButton);


        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#core > div > div > div > p"))
        );
        Assert.assertTrue(
                successMessage.getText().contains("Thank you for validating your ticket"),
                "Expected success message confirming ticket validation after valid submission"
        );
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }
}