@ignore
Feature: Broadcast incorrectly called two times.

  The correct value of variable should be 1.
  In Landscape mode, when the bug occurs, the incorrect value will be 2.0.

  Background:
    Given I have a Program
    And this program has an Object 'Object'

  Scenario:  Broadcast incorrectly is called two times

    Given 'Object' has a Start script
    And wait 1 seconds
    And broadcast 'hello'
    And 'Object' has a When 'hello' script
    And change 't' by 1

    When I start the program
    And  I wait until the program has stopped in 1 seconds
    Then the variable 't' should be equal 1