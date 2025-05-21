package com.fifo.ticketing.domain.user.service;

import com.fifo.ticketing.domain.user.dto.UserDto;
import com.fifo.ticketing.domain.user.entity.Role;
import com.fifo.ticketing.domain.user.entity.User;
import com.fifo.ticketing.domain.user.mapper.UserMapper;
import com.fifo.ticketing.domain.user.repository.UserRepository;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<User> allUserInfo = userRepository.findAll(pageable);
        return UserMapper.toUserDtoPage(allUserInfo);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getUsersByName(Pageable pageable, String name) {
        Page<User> byUsernameContaining = userRepository.findByUsernameContaining(name, pageable);
        return UserMapper.toUserDtoPage(byUsernameContaining);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getUsersByRole(Pageable pageable, Role role) {
        Page<User> byRole = userRepository.findByRole(role, pageable);
        return UserMapper.toUserDtoPage(byRole);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getUsersByRoleAndName(Pageable pageable, Role role, String name) {
        Page<User> byUsernameContainingAndRole = userRepository.findByUsernameContainingAndRole(
            name, role, pageable);
        return UserMapper.toUserDtoPage(byUsernameContainingAndRole);
    }

    @Transactional
    public void updateUserStatus(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
            new ErrorException(ErrorCode.NOT_FOUND_MEMBER)
        );
        user.updateBlockedState();
    }

}
