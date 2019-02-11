package com.minewtech.thingoo.api.operation;

import com.minewtech.thingoo.api.user.UserService;
import com.minewtech.thingoo.model.operation.Operation;
import com.minewtech.thingoo.model.response.OperationResponse;
import com.minewtech.thingoo.model.response.PageResponse;
import com.minewtech.thingoo.repository.OperationRepository;
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

@RestController
@Api(tags = {"Operation"})
public class OperationController {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Gets current operation information", response = Page.class)
    @RequestMapping(value="/operations", method= RequestMethod.GET)
    public PageResponse getOperatinList(
            @ApiParam(value = "" ) @PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable){
        PageResponse resp = new PageResponse();
        String loggedInUserId = userService.getLoggedInUserId();

        Page<Operation> r;

        r = operationRepository.findByUserId(loggedInUserId, pageable);

        resp.setPageStats(r,true);

        return resp;
    }



    @ApiOperation(value = "Add new operation", response = OperationResponse.class)
    @RequestMapping(value = "/operations", method = RequestMethod.POST, produces = {"application/json"})
    public OperationResponse postOperatin(@RequestBody Operation operation, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

        operation.setUserId(loggedInUserId);

        Operation r = operationRepository.save(operation);

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

    @ApiOperation(value = "Gets current operation information", response = OperationResponse.class)
    @RequestMapping(value="/operations/{uuid}", method=RequestMethod.GET, produces = {"application/json"})
    public OperationResponse getOperation(@PathVariable String uuid, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

        Operation operation =operationRepository.findOneByUuid(uuid).orElse(null);

        if (operation != null) {
            resp.setStatus(200);
            resp.setData(operation);
        } else {
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        }

        return resp;
    }

    @ApiOperation(value = "Edit current operation information", response = OperationResponse.class)
    @RequestMapping(value="/operations/{uuid}", method=RequestMethod.PUT)
    public OperationResponse putOperatin(@PathVariable String uuid, @RequestBody Operation operation) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

        if (this.operationRepository.exists(uuid) ){
            operation.setUuid(uuid);
            operation.setUserId(loggedInUserId);
            Operation r = this.operationRepository.save(operation);
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

    @ApiOperation(value = "Delete current operation", response = OperationResponse.class)
    @RequestMapping(value="/operations/{uuid}", method=RequestMethod.DELETE)
    public OperationResponse deleteOperatin(@PathVariable String uuid) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

        if (this.operationRepository.exists(uuid) ){
            Operation r =operationRepository.findOneByUuid(uuid).orElse(null);
            this.operationRepository.delete(uuid);
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