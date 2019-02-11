package com.minewtech.thingoo.api;

import com.alibaba.fastjson.JSON;
import com.minewtech.thingoo.model.instructions.Instructions;

import com.minewtech.thingoo.mqtt.service.MqttBroker;
import com.minewtech.thingoo.repository.OperationRepository;

import org.eclipse.paho.client.mqttv3.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
public class RestartController {


        @Autowired
        private OperationRepository operationRepository;


    @RequestMapping(value = "account/gatewayMac",method = RequestMethod.POST)
    public String restart(@RequestParam String gatewayMac) throws MqttException {

        Instructions instructions =new Instructions();

            instructions.setAction("reboot");
            instructions.setRequestId(gatewayMac);
            String topic="/gw/"+gatewayMac+"/action";
        System.out.println(topic);
        String o=JSON.toJSONString(instructions);
        MqttBroker.publish(gatewayMac, UUID.randomUUID().toString(),topic);
        System.out.println("ww"+o);
        return "ok";
    }
}
