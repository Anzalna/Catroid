@ignore
Feature: Broadcast and wait
  Correct Behavior: The variable should change/increase their value with
                    five seconds intervals.
  Incorrect behavior: The variable change/increase their value without further waits.

  Background:
    Given I have a program
    And this program has an object 'Object'

  Scenario: Broadcast and Wait does not wait.

    Given 'Object' has a start script
    And when receive 'hello'
    And change 'var' by 1
    And wait 5 seconds
    And when program starts
    And forever
    And broadcastWait 'hello'
    And forever end

    When I start the program
    And  wait 1 seconds
    Then the variable 'var' should be less than or equal 4

