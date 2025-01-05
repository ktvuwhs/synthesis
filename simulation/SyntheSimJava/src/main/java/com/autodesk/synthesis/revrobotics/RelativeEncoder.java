package com.autodesk.synthesis.revrobotics;

import com.autodesk.synthesis.CANEncoder;
import com.revrobotics.REVLibError;

public class RelativeEncoder implements com.revrobotics.RelativeEncoder {

    // TODO: Try changing m_original to a REV SparkMax and use REV SparkMax configAccess to re-add conversion ratios to get methods
    private com.revrobotics.RelativeEncoder m_original;
    private double m_zero = 0.0;
    private CANEncoder m_encoder;

    public RelativeEncoder(com.revrobotics.RelativeEncoder original, CANEncoder encoder) {
        m_original = original;
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
