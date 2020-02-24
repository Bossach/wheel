public class Motor extends Wheel {
    private static final double DEFAULT_MAX_TORQUE = 10;

    private double maxTorque;
    private boolean isActive = false;
    private int turnDirection = 1;
    private double currentTorque;

    public Motor(double maxTorque) {
        super();
        this.maxTorque = maxTorque;
    }

    public void activate(double valueFromZeroToOne) {
        if (valueFromZeroToOne < 0) {
            valueFromZeroToOne = 0;
        } else if (valueFromZeroToOne > 1) {
            valueFromZeroToOne = 1;
        }
        setTorque(turnDirection * valueFromZeroToOne * maxTorque);
        isActive = true;
    }

    public void deactivate() {
        setTorque(0);
        isActive = false;
    }

    private void setTorque(double force) {
        currentTorque = force;
    }

    public void switchTurnDirection() {
        turnDirection = -turnDirection;
    }

    public int getTurnDirection() {
        return turnDirection;
    }

    public void setTurnDirection(int direction) {
        if (direction > 0) {
            turnDirection = 1;
        } else if (direction < 0) {
            turnDirection = -1;
        } else {
            turnDirection = 0;
        }
    }
}
