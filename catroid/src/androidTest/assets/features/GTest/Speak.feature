Feature: Speak Sound



  Background:
    Given I have a Program
    And this program has an Object 'Object'

  Scenario: Speak Sound

    Given 'Object' has a Start script
    #And this script has a start sound1 'Sound'
    And this script has a speak 'Sound'

    When I start the program
    And  I wait until the program has stopped
    Then the name of the speak should be equal to 'Sound'