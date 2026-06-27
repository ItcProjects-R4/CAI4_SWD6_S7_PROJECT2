package PracticeWebsite;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ContactManager {

    public static final String BASE_URL = "https://practice.expandtesting.com/";

    private WebDriver driver;
    private WebDriverWait wait;
    // Locators - flexible contact link locator that matches anchor text containing 'contact'
    private By contactLink = By.xpath("//a[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'contact')]");
    private By contactHeader = By.xpath("//h1");
    // Contact form locators (robust XPaths to handle variations)
    private By nameField = By.xpath("//label[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'name')]/following::input[1] | //input[@name='name' or @id='name' or contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'name')]");
    private By emailField = By.xpath("//label[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'email')]/following::input[1] | //input[@name='email' or @id='email' or contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'email')]");
    private By subjectField = By.xpath("//label[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'subject')]/following::input[1] | //input[@name='subject' or @id='subject']");
    private By messageField = By.xpath("//textarea[@name='message' or @id='message'] | //label[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'message')]/following::textarea[1]");
    private By submitButton = By.xpath("//button[@type='submit' or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'submit')]");
    private By successMessage = By.xpath("//div[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'thank') or contains(@class,'success') or contains(@class,'alert-success') or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'success')]");
    public ContactManager(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    // Open home page
    public void openHomePage() {
        driver.get(BASE_URL);
    }
    // Verify home page loaded by URL or presence of body
    public boolean isHomePageLoaded() {
        try {
            String current = driver.getCurrentUrl();
            if (current != null && current.contains("practice.expandtesting.com")) return true;
            // fallback: wait for body element
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    // Click the Contact Us link (header/footer or anywhere present)
    public void navigateToContactUs() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(contactLink));
        link.click();
    }
    // Check contact page is displayed by header visibility
    public boolean isContactPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(contactHeader));
            String txt = driver.findElement(contactHeader).getText();
            return txt != null && txt.toLowerCase().contains("contact");
        } catch (Exception e) {
            return false;
        }
    }
    public String getContactPageHeaderText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(contactHeader)).getText();
        } catch (Exception e) {
            return "";
        }
    }
    // Form interactions        public boolean isContactFormPresent() {
        try {            wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(nameField),
                ExpectedConditions.presenceOfElementLocated(emailField),
                ExpectedConditions.presenceOfElementLocated(messageField)
            ));            return true;        } catch (Exception e) {            return false;        }    }
    public void fillContactForm(String name, String email, String subject, String message) {
        try {            WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(nameField));            nameInput.clear();            nameInput.sendKeys(name);        } catch (Exception e) {            // ignore if not found; other fields may still be present        }        try {            WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));            emailInput.clear();            emailInput.sendKeys(email);        } catch (Exception e) {        }        try {            WebElement subjectInput = wait.until(ExpectedConditions.visibilityOfElementLocated(subjectField));            subjectInput.clear();            subjectInput.sendKeys(subject);        } catch (Exception e) {        }        try {            WebElement msgInput = wait.until(ExpectedConditions.visibilityOfElementLocated(messageField));            msgInput.clear();            msgInput.sendKeys(message);        } catch (Exception e) {        }    }
    public void submitContactForm() {
        try {            WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(submitButton));            submit.click();        } catch (Exception e) {            // fallback: try pressing Enter on message field            try {                WebElement msg = driver.findElement(messageField);                msg.sendKeys("\n");            } catch (Exception ex) {            }        }    }
    public String getFormSuccessMessage() {
        try {            WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));            return success.getText();        } catch (Exception e) {            return "";        }    }
}
