import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Car extends Thread {

    protected int id;
    protected int capacity;
    protected int passengers;
    protected String status;

    @Override
    public void run() {

        try {
            while (true) {
                Thread.sleep(1 * 1000);
                if (!notFull()) {
                    setStatus("run");
                    Thread.sleep(10 * 1000); // 10 secs
                    setStatus("unload");

                    while (passengers > 0) {
                        // wait for passengers to unboard
                        Thread.sleep(50);
                    }
                    System.out.println(getTimeStamp() + ": All ashore from car " + id + ".");

                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Car(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.passengers = 0;

        if (id == 0) {
            setStatus("load");
        }
    }

    private String getTimeStamp() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dateTime.format(formatter);
    }

    public int carId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
        if (status.equalsIgnoreCase("run")) {
            System.out.println(getTimeStamp() + ": All aboard car " + id + ".");
            System.out.println(getTimeStamp() + ": Car " + id + " is " + status + "ning.");
        } else {
            System.out.println(getTimeStamp() + ": Car " + id + " is " + status + "ing.");
        }
    }

    public String getStatus() {
        return status;
    }

    public int getPassenger() {
        return passengers;
    }

    public boolean notFull() {
        return passengers < capacity;
    }

    public void loadPassenger() {
        passengers++;
    }

    public void unloadPassenger() {
        passengers--;
    }
}