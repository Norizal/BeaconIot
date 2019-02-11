package com.minewtech.thingoo.mqtt.service;



import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public class PubMsg {
    @Value("${host:localhost}")
    private static  String host;

    @Value("${port:1883}")
    private   int port;
    @Value("${clientId:clientTest}")
    private  String clientId;

    private MqttClient client;
    public MqttTopic topic11;
    public   String HOST;
    private  String  getBroker(){
        java.util.Properties sslClientProps = new java.util.Properties();
        if (port == 1883){
            HOST="tcp://" + host + ":" + port;
        } else{
            HOST="ssl://" + host + ":" + port;
        }

        return  HOST;
    };


  public void conent(String topic,String o) throws MqttException {
      if (clientId.equals("clientTest")){
          //生成唯一UUID
          clientId = UUID.randomUUID().toString();

      }
      // MemoryPersistence设置clientid的保存形式，默认为以内存保存
      client = new MqttClient( getBroker(), clientId, new MemoryPersistence());
      MqttConnectOptions options = new MqttConnectOptions();
      options.setCleanSession(true);
      //options.setUserName(userName);
      //options.setPassword(passWord.toCharArray());
      // 设置超时时间
      options.setConnectionTimeout(10);
      // 设置会话心跳时间
      options.setKeepAliveInterval(20);
      try {
          client.setCallback(new PushCallback());
          client.connect(options);
          MqttMessage message = new MqttMessage();
          message.setQos(0);
          message.setRetained(false);
          message.setPayload(o.getBytes());
          topic11 = client.getTopic(topic);
          MqttDeliveryToken token = topic11.publish(message);
          token.waitForCompletion();
          System.out.println("message is published completely! "
                  + token.isComplete());
      } catch (Exception e) {
          e.printStackTrace();
      }

  }

   /** public void publish(MqttTopic topic ,String o) throws MqttPersistenceException,
            MqttException {


    }**/
}
