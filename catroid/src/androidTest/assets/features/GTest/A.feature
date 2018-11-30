@ignore
Feature: BroadcastAndWait blocking behavior (like in Scratch)

  Background:
    Given I have a Program
    And this program has an Object 'object'

  Scenario: A waiting BroadcastAndWait brick is unblocked when the
  broadcast message is sent again.
   # Given Object 'object' has the following scripts:
      when program started
         broadcast 'Print a after 0.1 seconds, and then b after another
           0.3 seconds' and wait
         print 'c'
      when program started
         wait 0.2 seconds
         broadcast 'Print a after 0.1 seconds, and then b after another
          0.3 seconds'
      when I receive 'Print a after 0.1 seconds, and then b after another
          0.3 seconds'
         wait 0.1 seconds
         print 'a'
         wait 0.3 seconds
         print 'b'

    When I start the program
    And I wait until the program has stopped
    Then I should see the printed output 'acab'






