import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class SubServerThread extends Thread {

    private Socket sock;
    private String threadType;

    public int id;

    public Vector<Customer> customers;

    private int numCustomers;
    private int storeCapacity;
    private int numRegisters;

    private int numGroups;

    private Manager m;
    private Employee e;

    private Notifier managerNotifier;
    private Notifier employeeNotifier;

    private WaitingQueue outsideLine;
    private WaitingQueue groupLine;
    private WaitingQueue cashLine;
    private WaitingQueue parkingLot;

    Vector<Integer> registers;

    public SubServerThread(Socket sock, int numCustomers, int storeCapacity, int numRegisters, int numGroups,
            Manager manager, Employee employee, Notifier managerNotifier, Notifier employeeNotifier,
            WaitingQueue outsideLine, WaitingQueue groupLine, WaitingQueue cashLine, WaitingQueue parkingLot,
            Vector<Integer> registers, Vector<Customer> customers) {
        this.sock = sock;

        this.id = -1;
        this.customers = customers;

        this.numCustomers = numCustomers;
        this.storeCapacity = storeCapacity;
        this.numRegisters = numRegisters;
        this.numGroups = numGroups;

        this.m = manager;
        this.e = employee;

        this.managerNotifier = managerNotifier;
        this.employeeNotifier = employeeNotifier;

        this.outsideLine = outsideLine;
        this.groupLine = groupLine;
        this.cashLine = cashLine;
        this.parkingLot = parkingLot;
        this.registers = registers;

    }

    public void run() {

        try {
            PrintWriter pw = new PrintWriter(sock.getOutputStream(), true);
            BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            String input;
            while ((input = br.readLine()) != null) {
                char type = input.charAt(0);
                int methodNumber = Character.getNumericValue(input.charAt(1));
                int thread_id = -1;
                if (type == 'C')
                    thread_id = Integer.parseInt(input.substring(2));
                if (methodNumber == 1) {
                    if (type == 'C') {
                        threadType = "CUSTOMER";
                        this.id = thread_id;
                        Customer c = new Customer(id, numCustomers, storeCapacity, numRegisters, numGroups, outsideLine,
                                groupLine, cashLine, parkingLot, registers, managerNotifier, employeeNotifier);
                        customers.setElementAt(c, id - 1);
                    } else if (type == 'M')
                        threadType = "MANAGER";
                    else { // type = 'E'
                        threadType = "EMPLOYEE";
                    }
                }
                runMethod(methodNumber, id, pw);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runMethod(int methodNumber, int id, PrintWriter pw) {
        if (threadType.equals("CUSTOMER")) {
            Customer c = customers.elementAt(id - 1);
            String reply = String.format("Server executes method %d for Customer %2d.", methodNumber, id);
            switch (methodNumber) {
                case 1:
                    c.arrive();
                    pw.println(reply);
                    break;
                case 2:
                    c.formGroup();
                    pw.println(reply);
                    break;
                case 3:
                    c.shop();
                    pw.println(reply);
                    break;
                case 4:
                    c.pay();
                    pw.println(reply);
                    break;
                case 5:
                    c.leave();
                    pw.println(reply);
                    break;
            }
        } else if (threadType.equals("MANAGER")) {
            String reply = String.format("Server executes method %d for Manager.", methodNumber);
            switch (methodNumber) {
                case 1:
                    m.arrive();
                    pw.println(reply);
                    break;
                case 2:
                    m.openStore();
                    pw.println(reply);
                    break;
                case 3:
                    m.letInGroups();
                    pw.println(reply);
                    break;
                case 4:
                    m.finish();
                    pw.println(reply);
                    break;

            }
        } else { // threadType == EMPLOYEE
            String reply = String.format("Server executes method %d for Employee.", methodNumber);
            switch (methodNumber) {
                case 1:
                    e.arrive();
                    pw.println(reply);
                    break;
                case 2:
                    e.directCustomer();
                    pw.println(reply);
                    break;
                case 3:
                    e.closeStore();
                    pw.println(reply);
                    break;
                case 4:
                    e.leave();
                    pw.println(reply);
                    break;
            }
        }
    }

}