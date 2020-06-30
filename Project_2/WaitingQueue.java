import java.util.Vector;

public class WaitingQueue{

    public Vector<Object> v;
    public int maxSize;
    public int totalSize;
    public Vector<Vector<Integer>> idList;

    private Vector<Object> waitingModifiers;


    public int type;
    //0 = outside
    //1 = group
    //2 = cash
    //3 = parking lot
    public Notifier managerNotifier;
    public Notifier employeeNotifier;

    private boolean isModifying;


    public WaitingQueue(int maxSize, int totalCustomers, Notifier managerNotifier, Notifier employeeNotifier, int type){
        this.v = new Vector<Object>();
        this.idList = new Vector<Vector<Integer>>();
        this.maxSize = maxSize;
        this.totalSize = totalCustomers;
        
        this.waitingModifiers = new Vector<Object>();
        
        this.managerNotifier = managerNotifier;
        this.employeeNotifier = employeeNotifier;

        this.type = type;

        this.isModifying = false;
    }

    public void addToQueue(int id){
        startModify();
        if (addToPrevious()){
            addToQueueSpaceAvailable(id);
        }
        else{
            addToQueueFull(id);
        }
        //endModify();
    }

    public void addToQueueSpaceAvailable(int id){
        Object convey = v.lastElement();
        synchronized(convey){
            idList.lastElement().addElement(id);
            /*
            if ((type == 0 || type == 1) && idSize()==totalSize) {
                System.out.println(type);
                System.out.println(idSize());
                System.out.println(this.toString());
                managerNotifier.notifyEntity();
            }
            */
            endModify();
            if ((type == 0 || type == 1) && idSize()==totalSize) {
                //System.out.println(type);
                //System.out.println(idSize());
                //System.out.println(this.toString());
                managerNotifier.notifyEntity();
            }
            while (true){
                try { convey.wait(); break; }
                catch (InterruptedException e) { continue; }
            }
        }
    }

    public void addToQueueFull(int id){
        Object convey = new Object();
            synchronized(convey){
                v.addElement(convey);
                idList.addElement(new Vector<Integer>());
                idList.lastElement().addElement(id);
                /*
                if ((type == 0 || type == 1) && idSize() == totalSize) {
                    managerNotifier.notifyEntity();
                }
                if (type == 2 || type == 3){ //(type == 3 && idSize() == totalSize)) {
                    employeeNotifier.notifyEntity();
                }
                */
                endModify();
                if ((type == 0 || type == 1) && idSize() == totalSize) {
                    managerNotifier.notifyEntity();
                }
                if (type == 2 || type == 3){ //(type == 3 && idSize() == totalSize)) {
                    employeeNotifier.notifyEntity();
                }
                while (true){
                    try { convey.wait(); break; }
                    catch (InterruptedException e) { continue; }
                }
            }
    }

    public synchronized void removeFromQueue(){
        startModify();
        if (!v.isEmpty()){
            synchronized (v.elementAt(0)){
                idList.removeElementAt(0);
                v.elementAt(0).notifyAll();
            } 
            v.removeElementAt(0);
        }
        endModify();
    }

    public synchronized void removeFromQueueAt(int i){
        startModify();
        if (!v.isEmpty()){
            synchronized (v.elementAt(i)){
                idList.removeElementAt(i);
                v.elementAt(i).notifyAll();
            } 
            v.removeElementAt(i);
        }
        endModify();
    }

    public synchronized boolean addToPrevious(){
        return !v.isEmpty() && idList.lastElement().size() < maxSize;
    }

    public void startModify() {
        //System.out.println("START MODIFY");
        Object convey = new Object();
        synchronized (convey) {
        if (cannotModifyNow(convey))
            while (true){ // wait to be notified, not interrupted
                try { convey.wait(); break; }
                catch (InterruptedException e) { continue; }
            }
        }
    }

    private synchronized boolean cannotModifyNow(Object convey) {
        //System.out.println("TESTING");
        boolean status;
        if (isModifying) {
            waitingModifiers.addElement(convey);
            status = true;
        } else {
            //System.out.println("CAN MODIFY");
            isModifying = true;
            status = false;
        }
        return status;
    }

    public synchronized void endModify() {
        //System.out.println("END MODIFY");
        isModifying = false;
        if (waitingModifiers.size() > 0) {
            synchronized (waitingModifiers.elementAt(0)) {
                waitingModifiers.elementAt(0).notify();
            }
            waitingModifiers.removeElementAt(0);
            isModifying = true;
      }
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
            s += idList.elementAt(i).elementAt(j);
            if (j < idList.elementAt(i).size()-1) s+= ", ";
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