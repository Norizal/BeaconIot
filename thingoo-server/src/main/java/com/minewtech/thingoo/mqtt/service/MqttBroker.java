package com.minewtech.thingoo.mqtt.service;

import com.alibaba.fastjson.JSON;
import com.minewtech.thingoo.model.instructions.Instructions;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttBroker {

    private static int qos = 0; //只有一次
    private static String broker = "tcp://localhost:1883";
    private static String userName = "tuyou";
    private static String passWord = "tuyou";


    private static MqttClient connect(String clientId, String userName,
                                      String password) throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(userName);
        connOpts.setPassword(password.toCharArray());
        connOpts.setConnectionTimeout(10);
        connOpts.setKeepAliveInterval(20);
//      String[] uris = {"tcp://10.100.124.206:1883","tcp://10.100.124.207:1883"};
//      connOpts.setServerURIs(uris);  //起到负载均衡和高可用的作用
        MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
        mqttClient.setCallback(new PushCallback());
        mqttClient.connect(connOpts);
        return mqttClient;
    }

    private static void pub(MqttClient sampleClient, String msg,String topic)
            throws MqttPersistenceException, MqttException {
        Instructions instructions=new Instructions();
        instructions.setAction("reboot");
        instructions.setRequestId(msg);
        String o= JSON.toJSONString(instructions);
        MqttMessage message = new MqttMessage(o.getBytes());
        message.setQos(qos);
        message.setRetained(false);
        sampleClient.publish(topic, message);
    }

    public static void publish(String str,String clientId,String topic) throws MqttException{
        MqttClient mqttClient = connect(clientId,userName,passWord);

        if (mqttClient != null) {
            pub(mqttClient, str, topic);
            System.out.println("pub-->" + str);
        }

        if (mqttClient != null) {
            mqttClient.disconnect();
        }
    }



}
