package com.autodesk.synthesis.ctre;

import com.autodesk.synthesis.CANEncoder;
import com.autodesk.synthesis.CANMotor;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.hardware.DeviceIdentifier;

public class TalonFX extends com.ctre.phoenix6.hardware.TalonFX {
    private CANMotor m_motor;
    private CANEncoder m_encoder;

    /**
     * Creates a new TalonFX, wrapped with simulation support.
     * 
     * @param deviceNumber CAN Device ID.
     */
    public TalonFX(int deviceNumber) {
        super(deviceNumber);

        this.m_motor = new CANMotor("SYN TalonFX", deviceNumber, 0.0, false, 0.3);
        this.m_encoder = new CANEncoder("SYN TalonFX", deviceNumber);
    }

    @Override
    public void set(double percentOutput) {
        super.set(percentOutput);
        this.m_motor.setPercentOutput(percentOutput);
    }

    @Override
    public StatusCode setNeutralMode(NeutralModeValue mode) {
        this.m_motor.setBrakeMode(mode == NeutralModeValue.Brake);
        return super.setNeutralMode(mode);
    }

    @Override
    public TalonFXConfigurator getConfigurator() {
        DeviceIdentifier id = this.deviceIdentifier;
        return new com.autodesk.synthesis.ctre.TalonFXConfigurator(id, this);
    }

    // called internally by the configurator to set the deadband, not for user use
    public void setNeutralDeadband(double deadband) {
        this.m_motor.setNeutralDeadband(deadband);
    }

    public double getPositionSim() {
        return this.m_encoder.getPosition();
    }

    public double getVelocitySim() {
        return this.m_encoder.getVelocity();
    }
}
