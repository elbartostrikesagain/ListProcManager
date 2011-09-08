This was a large hackfest, code is terrible not complete but it works. I'll make a readme if anyone is interseted.

Setup(eclipse)
1. Create a new java project
2. File => Import => File System
3. Click the lightbulb by the build errors and chose the bottom option about fixing build path something something


---Sample config file setup
	//This file is not used, it is just for demo purposes
	//use a double slash to comment in a config file
	//add lists... "List ListName ListPassword"
	List test testPassword
	List test2 test2Password
	//define email that manages the lists
	Email yourEmailHere@gmail.com
	//define the listproc email address
	ListProcEmail listproc@lists.yourServerHere.edu
	//The following will also add uses to test2 automatically
	AlsoAddTo test2 
	
---Known Bugs
	*Removing a user via the gui sends the email but doesn't remove them from the text doc
