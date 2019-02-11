package com.minewtech.thingoo.api.gateway;

import com.minewtech.thingoo.api.user.UserService;
import com.minewtech.thingoo.model.gateway.Gateway;
import com.minewtech.thingoo.model.response.OperationResponse;
import com.minewtech.thingoo.model.response.PageResponse;
import com.minewtech.thingoo.repository.GatewayRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api(tags = {"Gateway"})
public class GatewayController {

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Gets current gateway information", response = Page.class)
    @RequestMapping(value="/gateways", method= RequestMethod.GET)
    public PageResponse getGatewayList(
            @ApiParam(value = "" ) @RequestParam(value = "user"  ,  required = false) String user,
            @ApiParam(value = "" ) @PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable){
        PageResponse resp = new PageResponse();
        String loggedInUserId = userService.getLoggedInUserId();

        Page<Gateway> r;

        if (user != null && !user.isEmpty()){
            r = gatewayRepository.findByUserId(user, pageable);
        } else {
            r = gatewayRepository.findByUserId(loggedInUserId, pageable);
        }

        resp.setPageStats(r,true);

        return resp;
    }



    @ApiOperation(value = "Add new gateway", response = OperationResponse.class)
    @RequestMapping(value = "/gateways", method = RequestMethod.POST, produces = {"application/json"})
    public OperationResponse postGateway(@RequestBody Gateway gateway, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);
        List<Gateway> gateway1= gatewayRepository.findByMac(gateway.getMac());
        if (gateway1.size()==0){
        gateway.setUserId(loggedInUserId);
        Gateway r = gatewayRepository.save(gateway);
        if (r.getUuid() != "") {
            resp.setStatus(200);
            resp.setMessage("Record Added");
            resp.setData(r);
        } else {
            resp.setStatus(400);
            resp.setMessage("Unable to add Record");
        }}else {
            resp.setStatus(404);
            resp.setMessage("The gateway has been bound by other users");
        }
        return resp;
    }

    @ApiOperation(value = "Gets current gateway information", response = OperationResponse.class)
    @RequestMapping(value="/gateways/{uuid}", method=RequestMethod.GET, produces = {"application/json"})
    public OperationResponse getGateway(@PathVariable String uuid, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

        Gateway gateway = null;

        if (uuid != null && (uuid.length() == 12)){ // Check if Mac Address
            gateway = gatewayRepository.findOneByMac(uuid).orElse(null);
        } else if (uuid != null){
            gateway = gatewayRepository.findOneByUuid(uuid).orElse(null);
        }

        if (gateway != null) {
            resp.setStatus(200);
            resp.setData(gateway);
        } else {
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        }

        return resp;
    }

    @ApiOperation(value = "Edit current gateway information", response = OperationResponse.class)
    @RequestMapping(value="/gateways/{uuid}", method=RequestMethod.PUT)
    public OperationResponse putGateway(@PathVariable String uuid, @RequestBody Gateway gateway) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

        if (this.gatewayRepository.exists(uuid) ){
            gateway.setUuid(uuid);
            gateway.setUserId(loggedInUserId);
            Gateway r = this.gatewayRepository.save(gateway);
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

    @ApiOperation(value = "Delete current gateway", response = OperationResponse.class)
    @RequestMapping(value="/gateways/{uuid}", method=RequestMethod.DELETE)
    public OperationResponse deleteGateway(@PathVariable String uuid) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

        if (this.gatewayRepository.exists(uuid) ){
            Gateway r =gatewayRepository.findOneByUuid(uuid).orElse(null);
            this.gatewayRepository.delete(uuid);
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