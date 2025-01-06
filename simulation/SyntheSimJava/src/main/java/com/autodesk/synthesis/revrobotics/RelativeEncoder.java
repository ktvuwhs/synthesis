package com.autodesk.synthesis.revrobotics;

import com.autodesk.synthesis.CANEncoder;
import com.autodesk.synthesis.revrobotics.SparkMax;
import com.revrobotics.REVLibError;

public class RelativeEncoder implements com.revrobotics.RelativeEncoder {

    // TODO: Try changing m_original to a REV SparkMax and use REV SparkMax configAccess to re-add conversion ratios to get methods
    private SparkMax m_motor;
    private double m_zero = 0.0;
    private double m_positionConversionFactor = 1.0;
    private double m_velocityConversionFactor = 1.0;
    private double m_invertedFactor = 1.0;

    public RelativeEncoder(SparkMax motor) {
        m_motor = motor;
        m_positionConversionFactor = motor.configAccessor.encoder.getPositionConversionFactor();
        m_velocityConversionFactor = motor.configAccessor.encoder.getVelocityConversionFactor();
        m_invertedFactor = motor.configAccessor.encoder.getInverted() ? -1.0 : 1.0;
    }

    @Override
    public double getPosition() {
        return m_motor.m_encoder.getPosition() * m_positionConversionFactor * m_invertedFactor - m_zero;
    }

    @Override
    public double getVelocity() {
        return m_motor.m_encoder.getVelocity() * m_velocityConversionFactor * m_invertedFactor;
    }

    @Override
    public REVLibError setPosition(double position) {
        m_zero = m_motor.m_encoder.getPosition() * m_positionConversionFactor * m_invertedFactor - position;
        return REVLibError.kOk;
    }
    
}
