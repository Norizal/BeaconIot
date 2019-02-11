package com.minewtech.thingoo.api.http;

import com.alibaba.fastjson.JSON;
import com.minewtech.thingoo.mqtt.model.StatusMessage;
import com.minewtech.thingoo.mqtt.service.StatusService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.List;


@RestController
@Api(tags = "http")
public class HttpController {

    private static Logger logger = LoggerFactory.getLogger(StatusService.class);

    @Autowired
    private StatusService statusService;

    @RequestMapping(value = "gw/{gtwMac}/status",method = RequestMethod.POST,produces = "application/json")
    public String ServiceAccess(@PathVariable("gtwMac") String gtwMac, HttpServletRequest request){
        gtwMac=gtwMac.toUpperCase();
        String json = readJSONString(request);
      // JSONArray j= JSONArray.parseArray(json);
        //System.out.println(json);
        List<StatusMessage> events=JSON.parseArray(json, StatusMessage.class);
        statusService.handleEvenet(gtwMac,events);
        return "ok";
    }
    private String readJSONString(HttpServletRequest request) {
        StringBuffer json = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            reader.close();
        }
        catch(Exception e) {
            //System.out.println(e.toString());
        }
        return json.toString();
    }
}
