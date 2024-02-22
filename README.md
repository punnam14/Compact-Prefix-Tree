Implemented a Dictionary ADT with extra functionality of being able to find the closest entries in the dictionary to the given invalid word using a compact prefix tree. <br> <br>

![Alt text for the image](https://raw.githubusercontent.com/punnam14/Compact-Prefix-Tree/main/Tree%20Images/Screenshot%202024-02-21%20at%209.22.22%20PM.png) <br><br>
![Alt text for the image](https://raw.githubusercontent.com/punnam14/Compact-Prefix-Tree/main/Tree%20Images/Screenshot%202024-02-21%20at%209.22.42%20PM.png) <br> <br>

the word passed was camera, camera does not exist in our tree check method will return false and 
the code goes to else loop in suggest

so the suggestRec function will go to the current node with longest common prefix
after that it will call the child function to find children of that node 

After this since array size was still not fulfilled i.e. c has only 5 words we still need 15
also, since we need to avoid the tree traversing into nodes it has already traversed 
we set previous node to node after the recursive call to child 

once it comes to parent who made the call, it will go to next node i.e a - it will keep going forward
at this point previous node is maintained at c, so it will skip the c node since it is already traversed
At any point if array gets full, it will not include more words
