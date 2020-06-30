import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class EmployeeClient extends Thread{

    private int portNumber;
    private String ip;

    private int numMethods;

    
    
    public EmployeeClient(String ip,int portNumber, int numMethods){
        this.portNumber = portNumber;
        this.ip = ip;

        setName("Employee Client");

        this.numMethods = numMethods;

    }

    public void run(){
        try{
            Socket sock = new Socket(ip,portNumber);
            PrintWriter pw = new PrintWriter(sock.getOutputStream(), true);
			BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            int currMethod = 1;
            while (currMethod <= numMethods){
                //send message
                pw.println("E"+ "" + currMethod);
                String pretty = String.format("Employee Client    sends request for method %d.",currMethod);
                System.out.println(pretty);

                //get reply
                String reply = br.readLine();
                System.out.println(getName() + " receives following reply from server: \n\t    " + reply);
                
                currMethod++;
            }
            pw.close();
            br.close();
            sock.close();
            System.out.println(getName() + " has finished");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}