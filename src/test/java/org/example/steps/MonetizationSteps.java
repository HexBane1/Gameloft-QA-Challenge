package org.example.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.pages.HomePage;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class MonetizationSteps {

    WebDriver driver;
    HomePage homePage;

    @Before("@MonetizationTest")
    public void setup() {
        if (driver == null) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();

            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--disable-web-security");
            options.addArguments("--allow-running-insecure-content");
            options.addArguments("--no-sandbox");

            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        }
    }

    @Given("the Gameloft browser environment is initialized")
    public void the_gameloft_browser_environment_is_initialized() {
        homePage = new HomePage(driver);
    }

    @When("the user navigates to the LudiGames homepage at {string}")
    public void the_user_navigates_to_the_ludigames_homepage_at(String url) {
        homePage.navigateTo(url);
    }

    @Then("the primary advertisement should be successfully rendered")
    public void the_primary_advertisement_should_be_successfully_rendered() {
        Assertions.assertTrue(homePage.isAdRendered(), "The ad image failed to render or had 0x0 dimensions.");
    }

    @After("@MonetizationTest")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}