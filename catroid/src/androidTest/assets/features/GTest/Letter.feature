Feature: Variable Name

  Background:
    Given I have a Program
    And this program has an Object 'Object'

  Scenario: Letters

    Given 'Object' has a Start script
    And this script has a set 'test' letter no '1' to "پاکستان"
    And Show Variable 'test' at X 100 Y 200
    When I start the program
    And  I wait until the program has stopped
    Then the '7' no of the letter "پاکستان" should be equal to "ن"