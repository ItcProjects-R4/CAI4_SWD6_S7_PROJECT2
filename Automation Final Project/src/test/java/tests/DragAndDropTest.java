package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class DragAndDropTest {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;

    @BeforeMethod
    public void setup() {
       WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
    }

    @Test
    public void testDragAndDropFullJourney() throws InterruptedException {

        driver.get("https://practice.expandtesting.com/");

        driver.get("https://practice.expandtesting.com/drag-and-drop");

        WebElement columnA = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("column-a")));
        WebElement columnB = driver.findElement(By.id("column-b"));

        String initialTextA = columnA.getText();
        String initialTextB = columnB.getText();


        actions.clickAndHold(columnA).moveToElement(columnB).pause(Duration.ofMillis(500)).release().perform();
        wait.until(driver1 -> !columnA.getText().equals(initialTextA));

        String finalTextA = columnA.getText();
        String finalTextB = columnB.getText();

        Assert.assertEquals(finalTextA, initialTextB, "Expected column A to now contain what was originally in column B");
        Assert.assertEquals(finalTextB, initialTextA, "Expected column B to now contain what was originally in column A");
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }
}