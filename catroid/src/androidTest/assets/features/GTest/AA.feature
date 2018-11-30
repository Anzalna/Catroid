
Feature: Broadcast and wait

                    #CAT-1576
  Background:
    Given I have a Program
    And this program has an Object 'Object'

  Scenario: Broadcast and Wait does not wait.

    Given 'Object' has a Start script
    Given 'Object' has a When 'hello' script
    And this script has a set 'var' to 10 brick
    And this script has a Wait 5 seconds brick
    And when program starts
    And this script has a Forever brick
    And this script has a BroadcastWait 'hello' brick
    And this script has a Forever end brick

    When I start the program
    And  I wait until the program has stopped
    Then the variable 'var' should be equal 10
   # Then the variable 'var' should not be equal 1

  Scenario:  Broadcast incorrectly is called two times
  #CAT-1689
    Given 'Object' has a Start script
    And this script has a Wait 1 seconds brick
    And this script has a Broadcast 'hello' brick
    And  'Object' has a When 'hello' script
    And Show Variable 't' at X 100 Y 200
    And this script has a change 't' by 1 brick

    When I start the program
    And  I wait until the program has stopped
    Then the variable 't' should be equal 1

  Scenario:  Deterministic crash with Broadcast scripts
    #CAT-1693
    Given 'Object' has a Start script
    And this script has a set 'A' to 10 brick
    And this script has a BroadcastWait '1' brick
    And  'Object' has a When '1' script
    And this script has a BroadcastWait '2' brick
    And  'Object' has a When '2' script
    And this script has a Broadcast '1' brick
    And this script has a set 'A' to 20 brick

    When I start the program
    And  I wait until the program has stopped
    Then the variable 'A' should be equal 20


  Scenario: Broadcast.


    Given 'Object' has a Start script

    #And this script has a BroadcastWait 'hello' brick
    #And When screen is touched
   # And When tapped


    #Given 'Object' has a When 'hello' script
   # And this script has a Stop this Script brick


    When I start the program
    And  I wait until the program has stopped
    #Then the variable 'var' should be equal 0.0
   # Then the variable 'var' should not be equal 1
