@MonetizationTest
  Feature: Monetization Infrastructure
    As a business stakeholder,
    I want to ensure the advertisement containers render correctly
    So that the portal can successfully display monetized content.

  Scenario: Verify the first ad container loads on the homepage
    Given the Gameloft browser environment is initialized
    When the user navigates to the LudiGames homepage at "https://play.ludigames.com"
    Then the primary advertisement should be successfully rendered