package io.mercury.library.ignite.user.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.mercury.library.ignite.user.model.Person;
import io.mercury.library.ignite.user.model.ReqPerson;
import io.mercury.library.ignite.user.model.RespResult;
import io.mercury.library.ignite.user.model.Role;
import io.mercury.library.ignite.user.service.PersonService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 **/
@RestController
public class PersonController {

    @Resource
    private PersonService personService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(HttpServletRequest request) throws UnknownHostException {
        System.out.println(request.getRemoteAddr());
        System.out.println(request.getRemoteHost());
        System.out.println(InetAddress.getLocalHost().getHostAddress());
//        String ip = null;
//
//        //X-Forwarded-For：Squid 服务代理
//        String ipAddresses = request.getHeader("X-Forwarded-For");
//
//        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//            //Proxy-Client-IP：apache 服务代理
//            ipAddresses = request.getHeader("Proxy-Client-IP");
//        }
//
//        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//            //WL-Proxy-Client-IP：weblogic 服务代理
//            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
//        }
//
//        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//            //HTTP_CLIENT_IP：有些代理服务器
//            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
//        }
//
//        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//            //X-Real-IP：nginx服务代理
//            ipAddresses = request.getHeader("X-Real-IP");
//        }
//
//        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
//        if (ipAddresses != null && ipAddresses.length() != 0) {
//            ip = ipAddresses.split(",")[0];
//        }
//
//        //还是不能获取到，最后再通过request.getRemoteAddr();获取
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//            ip = request.getRemoteAddr();
//        }
        return "";
    }


    /**
     * User register with whose username and password
     *
     * @param reqPerson ReqPerson
     * @return Success message
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RespResult<String> register(@RequestBody() ReqPerson reqPerson) throws ServletException {
        // Check if username and password is null
        if (Objects.equals(reqPerson.username(), "") || reqPerson.username() == null
                || Objects.equals(reqPerson.password(), "") || reqPerson.password() == null)
            throw new ServletException("Username or Password invalid!");

        // Check if the username is used
        if (personService.findPersonByUsername(reqPerson.username()) != null)
            throw new ServletException("Username is used!");

        // Give a default role : MEMBER
        List<Role> roles = new ArrayList<>();
        roles.add(Role.MEMBER);

        // Create a person in ignite
        personService.save(new Person(reqPerson.username(), reqPerson.password(), roles));

        return new RespResult<>("201 CREATED", "register success", "");
    }

    /**
     * Check user`s login info, then create a jwt token returned to front end
     *
     * @param reqPerson ReqPerson
     * @return jwt token
     * @throws ServletException e
     */
    @PostMapping
    public RespResult<String> login(@RequestBody ReqPerson reqPerson) throws ServletException {
        // Check if username and password is null
        if (Objects.equals(reqPerson.username(), "") || reqPerson.username() == null
                || Objects.equals(reqPerson.password(), "") || reqPerson.password() == null)
            throw new ServletException("Please fill in username and password");

        // Check if the username is used
        if (personService.findPersonByUsername(reqPerson.username()) == null
                || !reqPerson.password().equals(personService.findPersonByUsername(reqPerson.username()).getPassword())) {
            throw new ServletException("Please fill in username and password");
        }

        // Create Twt token
        String jwtToken = Jwts.builder()
                .setSubject(reqPerson.username())
                .claim("roles", "member")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey")
                .compact();

        return new RespResult<>("200 OK", "login success", jwtToken);
    }
}
