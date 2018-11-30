@ignore
Feature: ShowVariable

Correct behavior: The set variable text, RTL languages i.e Urdu/Arabic are shown in the stage.
Incorrect behavior: The set variable text, RTL languages are not shown in the stage.

  Background:
    Given I have a Program
    And this program has an Object 'Object'

  Scenario: Set Variable, ShowVariable then take screenshot of the stage.

    Given 'Object' has a Start script
    #And this script has a Broadcast 'hello' brick
   # And this script has a set 'g' to 4 brick
    And this script has a set 'g' to "پاکستان" brick
    #And this script has a set 'g' into 'پاکستان' brick
   #And this script has a Wait 5 seconds brick
   # When when you received Broadcast'hello'
   # And this script has a Wait 2 seconds brick
    And Show Variable 'g' at X 100 Y 200
   # And this script has a Wait 2 seconds brick
   # And this script has a set 'g' to 5 brick

  # When I start the program
    And Take a screenshot of the stage
    #And I wait until the program has stopped
    Then the stage/screenshot is not empty
