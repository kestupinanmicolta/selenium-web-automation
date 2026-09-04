package com.karen.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        int timeout = System.getenv("CI") != null ? 40 : 20;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        PageFactory.initElements(driver, this);
    }

    @Step("Navegar a la pagina principal")
    public void navigateTo() {
        driver.get("https://practicesoftwaretesting.com");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='search-query']")));
        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
    }

    @Step("Buscar producto: {0}")
    public void searchProduct(String query) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-query']")));
        input.clear();
        input.sendKeys(query);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='search-submit']"))).click();
        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
    }

    @Step("Obtener cantidad de productos")
    public int getProductCount() {
        List<WebElement> products = driver.findElements(By.cssSelector("a[href*='/product/']"));
        return products.size();
    }

    @Step("Hacer clic en producto indice: {0}")
    public void clickProduct(int index) {
        List<WebElement> products = wait.until(
            ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("a[href*='/product/']"))
        );
        if (index < products.size()) {
            products.get(index).click();
        }
    }

    @Step("Obtener conteo del carrito")
    public String getCartCount() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='cart-quantity']"))).getText();
    }
}
