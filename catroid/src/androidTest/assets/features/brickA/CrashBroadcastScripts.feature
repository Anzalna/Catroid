@ignore
Feature: Crash with Broadcast Scripts

  Correct Behavior. The program should execute without crashing.
  Incorrect Behavior, the program immediately crash, Pocket code has stopped message.

  Background:
    Given I have a Program
    And this program has an Object 'Object'

  Scenario:  Deterministic crash with Broadcast scripts

    Given 'Object' has a Start script
    And set 'A' to 10
    And broadcastWait '1'
    And when receive '1'
    And broadcastWait '2'
    And when receive '2'
    And broadcast '1'
    And set 'A' to 20

    When I start the program
    And  I wait until the program has stopped in 1 seconds
    Then the variable 'A' should be equal 20
