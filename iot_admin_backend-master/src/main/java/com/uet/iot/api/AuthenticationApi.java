package com.uet.iot.api;

import com.uet.iot.auth.AuthUserDetailService;
import com.uet.iot.auth.JwtUtil;
import com.uet.iot.base.BaseResponse;
import com.uet.iot.request.AuthenticationReq;
import com.uet.iot.res.AuthenticationRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Authentication api")
public class AuthenticationApi {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthUserDetailService authUserDetailService;

    @Autowired
    private JwtUtil jwtUtil;

//    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
//    @ApiOperation(value = "get all devices information api", response = ResponseEntity.class)
//    public ResponseEntity<BaseResponse> generateToken(@RequestBody AuthenticationReq authRequest) throws Exception {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
//            );
//        } catch (Exception ex) {
//            throw new Exception("inavalid username/password");
//        }
//
//        final UserDetails userDetails = authUserDetailService.loadUserByUsername(authRequest.getUsername());
//        return new ResponseEntity<>(new BaseResponse(new AuthenticationRes("Bearer " + jwtUtil.generateToken(userDetails))), HttpStatus.OK);
//    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @ApiOperation(value = "authentication api", response = ResponseEntity.class)
    public ResponseEntity<BaseResponse> generateToken(@RequestBody AuthenticationReq authenticationReq) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationReq.getUsername(), authenticationReq.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = authUserDetailService.loadUserByUsername(authenticationReq.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return new ResponseEntity<>(new BaseResponse(new AuthenticationRes("Bearer " + jwt)), HttpStatus.OK);
    }
}
