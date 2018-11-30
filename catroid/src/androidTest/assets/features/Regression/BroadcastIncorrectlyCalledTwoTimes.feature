
Feature: Broadcast incorrectly called.

  Correct behavior: The correct value of variable should be equal to 1.
  Incorrrect behavior: In Landscape mode, when the bug occurs, the incorrect value will be 2.0.

  Background:
    Given I have a program with landscape
    And this program has an object 'Object'

  Scenario:  Broadcast incorrectly called two times

    Given 'Object' has a start script
    And wait 1 seconds
    And broadcast 'hello'
    Given 'Object' has a When 'hello' script
    And change 'var' by 1

    When I start the program
    And  wait 1 seconds
    Then the variable 'var' should be equal 1