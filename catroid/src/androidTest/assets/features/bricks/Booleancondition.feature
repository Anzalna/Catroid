
Feature: conditional statement

  Correct Behavior: The program should execute with forever loop, if statement and if else statement
  without crashing.

  Background:
    Given I have a program
    And this program has an object 'Object'

  Scenario: If()then, Else brick is a control brick to check its Boolean condition.
    Given 'Object' has a start script
    And set 'score' to 10
    And if 'score' < 9 is true then..Else
    And stop this script
    And Else
    And change 'score' by 10
    And end if else

    When I start the program
    Then the variable 'score' should be equal 20

  Scenario: If()then is a control brick to check the Boolean condition (Equal)
    Given 'Object' has a start script
    And set 'score' to 10
    And if 'score' = 10 is true then
    And change 'score' by 10
    And end if

    When I start the program
    Then the variable 'score' should be equal 20

  Scenario: If()then is a control brick to check the Boolean condition (Less_Then)
    Given 'Object' has a start script
    And set 'score' to 2
    And forever
    And if 'score' < 20 is true then
    And change 'score' by 1
    And end if
    And forever end

    When I start the program
    And  wait 1 seconds
    Then the variable 'score' should be equal 20


