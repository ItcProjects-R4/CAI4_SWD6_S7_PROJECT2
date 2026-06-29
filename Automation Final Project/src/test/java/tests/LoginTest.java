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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * DataProvider containing the hardcoded test cases.
     * Columns: Test Name | Username | Password | Expected to Succeed?
     */
    @DataProvider(name = "loginScenarios")
    public Object[][] getLoginData() {
        return new Object[][] {
                // 1. Valid Login
                {"Valid Login", "practice", "SuperSecretPassword!", true},

                // 2. Invalid Username
                {"Invalid Username", "wrongUser", "SuperSecretPassword!", false},

                // 3. Invalid Password
                {"Invalid Password", "practice", "WrongPassword123", false},

                // 4. Empty Fields
                {"Empty Fields", "", "", false}
        };
    }

    @Test(dataProvider = "loginScenarios")
    public void testLoginFlows(String testName, String username, String password, boolean expectedSuccess) {
        System.out.println("Executing Test: " + testName);

        driver.get("https://practice.expandtesting.com/");
        driver.get("https://practice.expandtesting.com/login");

        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameField.clear();
        usernameField.sendKeys(username);

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys(password);

        WebElement loginButton = driver.findElement(By.id("submit-login"));

        // JS scroll + click: a Google Ads iframe can overlay the button and intercept a native click.
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true)", loginButton);
        js.executeScript("arguments[0].click();", loginButton);

        if (expectedSuccess) {
            // Successful login redirects to /secure and shows a Logout button
            wait.until(ExpectedConditions.urlContains("/secure"));
            WebElement flash = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
            Assert.assertTrue(flash.getText().toLowerCase().contains("you logged into a secure area"),
                    "[" + testName + "] Expected success message for user '" + username + "', but got: " + flash.getText());

            WebElement logoutButton = driver.findElement(By.cssSelector("a[href='/logout']"));
            Assert.assertTrue(logoutButton.isDisplayed(),
                    "[" + testName + "] Expected a Logout button after successful login, but none was found.");

            // Log out so the state is clear
            js.executeScript("arguments[0].click();", logoutButton);
            wait.until(ExpectedConditions.urlContains("/login"));
        } else {
            // Invalid attempts stay on the login flow and surface an error flash message
            WebElement flash = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
            Assert.assertTrue(flash.isDisplayed(),
                    "[" + testName + "] Expected an error message to be displayed, but none was found.");
            Assert.assertFalse(driver.getCurrentUrl().contains("/secure"),
                    "[" + testName + "] Did not expect to reach the secure area for invalid credentials.");
        }
    }

    @AfterMethod
    public void teardown() {
            driver.quit();
    }
}
