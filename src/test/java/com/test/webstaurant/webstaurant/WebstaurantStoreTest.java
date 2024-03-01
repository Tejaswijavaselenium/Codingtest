package com.test.webstaurant.webstaurant;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.List;

public class WebstaurantStoreTest {

    private WebDriver driver;
    private String baseUrl = "https://www.webstaurantstore.com/";

    @BeforeTest
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void testWebstaurantStore() throws InterruptedException {
        driver.get(baseUrl);

        // Search for 'stainless work table'
        driver.findElement(By.id("search_query")).sendKeys("stainless work table");
        driver.findElement(By.id("search_submit")).click();

        // Wait for search results to load
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.className("product-grid-item")));

        // Check if all product titles contain 'Table'
        List<WebElement> products = driver.findElements(By.className("product-title"));
        for (WebElement product : products) {
            Assert.assertTrue(product.getText().contains("Table"), "Product title does not contain 'Table'");
        }

        // Add the last found item to cart
        List<WebElement> addToCartButtons = driver.findElements(By.className("add-to-cart-button"));
        addToCartButtons.get(addToCartButtons.size() - 1).click();

        // Wait for cart to update
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("cart_link")));

        // Empty cart
        driver.findElement(By.id("cart_link")).click();
        driver.findElement(By.id("empty_cart_button")).click();

        // Confirm empty cart
        new WebDriverWait(driver, 10).until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        // Wait for cart to update
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.className("cart-empty")));

        // Close the browser
        driver.quit();
    }
}
