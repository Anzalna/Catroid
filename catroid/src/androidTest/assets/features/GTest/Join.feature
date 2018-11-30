Feature: Variable Name



  Background:
    Given I have a Program
    And this program has an Object 'Object'

  Scenario: Join

    Given 'Object' has a Start script
    And this script has a set 'test' with string "زبان" and "اردو" brick
    And Show Variable 'test' at X 100 Y 200
    When I start the program
    And  I wait until the program has stopped
    Then the join string "زبان" and "اردو" should be equal to "زباناردو"