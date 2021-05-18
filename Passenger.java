import java.util.*;
import java.util.concurrent.Semaphore;

class Passenger extends Thread {

    protected int id;
    protected Queue<Car> cars; // kachow
    protected Queue<Car> doneCars; // kachow
    private int riding;
    private Random rand = new Random();
    private Semaphore mutex = new Semaphore(1);

    @Override
    public void run() {
        while (true) {
            if (riding == -1) {
                wander();
                board();
                unboard();

                if (cars.peek() == null && doneCars.peek() == null) {
                    break;
                }
            }

        }
    }

    public Passenger(int id, Queue<Car> cars, Queue<Car> doneCars) {
        this.id = id;
        this.cars = cars;
        this.doneCars = doneCars;
        this.riding = -1;
    }

    private void wander() {
        try {
            System.out.println("Passenger " + id + " is wandering.");
            Thread.sleep((rand.nextInt(10) + 2) * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * board car
     */
    private void board() {
        try {
            Car car = cars.peek();
            if (car.getStatus().equalsIgnoreCase("load") && car.notFull()) {
                riding = car.carId();
                car.loadPassenger();
                mutex.acquire();
                System.out.println("Passenger " + id + " boarded Car " + riding + ".");

                if (!car.notFull()) {
                    Thread.sleep(1 * 1000); // sync to Car run
                    doneCars.add(cars.remove());
                    cars.peek().setStatus("load");
                }
            }
            Thread.sleep(10 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unboard() {
        try {
            while (true) {
                Car car = doneCars.peek();
                if (car != null && riding != -1) {

                    while (!car.getStatus().equalsIgnoreCase("unload")) {
                        //
                    }

                    if (car.carId() == riding) {
                        car.unloadPassenger();
                        mutex.release();
                        System.out.println("Passenger " + id + " unboarded Car " + car.carId() + ".");
                        riding = -1;

                        if (car.getPassenger() == 0)
                            doneCars.remove();

                    }
                    if (riding == -1) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}