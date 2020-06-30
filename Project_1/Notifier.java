public class Notifier{

    public Object o;

    public Notifier(){
        o = new Object();
    }

    public void waitOn(){
        synchronized(o){
            while (true){
                try { o.wait(); break; }
                catch (InterruptedException e) { continue; }
            }
        }
    }

    public synchronized void notifyEntity() {
        synchronized(o){
            o.notify();
        }
    }
        
}