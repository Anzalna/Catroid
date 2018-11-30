
Feature: Set variable name

  Background:
    Given I have a Program
    And this program has an Object 'Object'

  Scenario: Variable name in urdu

    Given when program starts
    And this script has a set "پاکستان" to 4 brick

    When I start the program
    And I wait until the program has stopped
    Then the name of the variable should be equal "پاکستان"