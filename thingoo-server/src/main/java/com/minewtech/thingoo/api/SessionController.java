package com.minewtech.thingoo.api;

import com.minewtech.thingoo.model.session.SessionItem;
import com.minewtech.thingoo.model.session.SessionResponse;
import com.minewtech.thingoo.model.user.LoginRequest;
import com.minewtech.thingoo.model.user.User;
import com.minewtech.thingoo.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/*
This is a dummy rest controller, for the purpose of documentation (/session) path is map to a filter
 - This will only be invoked if security is disabled
 - If Security is enabled then SessionFilter.java is invoked
 - Enabling and Disabling Security is done at config/applicaton.properties 'security.ignored=/**'
*/

@RestController
@Api(tags = {"Authentication"})
public class SessionController {

    @Autowired
    private UserRepository userRepository;

    @ApiResponses(value = { @ApiResponse(code = 200, message = "Will return a security token, which must be passed in every request", response = SessionResponse.class) })
    @RequestMapping(value = "/token", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SessionResponse newSession(@RequestBody LoginRequest login, HttpServletRequest request, HttpServletResponse response) {
        System.out.format("\n /Token Called username=%s\n", login.getUsername());
        User user = userRepository.findOneByUuidAndPassword(login.getUsername(), login.getPassword()).orElse(null);
        System.out.println(user);
        SessionResponse resp = new SessionResponse();
        SessionItem sessionItem = new SessionItem();

        if (user != null){
            System.out.format("\n /User Details=%s\n", user.getName());
            sessionItem.setToken("xxx.xxx.xxx");
            sessionItem.setUserId(user.getUuid());
            sessionItem.setName(user.getName());
            sessionItem.setEmail(user.getEmail());
            //sessionItem.setRole(user.getRole());

            resp.setStatus(200);
            resp.setMessage("Dummy Login Success");
            resp.setData(sessionItem);
      }
      else{
            resp.setStatus(400);
            resp.setMessage("Login Failed");
      }

      return resp;
  }

}
