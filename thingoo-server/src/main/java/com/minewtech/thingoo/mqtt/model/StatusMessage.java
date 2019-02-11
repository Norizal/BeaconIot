package com.minewtech.thingoo.mqtt.model;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class StatusMessage {

    @Getter @Setter private String gatewayMac;
    @Getter @Setter private String mac;
//    @Enumerated(EnumType.STRING)

        @Getter @Setter private String type;
    @Getter @Setter private String bleName;
    @Getter @Setter private String ibeaconUuid;
    @Getter @Setter private Integer ibeaconMajor;
    @Getter @Setter private Integer ibeaconMinor;
    @Getter @Setter private Integer ibeaconTxPower;
    @Getter @Setter private Integer rssi;
    @Getter @Setter private Integer battery;
    @Getter @Setter private Float temperature;
    @Getter @Setter private Float humidity;
    @Getter @Setter private String rawData;
    @Getter @Setter private Float gatewayLoad;
    @Getter @Setter private Float gatewayFree;

    @Getter @Setter private Date timestamp;

    @Override
    public String toString() {
        return "StatusMessage{" +
                "gatewayMac='" + gatewayMac + '\'' +
                ", mac='" + mac + '\'' +
                ", type='" + type + '\'' +
                ", bleName='" + bleName + '\'' +
                ", ibeaconUuid='" + ibeaconUuid + '\'' +
                ", ibeaconMajor=" + ibeaconMajor +
                ", ibeaconMinor=" + ibeaconMinor +
                ", ibeaconTxPower=" + ibeaconTxPower +
                ", rssi=" + rssi +
                ", battery=" + battery +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", rawData='" + rawData + '\'' +
                ", gatewayLoad=" + gatewayLoad +
                ", gatewayFree=" + gatewayFree +
                ", timestamp=" + timestamp +
                '}';
    }
}
