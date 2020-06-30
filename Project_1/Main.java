import java.util.Vector;


public class Main{


    public static void main(String[] args){
        int numCustomers = 20;
        int storeCapacity = 6;
        int numRegisters = 4;

        if (args.length >= 1) numCustomers = Integer.parseInt(args[0]);
        
        int numGroups = (numCustomers+storeCapacity-1)/storeCapacity; //ceiling of customers/capacity to get number of groups;

        Notifier managerNotifier = new Notifier();
        Notifier employeeNotifier = new Notifier();

        WaitingQueue outsideLine = new WaitingQueue(1,numCustomers,managerNotifier,employeeNotifier,0);
        WaitingQueue groupLine = new WaitingQueue(storeCapacity,numCustomers,managerNotifier,employeeNotifier,1);
        WaitingQueue cashLine = new WaitingQueue(1,numCustomers,managerNotifier,employeeNotifier,2);
        WaitingQueue parkingLot = new WaitingQueue(1,numCustomers,managerNotifier,employeeNotifier,3);

        Vector<Integer> registers = new Vector<Integer>();
        for (int i = 0; i < numRegisters; i++) registers.addElement(0);


        Employee eThread = new Employee(storeCapacity,numRegisters,numCustomers, managerNotifier, employeeNotifier, outsideLine, groupLine,cashLine,registers,parkingLot);
        Manager mThread = new Manager(storeCapacity,numCustomers,numGroups,managerNotifier,employeeNotifier,eThread,outsideLine,groupLine);

        mThread.start();
        eThread.start();

        for (int i = 1; i <= numCustomers; i++){
            Customer cThread = new Customer(i,numCustomers,storeCapacity,numRegisters,numGroups,outsideLine,groupLine,cashLine,parkingLot,registers,managerNotifier,employeeNotifier);
            cThread.start();
        }


    }

}