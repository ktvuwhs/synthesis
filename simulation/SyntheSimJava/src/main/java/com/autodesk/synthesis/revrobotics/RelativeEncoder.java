package com.autodesk.synthesis.revrobotics;

import com.autodesk.synthesis.CANEncoder;
import com.revrobotics.REVLibError;

public class RelativeEncoder implements com.revrobotics.RelativeEncoder {

    private com.revrobotics.RelativeEncoder m_original;
    private CANEncoder m_encoder;

    public RelativeEncoder(com.revrobotics.RelativeEncoder original, CANEncoder encoder) {
        m_original = original;
        m_encoder = encoder;
    }

    @Override
    public double getPosition() {
        return m_encoder.getPosition();
    }

    @Override
    public double getVelocity() {
        return m_encoder.getVelocity();
    }

    @Override
    public REVLibError setPosition(double position) {
        m_zero = m_encoder.getPosition();
        return REVLibError.kOk;
    }
    
}
