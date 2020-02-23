public class Wheel {
    private static final double DEFAULT_MASS = 20;
    private double BEARING_FRICTION = 0.05;
    private final long TICK_TIME_ms = 20;
    private static final double STOP_THRESHOLD = 0.02;

    private Thread ticker;

    private volatile double currentVelocity;
    private final double mass;
    private volatile double externalForce;
    private volatile double brakeForce;

    public Wheel(double mass, double startVelocity, double startForce) {
        this.mass = mass;
        this.currentVelocity = startVelocity;
        this.externalForce = startForce;
        startTicker();
    }

    public Wheel(double mass, double startVelocity) {
        this(mass, startVelocity, 0);
    }

    public Wheel(double mass) {
        this(mass, 0, 0);
    }

    public Wheel() {
        this(DEFAULT_MASS);
    }

    private void startTicker() {
        ticker = new Thread(new Runnable() {
            @Override
            public void run() {
                long prevTime = System.currentTimeMillis();
                while (true) {
                    if (!Thread.interrupted()) {
                        if (prevTime + TICK_TIME_ms <= System.currentTimeMillis()) {
                            tick();
                            prevTime = System.currentTimeMillis();
                        } else {
                            Thread.yield();
                        }
                    } else return;
                }
            }
        });
        ticker.setDaemon(true);
        ticker.start();
    }

    private void tick() {
        //velocity = oldVelocity + acceleration
        double resistForce = Math.signum(currentVelocity) * -1 * (BEARING_FRICTION + brakeForce);
        double acceleration = (resistForce + externalForce) / mass;
        double velocity = currentVelocity + acceleration * ((double) TICK_TIME_ms / 1000);
        if (Math.abs(velocity) < STOP_THRESHOLD) {
            velocity = 0;
        }
        setCurrentVelocity(velocity);
    }

    private void setCurrentVelocity(double velocity) {
        currentVelocity = velocity;
    }

    public void addForce(double force) {
        System.out.println("addForce(" + force + ")");
        force += externalForce;
        externalForce = force;
    }

    public void addForce(double force, long msTimeout) {
        System.out.println("add force timer");
        new Thread(new Runnable() {
            @Override
            public void run() {
                addForce(force);
                try {
                    Thread.sleep(msTimeout);
                } catch (InterruptedException e) {
                    addForce(-force);
                    return;
                }
                addForce(-force);
            }
        }).start();
    }

    public void brake(double force) {
        force += brakeForce;
        brakeForce = force;
    }

    public Thread brake(double force, long timeout){
        Thread brakeTimeout = new Thread(new Runnable() {
            @Override
            public void run() {

                brake(force);
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {}
                brake(-force);
            }
        });
        brakeTimeout.start();
        return brakeTimeout;
    }

    public Thread brakeUntilStop(double force) {
        Thread brakeUntilStop = new Thread(new Runnable() {
            @Override
            public void run() {
                brake(force);
                while (!isStopped() && !Thread.interrupted()) {
                    Thread.yield();
                }
                brake(-force);
            }
        });
        brakeUntilStop.start();
        return brakeUntilStop;
    }

    public boolean isStopped() {
        return currentVelocity == 0;
    }

    public double getCurrentVelocity() {
        return currentVelocity;
    }
}
