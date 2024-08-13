package com.shopProject.shop.controller;

import com.shopProject.shop.dto.AuthDTO;
import com.shopProject.shop.dto.ResponseDTO;
import com.shopProject.shop.dto.UserDTO;
import com.shopProject.shop.service.impl.UserServiceImpl;
import com.shopProject.shop.util.JwtUtil;
import com.shopProject.shop.util.VarListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ResponseDTO responseDTO;


    @PostMapping("/authenticate")
    public ResponseEntity<ResponseDTO> authenticate(@RequestBody UserDTO userDTO) throws Exception{
        System.out.println(userDTO);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDTO.getUsername(),
                            userDTO.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails
                = userService.loadUserByUsername(userDTO.getUsername());

        final UserDTO userDTO2=userService.loadUserDetailsByUsername(userDTO.getUsername());

        final String token =
                jwtUtil.generateToken(userDTO2);

        if (token!=null  && !token.isEmpty() ){

            UserDTO userDTO1=userService.loadUserDetailsByUsername(userDTO.getUsername());
            AuthDTO authDTO = new AuthDTO();
            authDTO.setUsername(userDTO1.getUsername());

            authDTO.setRoleCode(userDTO1.getRoleCode());
            authDTO.setToken(token);

            responseDTO.setCode(VarListUtil.RSP_SUCCESS);
            responseDTO.setMessage("Success");
            responseDTO.setContent(authDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }
        responseDTO.setCode(VarListUtil.RSP_ERROR);
        responseDTO.setMessage("User Name Not Found");
        responseDTO.setContent(null);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
