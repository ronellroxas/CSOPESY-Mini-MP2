import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Semaphore;

class Main {
    public static void main(String[] args) {
        Semaphore mutex1 = new Semaphore(1);
        Semaphore mutex2 = new Semaphore(1);

        Scanner sc = new Scanner(System.in);
        int n, c, m;

        System.out.print("Number of Passengers: ");
        n = sc.nextInt();
        System.out.print("Capacity of a Car: ");
        c = sc.nextInt();
        System.out.print("Number of Cars: ");
        m = sc.nextInt();

        // cars and passengers
        Queue<Car> cars = new LinkedList<Car>();
        Queue<Car> doneCars = new LinkedList<Car>();
        Passenger[] passenger = new Passenger[n];

        if (c <= n) {
            // initialize cars and run kachow
            for (int i = 0; i < m; i++) {
                Car car = new Car(i, c);
                car.start();
                cars.add(car);
            }

            // initialize passengers
            for (int i = 0; i < n; i++) {
                passenger[i] = new Passenger(i, cars, doneCars, mutex1, mutex2);
                passenger[i].start();
            }
            try {
                passenger[(n - 1)].join();
                LocalDateTime dateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                System.out.println(dateTime.format(formatter) + ": All rides completed.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Invalid input.");
        }

        sc.close();
    }
}