import java.util.Vector;

public class WaitingQueue{

    public Vector<Object> v;
    public int maxSize;
    public int totalSize;
    public Vector<Vector<Integer>> idList;


    public int type;
    //0 = outside
    //1 = group
    //2 = cash
    //3 = parking lot
    public Notifier managerNotifier;
    public Notifier employeeNotifier;


    public WaitingQueue(int maxSize, int totalCustomers, Notifier managerNotifier, Notifier employeeNotifier, int type){
        this.v = new Vector<Object>();
        this.idList = new Vector<Vector<Integer>>();
        this.maxSize = maxSize;
        this.totalSize = totalCustomers;
        
        this.managerNotifier = managerNotifier;
        this.employeeNotifier = employeeNotifier;

        this.type = type;
    }

    public void addToQueue(int id){

        Object convey;
        if (addToPrevious()){
            convey = v.lastElement();
            synchronized(convey){
                idList.lastElement().addElement(id);
                if ((type == 0 || type == 1) && idSize()==totalSize) {
                    managerNotifier.notifyEntity();
                }
                while (true){
                    try { convey.wait(); break; }
                    catch (InterruptedException e) { continue; }
                }
            }
        }
        else{
            convey = new Object();
            synchronized(convey){
                v.addElement(convey);
                idList.addElement(new Vector<Integer>());
                idList.lastElement().addElement(id);
                if ((type == 0 || type == 1) && idSize() == totalSize) {
                    managerNotifier.notifyEntity();
                }
                if (type == 2) {
                    employeeNotifier.notifyEntity();
                }
                if (type == 3){
                    employeeNotifier.notifyEntity();
                }
                while (true){
                    try { convey.wait(); break; }
                    catch (InterruptedException e) { continue; }
                }
            }
        }
        //}
    }

    public synchronized void removeFromQueue(){
        if (!v.isEmpty()){
            synchronized (v.elementAt(0)){
                idList.removeElementAt(0);
                v.elementAt(0).notifyAll();
            } 
            v.removeElementAt(0);
           

        }
    }

    public synchronized void removeFromQueueAt(int i){
        if (!v.isEmpty()){
            synchronized (v.elementAt(i)){
                idList.removeElementAt(i);
                v.elementAt(i).notifyAll();
            } 
            v.removeElementAt(i);
           

        }
    }
    public synchronized boolean addToPrevious(){
        return !v.isEmpty() && idList.lastElement().size() < maxSize;
    }

    public synchronized boolean isEmpty(){
        return v.isEmpty();
    }

    public synchronized int size(){
        return v.size();
    }

    public synchronized int idSize(){
        int sz = 0;
        for (int i = 0; i < idList.size(); i++){
            sz += idList.elementAt(i).size();
        }
        return sz;
    }

    public synchronized String toStringID(int i){
        String s = "";
        for (int j = 0; j < idList.elementAt(i).size(); j++){
            s += idList.elementAt(i).elementAt(j) + " ";
        }
        return s;
    }

    public synchronized String toString(){
        String s = "";
        for (int i = 0; i < idList.size(); i++){
            s += toStringID(i) + "    ";
        }
        return s;
    }


    




    
}