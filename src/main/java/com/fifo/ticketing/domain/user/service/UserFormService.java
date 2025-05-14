package com.fifo.ticketing.domain.user.service;

import com.fifo.ticketing.domain.user.dto.form.SignUpForm;
import com.fifo.ticketing.domain.user.dto.form.UserFormDetails;
import com.fifo.ticketing.domain.user.entity.User;
import com.fifo.ticketing.domain.user.repository.UserRepository;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserFormService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void save(SignUpForm signUpForm) {
        if (userRepository.findByEmail(signUpForm.email()).isPresent()) {
            throw new ErrorException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        User user = User.builder()
            .username(signUpForm.username())
            .password(passwordEncoder.encode(signUpForm.password()))
            .email(signUpForm.email())
            .build();
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User findUser = userOptional.orElseThrow(
            () -> new UsernameNotFoundException("잘못된 이메일 혹은 비밀번호 입니다.")
        );
        return new UserFormDetails(findUser);
    }
}
