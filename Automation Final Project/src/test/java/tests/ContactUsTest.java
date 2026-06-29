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

public class ContactUsTest {

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
    //(description = "Verify home page loads")
    public void verifyHomePageLoads() {
        driver.get("https://practice.expandtesting.com/");
        Assert.assertTrue(driver.getCurrentUrl().contains("practice.expandtesting.com"), "Home page did not load");
        Assert.assertFalse(driver.getTitle().isEmpty(), "Home page title is empty - page did not load");
    }

    @Test
    // (description = "Navigate to Contact Us page from home page")
    public void navigateToContactUs() {
        driver.get("https://practice.expandtesting.com/");
        driver.get("https://practice.expandtesting.com/contact");

        wait.until(ExpectedConditions.urlContains("/contact"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/contact"), "Contact page not displayed");
    }

    @Test
    // (description = "Verify Contact Us header contains 'contact'")
    public void verifyContactHeader() {
        driver.get("https://practice.expandtesting.com/");
        driver.get("https://practice.expandtesting.com/contact");

        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#core h1")));
        Assert.assertTrue(header.getText().toLowerCase().contains("contact"),
                "Contact header did not contain 'contact' - found: " + header.getText());
    }

    @Test
    // (description = "Verify contact form fields are present")
    public void verifyContactFormFieldsPresent() {
        driver.get("https://practice.expandtesting.com/");
        driver.get("https://practice.expandtesting.com/contact");

        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#core input.form-control:nth-of-type(1)")));
        WebElement emailField = driver.findElement(By.xpath("(//input[contains(@class,'form-control')])[2]"));
        WebElement messageField = driver.findElement(By.cssSelector("#core textarea.form-control"));

        Assert.assertTrue(nameField.isDisplayed(), "Name field not present");
        Assert.assertTrue(emailField.isDisplayed(), "Email field not present");
        Assert.assertTrue(messageField.isDisplayed(), "Message field not present");
    }

    @Test
    //(description = "Submit contact form and verify success message")
    public void submitContactForm() {
        /* >>there's a bug on this website as the form failed to be submitted "A bug raised By Eslam "
        The bug exactly is that the submit button isn't submit the form data, it move the user to the first of the page which is the unexpected behavior
        */
        driver.get("https://practice.expandtesting.com/");
        driver.get("https://practice.expandtesting.com/contact");

        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#core input.form-control:nth-of-type(1)")));
        WebElement emailField = driver.findElement(By.xpath("(//input[contains(@class,'form-control')])[2]"));
        WebElement messageField = driver.findElement(By.cssSelector("#core textarea.form-control"));

        nameField.sendKeys("Test User");
        emailField.sendKeys("test+auto@example.com");
        messageField.sendKeys("This is a test message from automation.");

        WebElement sendButton = driver.findElement(By.xpath("//a[normalize-space()='Send']"));

        // JS scroll + click: a Google Ads iframe can overlay the button and intercept a native click.
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true)", sendButton);
        js.executeScript("arguments[0].click();", sendButton);

        boolean success = driver.getPageSource().toLowerCase().contains("thank")
                || driver.getPageSource().toLowerCase().contains("success");
        Assert.assertTrue(success, "KNOWN BUG (raised by Eslam): the 'Send' control is an <a href=\"#\"> link, "
                + "not a real submit button - clicking it only scrolls to the top of the page and never submits the form, "
                + "so no success/confirmation message appears. This test stays red until the bug is fixed.");
    }

    @AfterMethod
    public void teardown() {
            driver.quit();
    }
}
