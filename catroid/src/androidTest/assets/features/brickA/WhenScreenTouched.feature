
Feature: When Screen Tap

  Correct behavior: Independently how often the screen is tapped,
  after the last tap, the variable gets increased at the rate of 1 unit per
  second, because only a single thread instance runs at the same time.

  Background:
    Given I have a program
    And this program has an object 'Object'

  Scenario: when screen is touched scripts can run only in one instance

    Given 'Object' has a when screen is touched script

    And forever
    And change 'x' by 1
    And wait 1 seconds

    And forever end

    When I start the program
    And  tap the screen 10 times
    And set 't' to 'x'
    And wait 1 seconds
    Then the variable 'x' - 't' should be less than or equal 2






