import java.util.*;

class Passenger extends Thread {

    protected int id;
    protected Queue<Car> cars; //kachow
    protected Queue<Car> doneCars; //kachow
    private int riding;
    private Random rand = new Random();

    @Override
    public void run() {
        while(true) {
            if(riding == -1) {
                wander();
                board();
                unboard();

                if(cars.peek() == null && doneCars.peek() == null) {
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
            Thread.sleep((rand.nextInt(10) + 2)* 1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * board car
     */
    private void board() {
        Car car = cars.peek();
        if(car.getStatus().equalsIgnoreCase("load") && car.notFull()) {
            riding = car.carId();
            car.loadPassenger();
            System.out.println("Passenger " + id + " boarded Car " + riding + ".");

            if(!car.notFull()) {
                doneCars.add(cars.remove());
                cars.peek().setStatus("load");
            }
        }
    }

    private void unboard() {
        if(doneCars.peek() != null) {
            Car car = doneCars.peek();
            while(!car.getStatus().equalsIgnoreCase("unload")) {
                //
            }

            if(car.carId() == riding) {
                car.unloadPassenger();
                System.out.println("Passenger " + id + " unboarded Car " + car.carId() + ".");
                riding = -1;
                

                if(car.getPassenger() == 0)
                    doneCars.remove();
                
            }
        }
    }
}