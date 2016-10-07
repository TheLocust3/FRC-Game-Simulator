# FIRST Stronghold Simulator  
  
### What is it?  
A simple simulator that communicates to the server throught sockets. This simulates the basic Stronghold game but attempts to keep it as simple as possible. For instance, instead of simulating pieces of the robot, the robot just tells the server how long it takes to shoot and how accurate it is.  
  
### Why?  
As you can see, many pieces that are often considered an integral part of FRC have been purposely left out. This is because this simulator was built to train AI on. Naturally, you can't train untrustworthy AI on real robots for fear of breaking them, so this is the next best thing. The AI was also built to be a top down bot, that doesn't care about "silly" details like moving its appendages. The AI looks only commands the bots with things like shoot and pickup so it made sense to make a simulator that also doesn't care about those details.  
  
### To-Do  
 - Add physics to the ball  
 - Add physics to robot  
 - Learn jbox2d  
 - Create a README and push to Github  
 - Give robots controls  
   - Add actions to robot class  