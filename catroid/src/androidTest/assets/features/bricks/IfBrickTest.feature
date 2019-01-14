
Feature: conditional statement

  Correct Behavior: The program should execute with forever loop, if statement and if else statement without crashing.

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
    And I wait until the program has stopped
    Then the variable 'testUservariable' should be equal 42

  Scenario: testIfBrick()
    Given 'Object' has a start script
    And if 1 < 2 is true then..Else
    And set 'testUservariable' to 42
    And Else
    And end if else

    When I start the program
    And I wait until the program has stopped
    Then the variable 'testUservariable' should be equal 42

  Scenario: testIfElseBrick()
    Given 'Object' has a start script
    And if 2 < 1 is true then..Else
    And Else
    And set 'testUservariable' to 42
    And end if else

    When I start the program
    And I wait until the program has stopped
    Then the variable 'testUservariable' should be equal 42

  Scenario: testBrickWithValidStringFormula()
    Given 'Object' has a start script
    And if "2.1" is true then..Else
    And set 'testUservariable' to 42
    And Else
    And set 'testUservariable' to 32
    And end if else

    When I start the program
    And I wait until the program has stopped
    Then the variable 'testUservariable' should be equal 42

  Scenario: testBrickWithInValidStringFormula()
    Given 'Object' has a start script
    And if "TRUE" is true then..Else
    #And if "text" is true then..Else
    And set 'testUservariable' to 42
    And Else
    And set 'testUservariable' to 32
    And end if else

    When I start the program
    And I wait until the program has stopped
    Then the variable 'testUservariable' should be equal 0

  Scenario: testNotANumberFormula()
    Given 'Object' has a start script
    And if "sqrt(-1)" is true then..Else
    And set 'testUservariable' to 42
    And Else
    And set 'testUservariable' to 32
    And end if else

    When I start the program
    And I wait until the program has stopped
    Then the variable 'testUservariable' should be equal 0

  Scenario: testIfBrickwithNotANumber()
    Given 'Object' has a start script
    And if "sqrt(-1)" < 2 is true then..Else
    And set 'testUservariable' to 42
    And Else
    And set 'testUservariable' to 32
    And end if else

    When I start the program
    And I wait until the program has stopped
    Then the variable 'testUservariable' should be equal 32

  Scenario: testIfBrickwithValidString()
    Given 'Object' has a start script
    And if "01" < 2 is true then..Else
    And set 'testUservariable' to 42
    And Else
    And set 'testUservariable' to 32
    And end if else

    When I start the program
    And I wait until the program has stopped
    Then the variable 'testUservariable' should be equal 42