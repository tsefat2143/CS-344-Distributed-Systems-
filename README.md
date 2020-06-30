Project Details

First, management has imposed a strict limit on the number of people that can physically be in
the store at any given time. As the first customers of the day arrive at the store (use
sleep(random_time) to simulate arrival), they wait outside in line for the store to open (each
customer will wait on a different object; enforce a FCFS similar to the implementation used in
rwcv.java ). The manager of the store opens up not long after seeing customers starting to queue
up outside. Once the store’s doors open customers are asked to create groups to make it possible
for them to maintain a distance of 6-feet from one another (group size is determined by
store_capacity. Have all the members of each group block on the same object). It might take a
little time for the customers to group but once the group is created the manager will allow the
members of the first group (use notifyAll()) to enter the store. After the customers in the store
have made their purchases and left, the manager will allow the next group of waiting customers
into the store.

Inside the store, customers take their time picking up groceries (use sleep(random_time)). When
done picking up what they need, they rush to the self-checkout so they can quickly pay and leave.
The customers wait in line until the store employee directs them to a free register. With the best
interest of the customer in mind, management asks the customers to use every other selfcheckout register (number of self-checkout registers is determined by numRegister). When one
customer is done paying, the store employee will ask (notify) the next customer in line to use
one of the free registers (clearly print a message to the screen that states which customer is
sent to which register).

After all customers in the store leave, the manager sends in the next group of shoppers waiting
outside. When the last group of customers enter the store, the manager has finished his duties
for the day (manager thread terminates).
As much as every customer would like to leave after purchasing their groceries, the parking lot is
absolute mayhem! It’s as if the customers have forgotten the rules of the road. Before any of the
customers can leave, an accident occurs in the parking lot obstructing the exit! The customers in
the first group and every group after that must remain (wait) in their cars until help arrives.
Unfortunately for the customers, help arrives just as the last customer of the day is served.
The store employee closes up the store and walks out. One by one, the shoppers leave (will be
notify() ed) and the employee follows along immediately after (customers leave in ascending 
order of their ID: Customer-1 leaves, Customer-2 leaves, Customer-3 leaves,…., Employee leaves;
make sure the threads print a message to indicate that they’ve left e.g. Customer-1: leaves the
parking lot)
