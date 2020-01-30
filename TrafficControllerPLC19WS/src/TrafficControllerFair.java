import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficControllerFair implements TrafficController {
    private TrafficRegistrar registrar;

    private Lock lock = new ReentrantLock(true);
    private Condition bridgeFree = lock.newCondition();

    private boolean free = true;

    public TrafficControllerFair(TrafficRegistrar r) {
        this.registrar = r;
    }

    public void enterRight(Vehicle v){
        lock.lock();
        try {
            while (!free) bridgeFree.await();
        }catch (InterruptedException e) {}

        free = false;
        registrar.registerRight(v);
    }

    public void enterLeft(Vehicle v) {
        lock.lock();
        try {
            while (!free) bridgeFree.await();
        }catch (InterruptedException e) {}

        free = false;
        registrar.registerLeft(v);
    }

    public void leaveLeft(Vehicle v) {
        lock.unlock();
        free = true;
        registrar.deregisterLeft(v);
    }

    public void leaveRight(Vehicle v) {
        lock.unlock();
        free = true;
        registrar.deregisterRight(v);
    }
}
