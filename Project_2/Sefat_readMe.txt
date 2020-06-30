=================
Overview of Files
=================

Server Side Files
    Project 1 Files
      Customer.java, Manager.java, Employee.java, WaitingQueue.java, Notifier. java
      These files from Project 1 form the bulk of the server side code and do most
      of the actual heavy lifting and logic.
	
    SubServerThread.java
      Thread which handles each incoming client connection and calls the 
      appropriate method depending on the type of thread and current state
      of the client thread. Use a switch-case structure to find the appropriate  
      method to call for a given thread type. Server must be closed manually to end program with Ctrl C.

    Server.java
      Main server file. Responsible for listening for connections and then 
      spawning a SubServerThread for each client thread connection. The 
      SubServerThread will specialize depending on the type of the client thread
      and do most of the actual work to call the appropriate methods implemented
      in the Project 1 files. Server must be closed manually to end program with Ctrl C.

    
Client Side Files
    CustomerClient.java
      Implements the client-side customer thread code (for a single customer).
      Sends message to server encoded as "C[N][ID...]", where the first character 
      'C' denotes that it is a customer, the second character [N] is a number
      representing the current method to call. The remaining characters [ID...] 
      represent the Customer Thread ID as a number to allow unique identification
      of a customer.
      
    
    ManagerClient.java
      Implements the client-side manager thread. Sends message to server encoded
      as "M[N]" where the character 'M' stands for Manager and [N] is the number
      representing the current method to call.
    
    EmployeeClient.java
      Implements the client-side employee thread. Sends message to server encoded
      as "E[N]" where the character 'E' stands for Manager and [N] is the number
      representing the current method to call.
    
    Clients.java
      Main client program. Creates and starts client threads for the employee client   
      and manager client and creates customer client threads based on numCustomers 
      parameter with default value 20.

======================
Instructions
======================

To compile server:
    javac Server.java
To run server:
    java Server (arg1) (arg2)
    Optional arg1 -- the port number for the server to listen on.     
                     Default: 3000
    Optional arg2 -- the total number of customers in the simulation. 
                     Default: 20

To compile client:
    javac Clients.java
To run client:
    java Client (arg1) (arg2) (arg3)
    Optional arg1 -- string of IP address of server for client to connect to.              
                     Default: 127.0.0.1 (localhost)
    Optional arg2 -- port number of server to connect to.
                     Default: 3000
    Optional arg3 -- the total number of customer client threads to create.
                     Default: 20

Port number and number of customers should agree between server and client. Server process
must be started before client process is started.

