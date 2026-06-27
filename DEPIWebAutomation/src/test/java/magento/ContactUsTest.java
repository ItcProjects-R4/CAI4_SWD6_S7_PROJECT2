package magento;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

public class ContactUsTest {
    private WebDriver driver;
    private final String BASE_URL = "https://magento2demo.firebearstudio.com/";

    @BeforeMethod
    public void setUp() {
        // Set up ChromeDriver automatically with WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test(description = "Test navigation to Contact Us page from footer")
    public void verifyFooterContactUsNavigation() {
        // 1. Navigate to the base URL
        driver.get(BASE_URL);
        
        // 2. Verify home page is loaded
        HomePageManager homePage = new HomePageManager(driver);
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page is not loaded");

        // 3. Scroll to footer and click "Contact Us"
        ContactUsManager contactPage = homePage.navigateToContactUsViaFooter();

        // 4. Verify navigation was successful
        String currentUrl = contactPage.getPageUrl();
        Assert.assertTrue(currentUrl.contains("/contact/"), 
            "URL does not contain '/contact/', actual URL: " + currentUrl);

        // 5. Verify the page header is displayed
        Assert.assertTrue(contactPage.isContactUsPageDisplayed(), 
            "Contact Us page header is not displayed");

        // 6. Verify the page title/header
        String pageHeader = contactPage.getPageHeaderTitle();
        Assert.assertEquals(pageHeader, "Contact Us", 
            "Page header text does not match. Expected: 'Contact Us', Actual: '" + pageHeader + "'");
    }

    @Test(description = "Test that Contact Us page URL is correct")
    public void verifyContactUsPageUrl() {
        // 1. Navigate to the base URL
        driver.get(BASE_URL);
        
        // 2. Navigate to Contact Us page
        HomePageManager homePage = new HomePageManager(driver);
        ContactUsManager contactPage = homePage.navigateToContactUsViaFooter();

        // 3. Verify URL is correct
        Assert.assertTrue(contactPage.isPageUrlCorrect(), 
            "Contact Us page URL is incorrect");
    }

    @Test(description = "Test Contact Us page contains form fields")
    public void verifyContactUsFormFields() {
        // 1. Navigate to the Contact Us page
        driver.get(BASE_URL);
        HomePageManager homePage = new HomePageManager(driver);
        ContactUsManager contactPage = homePage.navigateToContactUsViaFooter();

        // 2. Verify form fields are available
        Assert.assertNotNull(contactPage.getNameField(), "Name field locator is null");
        Assert.assertNotNull(contactPage.getEmailField(), "Email field locator is null");
        Assert.assertNotNull(contactPage.getPhoneField(), "Phone field locator is null");
        Assert.assertNotNull(contactPage.getMessageField(), "Message field locator is null");
        Assert.assertNotNull(contactPage.getSubmitButton(), "Submit button locator is null");
    }

    @AfterMethod
    public void tearDown() {
        // Close the browser and end the session
        if (driver != null) {
            driver.quit();
        }
    }
}