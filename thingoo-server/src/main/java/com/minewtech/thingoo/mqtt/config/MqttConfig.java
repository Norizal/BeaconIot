package com.minewtech.thingoo.mqtt.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import com.minewtech.thingoo.model.Success;
import com.minewtech.thingoo.mqtt.model.StatusMessage;
import com.minewtech.thingoo.mqtt.service.StatusService;
import com.minewtech.thingoo.repository.StatusRepository;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.util.List;
import java.util.UUID;


@IntegrationComponentScan
@EnableAutoConfiguration
@Component
@ComponentScan(basePackages = {"com.minewtech.thingoo"})
@Configuration
public class MqttConfig{

    private static Logger logger = LoggerFactory.getLogger(MqttConfig.class);

    @Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;

    @Autowired
    private StatusService statusService;

    @Value("${user:admin}")
    private String user;

    @Value("${password:admin}")
    private String password;

    @Value("${clientId:clientTest}")
    private String clientId;

    @Value("${host:localhost}")
    private  String host;

    @Value("${port:1883}")
    private  int port;

    @Value("${waitTime:5}")
    private int waitTime;

    @Value("${qos:0}")
    private int qos;

    @Value("${topic:/gw/#}")
    private String topic;

    @Value("${numMessages:1000}")
    private long numMessages = 0;

    /**
     * MessageChannel method creates a new mqttInputChannel
     * to connect to the Broker with a single threaded connection.
     * Downstream components are connected via Direct Channel.
     *
     * @return   DirectChannel single threaded connection
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * MqttPahoClientFactory method establishes the URL of the server along with the host and
     * port, the username, and the password for connecting to the determined broker.
     * Generates the Last Will and Testament for the publisher clientId
     * for a lost connection situation. SSL connection to the broker is possible with
     * the correct keyStore and trustStore provided.
     *
     * @return   factory with given variables
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        java.util.Properties sslClientProps = new java.util.Properties();

      /*sslClientProps.setProperty("com.ibm.ssl.keyStore","src/main/resources/client.ks");
        sslClientProps.setProperty("com.ibm.ssl.keyStorePassword","password");

        sslClientProps.setProperty("com.ibm.ssl.trustStore","src/main/resources/client.ts");
        sslClientProps.setProperty("com.ibm.ssl.trustStorePassword","password");*/

        // TODO: Provide better logic in case user uses something besides the default MQTT ports
        if (port == 1883){
            factory.setServerURIs("tcp://" + host + ":" + port);
        } else{
            factory.setServerURIs("ssl://" + host + ":" + port);
        }

//        factory.setUserName(user);
//        factory.setPassword(password);
//        factory.setSslProperties(sslClientProps);
//        factory.setWill(new DefaultMqttPahoClientFactory.Will(topic, "I have died...".getBytes(), qos, true ));
        return factory;
    }

    /**
     * MessageProducer generates a clientID for the subscriber by using a randomUUID
     * for creating a connection to a broker. Creates a new
     * connection adapter to a broker by setting the clientID, MqttClientFactory,
     * topic given to subscribe to, Completion Timeout, Converter,
     * Qos, and the Output Channel for numMessages to go to.
     *
     * @return   adapter for the mqttInbound connection
     */
    @Bean
    public MessageProducer mqttInbound() {
        if (clientId.equals("clientTest")){
            //生成唯一UUID
            clientId = UUID.randomUUID().toString();

        }

        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId, mqttClientFactory(), topic);

        adapter.setCompletionTimeout(100000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(qos);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    /**
     * Message handler calculates the amount of messages that have come in
     * from the subscribed topic and posts the result.
     *
     * @return   message is the numMessages amount
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {

            //if (count % 10 == 0) {
             //   System.out.println(String.format("Received %d num Messages from " + topic + ":" + message.getHeaders() + " topic.", count));
           // }

            //count++;
            //logger.info(message.getPayload().toString());

            String mqttTopic = (String) message.getHeaders().get("mqtt_topic");
            if (mqttTopic.startsWith("/gw/") && mqttTopic.endsWith("/status")) {
                if (mqttTopic.length() > 16) {
                    //System.out.println(mqttTopic);
                    String gatewayMac = mqttTopic.substring(4, 16).toUpperCase();
                    gatewayMac = gatewayMac.toUpperCase();
                    Object messagePayload = message.getPayload();
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.registerModule(new JodaModule());

                    try {
                        List<StatusMessage> events = mapper.readValue(messagePayload.toString(), new TypeReference<List<StatusMessage>>() {
                        });
//                    logger.info("events:" + events);

                        statusService.handleEvenet(gatewayMac, events);

                    } catch (IOException e) {
                        //logger.error(e.getMessage());
                        e.printStackTrace();
                    }
                }

            }
        };
    }

    /**
     * Creates a new instance of the PropertySourcesPlaceholderConfigurer
     * to pass property sources from the application.properties file.
     *
     * @return   PropertySourcesPlaceholderConfigurer new instance
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


}
