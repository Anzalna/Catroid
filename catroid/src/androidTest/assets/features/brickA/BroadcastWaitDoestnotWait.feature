@ignore
Feature: Broadcast and wait

  Incorrect behavior change the variable without further waits.

  Background:
    Given I have a Program
    And this program has an Object 'Object'

  Scenario: Broadcast and Wait does not wait.

    Given 'Object' has a Start script
    And when receive 'hello'
    And change 'var' by 1
    And wait 5 seconds
    And when program starts
    And forever
    And broadcastWait 'hello'
    And forever end

    When I start the program
    And  I wait until the program has stopped in 5 seconds
    Then the variable 'var' should be less than or equal 4
    #And the variable 'var' should be greater than or equal 0
