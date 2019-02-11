package com.minewtech.thingoo.api.device;

import com.minewtech.thingoo.api.user.UserService;
import com.minewtech.thingoo.model.device.Device;
import com.minewtech.thingoo.model.device.DeviceType;
import com.minewtech.thingoo.model.response.OperationResponse;
import com.minewtech.thingoo.model.response.PageResponse;
import com.minewtech.thingoo.repository.DeviceRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = {"Device"})
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Gets current device information", response = Page.class)
    @RequestMapping(value="/devices", method= RequestMethod.GET)
    public PageResponse getDeviceList(
            @ApiParam(value = "" ) @RequestParam(value = "type"  ,  required = false) DeviceType type,
            @ApiParam(value = "" ) @PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable){
        PageResponse resp = new PageResponse();
        String loggedInUserId = userService.getLoggedInUserId();

        Page<Device> r;

//        r = deviceRepository.findByUserId(loggedInUserId, pageable);
        r = deviceRepository.findAll(Specifications.where(getWhereClause(loggedInUserId, type)), pageable);

        resp.setPageStats(r,true);

        return resp;
    }

    private Specification<Device> getWhereClause(String userId, DeviceType type) {
        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(Root<Device> r, CriteriaQuery<?> q, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (type != null) {
                    predicate.getExpressions().add(
                            cb.equal(r.<DeviceType>get("type"), type)
                    );

                }

                if (userId != null && !userId.isEmpty()) {
                    predicate.getExpressions().add(
                            cb.equal(r.<String>get("userId"), userId)
                    );
                }
                return predicate;
            }
        };
    }

    @ApiOperation(value = "Add new device", response = OperationResponse.class)
    @RequestMapping(value = "/devices", method = RequestMethod.POST, produces = {"application/json"})
    public OperationResponse postDevice(@RequestBody Device device, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

        device.setUserId(loggedInUserId);

        Device r = deviceRepository.save(device);

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

    @ApiOperation(value = "Gets current device information", response = OperationResponse.class)
    @RequestMapping(value="/devices/{uuid}", method=RequestMethod.GET, produces = {"application/json"})
    public OperationResponse getDevice(@PathVariable String uuid, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);
        Device device = null;
        if (uuid != null && (uuid.length() == 12)){ // Check if Mac Address
            device = deviceRepository.findOneByMac(uuid).orElse(null);
        } else if (uuid != null){
            device = deviceRepository.findOneByUuid(uuid).orElse(null);
        }

        if (device != null) {
            resp.setStatus(200);
            resp.setData(device);
        } else {
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        }

        return resp;
    }

    @ApiOperation(value = "Edit current device information", response = OperationResponse.class)
    @RequestMapping(value="/devices/{uuid}", method=RequestMethod.PUT)
    public OperationResponse putDevice(@PathVariable String uuid, @RequestBody Device device) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

        if (this.deviceRepository.exists(uuid) ){
            device.setUuid(uuid);
            device.setUserId(loggedInUserId);
            Device r = this.deviceRepository.save(device);
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

    @ApiOperation(value = "Delete current device", response = OperationResponse.class)
    @RequestMapping(value="/devices/{uuid}", method=RequestMethod.DELETE)
    public OperationResponse deleteDevice(@PathVariable String uuid) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

        if (this.deviceRepository.exists(uuid) ){
            Device r =deviceRepository.findOneByUuid(uuid).orElse(null);
            this.deviceRepository.delete(uuid);
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