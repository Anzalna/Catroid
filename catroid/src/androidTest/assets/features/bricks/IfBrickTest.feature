
Feature: conditional statement

  Correct Behavior: The program should execute with forever loop, if statement and if else statement
  without crashing.

  Background:
    Given I have a program
    And this program has an object 'Object'

  Scenario: testNestedIfBricks()

    Given 'Object' has a start script
    And if 1 < 2 is true then..Else
    And if 1 < 2 is true then..Else
    And set 'testUservariable' to 42
    And Else
    And end if else
    And Else
    And end if else

    When I start the program
    Then the variable 'testUservariable' should be equal 42


  Scenario: testIfBrick()
    Given 'Object' has a start script
    And if 1 < 2 is true then..Else
    And set 'testUservariable' to 42
    And Else
    And end if else

    When I start the program
    Then the variable 'testUservariable' should be equal 42


  Scenario: testIfElseBrick()
    Given 'Object' has a start script
    And if 1 < 2 is true then..Else
    And Else
    And set 'testUservariable' to 42
    And end if else

    When I start the program
    Then the variable 'testUservariable' should be equal 42


  Scenario: testNotANumberFormula()
    Given 'Object' has a start script
    And if 3 < 2 is true then..Else
    And set 'testUservariable' to 42
    And Else
    And set 'testUservariable' to 32
    And end if else

    When I start the program
    Then the variable 'testUservariable' should be equal 32





