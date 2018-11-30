@ignore
Feature: Broadcast and wait

  Correct behavior: The variable should be equal to set variable 1.
  Incorrect behavior: The variable should not equal to set variable.

  Background:
    Given I have a program
     And this program has an object 'Object'

  Scenario: Broadcast and Wait brick should stop waiting even when you
            receive broadcast script stopped using Stop Script.

    Given 'Object' has a start script
     And broadcastWait 'hello'
     And set 'var' to 1

    Given 'Object' has a When 'hello' script
     And stop this Script

    When I start the program
     And  I wait until the program has stopped
    Then the variable 'var' should be equal 1




