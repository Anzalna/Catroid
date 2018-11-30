@ignore
Feature: SetVariable

  Correct behavior: The set variable text, RTL languages i.e. Urdu/Arabic should be shown in the stage.
  Incorrect behavior: The set variable text, RTL languages are not shown in the stage.

  Background:
    Given I have a program
    And this program has an object 'Object'

  Scenario: Set Variable, ShowVariable then take screenshot of the stage.

    Given 'Object' has a start script
    And set 'var' to "متغیر"
    And Show Variable 'var' at X 100 Y 200
    When I start the program
    And wait 2 seconds
    And Take a screenshot of the stage
    Then the stage/screenshot is not empty

