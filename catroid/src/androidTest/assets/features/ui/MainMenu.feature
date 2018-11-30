
Feature: Main menu

  In order to give the user a starting point
  The main menu offers a number of distinctive options

  Background:
    Given I have a Program

  Scenario: The main menu has a list of labeled buttons
    Given I am in the main menu
    Then I should see the following buttons
      | Continue |
      | New      |
      | Programs |
      | Help     |
      | Explore  |
      | Upload   |

  Scenario: The Continue button leads to the program view
    Given I am in the main menu
    When I press the Continue button
    Then I should switch to the Continue view

  Scenario: The Programs button leads to the programs view
    Given I am in the main menu
    When I press the Programs button
    Then I should switch to the Programs view