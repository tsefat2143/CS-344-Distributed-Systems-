import java.util.Random;
import java.util.Vector;

public class Customer extends Thread{

    public static long time = System.currentTimeMillis();
    private int id; 
    private String name;
    private int group_id;

    private int numCustomers;
    private int storeCapacity;
    private int numRegisters;
    private int numGroups;

    public Vector<Integer> registers;
    
    public WaitingQueue outsideLine;
    public WaitingQueue groupLine;
    public WaitingQueue cashLine;
    public WaitingQueue parkingLot;

    public Notifier managerNotifier;
    public Notifier employeeNotifier;

    private static Random rand;



    public Customer(int id, int numCustomers, int storeCapacity, int numRegisters, int numGroups, WaitingQueue outsideLine,WaitingQueue groupLine,WaitingQueue cashLine, WaitingQueue parkingLot, Vector<Integer> registers, Notifier managerNotifier,Notifier employeeNotifier){
        this.id = id;
        this.setName(String.format("Customer %2d",id));

        this.numCustomers = numCustomers;
        this.storeCapacity = storeCapacity;
        this.numRegisters = numRegisters;
        this.numGroups = numGroups;

        this.outsideLine = outsideLine;
        this.groupLine = groupLine;
        this.cashLine = cashLine;
        this.parkingLot = parkingLot;
        this.registers = registers;


        this.rand = new Random();
        this.managerNotifier = managerNotifier;
        this.employeeNotifier = employeeNotifier;

    }


    public void run(){ 
        arrive();
        formGroup();
        shop();
        pay();
        leave();
    }

    public void arrive(){
        // arrive at store after sleeping
        int sleepTime = rand.nextInt(15000+1);
        try {Thread.sleep(sleepTime);}
        catch (InterruptedException e) {e.printStackTrace();}


        //System.out.println(outsideLine + "id: " + id);
        msg("Arriving at store and waiting in line outside.");
        outsideLine.addToQueue(id);

    }

    public void formGroup(){
        msg("Attempting to form groups.");
        groupLine.addToQueue(id);
 
    }

    public void shop(){
        msg("Shopping for groceries.");
        int sleepTime = rand.nextInt(9000+1);
        try {Thread.sleep(sleepTime);}
        catch (InterruptedException e) {e.printStackTrace();}
        

        msg("Getting in line to pay.");
        
        cashLine.addToQueue(id);
       
        //msg("Off line.");
    }

    public void pay(){
        msg("Paying for groceries."); 
        int sleepTime = rand.nextInt(6000+1);
        try {Thread.sleep(sleepTime);}
        catch (InterruptedException e) {e.printStackTrace();}

        synchronized(employeeNotifier){
        synchronized(registers){
            for (int i = 0; i < registers.size(); i++){
                if (registers.elementAt(i) == id) registers.setElementAt(0,i);
            }
            employeeNotifier.notifyEntity();
        }
        }

        msg("Paid for groceries. Leaving store and heading to parking lot.");

        parkingLot.addToQueue(id);

    }

    public void leave(){
        msg("Leaving the parking lot.");
    }

    
    public void msg(String m) {
        String timeStr = ("["+(System.currentTimeMillis()-time)+"]");
        String pretty = String.format("%-7s %-11s: %s",timeStr,getName(),m);
        //System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m); 
        System.out.println(pretty);
    }

}