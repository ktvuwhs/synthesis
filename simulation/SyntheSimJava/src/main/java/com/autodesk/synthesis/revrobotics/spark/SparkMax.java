package com.autodesk.synthesis.revrobotics.spark;

import java.util.HashMap;
import java.util.HashSet;

import com.autodesk.synthesis.CANEncoder;
import com.autodesk.synthesis.CANMotor;
import com.revrobotics.REVLibError;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

/**
 * CANSparkMax wrapper to add proper WPILib HALSim support.
 */
public class SparkMax extends com.revrobotics.spark.SparkMax {

    public CANMotor m_motor;
    public CANEncoder m_encoder;
    public boolean m_inverted = false;
    public HashSet<FollowerInfo> followers = new HashSet<>();

    private static HashMap<Integer, SparkMax> s_motors = new HashMap<>();

    /**
     * Creates a new CANSparkMax, wrapped with simulation support.
     * 
     * @param deviceId  CAN Device ID.
     * @param motorType Motor type. For Simulation purposes, this is discarded at
     *                  the
     *                  moment.
     *
     *                  See original documentation for more information
     *                  https://codedocs.revrobotics.com/java/com/revrobotics/cansparkmax
     */
    public SparkMax(int deviceId, com.revrobotics.spark.SparkLowLevel.MotorType motorType) {
        super(deviceId, motorType);

        this.m_motor = new CANMotor("SYN CANSparkMax", deviceId, 0.0, false, 0.3);
        this.m_encoder = new CANEncoder("SYN CANSparkMax", deviceId);

        s_motors.put(this.getDeviceId(), this);
    }

    /**
     * Sets the percent output of the real and simulated motors
     * Setting a follower doesn't break the simulated follower - leader
     * relationship, which it does for exclusively non-simulated motors
     *
     * @param percent The new percent output of the motor
     *
     *                See the original documentation for more information
     */
    @Override
    public void set(double percent) {
        super.set(m_inverted ? -percent : percent);
        this.m_motor.setPercentOutput(percent);
        for (FollowerInfo info : this.followers) {
            SparkMax.s_motors.get(info.canId).set(info.inverted ? -percent : percent);
        }
    }

    @Override
    public REVLibError configure(SparkBaseConfig config, ResetMode resetMode, PersistMode persistMode) {
        REVLibError res = super.configure(config, resetMode, persistMode);
        if (res != REVLibError.kOk) {
            return res;
        }

        SparkMax.s_motors.entrySet()
                .forEach((e) -> e.getValue().followers.remove(new FollowerInfo(this.getDeviceId(), false)));
        int leaderId = this.configAccessor.getFollowerModeLeaderId();
        SparkMax leader = SparkMax.s_motors.get(leaderId);
        if (leader != null) {
            leader.followers.add(
                    new FollowerInfo(this.getDeviceId(), this.configAccessor.getFollowerModeInverted()));
        }

        this.m_inverted = this.configAccessor.getInverted();

        this.m_motor.setBrakeMode(this.configAccessor.getIdleMode() == IdleMode.kBrake);

        return res;
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
     * Gets a simulation-supported SparkAbsoluteEncoder containing the position and
     * velocity of the motor in fission.
     * All information returned by this class besides position and velocity is from
     * the real motor.
     * Use instead of getAbsoluteEncoder(), everything except for the name of the
     * method works exactly the same.
     * 
     * @return The simulation-supported SparkAbsoluteEncoder.
     */
    public com.autodesk.synthesis.revrobotics.spark.SparkAbsoluteEncoder getAbsoluteEncoderSim() {
        return new com.autodesk.synthesis.revrobotics.spark.SparkAbsoluteEncoder(super.getAbsoluteEncoder(), this.m_encoder, this.configAccessor.absoluteEncoder);
    }

    public com.autodesk.synthesis.revrobotics.spark.SparkRelativeEncoder getEncoderSim() {
        return new com.autodesk.synthesis.revrobotics.spark.SparkRelativeEncoder(super.getEncoder(), this.m_encoder, this.configAccessor.encoder);
    }

    public class FollowerInfo {
        public int canId;
        public boolean inverted;

        public FollowerInfo(int canId, boolean inverted) {
            this.canId = canId;

            this.inverted = inverted;
        }

        @Override
        public int hashCode() {
            return canId;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof FollowerInfo)) {
                return false;
            }

            return ((FollowerInfo) obj).canId == this.canId;
        }
    }
}
