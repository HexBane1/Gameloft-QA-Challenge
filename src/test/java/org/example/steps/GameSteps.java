package org.example.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.pages.GamePage;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class GameSteps {

    WebDriver driver;
    GamePage gamePage;

    @Before("@GameTest")
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

    @Given("the browser environment is ready")
    public void browser_environment_is_ready() {
        gamePage = new GamePage(driver);
    }

    @When("the user navigates to the game with URL {string}")
    public void the_user_navigates_to_the_game_with_url(String gameUrl) {
        gamePage.navigateTo(gameUrl);
    }

    @Then("the game rendering canvas should be visible and active")
    public void the_game_rendering_canvas_should_be_visible_and_active() {
        boolean isRendered = gamePage.isCanvasRendered();
        Assertions.assertTrue(isRendered, "The game canvas failed to render or had 0x0 dimensions.");
    }

    @After("@GameTest")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}