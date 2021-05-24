import java.util.*;
import java.util.concurrent.Semaphore;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Passenger extends Thread {

    protected int id;
    protected Queue<Car> cars; // kachow
    protected Queue<Car> doneCars; // kachow
    private int riding;
    private Random rand = new Random();
    private Semaphore mutex1 = new Semaphore(1);
    private Semaphore mutex2 = new Semaphore(1);

    @Override
    public void run() {

        try {
            while (true) {
                if (riding == -1) {
                    if (cars.peek() != null || doneCars.peek() != null) {
                        wander();
                        board();
                        Thread.sleep(10 * 1000);
                        unboard();
                    } else {
                        break;
                    }
                    Thread.sleep(3 * 1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Passenger(int id, Queue<Car> cars, Queue<Car> doneCars, Semaphore mutex1, Semaphore mutex2) {
        this.id = id;
        this.cars = cars;
        this.doneCars = doneCars;
        this.riding = -1;
        this.mutex1 = mutex1;
        this.mutex2 = mutex2;
    }

    private String getTimeStamp() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dateTime.format(formatter);
    }

    private void wander() {
        try {
            System.out.println(getTimeStamp() + ": Passenger " + id + " is wandering.");
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
            mutex1.acquire();
            Car car = cars.peek();
            if (car != null && car.getStatus().equalsIgnoreCase("load") && car.notFull()) {
                riding = car.carId();
                car.loadPassenger();
                System.out.println(getTimeStamp() + ": Passenger " + id + " boarded Car " + riding + ".");

                if (!car.notFull()) {
                    Thread.sleep(1 * 1000); // sync to Car run
                    doneCars.add(cars.remove());
                    if (cars.peek() != null) // if not last car
                        cars.peek().setStatus("load");
                }
            }
            mutex1.release();

            while (riding != -1 && (doneCars.peek() == null || doneCars.peek().carId() != riding)) { // waiting for last
                                                                                                     // passenger to
                                                                                                     // move car to
                                                                                                     // donecars
                Thread.sleep(1000);

                // for program end
                if (doneCars.peek() == null && cars.peek() == null) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unboard() {
        try {
            mutex2.tryAcquire();
            while (true) {
                Thread.sleep(1000);

                Car car = doneCars.peek();
                if (car != null && riding == car.carId()) {

                    while (!car.getStatus().equalsIgnoreCase("unload")) {
                        Thread.sleep(1000);
                    }
                    
                    if (car.carId() == riding) {
                        car.unloadPassenger();
                        System.out.println(getTimeStamp() + ": Passenger " + id + " unboarded Car " + car.carId() + ".");
                        riding = -1;

                        if (doneCars.peek() != null && car.getPassenger() == 0) {
                            doneCars.remove();
                        }
                        mutex2.release();
                        break;
                    }
                }
                // for program end
                if (car == null && cars.peek() == null) {
                    mutex2.release();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}