Feature: Variable Name



  Background:
    Given I have a Program
    And this program has an Object 'Object'

  Scenario: Length

    Given 'Object' has a Start script
    And this script has a set 'test' to length "زبان" brick
    And Show Variable 'test' at X 100 Y 200
    When I start the program
    And  I wait until the program has stopped
    Then the length of the string "زبان" should be equal to 4.0
