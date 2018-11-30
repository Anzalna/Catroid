
Feature: Repeat brick

  A Repeat brick repeats another set of bricks a given number of times.

  Background:
    Given I have a program
    And this program has an object 'Object'

  Scenario: Increment variable inside loop

    Given 'Object' has a start script
    And this script has a set 'i' to 1 brick
    And this script has a Repeat 8 times brick
    And this script has a change 'i' by 1 brick
    And this script has a Repeat end brick

    When I start the program
    And I wait until the program has stopped
    Then the variable 'i' should be equal 9
