Implemented a Dictionary ADT with extra functionality of being able to find the closest entries in the dictionary to the given invalid word using a compact prefix tree.

CompactPrefixTree test = new CompactPrefixTree();

test.add("accer");                ac                
test.add("acrobats");               cer*  
test.add("acro");                   ro*
test.add("actor");                     bats*
test.add("cat");                    tor*
test.add("carts");                ba
test.add("carted");                 ll*
test.add("crispy");                 sket*
test.add("crispyness");           c        
test.add("ball");                   a
test.add("basket");                   rt
test.add("dog");                        ed*
test.add("donkey");                     s*
test.add("egg");                      t*
test.add("egregious");              rispy*
test.add("dogmatic");                 ness*
                                  do
                                    g*
                                      matic*
                                    nkey*
                                  eg
                                    g*
                                    regious*

test.suggest("camera", 20);

THE ARRAY OF RETURNED WORDS :

element added: carted           |
element added: carts            |
element added: cat              |
element added: crispy           |
element added: crispyness      _| c exhausted
element added: accer            |
element added: acro             |
element added: acrobats         | 
element added: actor           _| a exhausted
element added: ball             | 
element added: basket           | b exhausted
element added: dog             _| 
element added: dogmatic         |
element added: donkey           | d exhausted
element added: egg              |
element added: egregious       _| e exhausted and all words in tree exhausted - all words added to array

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