package com.minewtech.thingoo.mqtt.service;

import com.minewtech.thingoo.model.device.DeviceType;
import com.minewtech.thingoo.model.status.Status;
import com.minewtech.thingoo.mqtt.model.StatusMessage;
import com.minewtech.thingoo.repository.StatusRepository;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class StatusService {

    private static Logger logger = LoggerFactory.getLogger(StatusService.class);
    @Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;

    @Autowired
    private StatusRepository statusRepository;

    public void handleEvenet(String gatewayMac, List<StatusMessage> statusMessages){
        long currentTimeMillis = System.currentTimeMillis();
        List<Point> points = new ArrayList<>();
        List<Point> pointrs = new ArrayList<>();
        List<String> macs = new ArrayList<>();
        List<Status> statuses = new ArrayList<>();
        List<Status> savedStatuses = new ArrayList<>();
        Map<String, Status> statusMap = new HashMap<>();

        for (int i = 0; i < statusMessages.size(); i++) {
            StatusMessage statusMessage = statusMessages.get(i);
            if (statusMessage.getType().equals("S1")) {
//              logger.info("event:" + statusMessage);
                // Create some data...
                points.add(Point.measurement("sensor")
//                                .time(currentTimeMillis, TimeUnit.MILLISECONDS)
                        .time(statusMessage.getTimestamp().getTime(), TimeUnit.MILLISECONDS)
                        .tag("gatewayMac", gatewayMac)
                        .tag("mac", statusMessage.getMac())
                        .addField("temperature", statusMessage.getTemperature())
                        .addField("humidity", statusMessage.getHumidity())
                        .addField("battery", statusMessage.getBattery())
                        .addField("rssi",statusMessage.getRssi())
                        .build());


            }
            if (statusMessage.getType().equals("iBeacon")){
                pointrs.add(Point.measurement("iBeacon")
                        // .time(currentTimeMillis, TimeUnit.MILLISECONDS)
                        .time(statusMessage.getTimestamp().getTime(), TimeUnit.MILLISECONDS)
                        .tag("gatewayMac", gatewayMac)
                        .tag("mac", statusMessage.getMac())
                        .addField("rssi",statusMessage.getRssi())
                        .addField("battery", statusMessage.getBattery())
                        .build()
                );
            }


            if (statusMessages.get(i).getMac() != null)
                macs.add(statusMessages.get(i).getMac());

//            if (statusMessage.getType().equals("Gateway")) {
//                logger.info("Gateway:" + statusMessage);
//            }

        }

        if (points.size() > 0) {
            influxDBTemplate.write(points);
        }
        if(pointrs.size()>0){
            influxDBTemplate.write(pointrs);
        }
        //logger.info("points:" + points);
       // logger.info("pointrs"+pointrs);

        for (int i = 0; i < statusMessages.size(); i++) {
            // System.out.println(statusMessages.get(i).getMac());
            statuses = statusRepository.findAllByGatewayMacAndMacIn(gatewayMac, statusMessages.get(i).getMac());
            StatusMessage statusMessage = statusMessages.get(i);
            Status status=null;
            // System.out.println(statuses);
            if (statuses.size()== 0 ) {
                status = new Status();
                status.copyFrom(gatewayMac, statusMessage);
                statusRepository.save(status);
            } else {
                for (int t = 0; t < statuses.size(); t++) {
                    status = statuses.get(t);
                    //System.out.println(status);
                    if (DeviceType.iBeacon.equals(status.getType())) {
                        status.copyFrom(gatewayMac, statusMessage);
                        // System.out.println(status);
                        statusRepository.save(status);
                    } else if (DeviceType.S1.equals(status.getType())) {
                        status.copyFrom(gatewayMac, statusMessage);
                        statusRepository.save(status);
                    } else if (DeviceType.Unknown.equals(status.getType())) {
                        status.copyFrom(gatewayMac, statusMessage);
                        statusRepository.save(status);
                    } else if (DeviceType.Gateway.equals(status.getType())) {
                        status.copyFrom(gatewayMac, statusMessage);
                        statusRepository.save(status);
                    }
                }
            }
        }
    }
}
