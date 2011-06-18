This was a large hackfest, code is terrible not complete but it works. I'll make a readme if anyone is interseted.

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
