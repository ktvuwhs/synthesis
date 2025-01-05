package com.autodesk.synthesis.revrobotics;

import java.util.ArrayList;

import com.autodesk.synthesis.CANEncoder;
import com.autodesk.synthesis.CANMotor;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.REVLibError;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

/**
 * CANSparkMax wrapper to add proper WPILib HALSim support.
 */
public class SparkMax extends com.revrobotics.spark.SparkMax {

    private CANMotor m_motor;
    public CANEncoder m_encoder;
    private ArrayList<SparkMax> followers;

    /**
     * Creates a new CANSparkMax, wrapped with simulation support.
     * 
     * @param deviceId  CAN Device ID.
     * @param motorType Motor type. For Simulation purposes, this is discarded at the
     *                  moment.
     *
     * See original documentation for more information https://codedocs.revrobotics.com/java/com/revrobotics/cansparkmax
     */
    public SparkMax(int deviceId, com.revrobotics.spark.SparkLowLevel.MotorType motorType) {
        super(deviceId, motorType);

        this.m_motor = new CANMotor("SYN CANSparkMax", deviceId, 0.0, false, 0.3);
        this.m_encoder = new CANEncoder("SYN CANSparkMax", deviceId);
        this.followers = new ArrayList<SparkMax>();
    }

    /**
     * Sets the percent output of the real and simulated motors
     * Setting a follower doesn't break the simulated follower - leader relationship, which it does for exclusively non-simulated motors
     *
     * @param percent The new percent output of the motor
     *
     * See the original documentation for more information 
     */
    @Override
    public void set(double percent) {
        super.set(percent);
        this.m_motor.setPercentOutput(percent);
        for (SparkMax follower : this.followers) {
            follower.set(percent);
        }
    }

    /**
     * Sets the neutralDeadband of the real and simulated motors
     *
     * @param n The new neutral deadband
     */
    void setNeutralDeadband(double n) {
        this.m_motor.setNeutralDeadband(n);
    }

    /**
     * Sets the real and simulated motors to an idle mode
     *
     * @param mode The specific idle mode (Brake, Coast)
     */
    public void setIdleMode(com.revrobotics.spark.config.SparkBaseConfig.IdleMode mode) {
        if (mode != null) {
            SparkMaxConfig config = new SparkMaxConfig();
            config.idleMode(mode);
            super.configure(config, com.revrobotics.spark.SparkBase.ResetMode.kResetSafeParameters, com.revrobotics.spark.SparkBase.PersistMode.kPersistParameters);
            // this.m_motor.setBrakeMode(mode.equals(com.revrobotics.CANSparkBase.IdleMode.kBrake));
        }
    }

    /** 
     * Gets a simulation-supported SparkAbsoluteEncoder containing the position and velocity of the motor in fission.
     * All information returned by this class besides position and velocity is from the real motor.
     * Use instead of getAbsoluteEncoder(), everything except for the name of the method works exactly the same.

     * @return The simulation-supported SparkAbsoluteEncoder.
     */
    public com.autodesk.synthesis.revrobotics.SparkAbsoluteEncoder getAbsoluteEncoderSim() {
        return new SparkAbsoluteEncoder(super.getAbsoluteEncoder(), this.m_encoder);
    }

    public com.autodesk.synthesis.revrobotics.RelativeEncoder getEncoderSim() {
        return new RelativeEncoder(super.getEncoder(), this.m_encoder);
    }

    /**
     * Adds a follower to this motor controller.
     *
     * @param f The new follower
     */
    void newFollower(SparkMax f) {
        this.followers.add(f);
    }

    /** 
     * Causes a simulation-supported leader to follow another simulation-supported leader.
     * The real versions of these motors will also follow each other.
     *
     * @param leader The motor for this robot to follow
     */
    public void follow(com.revrobotics.spark.SparkBase leader) {
        if (leader instanceof SparkMax) {
            ((SparkMax) leader).newFollower(this);
        }
    }
}
