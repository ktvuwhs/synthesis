package com.autodesk.synthesis.revrobotics;

import com.autodesk.synthesis.CANEncoder;
import com.revrobotics.REVLibError;
import com.revrobotics.spark.config.SparkMaxConfigAccessor;

public class RelativeEncoder implements com.revrobotics.RelativeEncoder {

    private com.revrobotics.RelativeEncoder m_original;
    private double m_zero = 0.0;
    private CANEncoder m_encoder;
    private SparkMaxConfigAccessor m_accessor;
    
    public RelativeEncoder(com.revrobotics.RelativeEncoder original, CANEncoder encoder, SparkMaxConfigAccessor accessor) {
        m_original = original;
        m_encoder = encoder;
        m_accessor = accessor;
    }

    @Override
    public double getPosition() {
        return m_encoder.getPosition() * m_accessor.encoder.getPositionConversionFactor() * (m_accessor.encoder.getInverted() ? -1.0 : 1.0) - m_zero;
    }

    @Override
    public double getVelocity() {
        return m_encoder.getVelocity() * m_accessor.encoder.getVelocityConversionFactor() * (m_accessor.encoder.getInverted() ? -1.0 : 1.0);
    }

    @Override
    public REVLibError setPosition(double position) {
        m_zero = this.getPosition() - position;
        return REVLibError.kOk;
    }
    
}
