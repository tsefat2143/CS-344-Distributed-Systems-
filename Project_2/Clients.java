public class Clients{


    public static void main(String[] args){
        String ip = "127.0.0.1";
        int portNumber = 3000;
        int numCustomers = 20;

        int numEmployeeMethods = 4;
        int numManagerMethods = 4;
        int numCustomerMethods = 5;

        if (args.length >= 1) ip = args[0];
        if (args.length >= 2) portNumber = Integer.parseInt(args[1]); 
        if (args.length >= 3) numCustomers = Integer.parseInt(args[2]);


        //Employee must go first since Manager constructor requires an Employee
        EmployeeClient e_client = new EmployeeClient(ip,portNumber,numEmployeeMethods);
        ManagerClient m_client = new ManagerClient(ip,portNumber,numManagerMethods);

        e_client.start();
        m_client.start();

        for (int i = 1; i <= numCustomers; i++){
            CustomerClient c_client = new CustomerClient(ip,portNumber,numCustomerMethods,i);
            c_client.start();
        }

    }


}
