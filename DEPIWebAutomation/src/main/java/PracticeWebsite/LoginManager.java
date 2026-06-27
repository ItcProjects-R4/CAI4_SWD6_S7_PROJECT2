package PracticeWebsite;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginManager {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private final By loginLink = By.xpath("//a[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'login') or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign in')]");
    private final By usernameField = By.xpath("//input[@type='email' or @type='text' or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'user') or contains(translate(@id,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'user') or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'email') or contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'email')]");
    private final By passwordField = By.xpath("//input[@type='password' or contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'password')]");
    private final By submitButton = By.xpath("//button[@type='submit' or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'login') or //input[@type='submit']]");
    private final By logoutLink = By.xpath("//a[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'logout') or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign out')]");
    private final By loginError = By.xpath("//*[contains(@class,'error') or contains(@class,'alert') or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'invalid')]");

    private static final String BASE_URL = "https://practice.expandtesting.com/";

    public LoginManager(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navigates to the login page. Tries several strategies and falls back to direct URL.
     */
    public void openLoginPage() {
        // If already on login page, do nothing
        try {
            if (isLoginPageLoaded()) return;
        } catch (Exception e) {
            // continue to try other ways
        }

        // Try clicking a login/sign-in link if present
        try {
            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(loginLink));
            link.click();
            if (isLoginPageLoaded()) return;
        } catch (Exception e) {
            // ignore and try direct navigation
        }

        // Try canonical login URLs
        try {
            driver.get(BASE_URL + "login");
            if (isLoginPageLoaded()) return;
        } catch (Exception e) {
        }
        try {
            driver.get(BASE_URL + "login.html");
            if (isLoginPageLoaded()) return;
        } catch (Exception e) {
        }

        // As a last attempt navigate to base URL and search for fields
        try {
            driver.get(BASE_URL);
            // small wait for page
            Thread.sleep(1000);
            if (isLoginPageLoaded()) return;
        } catch (Exception e) {
            // ignore
        }

        // If still not located, throw a clear exception to help debugging
        throw new RuntimeException("Unable to open login page: no login link found and known login URLs didn't load login fields.");
    }

    public boolean isLoginPageLoaded() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            // presence of username or password field or submit button indicates login form
            shortWait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(usernameField),
                ExpectedConditions.presenceOfElementLocated(passwordField),
                ExpectedConditions.presenceOfElementLocated(submitButton)
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void login(String username, String password) {
        // Accept empty strings; clear and send keys when fields exist
        if (!isLoginPageLoaded()) {
            openLoginPage();
        }

        WebElement user = null;
        try {
            user = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
            user.clear();
            user.sendKeys(username == null ? "" : username);
        } catch (Exception e) {
            // proceed even if username not present (some forms only have one field)
        }

        WebElement pass = null;
        try {
            pass = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
            pass.clear();
            pass.sendKeys(password == null ? "" : password);
        } catch (Exception e) {
            // ignore
        }

        // Try clicking submit button or pressing Enter on password field
        try {
            WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
            try {
                submit.click();
            } catch (ElementClickInterceptedException ecie) {
                // If an overlay intercepts the click, try JS click as fallback
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
                } catch (Exception jsEx) {
                    // last resort: try submitting the password field
                    if (pass != null) {
                        pass.submit();
                    } else {
                        throw jsEx;
                    }
                }
            }
            return;
        } catch (Exception e) {
            // fallback: press enter in password field
            try {
                if (pass != null) pass.submit();
            } catch (Exception ex) {
                // nothing more we can do
            }
        }
    }

    public boolean isLoginSuccessful() {
        try {
            WebDriverWait shortt = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortt.until(ExpectedConditions.presenceOfElementLocated(logoutLink));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginErrorDisplayed() {
        try {
            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(loginError));
            String t = errorElement.getText();
            if (t != null) t = t.toLowerCase();
            return t != null && (t.contains("invalid") || t.contains("incorrect") || t.contains("error") || t.contains("failed") || t.contains("your username is invalid"));
        } catch (Exception e) {
            // fallback to scanning page source for common messages
            try {
                String page = driver.getPageSource().toLowerCase();
                return page.contains("your username is invalid") || page.contains("invalid username") || page.contains("invalid") || page.contains("incorrect");
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public void logout() {
        try {
            WebElement l = wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
            l.click();
        } catch (Exception e) {
            // ignore
        }
    }
}
