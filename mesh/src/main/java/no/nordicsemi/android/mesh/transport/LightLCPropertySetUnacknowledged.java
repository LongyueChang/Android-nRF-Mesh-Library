package no.nordicsemi.android.mesh.transport;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import androidx.annotation.NonNull;
import no.nordicsemi.android.mesh.ApplicationKey;
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes;

/**
 * LightLCPropertySetUnacknowledged
 */
@SuppressWarnings("unused")
public class LightLCPropertySetUnacknowledged extends GenericMessage {

    private static final String TAG = LightLCPropertySetUnacknowledged.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.LIGHT_LC_PROPERTY_SET_UNACKNOWLEDGED;
    private final short property;
    private final byte[] value;

    /**
     * Constructs LightLCPropertySetUnacknowledged message.
     *
     * @param appKey {@link ApplicationKey} key for this message
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    public LightLCPropertySetUnacknowledged(@NonNull final ApplicationKey appKey, final short property, @NonNull final byte[] value) throws IllegalArgumentException {
        super(appKey);
        this.property = property;
        this.value = value;
        assembleMessageParameters();
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    @Override
    void assembleMessageParameters() {
        mAid = (byte) mAppKey.getAid();
        mParameters = ByteBuffer.allocate(2 + value.length).order(ByteOrder.LITTLE_ENDIAN).putShort(property).put(value).array();
    }
}