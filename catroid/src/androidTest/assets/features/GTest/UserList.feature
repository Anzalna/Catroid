
Feature: UserList


  Background:
    Given I have a Program
    And this program has an Object 'Object'

  Scenario: Userlist for Integer

    Given 'Object' has a Start script
    And this script add 4 to list  'time'


    When I start the program
    And  I wait until the program has stopped
    Then Current list 'time' should be equal to 4


  Scenario: Userlist for RtoL Languages

    Given 'Object' has a Start script
    And this script add variable "پاکستان" to list name 'time' brick


    When I start the program
    And  I wait until the program has stopped
    Then Current list 'time' should be equal to "پاکستان"