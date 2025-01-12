package com.autodesk.synthesis.revrobotics.spark;

import com.autodesk.synthesis.CANEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.config.EncoderConfigAccessor;
import com.revrobotics.REVLibError;

public class SparkRelativeEncoder implements RelativeEncoder {

    private com.revrobotics.RelativeEncoder m_original;
    private double m_zero = 0.0;
    private CANEncoder m_encoder;
    private EncoderConfigAccessor m_accessor;
    
    public SparkRelativeEncoder(com.revrobotics.RelativeEncoder original, CANEncoder encoder, EncoderConfigAccessor accessor) {
        m_original = original;
        m_encoder = encoder;
        m_accessor = accessor;
    }

    @Override
    public double getPosition() {
        return m_encoder.getPosition() * m_accessor.getPositionConversionFactor() * (m_accessor.getInverted() ? -1.0 : 1.0) - m_zero;
    }

    @Override
    public double getVelocity() {
        return m_encoder.getVelocity() * m_accessor.getVelocityConversionFactor() * (m_accessor.getInverted() ? -1.0 : 1.0);
    }

    @Override
    public REVLibError setPosition(double position) {
        m_zero = this.getPosition() - position;
        return REVLibError.kOk;
    }
    
}
