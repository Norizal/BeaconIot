package com.minewtech.thingoo.api.user;

import com.google.common.base.Strings;
import com.minewtech.thingoo.api.Email.RegisterValidateService;
import com.minewtech.thingoo.model.response.OperationResponse;
import com.minewtech.thingoo.model.response.PageResponse;
import com.minewtech.thingoo.model.user.User;
import com.minewtech.thingoo.model.user.UserResponse;
import com.minewtech.thingoo.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = {"User"})
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Gets current user information", response = Page.class)
    @RequestMapping(value="/users", method=RequestMethod.GET)
    public PageResponse getUserList(
            @ApiParam(value = ""    )               @RequestParam(value = "page"  ,  defaultValue="1"   ,  required = false) Integer page,
            @ApiParam(value = "between 1 to 1000" ) @RequestParam(value = "size"  ,  defaultValue="20"  ,  required = false) Integer size,
            @ApiParam(value = "" ) @RequestParam(value = "sort"  ,  defaultValue="created_at"  ,  required = false) String sort,
            Pageable pageable) {
        PageResponse resp = new PageResponse();
        String loggedInUserId = userService.getLoggedInUserId();
        resp.setLoggedInUserId(loggedInUserId);

        Page<User> r = userRepository.findByUserId(loggedInUserId, pageable);

        resp.setPageStats(r,true);

        return resp;
    }

    @ApiOperation(value = "Add new user", response = OperationResponse.class)
    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = {"application/json"})
    public OperationResponse postUser(@RequestBody User user, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        User u = userService.getUserByUuid(user.getUuid());

        //user.setUserId(loggedInUserId);
        //resp.setLoggedInUserId(loggedInUserId);
        user.setActive(true);
        //System.out.println("u"+u);

        //System.out.println(user);
        User r = userRepository.save(user);

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

    @ApiOperation(value = "Add new user", response = OperationResponse.class)
    @RequestMapping(value = "/account/register", method = RequestMethod.POST, produces = {"application/json"})
    public OperationResponse newUser(@RequestBody User user, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
       String email= user.getEmail();
        User u=userRepository.findOneByEmail(email).orElseGet( () -> new User());
        System.out.println("user:=="+u);
        if (u.getEmail()==""){
        user.setUserId("0cbafa88e7a94776bc8bb3fa5bfe58e3");
        User r = userRepository.save(user);
            RegisterValidateService.processregister(email);
        if (r.getUuid() != "") {
            resp.setStatus(200);
            resp.setMessage("Record Added");
            resp.setData(r);
        } else {
            resp.setStatus(400);
            resp.setMessage("Unable to add Record");
        }}else {
                resp.setStatus(450);
                resp.setMessage("This account exists");
            }
        return resp;
    }


    @ApiOperation(value = "Gets current user information", response = OperationResponse.class)
    @RequestMapping(value="/users/{uuid}", method=RequestMethod.GET, produces = {"application/json"})
    public OperationResponse getUser(@PathVariable String uuid, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
        resp.setLoggedInUserId(loggedInUserId);

        User user;
        boolean provideUserDetails = false;

        if (Strings.isNullOrEmpty(uuid)) {
            provideUserDetails = true;
            user = userService.getLoggedInUser();
        } else if (loggedInUserId.equals(uuid)) {
            provideUserDetails = true;
            user = userService.getLoggedInUser();
        } else {
            //Check if the current user is superuser then provide the details of requested user
            provideUserDetails = true;
            user =userRepository.findOneByUuid(uuid).orElse(null);
        }

        if (provideUserDetails && user != null) {
            resp.setStatus(200);
            resp.setData(user);
        } else if (user == null) {
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        } else {
            resp.setStatus(401);
            resp.setMessage("No Access");
        }

        return resp;
    }

    @ApiOperation(value = "Edit current user information", response = OperationResponse.class)
    @RequestMapping(value="/users/{uuid}", method=RequestMethod.PUT)
    public OperationResponse putUser(@PathVariable String uuid, @RequestBody User user) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
        resp.setLoggedInUserId(loggedInUserId);

        if (this.userRepository.exists(uuid) ){
            user.setUuid(uuid);
            user.setActive(true);
            User r = this.userRepository.save(user);
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

    @ApiOperation(value = "Delete current user", response = OperationResponse.class)
    @RequestMapping(value="/users/{uuid}", method=RequestMethod.DELETE)
    public OperationResponse deleteUser(@PathVariable String uuid) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
        resp.setLoggedInUserId(loggedInUserId);

        if (this.userRepository.exists(uuid) ){
            User r =userRepository.findOneByUuid(uuid).orElse(null);
            this.userRepository.delete(uuid);
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
    @RequestMapping(value = "/account/activate", method = RequestMethod.GET)
    public OperationResponse actiUser(HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String email=req.getParameter("email");
        User u=userRepository.findOneByEmail(email).orElseGet( () -> new User());

        System.out.println(u);
        if (u!=null){
            if (u.isActive()==false)
                    u.setActive(true);
                User r = userRepository.save(u);

            if (r.getUuid() != "") {
                resp.setStatus(200);
                resp.setMessage("Record Added");
            } else {
                resp.setStatus(400);
                resp.setMessage("Unable to add Record");
            }}else {
            resp.setStatus(450);
            resp.setMessage("The mailbox is not registered (the mailbox address does not exist)!");
        }
        return resp;
    }


}
