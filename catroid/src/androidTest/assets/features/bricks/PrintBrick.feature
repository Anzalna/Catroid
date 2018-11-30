
Feature: Print brick

  A Print brick prints a given text on the screen.

  Background:
    Given I have a program
    And this program has an object 'Object'

  Scenario: A Print brick prints one line

    Given 'Object' has a start script
    And this script has a Print brick with 'Hello, world!'

    When I start the program
    And I wait until the program has stopped
    Then I should see the printed output 'Hello, world!'

  Scenario: A Print brick prints two lines

    Given 'Object' has a start script
    And this script has a Print brick with
    """
    Hello,
    world!
    """

    When I start the program
    And I wait until the program has stopped
    Then I should see the printed output
    """
    Hello,
    world!
    """
