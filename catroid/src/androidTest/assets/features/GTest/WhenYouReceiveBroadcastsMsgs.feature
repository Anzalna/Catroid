
Feature: Receive  BroadcastMessages

  Expected, correct behavior: After 10 seconds, the number 10 (sometimes 9)
  is shown for 3 seconds, then after 10 seconds, the number 10 (sometimes 9)
  is again shown for 4 seconds, then after another 10 seconds, the number 10 (or 9)
  again is shown continuously.

  Background:
    Given I have a Program
    And this program has an Object 'Object'

  Scenario: "When you receive script executed with double speed on second call

    Given 'Object' has a Start script
    And this script has a Forever brick
    And this script has a Wait 1 seconds brick
    And this script has a change 'MySecond' by 1 brick
    And this script has a Forever end brick
    Given 'Object' has a Start script
    And this script has a Broadcast 'start' brick
    And this script has a Wait 13 seconds brick
    And this script has a BroadcastWait 'start' brick
    Given 'Object' has a When 'hello' script
    #And When the set variable 'timer' hide
    And this script has a set 'starttime' to 'MySecond' brick
    And this script has a Wait 10 seconds brick
    And this script has a set 'timer' have set 'MySecond' minus set 'starttime'
    #And Show Variable at X 100 Y 200



    When I start the program
    And  I wait until the program has stopped
    #Then the variable 'timer' should be equal 50
    Then the variable 'timer' should be less than or equal 20
    And the variable 'timer' should be greater than or equal 1
