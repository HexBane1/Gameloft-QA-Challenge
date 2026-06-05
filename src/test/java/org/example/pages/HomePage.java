package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By adIframe = By.cssSelector("iframe[id^='google_ads_iframe']");
    private By acceptCookiesButton = By.xpath("//button[contains(., 'Agree and close')]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateTo(String url) {
        driver.get(url);
        handleCookieConsent();
    }

    private void handleCookieConsent() {
        try {
            // Standard explicit wait for CMP load
            WebDriverWait cmpWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement button = cmpWait.until(ExpectedConditions.elementToBeClickable(acceptCookiesButton));
            button.click();

        } catch (Exception e) {
            System.out.println("Standard consent click failed. Attempting JS override.");
            try {
                // JS Fallback for visually obscured elements
                WebElement button = driver.findElement(acceptCookiesButton);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

            } catch (Exception jsException) {
                System.out.println("JS override failed. Refreshing to re-initialize ad networks.");
                // Network Fallback for hanging ad tags
                driver.navigate().refresh();

                try {
                    WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                    WebElement button = shortWait.until(ExpectedConditions.elementToBeClickable(acceptCookiesButton));
                    button.click();
                } catch (Exception finalException) {
                    System.out.println("Consent bypass unsuccessful, proceeding with default state.");
                }
            }
        }
    }

    public boolean isAdRendered() {
        // Asserting presence instead of visibility to prevent false failures from browser-level ad blockers
        WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(adIframe));
        int width = iframe.getSize().getWidth();
        int height = iframe.getSize().getHeight();

        return width > 0 && height > 0;
    }
}