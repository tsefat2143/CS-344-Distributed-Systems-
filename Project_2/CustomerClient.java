import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class CustomerClient extends Thread{

    private int portNumber;
    private String ip;

    private int id;
    private int numMethods;

    
    
    public CustomerClient(String ip,int portNumber,int numMethods, int id){
        this.portNumber = portNumber;
        this.ip = ip;

        this.id = id;
        setName(String.format("Customer %2d",id));

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
                pw.println("C"+ "" + currMethod + id);
                String pretty = String.format("Customer Client %2d sends request for method %d.",id,currMethod);
                System.out.println(pretty);

                //get reply
                String reply = br.readLine();
                System.out.println(getName() + " receives following reply from server: \n\t" + reply);
                
                currMethod++;
            }   
            System.out.println(getName() + " terminates.");
            pw.close();
            br.close();
            sock.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}