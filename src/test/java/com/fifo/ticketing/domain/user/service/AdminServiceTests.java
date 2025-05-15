package com.fifo.ticketing.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.fifo.ticketing.domain.user.dto.UserDto;
import com.fifo.ticketing.domain.user.entity.Role;
import com.fifo.ticketing.domain.user.entity.User;
import com.fifo.ticketing.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class AdminServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;

    private Pageable pageable;

    private List<User> userList;

    @BeforeEach
    void setUp() {

        pageable = PageRequest.of(0, 5);

        userList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            userList.add(User.builder()
                .id((long) i)
                .username("test" + i)
                .email("test" + i + "@test.com")
                .build());
        }
    }

    @Test
    void view_all_users() {
        Page<User> userPage = new PageImpl<>(userList, pageable, userList.size());

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserDto> allUsers = adminService.getAllUsers(pageable);

        assertThat(allUsers.getTotalElements()).isEqualTo(3);
        assertThat(allUsers.getContent().getFirst().getUsername()).isEqualTo("test0");
    }

    @Test
    void search_by_username() {
        Page<User> userPage = new PageImpl<>(userList, pageable, userList.size());
        when(userRepository.findByUsernameContaining("test", pageable)).thenReturn(userPage);

        Page<UserDto> nameData = adminService.getUsersByName(pageable, "test");

        assertThat(nameData.getTotalElements()).isEqualTo(3);
        assertThat(nameData.getContent().getFirst().getUsername()).isEqualTo("test0");
    }

    @Test
    void search_by_role() {
        Page<User> userPage = new PageImpl<>(userList, pageable, userList.size());
        when(userRepository.findByRole(Role.USER, pageable)).thenReturn(userPage);

        Page<UserDto> roleData = adminService.getUsersByRole(pageable, Role.USER);

        assertThat(roleData.getTotalElements()).isEqualTo(3);
        assertThat(roleData.getContent().getFirst().getUsername()).isEqualTo("test0");
    }

    @Test
    void search_by_username_and_role() {
        Page<User> userPage = new PageImpl<>(userList, pageable, userList.size());
        when(
            userRepository.findByUsernameContainingAndRole("test", Role.USER, pageable)).thenReturn(
            userPage);

        Page<UserDto> nameAndRoleData = adminService.getUsersByRoleAndName(pageable, Role.USER,
            "test");

        assertThat(nameAndRoleData.getContent()).hasSize(3);
        assertThat(nameAndRoleData.getContent().getFirst().getUsername()).startsWith("test");
    }

    @Test
    void check_user_update() {
        User user = User.builder()
            .id(1L)
            .email("test@test.com")
            .username("test")
            .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        adminService.updateUserStatus(user.getId());

        assertThat(user.isBlocked()).isTrue();

    }
}