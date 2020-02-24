public class Braker extends Wheel {
    private static final double DEFAULT_MAX_BRAKING_FORCE = 5;

    private double maxBrakingForce;
    private boolean isActive = false;

    public Braker(double maxBrakingForce) {
        super(0, 0);
        this.maxBrakingForce = maxBrakingForce;
    }

    public Braker() {
        this(DEFAULT_MAX_BRAKING_FORCE);
    }

    public void activate(double valueFromZeroToOne) {
        if (valueFromZeroToOne < 0) {
            valueFromZeroToOne = 0;
        } else if (valueFromZeroToOne > 1) {
            valueFromZeroToOne = 1;
        }
        setBrackingForce(maxBrakingForce * valueFromZeroToOne);
        isActive = true;
    }

    public void deactivate() {
        setBrackingForce(0);
        isActive = false;
    }
}
