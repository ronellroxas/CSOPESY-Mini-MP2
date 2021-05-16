import java.util.*;

class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n, c, m;
        
        System.out.print("Number of Passengers: ");
        n = sc.nextInt();
        System.out.print("Capacity of a Car: ");
        c = sc.nextInt();
        System.out.print("Number of Cars: ");
        m = sc.nextInt();
        

        //cars and passengers
        Queue<Car> cars = new LinkedList<Car>();
        Queue<Car> doneCars = new LinkedList<Car>();
        Passenger[] passenger = new Passenger[n];

        if(c < n) {
            //initialize cars and run kachow
            for(int i = 0; i < m; i++) {
                Car car = new Car(i, c);
                car.start();
                cars.add(car);
            } 

            //initialize passengers
            for(int i = 0; i < n; i++) { 
                passenger[i] = new Passenger(i, cars, doneCars);
                passenger[i].start();
            }


            
        }
        else {
            System.out.println("Invalid input.");
        }

        sc.close();
    }
}