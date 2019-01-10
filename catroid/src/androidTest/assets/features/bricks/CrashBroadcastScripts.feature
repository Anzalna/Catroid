
Feature: Crash with Broadcast Scripts

  Correct Behavior: The program should execute without crashing.
  Incorrect Behavior: The program immediately crash at the second time the message "1 is sent"

  Background:
    Given I have a program
    And this program has an object 'Object'

  Scenario:  Deterministic crash with Broadcast scripts

    Given 'Object' has a start script
    And set 'var' to 10
    And broadcastWait '1'
    Given 'Object' has a When '1' script
    And broadcastWait '2'
    Given 'Object' has a When '2' script
    And broadcast '1'
    And set 'var' to 20

    When I start the program
    And  wait 1 seconds
    Then the variable 'var' should be equal 20
