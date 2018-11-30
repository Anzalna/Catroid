@ignore
Feature: Object name

  Scenario:Object name with RTL languages.

    Given I have a program
    And this program has an object "آبجیکٹ"

    When I start the program
    Then the object should be equal to "آبجیکٹ"