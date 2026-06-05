@GameTest
  Feature: Game Launch Infrastructure
    As a player,
    I want the game rendering engine to boot successfully
    So that I can play the game without crashes.

  Scenario: Verify the Pixelcraft Parkour HTML5 canvas loads successfully
    Given the browser environment is ready
    When the user navigates to the game with URL "https://play.ludigames.com/game.html?pID=8070"
    Then the game rendering canvas should be visible and active