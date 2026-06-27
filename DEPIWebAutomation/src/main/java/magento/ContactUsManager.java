package magento;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ContactUsManager {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String CONTACT_PAGE_URL = "https://magento2demo.firebearstudio.com/contact/";

    // Locators for Contact Us Page
    private By contactUsPageHeader = By.xpath("//*[@id=\"maincontent\"]/div[1]/h1/span");
    private By nameField = By.id("name");
    private By emailField = By.id("email");
    private By phoneField = By.id("telephone");
    private By messageField = By.id("comment");
    private By submitButton = By.xpath("//button[@title='Submit']");
    private By pageTitle = By.xpath("/html/body/div[2]/main/div[1]");

    // Constructor
    public ContactUsManager(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Getters for locators
    public By getNameField() {
        return nameField;
    }

    public By getEmailField() {
        return emailField;
    }

    public By getPhoneField() {
        return phoneField;
    }

    public By getMessageField() {
        return messageField;
    }

    public By getSubmitButton() {
        return submitButton;
    }

    // Actions/Verifications
    public String getPageHeaderTitle() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(contactUsPageHeader));
        return driver.findElement(contactUsPageHeader).getText();
    }

    public boolean isContactUsPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(contactUsPageHeader));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageUrl() {
        return driver.getCurrentUrl();
    }

    public boolean isPageUrlCorrect() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("/contact/");
    }

    public void enterName(String name) {
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(nameField));
        nameInput.clear();
        nameInput.sendKeys(name);
    }

    public void enterEmail(String email) {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void enterPhone(String phone) {
        WebElement phoneInput = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneField));
        phoneInput.clear();
        phoneInput.sendKeys(phone);
    }

    public void enterMessage(String message) {
        WebElement messageInput = wait.until(ExpectedConditions.visibilityOfElementLocated(messageField));
        messageInput.clear();
        messageInput.sendKeys(message);
    }

    public void submitForm() {
        WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submit.click();
    }
}