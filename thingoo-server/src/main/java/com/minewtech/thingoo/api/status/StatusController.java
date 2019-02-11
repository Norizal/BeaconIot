package com.minewtech.thingoo.api.status;


import com.minewtech.thingoo.api.user.UserService;
import com.minewtech.thingoo.model.device.Device;
import com.minewtech.thingoo.model.device.DeviceType;
import com.minewtech.thingoo.model.gateway.Gateway;
import com.minewtech.thingoo.model.response.OperationResponse;
import com.minewtech.thingoo.model.response.PageResponse;
import com.minewtech.thingoo.model.status.Status;
import com.minewtech.thingoo.mqtt.model.StatusMessage;
import com.minewtech.thingoo.mqtt.service.StatusService;
import com.minewtech.thingoo.repository.DeviceRepository;
import com.minewtech.thingoo.repository.GatewayRepository;
import com.minewtech.thingoo.repository.StatusRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@Api(tags = {"Status"})
public class StatusController {

    //protected static final Logger LOG = LoggerFactory.getLogger(StatusController.class);

    @Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;

    @Autowired
    private StatusService statusService;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserService userService;

    @RequestMapping(value="/query", method= RequestMethod.GET)
    public PageResponse query(
            @ApiParam(value = "" ) @RequestParam(value = "q"  ,  required = false) String q) {
        PageResponse resp = new PageResponse();
        final QueryResult queryResult = influxDBTemplate.getConnection().query(new Query(q, influxDBTemplate.getDatabase()));
        //LOG.info(queryResult.toString());
        resp.setData(queryResult.getResults());
        return resp;

    }

    @ApiOperation(value = "Gets current status information", response = Page.class)
    @RequestMapping(value="/statuses", method= RequestMethod.GET)
    public PageResponse getStatusList(
            @ApiParam(value = "" ) @RequestParam(value = "gatewayMac"  ,  required = false) String gatewayMac,
            @ApiParam(value = "" ) @RequestParam(value = "mac"  ,  required = false) String mac,
            @ApiParam(value = "" ) @RequestParam(value = "type"  ,  required = false) DeviceType type,
            @ApiParam(value = "" ) @RequestParam(value = "excludeType"  ,  required = false) DeviceType excludeType,
            @ApiParam(value = "" ) @RequestParam(value = "timeRange"  ,  required = false) Long timeRange,
//            @ApiParam(value = "" ) @RequestParam(value ="startTime") @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ") Date startTime,
        @ApiParam(value = "" ) @PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable){
        PageResponse resp = new PageResponse();
        String loggedInUserId = userService.getLoggedInUserId();

        Page<Status> r;
//        r = statusRepository.findByUserId(loggedInUserId, pageable);
        r = statusRepository.findAll(Specifications.where(getWhereClause(loggedInUserId, gatewayMac, mac, type, excludeType, timeRange)), pageable);

        if (r.hasContent()){

           // LOG.debug("page[1]:" + r.getContent());
            List<String> gatewayMacs = new ArrayList<>();
            List<String> deviceMacs = new ArrayList<>();
            for (int i = 0; i < r.getContent().size(); i++) {
                Status item = r.getContent().get(i);
                gatewayMacs.add(item.getGatewayMac());
                deviceMacs.add(item.getMac());
            }

            List<Gateway> gateways = gatewayRepository.findAllByUserIdAndMacIn(loggedInUserId, gatewayMacs);
            List<Device> devices = deviceRepository.findByUserIdAndMacIn(loggedInUserId, deviceMacs);

           // LOG.debug("gateways[1]:" + gateways);
            //LOG.debug("devices[1]:" + devices);

            Map<String, Gateway> gatewayMap = new HashMap<>();
            Map<String, Device> deviceMap = new HashMap<>();

            for (int i = 0; i < gateways.size(); i++) {
                gatewayMap.put(gateways.get(i).getMac(), gateways.get(i));
            }
            for (int i = 0; i < devices.size(); i++) {
                deviceMap.put(devices.get(i).getMac(), devices.get(i));
            }

            for (int i = 0; i < r.getContent().size(); i++) {
                Status item = r.getContent().get(i);
                item.setDevice(deviceMap.get(item.getMac()));
                item.setGateway(gatewayMap.get(item.getGatewayMac()));
            }

            //LOG.debug("page[2]:" + r.getContent());
        }

        resp.setPageStats(r,true);

        return resp;
    }

    private Specification<Status> getWhereClause(String userId, String gatewayMac, String mac, DeviceType type, DeviceType excludeType, Long timeRange) {
        return new Specification<Status>() {
            @Override
            public Predicate toPredicate(Root<Status> r, CriteriaQuery<?> q, CriteriaBuilder cb) {
//                Join<Status, Device> deviceJoin = r.join(r.getModel().getSingularAttribute("device", Device.class));
//                Join<Status, Gateway> gatewayJoin = r.join(r.getModel().getSingularAttribute("gateway", Gateway.class));
                Predicate predicate = cb.conjunction();

                if (StringUtils.isNotBlank(userId)) {

                    List<Gateway> gateways = gatewayRepository.findAllByUserId(userId);

                    if (gateways.size() > 0){
                        //in条件拼接
                        CriteriaBuilder.In<String> in = cb.in(r.get("gatewayMac").as(String.class));
                        for (int i = 0; i < gateways.size(); i++) {
                            in.value(gateways.get(i).getMac());
                        }
                        predicate.getExpressions().add(in);
                    } else {
                        predicate.getExpressions().add(
                                cb.equal(r.<String>get("gatewayMac"), "")
                        );
                    }
                }


                if (gatewayMac != null && !gatewayMac.isEmpty()) {
                    predicate.getExpressions().add(
                            cb.equal(r.<String>get("gatewayMac"), gatewayMac.toUpperCase())
                    );

                }

                if (mac != null && !mac.isEmpty()) {
                    predicate.getExpressions().add(
                            cb.equal(r.<String>get("mac"), mac.toUpperCase())
                    );
                }

                if (type != null) {
                    predicate.getExpressions().add(
                            cb.equal(r.<DeviceType>get("type"), type)
                    );
                }

                if (excludeType != null) {
                    predicate.getExpressions().add(
                            cb.notEqual(r.<DeviceType>get("type"), excludeType)
                    );
                }


//                if (StringUtils.isNotBlank(userId)) {
//                    predicate.getExpressions().add(cb.or(
//                            cb.equal(gatewayJoin.get("userId").as(String.class), userId), cb.equal(gatewayJoin.get("userId").as(String.class), null))
//                    );
//                    predicate.getExpressions().add(cb.or(cb.equal(deviceJoin.get("userId").as(String.class), userId), cb.equal(deviceJoin.get("userId").as(String.class), null))
//
//                    );
//                }

                if (timeRange != null) {
                Date endTime = Calendar.getInstance().getTime();
                Date startTime = new Date(endTime.getTime() - timeRange*1000);
                //LOG.debug("Between:" + startTime + "~" + endTime);
                    predicate.getExpressions().add(
                            cb.between(r.<Date>get("updatedAt"), startTime, endTime)
                    );
                }


                return predicate;
            }
        };
    }



    @ApiOperation(value = "Add new status", response = OperationResponse.class)
    @RequestMapping(value = "/statuses", method = RequestMethod.POST, produces = {"application/json"})
    public OperationResponse postStatus(@RequestBody Status status, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

//        status.setUserId(loggedInUserId);

        Status r = statusRepository.save(status);

        if (r.getUuid() != "") {
            resp.setStatus(200);
            resp.setMessage("Record Added");
            resp.setData(r);
        } else {
            resp.setStatus(400);
            resp.setMessage("Unable to add Record");
        }
        return resp;
    }


    /*    @ApiOperation(value = "Add new status", response = OperationResponse.class)
      @RequestMapping(value = "/gw/{gatewayMac}/status", method = RequestMethod.POST, produces = {"application/json"})
       public OperationResponse postGatewayStatus(@PathVariable String gatewayMac, @RequestBody List<StatusMessage> statuses, HttpServletRequest req) {
           OperationResponse resp = new OperationResponse();

   //        int i = 0;
   //        for (Status status:statuses
   //             ) {
   //            status.setGatewayMac(gatewayMac.toUpperCase());
   //            Status r = statusRepository.save(status);
   //            LOG.debug("statuses[" +i + "]" + status.toString());
   //            i++;
   //        }
           if(gatewayMac != null)
               gatewayMac = gatewayMac.toUpperCase();

           statusService.handleEvenet(gatewayMac, statuses);

           resp.setStatus(200);
           resp.setMessage("Record Added");
           return resp;
       }
   */
    @ApiOperation(value = "Gets current status information", response = OperationResponse.class)
    @RequestMapping(value="/statuses/{uuid}", method=RequestMethod.GET, produces = {"application/json"})
    public OperationResponse getStatus(@PathVariable String uuid, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

        Status status =statusRepository.findOneByUuid(uuid).orElse(null);

        if (status != null) {
            resp.setStatus(200);
            resp.setData(status);
        } else {
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        }

        return resp;
    }

    @ApiOperation(value = "Edit current status information", response = OperationResponse.class)
    @RequestMapping(value="/statuses/{uuid}", method=RequestMethod.PUT)
    public OperationResponse putStatus(@PathVariable String uuid, @RequestBody Status status) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

        if (this.statusRepository.exists(uuid) ){
            status.setUuid(uuid);
//            status.setUserId(loggedInUserId);
            Status r = this.statusRepository.save(status);
            resp.setStatus(200);
            resp.setMessage("Record Updated");
            resp.setData(r);
        }
        else{
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        }

        return resp;
    }

    @ApiOperation(value = "Delete current status", response = OperationResponse.class)
    @RequestMapping(value="/statuses/{uuid}", method=RequestMethod.DELETE)
    public OperationResponse deleteStatus(@PathVariable String uuid) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

        if (this.statusRepository.exists(uuid) ){
            Status r =statusRepository.findOneByUuid(uuid).orElse(null);
            this.statusRepository.delete(uuid);
            resp.setStatus(200);
            resp.setMessage("Record Deleted");
            resp.setData(r);
        }
        else{
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        }
        return resp;
    }

}