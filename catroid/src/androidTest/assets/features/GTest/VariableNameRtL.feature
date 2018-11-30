Feature: Variable Name



  Background:
    Given I have a Program
    And this program has an Object 'Object'

  Scenario: Variable Name in RtoL Languages

    Given 'Object' has a Start script
    And this script has a set "پاکستان" to 4 brick

    When I start the program
    And  I wait until the program has stopped
    Then the name of the variable should be equal to "پاکستان"