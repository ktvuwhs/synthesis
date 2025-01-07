package com.autodesk.synthesis;

import edu.wpi.first.hal.SimDevice;
import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.hal.SimBoolean;
import edu.wpi.first.hal.SimDevice.Direction;

/**
 * CANEncoder class for easy implementation of documentation-compliant simulation data.
 * 
 * See https://github.com/wpilibsuite/allwpilib/blob/6478ba6e3fa317ee041b8a41e562d925602b6ea4/simulation/halsim_ws_core/doc/hardware_ws_api.md
 * for documentation on the WebSocket API Specification.
 */
public class CANEncoder {

    private SimDevice m_device;

    private SimBoolean m_init;
    private SimDouble m_position;
    private SimDouble m_velocity;
    private SimDouble m_positionConversionFactor;
    private SimDouble m_velocityConversionFactor;
    private SimDouble m_invertedFactor;

    /**
     * Creates an Encoder accessed by the CANBus.
     * 
     * @param name Name of the CAN Motor. This is generally the class name of the originating encoder, prefixed with something (ie. "SYN CANSparkMax/Encoder").
     * @param deviceId CAN Device ID.
     */
    public CANEncoder(String name, int deviceId) {
        m_device = SimDevice.create(String.format("%s:%s", "CANEncoder", name), deviceId);

        m_init = m_device.createBoolean("init", Direction.kOutput, true);
        m_position = m_device.createDouble("position", Direction.kInput, 0.0);
        m_velocity = m_device.createDouble("velocity", Direction.kInput, 0.0);
        m_positionConversionFactor = m_device.createDouble("position conversion factor", Direction.kInput, 1.0);
        m_velocityConversionFactor = m_device.createDouble("velocity conversion factor", Direction.kInput, 1.0);
        m_invertedFactor = m_device.createDouble("isInverted", Direction.kInput, 1.0);

        m_init.set(true);
    }

    /**
     * Gets the current position of the encoder, simulated.
     * 
     * @return Current position in revolutions.
     */
    public double getPosition() {
        return m_position.get() * m_positionConversionFactor.get() * m_invertedFactor.get();
    }

    /**
     * Gets the current velocity of the encoder, simulated.
     * 
     * @return Current velocity in revolutions per second.
     */
    public double getVelocity() {
        return m_velocity.get() * m_velocityConversionFactor.get() * m_invertedFactor.get();
    }

    /**
     * Sets the current position of the encoder, simulated.
     * This returns the native units of 'rotations' by default,
     * and can be changed by a scale factor using setPositionConversionFactor().
     * 
     * @param   pos Current position.
     */
    // public void setPosition(double pos) {
    //     if (pos == 0.0) {
    //         m_position.reset();
    //     } else {
    //         m_position.set(pos);
    //     }
    // }

    /**
     * Sets the position conversion factor of the encoder, simulated.
     * 
     * @param factor    The factor to multiply the position by
     */
    public void setPositionConversionFactor(double factor) {
        m_positionConversionFactor.set(factor);
    }

    /**
     * Sets the position conversion factor of the encoder, simulated.
     * 
     * @param factor    The factor to multiply the velocity by
     */
    public void setVelocityConversionFactor(double factor) {
        m_velocityConversionFactor.set(factor);
    }

    /**
     * Sets the position conversion factor of the encoder, simulated.
     * 
     * @param isInverted    Whether the encoder is inverted or not
     */
    public void setInverted(boolean isInverted) {
        m_invertedFactor.set(isInverted ? -1.0 : 1.0);
    }
}
