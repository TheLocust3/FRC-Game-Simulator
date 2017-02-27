# FIRST Steamworks Simulator [![Build Status](https://travis-ci.org/TheLocust3/FRC-Game-Simulator.svg?branch=master)](https://travis-ci.org/TheLocust3/FRC-Game-Simulator)  
  
### What is it?  
A simple simulator of the FRC 2017 game (Steamworks) that communicates to the server throught sockets. This simulates the basic Steamworks game but attempts to remove as many details as possible. For instance, instead of simulating pieces of the robot, the robot just tells the server how long it takes to shoot and how accurate it is. Currently, the simulator runs on a trust basis where there are very few rules that are actually enforced. This will change in the future, but currently, it isn't very relevant.  
  
### Why ignore so many details?  
As you can see, many pieces that are often considered an integral part of FRC have been purposely left out. This is because this simulator was built to train AI on. Naturally, you can't train untrustworthy AI on real robots for fear of breaking them, so this is the next best thing. The AI was also built to be purely strategic (go here and do that), and it doesn't care about "silly" details like moving its appendages or exactly how to pick up a ball. The AI only commands the bots with things like shoot and pickup so it makeds sense that the simulator should also only know about those details.  
  
### To-Do  
 - Update for the 2017 game, Steamworks (List in order of priority)
   - Intake gears from the ground
   - Limit the number of gears and balls that can be taken from the Loading Station
   - Put a limit on the number of balls that can be put into the robot
 - Better intaking of balls
   - Allow it to be turned off
   - Make sure balls that go through the robot are the only ones that are picked up
   - Allow balls to be pushed around by robots
   - Show number of balls in the robot
 - Manipulators should hold their game object
   - Should have separate action handler
 - Default robot values
 - Unit testing
 - Refactor everything
 - Simulate human player action
 - Create a wiki documenting how to actually write code for this simulator

### Bugs
 - Cannot move in amounts smaller than about 100 px/s
 - Simulator stalls if the wrong type is sent over the socket