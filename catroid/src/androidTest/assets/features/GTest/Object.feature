
Feature: Object name

  Scenario:Object name in urdu

    Given I have a Program
    And this program has an Object "پاکستان"

    When I start the program
    And I wait until the program has stopped
    Then the name of the object should be equal to "پاکستان"