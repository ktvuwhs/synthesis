package com.autodesk.synthesis.revrobotics;

import com.autodesk.synthesis.CANEncoder;
import com.autodesk.synthesis.revrobotics.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfigAccessor;
import com.revrobotics.REVLibError;

public class RelativeEncoder implements com.revrobotics.RelativeEncoder {
    private CANEncoder m_encoder;
    private double m_zero = 0.0;

    public RelativeEncoder(CANEncoder encoder) {
        m_encoder = encoder;
    }

    @Override
    public double getPosition() {
        return m_encoder.getPosition() - m_zero;
    }

    @Override
    public double getVelocity() {
        return m_encoder.getVelocity();
    }

    @Override
    public REVLibError setPosition(double position) {
        m_zero = m_encoder.getPosition() - position;
        return REVLibError.kOk;
    }
    
}
