package com.siuuuuu.commodeami.user.query.service;

import com.siuuuuu.commodeami.common.exception.CommonException;
import com.siuuuuu.commodeami.common.exception.ErrorCode;
import com.siuuuuu.commodeami.user.command.aggregate.dto.UserDTO;
import com.siuuuuu.commodeami.user.command.aggregate.entity.CustomUser;
import com.siuuuuu.commodeami.user.command.aggregate.entity.User;
import com.siuuuuu.commodeami.user.query.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User existingUser = userMapper.findByEmail(username);

        if (existingUser == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new CustomUser(existingUser, grantedAuthorities);
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userMapper.findByEmail(email);


        if (user == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setProfile(user.getProfile());
        userDTO.setNickname(user.getNickname());
        userDTO.setGender(user.getGender());
        userDTO.setBirthDate(user.getBirthDate());
        userDTO.setCreatedAt(user.getCreatedAt());

        return userDTO;
    }

    @Override
    public UserDTO findById(Long userId) {
        User user = userMapper.findByUserId(userId);

        if (user == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setProfile(user.getProfile());
        userDTO.setNickname(user.getNickname());
        userDTO.setGender(user.getGender());
        userDTO.setBirthDate(user.getBirthDate());
        userDTO.setCreatedAt(user.getCreatedAt());

        return userDTO;
    }

}
