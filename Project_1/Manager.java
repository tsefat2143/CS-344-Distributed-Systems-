public class Manager extends Thread{


    public static long time = System.currentTimeMillis();
    private int groupSize;
    private int numGroupsRemaining;
    private int currGroup;
    private int totalCustomers;

    public Notifier managerNotifier;
    public Notifier employeeNotifier; 

    public Employee employee;

    public WaitingQueue outsideLine;
    public WaitingQueue groupLine;
    


    public Manager(int groupSize, int totalCustomers, int numGroups, Notifier managerNotifier, Notifier employeeNotifier, Employee employee, WaitingQueue outsideLine, WaitingQueue groupLine){
        setName("Manager");
        this.groupSize = groupSize;
        this.totalCustomers = totalCustomers;
        this.numGroupsRemaining = numGroups;
        this.currGroup = 1;

        this.employee = employee;

        this.managerNotifier = managerNotifier;
        this.employeeNotifier = employeeNotifier;

        this.outsideLine = outsideLine;
        this.groupLine = groupLine;
    }

    public void run(){
        msg("Manager at store, waiting to open.");
        //employeeNotifier.notifyEntity();
        
        while (outsideLine.size() < totalCustomers){
            managerNotifier.waitOn();
        }

        openStore();
        
        while (groupLine.idSize() < totalCustomers){
            managerNotifier.waitOn();
        }
        while (true){
            letInGroup();
            if (numGroupsRemaining <= 0) break;
            managerNotifier.waitOn();
        }

        msg("All groups have been let in. Manager is done.");
    }

    public void openStore(){
        msg("Opening store and directing customers to form groups.");
        //System.out.println(outsideLine);
        int sz = outsideLine.size();
        for (int i = 0; i < sz; i++){
            outsideLine.removeFromQueue();
        }

        managerNotifier.waitOn();

    }


    public void letInGroup(){
        if (!groupLine.isEmpty()){
            //msg("Manager letting in group " + currGroup + " consisting of customers " + groupLine.toStringID(0));
            msg("Manager letting in group " + currGroup);
            numGroupsRemaining--;
            currGroup++;
            employee.currGroupRemaining = groupLine.idList.elementAt(0).size();
            groupLine.removeFromQueue();
        }

    }


    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m); 
    }



}