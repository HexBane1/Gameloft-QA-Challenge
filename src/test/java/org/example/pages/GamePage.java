package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class GamePage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By acceptCookiesButton = By.xpath("//button[contains(., 'Agree and close')]");
    private By gameCanvas = By.tagName("canvas");

    public GamePage(WebDriver driver) {
        this.driver = driver;
        // Extended timeout for game engine initialization
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigateTo(String url) {
        driver.get(url);
        handleCookieConsent();
    }

    private void handleCookieConsent() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement button = shortWait.until(ExpectedConditions.elementToBeClickable(acceptCookiesButton));
            button.click();
        } catch (Exception e) {
            System.out.println("Consent pop-up not present, proceeding.");
        }
    }

    public boolean isCanvasRendered() {
        try {
            WebDriverWait domWait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // Switch to outer CDN wrapper
            System.out.println("Switching to outer game frame...");
            WebElement level1 = domWait.until(ExpectedConditions.presenceOfElementLocated(By.id("game-frame")));
            driver.switchTo().frame(level1);

            // Switch to inner game engine wrapper
            System.out.println("Switching to inner game frame...");
            WebElement level2 = domWait.until(ExpectedConditions.presenceOfElementLocated(By.id("game_frame")));
            driver.switchTo().frame(level2);

            // Bypass browser AudioContext autoplay policy
            System.out.println("Sending click to initialize game engine...");
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("document.body.click();");

            // Verify canvas rendering
            WebElement canvas = domWait.until(ExpectedConditions.presenceOfElementLocated(gameCanvas));
            int width = canvas.getSize().getWidth();
            int height = canvas.getSize().getHeight();

            System.out.println("Canvas rendered at: " + width + "x" + height);

            driver.switchTo().defaultContent();
            return width > 0 && height > 0;

        } catch (Exception e) {
            System.out.println("Canvas rendering failed: " + e.getMessage());
            driver.switchTo().defaultContent();
            return false;
        }
    }
}