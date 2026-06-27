package magento;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePageManager {

    private WebDriver driver;
    private WebDriverWait wait;

    final String HomePageUrl = "https://magento2demo.firebearstudio.com/";
    
    // Locators for footer elements
    private By contactUsLink = By.xpath("//a[contains(text(), 'Contact Us')]");
    private By footer = By.xpath("//footer");

    // Constructor
    public HomePageManager(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Methods to navigate to Contact Us page
    public ContactUsManager navigateToContactUsViaFooter() {
        // Wait for the footer element to be present
        wait.until(ExpectedConditions.presenceOfElementLocated(footer));
        
        // Wait for the contact us link to be present in the DOM
        WebElement contactLink = wait.until(ExpectedConditions.presenceOfElementLocated(contactUsLink));

        // Scroll to the footer element to ensure it is in the viewport
        new Actions(driver).scrollToElement(contactLink).perform();

        // Wait for it to be clickable and click
        wait.until(ExpectedConditions.elementToBeClickable(contactLink)).click();

        // Return the Contact Us page object
        return new ContactUsManager(driver);
    }

    public boolean isHomePageLoaded() {
        try {
            String currentUrl = driver.getCurrentUrl();
            return currentUrl.contains(HomePageUrl);
        } catch (Exception e) {
            return false;
        }
    }
}