package com.example.filter;

import com.google.mlkit.common.MlKitException;

import java.nio.ByteBuffer;

public interface VisionImageProcessor {
    /** Processes ByteBuffer image data, e.g. used for Camera1 live preview case. */
    void processByteBuffer(
            ByteBuffer data, FrameMetadata frameMetadata, GraphicOverlay graphicOverlay)
            throws MlKitException;

    /** Stops the underlying machine learning model and release resources. */
    void stop();
}
