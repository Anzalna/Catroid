@ignore
Feature: Variables with different name.

  Background:
    Given I have a program
    And this program has an object 'Object'

  Scenario: Add two variable with different name (RtL & LtR) Languages.

    Given 'Object' has a start script
    And set "متغیر" to 4
    And set 'Variable' to 10
    And set 'test' have set "متغیر" + set 'Variable'


    When I start the program
    Then the 'test' should be equal to 14




