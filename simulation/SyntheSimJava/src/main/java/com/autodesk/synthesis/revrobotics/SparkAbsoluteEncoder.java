package com.autodesk.synthesis.revrobotics;

import com.autodesk.synthesis.CANEncoder;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.REVLibError;

/**
 * SparkAbsoluteEncoder wrapper to add proper WPILib HALSim support.
 */
public class SparkAbsoluteEncoder implements AbsoluteEncoder {
    private CANEncoder simEncoder;
    private com.revrobotics.spark.SparkAbsoluteEncoder realEncoder;

    /*
     * A SparkAbsoluteEncoder class that returns the motors position and velocity from the simulated motor in fission, rather than the actual motor.
     * All other parameters are returned from the real motor, which likely won't exist, not sure what it does then but we'll just call it UB.
     */
    public SparkAbsoluteEncoder(com.revrobotics.spark.SparkAbsoluteEncoder realEncoder, CANEncoder simEncoder) {
        this.realEncoder = realEncoder;
        this.simEncoder = simEncoder;
    }

    /**
     * Gets the position of the simulated motor.
     * This returns the native units of 'rotations' by default, and can be changed by a scale factor using setPositionConversionFactor().
     *
     * @return Number of rotations of the motor
     */
    public double getPosition() {
        return this.simEncoder.getPosition();
    }

    /**
     * Gets the velocity of the simulated motor. This returns the native units of 'rotations per second' by default, and can be changed by a scale factor using setVelocityConversionFactor().
     *
     * @return Number of rotations per second of the motor
     */
    public double getVelocity() {
        return this.simEncoder.getVelocity();
    }
}
