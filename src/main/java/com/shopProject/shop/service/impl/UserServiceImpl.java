package com.shopProject.shop.service.impl;


import com.shopProject.shop.dto.UserDTO;
import com.shopProject.shop.entity.User;
import com.shopProject.shop.repository.UserRepository;
import com.shopProject.shop.util.VarListUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private UserOTPRepository userOTPRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JavaMailSender emailSender;

    public String sendMail(
            String to, String subject, String text) throws MessagingException, IOException{

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("esoftassigmenets@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            System.out.println("------------------------------>");
            emailSender.send(message);
            System.out.println("------------------------------>");
            return VarListUtil.RSP_SUCCESS;
        }catch (Exception e){
            return VarListUtil.RSP_ERROR;
        }

    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
    }
    public UserDTO loadUserDetailsByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return modelMapper.map(user,UserDTO.class);
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoleCode().toString()));
        return authorities;
    }

    public String saveUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return VarListUtil.RSP_NO_DATA_FOUND;
        } else {
            userRepository.save(modelMapper.map(userDTO, User.class));
            return VarListUtil.RSP_SUCCESS;
        }
    }

    public String deleteUser(String username) {
        if (userRepository.existsByUsername(username)) {
            userRepository.deleteByUsername(username);
            return VarListUtil.RSP_SUCCESS;
        } else {
            return VarListUtil.RSP_NO_DATA_FOUND;
        }
    }
    public String updateUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            userRepository.updateUser(userDTO.getAddress(),userDTO.getEmail(),
                    userDTO.getIdPhoto(),userDTO.getName(),userDTO.getPassword(),
                    userDTO.getPhoneNo1(),userDTO.getPhoneNo2(),userDTO.getRemarks(),
                    userDTO.getRoleCode(),userDTO.getStatus(),userDTO.getUsername());
            return VarListUtil.RSP_SUCCESS;
        } else {
            return VarListUtil.RSP_NO_DATA_FOUND;
        }
    }

    public List<UserDTO> getAllUsers() {
        List<User> users=userRepository.findAll();
        return modelMapper.map(users, new TypeToken<ArrayList<UserDTO>>() {
        }.getType());
    }

    public UserDTO searchUser(String username) {
        if (userRepository.existsByUsername(username)) {
            User user=userRepository.findByUsername(username);
            return modelMapper.map(user,UserDTO.class);
        } else {
            return null;
        }
    }

    public String sendOTPCode(String email) throws IOException, MessagingException {

        String otp=getRandomNumberString();
        //send email
            UserOTPDTO userOTPDTO=new UserOTPDTO();
            userOTPDTO.setOtp(otp);
            userOTPDTO.setUseremail(email);
            if (userOTPRepository.existsByUseremail(email)){
                userOTPRepository.updateuserotp(otp,email);
            }else {
                userOTPRepository.save(modelMapper.map(userOTPDTO,UserOTP.class));
            }

            String response=sendMail(email,"OTP",otp);

        return response;
    }
    public String getOTPCode(String email)  {

        String otp = userOTPRepository.findByEmail(email);
        return otp;
    }
    // random number generator
    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

}
