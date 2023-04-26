package com.automate.vehicleservices.entity.enums;

import java.util.Optional;

/**
 * Chandrashekar V
 */
public enum SupportedFileType {

    PDF,
    JPG,
    PNG,
    JPEG;

    public static Optional<SupportedFileType> fetchEnumType(final String type) {

        final var supportedFileTypes = values();

        for (SupportedFileType supportedFileType : supportedFileTypes) {
            if (type.equalsIgnoreCase(supportedFileType.name()))
                return Optional.of(supportedFileType);
        }

        return Optional.empty();
    }
}
