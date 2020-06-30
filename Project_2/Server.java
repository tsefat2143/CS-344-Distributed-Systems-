import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {

    private int portNumber;
    private ServerSocket server_sock;

    private int numCustomers;
    private int storeCapacity;
    private int numRegisters;

    private int numGroups;

    private Manager manager;
    private Employee employee;

    private Notifier managerNotifier;
    private Notifier employeeNotifier;

    private WaitingQueue outsideLine;
    private WaitingQueue groupLine;
    private WaitingQueue cashLine;
    private WaitingQueue parkingLot;

    Vector<Integer> registers;

    Vector<Customer> customers;

    public Server(int portNumber, int numCustomers) {
        this.portNumber = portNumber;
        this.numCustomers = numCustomers;
        this.storeCapacity = 6;
        this.numRegisters = 4;

        this.numGroups = (numCustomers + storeCapacity - 1) / storeCapacity; // ceiling of customers/capacity to get
                                                                             // number of groups;

        this.managerNotifier = new Notifier();
        this.employeeNotifier = new Notifier();

        this.outsideLine = new WaitingQueue(1, numCustomers, managerNotifier, employeeNotifier, 0);
        this.groupLine = new WaitingQueue(storeCapacity, numCustomers, managerNotifier, employeeNotifier, 1);
        this.cashLine = new WaitingQueue(1, numCustomers, managerNotifier, employeeNotifier, 2);
        this.parkingLot = new WaitingQueue(1, numCustomers, managerNotifier, employeeNotifier, 3);

        this.registers = new Vector<Integer>();
        for (int i = 0; i < numRegisters; i++)
            registers.addElement(0);

        this.employee = new Employee(storeCapacity, numRegisters, numCustomers, managerNotifier, employeeNotifier,
                outsideLine, groupLine, cashLine, registers, parkingLot);
        this.manager = new Manager(storeCapacity, numCustomers, numGroups, managerNotifier, employeeNotifier, employee,
                outsideLine, groupLine);

        this.customers = new Vector<Customer>();
        for (int i = 0; i < numCustomers; i++)
            customers.addElement(null);

        System.out.println("Server starting.");
        try {

            server_sock = new ServerSocket(portNumber);
            System.out.println("Listening on port " + portNumber + "...");
            while (true) {
                Socket sock = server_sock.accept();
                new SubServerThread(sock, numCustomers, storeCapacity, numRegisters, numGroups, manager, employee,
                        managerNotifier, employeeNotifier, outsideLine, groupLine, cashLine, parkingLot, registers,
                        customers).start();// more parameters
            }

        } catch (IOException e) {
            System.out.println("Unable to listen to port " + portNumber + ".");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int portNumber = 3000;
        int numCustomers = 20;
        if (args.length >= 1)
            portNumber = Integer.parseInt(args[0]);
        if (args.length >= 2)
            numCustomers = Integer.parseInt(args[1]);
        new Server(portNumber, numCustomers);
    }
}