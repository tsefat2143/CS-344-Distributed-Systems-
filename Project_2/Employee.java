import java.util.Vector;


public class Employee extends Thread{
    
    public static long time = System.currentTimeMillis();
    public int groupSize;
    public int numRegisters;
    public int totalCustomersRemaining;
    public int currGroupRemaining;
    public int totalCustomers;
    
    public Notifier managerNotifier;
    public Notifier employeeNotifier;

    public WaitingQueue outsideLine;
    public WaitingQueue groupLine;
    public WaitingQueue cashLine;
    public WaitingQueue parkingLot;

    public Vector<Integer> registers;
    //state: 0 emopty, non-zero id = occupied by customer with id

    public Employee(int groupSize, int numRegisters, int totalCustomers, Notifier managerNotifier, Notifier employeeNotifier,WaitingQueue outsideLine, WaitingQueue groupLine, WaitingQueue cashLine, Vector<Integer> registers,WaitingQueue parkingLot){
        setName("Employee");
        this.groupSize = groupSize;
        this.numRegisters = numRegisters;
        this.totalCustomersRemaining = totalCustomers;
        this.currGroupRemaining = totalCustomers;
        this.totalCustomers = totalCustomers;


        this.managerNotifier = managerNotifier;
        this.employeeNotifier = employeeNotifier;
        
        this.outsideLine = outsideLine;
        this.groupLine = groupLine;
        this.cashLine = cashLine;
        this.parkingLot = parkingLot;

        this.registers =registers;

    }


    public void run(){
        arrive();

        directCustomer();

        closeStore();
        leave();

    }


    public void arrive(){
        msg("Employee at store, ready for work.");
        employeeNotifier.waitOn();
    }

    public void directCustomer(){
        while (true){
            //synchronized(employeeNotifier){
            if (totalCustomersRemaining <= 0) break;
            if (currGroupRemaining <= 0){ 
                managerNotifier.notifyEntity();
                employeeNotifier.waitOn();
            }
            synchronized(registers){
            //synchronized(cashLine){ //!!! the fix
                for (int i = 0; i < numRegisters; i++){
                    if (registers.elementAt(i) == 0 && !cashLine.isEmpty()){
                        int customer_id = cashLine.idList.elementAt(0).elementAt(0);
                        msg("Customer " + customer_id + " directed to register " + (i+1) + ".");
                        registers.setElementAt(customer_id,i);
                        cashLine.removeFromQueue();
                        //msg("Employee removed " + customer_id);
                        currGroupRemaining--;
                        totalCustomersRemaining--;

                    }
                }
            //}
            }
            //}
            employeeNotifier.waitOn();
        }
    }

    public void closeStore(){
        msg("All customers have been served. Employee is finished and heading out.");
        
        while (parkingLot.size() < totalCustomers){
            employeeNotifier.waitOn();
        }

        msg("All customers are in the parking lot.");
    }

    public void leave(){
        int sz = parkingLot.size();
        for (int i = 1; i <= sz; i++){
            for (int j = 0; j < parkingLot.size(); j++ ){
                if (parkingLot.idList.elementAt(j).elementAt(0) == i) {
                    parkingLot.removeFromQueueAt(j);
                }
            }
        }

        msg("Leaving the parking lot.");
    }




    public void msg(String m) {
        String timeStr = ("["+(System.currentTimeMillis()-time)+"]");
        String pretty = String.format("%-7s %-11s: %s",timeStr,getName(),m);
        //System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m); 
        System.out.println(pretty);
    }


}