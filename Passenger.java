import java.util.*;
import java.util.concurrent.Semaphore;

class Passenger extends Thread {

    protected int id;
    protected Queue<Car> cars; // kachow
    protected Queue<Car> doneCars; // kachow
    private int riding;
    private Random rand = new Random();
    private Semaphore mutexload;
    private Semaphore mutexunload;

    @Override
    public void run() {
        while (true) {
        	if (cars.size() == 0 && doneCars.size() == 0 ) {
                    break;
            }

            if (riding == -1) {
                wander();
                if (cars.size() == 0 && doneCars.size() == 0 ) {
                    break;
            	}
                board();
                unboard();
            }

            if (cars.size() == 0 && doneCars.size() == 0 ) {
                    break;
            }
        }
    }

    public Passenger(int id, Queue<Car> cars, Queue<Car> doneCars) {
        this.id = id;
        this.cars = cars;
        this.doneCars = doneCars;
        this.riding = -1;
        this.mutexload = new Semaphore(1);
        this.mutexunload = new Semaphore(1);
    }

    private void wander() {
        try {
            System.out.println("Passenger " + id + " is wandering.");
            Thread.sleep((rand.nextInt(5)) * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * board car
     */
    private void board() {
    	//if (cars.size() > 0)
        try {
        	while (!cars.peek().notFull()) {
        		//
        	}
        	mutexunload.acquire();
        	Car car = cars.peek();
            if (car.getStatus().equalsIgnoreCase("load")) {
                riding = car.carId();
                car.loadPassenger();
                System.out.println("Passenger " + this.id + " boarded Car " + riding + ".");

                if (!car.notFull()) {
                	System.out.println("car: " + cars + "donecars: " + doneCars);
                    doneCars.add(cars.poll());
                    if(cars.peek() != null) //if not last car
                        cars.peek().setStatus("load");
                }
            }
            Thread.sleep(3*1000);
            mutexunload.release();
        } catch (Exception e) {
        	System.out.println("Error: by " + id);
            e.printStackTrace();
            mutexload.release();
        }
    }

    private void unboard() {
        try {
            while (true) {
            	Car car = null;
            	for (int i = 0; i < doneCars.size(); i++) {
            		car = doneCars.peek();
            		if (car.carId() == riding)
            			break;
            	}

                if (car != null && riding != -1) {

                    while (!car.getStatus().equalsIgnoreCase("unload")) {
                        //
                    }
                    mutexload.acquire();
                    car.unloadPassenger();
                    System.out.println("Passenger " + id + " unboarded Car " + car.carId() + ".");
                    riding = -1;

                    if (car.getPassenger() == 0)
                        doneCars.remove(car);

                    mutexload.release();
                    break;
                }
            }
        } catch (Exception e) {
        	System.out.println("Error: by " + id);
            e.printStackTrace();
            mutexload.release();
        }
    }
}