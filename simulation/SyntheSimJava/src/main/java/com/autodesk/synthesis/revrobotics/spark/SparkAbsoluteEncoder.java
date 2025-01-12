package com.autodesk.synthesis.revrobotics.spark;

import com.autodesk.synthesis.CANEncoder;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.config.AbsoluteEncoderConfigAccessor;

/**
 * SparkAbsoluteEncoder wrapper to add proper WPILib HALSim support.
 */
public class SparkAbsoluteEncoder implements AbsoluteEncoder {
    private CANEncoder simEncoder;
    private com.revrobotics.spark.SparkAbsoluteEncoder realEncoder;
    private AbsoluteEncoderConfigAccessor m_accessor;

    /*
     * A SparkAbsoluteEncoder class that returns the motors position and velocity from the simulated motor in fission, rather than the actual motor.
     * All other parameters are returned from the real motor, which likely won't exist, not sure what it does then but we'll just call it UB.
     */
    public SparkAbsoluteEncoder(com.revrobotics.spark.SparkAbsoluteEncoder realEncoder, CANEncoder simEncoder, AbsoluteEncoderConfigAccessor accessor) {
        this.realEncoder = realEncoder;
        this.simEncoder = simEncoder;
        m_accessor = accessor;
    }

    @Override
    public double getPosition() {
        return simEncoder.getPosition() * m_accessor.getPositionConversionFactor() * (m_accessor.getInverted() ? -1.0 : 1.0) - m_accessor.getZeroOffset();
    }

    @Override
    public double getVelocity() {
        return simEncoder.getVelocity() * m_accessor.getVelocityConversionFactor() * (m_accessor.getInverted() ? -1.0 : 1.0);
    }
}
