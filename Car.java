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

                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Car " + id + " finished");
    }

    public Car(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.passengers = 0;

        if (id == 0) {
            setStatus("load");
        }
    }

    public int carId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
        System.out.println("Car " + id + " is " + status + "ing.");
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