# FIRST Steamworks Simulator  
  
### What is it?  
A simple simulator of the FRC 2017 game (Steamworks) that communicates to the server throught sockets. This simulates the basic Steamworks game but attempts to remove as many details as possible. For instance, instead of simulating pieces of the robot, the robot just tells the server how long it takes to shoot and how accurate it is.  
  
### Why ignore so many details?  
As you can see, many pieces that are often considered an integral part of FRC have been purposely left out. This is because this simulator was built to train AI on. Naturally, you can't train untrustworthy AI on real robots for fear of breaking them, so this is the next best thing. The AI was also built to be purely strategic (go here and do that), and it doesn't care about "silly" details like moving its appendages or exactly how to pick up a ball. The AI only commands the bots with things like shoot and pickup so it makeds sense that the simulator should also only know about those details.  
  
### To-Do  
 - Update for the 2017 game, Steamworks (List in order of priority)
   - Place gear on Airship
   - Climb
   - Load gear from Human Player Station
   - Load balls from Human Player Station
   - Highgoal on the Boiler
   - Lowgoal on the Boiler
   - Intake balls from the ground
   - Empty Hopper
   - Intake gears from the ground
 - Create a wiki documenting how to actually write code for this simulator

### Bugs
 - Cannot move in amounts smaller than about 100 px/s
 - Can change action during defense cross and get stuck in the defense
 - Simulator stalls if the wrong type is sent over the socket