
Feature: Variable Name

  Background:
    Given I have a Program
    And this program has an Object 'Object'

  Scenario: when screen is touched scripts can run only in one instance

    Given 'Object' has a when screen is touched script

    And this script has a Forever brick
    And this script has a change 'x' by 1 brick
    And this script has a Wait 1 seconds brick
    And this script has a set 't' to 'x' brick
    And this script has a Wait 1 seconds brick
    And Show Variable 't' at X 100 Y 200
    And this script has a Forever end brick

    When I start the program
    And click on the screen 5 times
    And I wait until the program has stopped
    #Then the variable 'x' minus 't' should be less than or equal 2
