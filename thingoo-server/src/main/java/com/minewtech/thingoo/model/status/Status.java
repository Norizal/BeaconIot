package com.minewtech.thingoo.model.status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minewtech.thingoo.model.device.Device;
import com.minewtech.thingoo.model.device.DeviceType;
import com.minewtech.thingoo.model.gateway.Gateway;
import com.minewtech.thingoo.mqtt.model.StatusMessage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Data
@Entity
public class Status  implements Serializable {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Getter @Setter private String uuid;
//    @Getter @Setter private String userId;
//    @Getter @Setter private String name;
    @Getter @Setter private String gatewayMac;

    @Getter @Setter private String mac;
    @Enumerated(EnumType.STRING)
    @Getter @Setter private DeviceType type;
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
//    @OneToOne @JoinColumn(name = "mac", referencedColumnName = "mac", insertable = false, updatable = false)
    @Transient
    @Getter @Setter private Device device;
//    @OneToOne @JoinColumn(name = "gatewayMac", referencedColumnName = "mac", insertable = false, updatable = false)
    @Transient
    @Getter @Setter private Gateway gateway;

    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date updatedAt;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date createdAt;

    public Status() {
    }

    public void copyFrom(String gatewayMac,StatusMessage status){
        this.gatewayMac = gatewayMac;
        this.mac = status.getMac();

        if (status.getType() == null){
            this.type = null;
        } else if (status.getType().equalsIgnoreCase("iBeacon")){
            this.type = DeviceType.iBeacon;
        } else if (status.getType().equalsIgnoreCase("S1")){
            this.type = DeviceType.S1;
        } else if (status.getType().equalsIgnoreCase("Unknown")){
            this.type = DeviceType.Unknown;
        } else if (status.getType().equalsIgnoreCase("Gateway")){
            this.type = DeviceType.Gateway;
        }

        this.bleName = status.getBleName();
        this.ibeaconUuid = status.getIbeaconUuid();
        this.ibeaconMajor = status.getIbeaconMajor();
        this.ibeaconMinor = status.getIbeaconMinor();
        this.ibeaconTxPower = status.getIbeaconTxPower();
        this.rssi = status.getRssi();
        this.battery = status.getBattery();
        this.temperature = status.getTemperature();
        this.humidity = status.getHumidity();
        this.rawData = status.getRawData();
        this.gatewayLoad = status.getGatewayLoad();
        this.gatewayFree = status.getGatewayFree();
        this.updatedAt = status.getTimestamp();
    }
}
