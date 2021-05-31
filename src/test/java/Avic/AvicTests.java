package Avic;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AvicTests {

    WebDriver driver;

    @BeforeTest
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
    }

    @BeforeMethod
    public void testsSetUp(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://avic.ua/");
    }

    @Test(priority = 1)
    public void checkThatUrlContainsSearchQuery(){
        driver.findElement(By.xpath("//input[@id='input_search']")).sendKeys("iPhone 11", Keys.ENTER);
        Assert.assertTrue(driver.getCurrentUrl().contains("query=iPhone"));
    }

    @Test(priority = 2)
    public void checkElementsAmountOnSearchPage(){
        driver.findElement(By.xpath("//input[@id='input_search']")).sendKeys("iPhone 11", Keys.ENTER);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        List<WebElement> elementList = driver.findElements(By.xpath("//div[@class='prod-cart__descr']"));
        int actualElementsSize = elementList.size();
        Assert.assertEquals(actualElementsSize, 12);
    }

    @Test(priority = 3)
    public void checkThatSearchResultsContainSearchWord(){
        driver.findElement(By.xpath("//input[@id='input_search']")).sendKeys("iPhone 11", Keys.ENTER);
        List<WebElement> elementList = driver.findElements(By.xpath("//div[@class='prod-cart__descr']"));
        for (WebElement webElement : elementList){
            Assert.assertTrue(webElement.getText().contains("iPhone 11"));
        }
    }

    @Test(priority = 4)
    public void checkAddToCart(){
        driver.findElement(By.xpath("//span[@class='sidebar-item']")).click();
        driver.findElement(By.xpath("//ul[contains(@class,'sidebar-list')]//a[contains(@href, 'apple-store')]")).click();
        driver.findElement(By.xpath("//div[@class='brand-box__title']/a[contains(@href,'iphone')]")).click();
        new WebDriverWait(driver,30).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        driver.findElements(By.xpath("//a[@class='prod-cart__buy']")).get(0).click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("js_cart")));
        driver.findElement(By.xpath("//div[@class='btns-cart-holder']//a[contains(@class,'btn--orange')]")).click();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        String actualProductsCountInCart =
                driver.findElement(By.xpath("//div[contains(@class,'header-bottom__cart')]//div[contains(@class,'cart_count')]")).getText();
        Assert.assertEquals(actualProductsCountInCart, "1");
    }

    @AfterMethod
    public void tearDown(){
        driver.close();
    }
}
