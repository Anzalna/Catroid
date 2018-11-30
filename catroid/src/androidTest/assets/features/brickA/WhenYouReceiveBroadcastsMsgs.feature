
Feature: Receive  Broadcast Messages

  Correct behavior:- correct behavior after 10 seconds, the variable “timer“ should be shown 10 or 9
  in the period of 3 seconds, then after 10 seconds, the variable “timer“ should be shown 10 or 9 in
  the period of 4 seconds, then after another 10 seconds, the variable “timer“ should be shown 10 or 9
  in the same period continuously
  Incorrect behavior:- The Incorrect behavior, after 10 second the variable “timer” should be shown 10 or 9
  in the period of 3 seconds. But after 5 seconds the variable “timer” shown 5 or 4 in the period of 4 seconds
  which is incorrect, then after another 5 seconds the variable “timer” shown 5 or 4 again in the period continuously.
  Apparently, the second script found incorrectly executed with twice speed, especially on second and third call.

  Background:
    Given I have a program
    And this program has an object 'Object'

  Scenario: "When you receive script should not be executed with double speed on second call"

    Given when program starts
    And forever
    And wait 1 seconds
    And change 'MySecond' by 1
    And forever end
    Given when program starts
    And broadcast 'start'
    And wait 13 seconds
    And broadcastWait 'start'
    Given 'Object' has a When 'start' script
   # And when receive 'start'
    And hide variable 'timer'
    And set 'starttime' to 'MySecond'
    And wait 10 seconds
    And set 'timer' have set 'MySecond' - set 'starttime'

    When I start the program
    And I wait for 24 seconds
    Then the variable 'timer' should be greater than or equal 9

