package com.automate.vehicleservices.api.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Chandrashekar V
 */
@Data
public class DownloadReference {

    private String url;

    private byte[] content;

    private String label;

    private String filename;

    private Map<String, Object> metadata = new HashMap<>();

}
