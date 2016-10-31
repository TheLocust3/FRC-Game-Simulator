# FIRST Stronghold Simulator  

As of now, development of this simulator is frozen due to the upcoming 2017 game. A simulator for the 2017 game will be created when the game is announced.
  
### What is it?  
A simple simulator of the FRC 2016 game (Stronghold) that communicates to the server throught sockets. This simulates the basic Stronghold game but attempts to remove as many details as possible. For instance, instead of simulating pieces of the robot, the robot just tells the server how long it takes to shoot and how accurate it is.  
  
### Why ignore so many details?  
As you can see, many pieces that are often considered an integral part of FRC have been purposely left out. This is because this simulator was built to train AI on. Naturally, you can't train untrustworthy AI on real robots for fear of breaking them, so this is the next best thing. The AI was also built to be purely strategic (go here and do that), and it doesn't care about "silly" details like moving its appendages or how to pick up a ball. The AI only commands the bots with things like shoot and pickup so it made sense to make a simulator that also only knows about those details.  
  
### To-Do  
 - Write a better README and push to Github  
 - Use a formula for the success chance of shooting a highgoal
 - Randomize the position of the ball after a missed highgoal shot
   - I don't want to deal with the physics part so just make sure the robot can't learn from the ball's end position
 - Implement climbing the tower

### Bugs
 - Cannot move in amounts smaller than about 100 px/s
 - Can change action during defense cross and get stuck in the defense