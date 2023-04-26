package com.automate.vehicleservices.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chandrashekar V
 */
@Data
@NoArgsConstructor
public class UploadReference {

    private String folder;

    private String filename;

    private String version;

    private boolean status;

    private String label;

}
