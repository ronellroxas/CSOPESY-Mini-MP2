class Car extends Thread {

    protected int id;
    protected int capacity;
    protected int passengers;
    protected String status;

    @Override
    public void run() {

        try {
            while(true) {
            	Thread.sleep(3000);
                if(!notFull()) {
                    setStatus("run");
                    Thread.sleep(3*1000);    //10 secs
                    setStatus("unload");

                    while(passengers > 0) {
                        //wait for passengers to unboard
                    }

                    break;
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Car(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.passengers = 0;

        if(id == 0) {
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