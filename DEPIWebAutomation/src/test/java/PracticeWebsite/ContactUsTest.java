package PracticeWebsite;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

public class ContactUsTest {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test(description = "Verify home page loads")
    public void verifyHomePageLoads() {
        ContactManager cm = new ContactManager(driver);
        cm.openHomePage();
        Assert.assertTrue(cm.isHomePageLoaded(), "Home page did not load");
    }

    @Test(description = "Navigate to Contact Us page from home page")
    public void navigateToContactUs() {
        ContactManager cm = new ContactManager(driver);
        cm.openHomePage();
        cm.navigateToContactUs();
        Assert.assertTrue(cm.isContactPageDisplayed(), "Contact page not displayed");
    }

    @Test(description = "Verify Contact Us header contains 'contact'")
    public void verifyContactHeader() {
        ContactManager cm = new ContactManager(driver);
        cm.openHomePage();
        cm.navigateToContactUs();
        String header = cm.getContactPageHeaderText();
        Assert.assertTrue(header.toLowerCase().contains("contact"), "Contact header did not contain 'contact' - found: " + header);
    }

    @Test(description = "Verify contact form fields are present")
    public void verifyContactFormFieldsPresent() {
        ContactManager cm = new ContactManager(driver);
        cm.openHomePage();
        cm.navigateToContactUs();
        Assert.assertTrue(cm.isContactFormPresent(), "Contact form fields not present");
    }

    @Test(description = "Submit contact form and verify success message")
    public void submitContactForm() {
        /* >>there's a bug on this website as the form failed to be submitted "A bug raised By Eslam "
        The bug exactly is that the submit button isn't submit the form data, it move the user to the first of the page which is the unexpected behaviour
        */
        ContactManager cm = new ContactManager(driver);
        cm.openHomePage();
        cm.navigateToContactUs();
        Assert.assertTrue(cm.isContactFormPresent(), "Contact form not present - cannot submit");
        cm.fillContactForm("Test User", "test+auto@example.com", "Automation Test", "This is a test message from automation.");
        cm.submitContactForm();
        String success = cm.getFormSuccessMessage();
        boolean ok = (success != null && !success.trim().isEmpty()) || driver.getPageSource().toLowerCase().contains("thank") || driver.getPageSource().toLowerCase().contains("success");
        Assert.assertTrue(ok, "No success message or confirmation found after submitting the contact form. Success text: '" + success + "'");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
