package com.hjc.blog.service.impl;

import com.hjc.blog.common.exception.BusinessException;
import com.hjc.blog.common.result.ResultCodeEnum;
import com.hjc.blog.common.utils.JwtUtil;
import com.hjc.blog.dto.LoginDto;
import com.hjc.blog.dto.RegisterDto;
import com.hjc.blog.entity.User;
import com.hjc.blog.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * AuthService 单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("认证服务测试")
class AuthServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private User normalUser;
    private User lockedUser;
    private LoginDto loginRequest;
    private RegisterDto registerRequest;

    @BeforeEach
    void setUp() {
        // 正常用户
        normalUser = new User();
        normalUser.setId(1L);
        normalUser.setUsername("testuser");
        normalUser.setPassword("$2a$10$encodedPassword");
        normalUser.setEmail("test@example.com");
        normalUser.setNickname("测试用户");
        normalUser.setAvatar("http://example.com/avatar.jpg");
        normalUser.setRole("USER");
        normalUser.setStatus(1);

        // 被锁定的用户
        lockedUser = new User();
        lockedUser.setId(2L);
        lockedUser.setUsername("lockeduser");
        lockedUser.setPassword("$2a$10$encodedPassword");
        lockedUser.setStatus(0);

        // 登录请求
        loginRequest = new LoginDto();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("123456");

        // 注册请求
        registerRequest = new RegisterDto();
        registerRequest.setUsername("newuser");
        registerRequest.setPassword("123456");
        registerRequest.setEmail("new@example.com");
        registerRequest.setNickname("新用户");
    }

    @Test
    @DisplayName("登录成功 - 用户名登录")
    void testLogin_Success_ByUsername() {
        // Arrange
        when(userService.getByUsername("testuser")).thenReturn(normalUser);
        when(passwordEncoder.matches("123456", "$2a$10$encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(1L, "testuser", "USER")).thenReturn("mock.jwt.token");
        doNothing().when(userService).updateLastLoginInfo(1L, "127.0.0.1");

        // Act
        var response = authService.login(loginRequest, "127.0.0.1");

        // Assert
        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());
        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getUsername());
        assertEquals("测试用户", response.getNickname());
        assertEquals("USER", response.getRole());

        verify(userService).getByUsername("testuser");
        verify(passwordEncoder).matches("123456", "$2a$10$encodedPassword");
        verify(jwtUtil).generateToken(1L, "testuser", "USER");
        verify(userService).updateLastLoginInfo(1L, "127.0.0.1");
    }

    @Test
    @DisplayName("登录成功 - 邮箱登录")
    void testLogin_Success_ByEmail() {
        // Arrange
        loginRequest.setUsername("test@example.com");
        when(userService.getByUsername("test@example.com")).thenReturn(null);
        when(userService.getByEmail("test@example.com")).thenReturn(normalUser);
        when(passwordEncoder.matches("123456", "$2a$10$encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(1L, "testuser", "USER")).thenReturn("mock.jwt.token");
        doNothing().when(userService).updateLastLoginInfo(1L, "127.0.0.1");

        // Act
        var response = authService.login(loginRequest, "127.0.0.1");

        // Assert
        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());

        verify(userService).getByUsername("test@example.com");
        verify(userService).getByEmail("test@example.com");
    }

    @Test
    @DisplayName("登录失败 - 用户不存在")
    void testLogin_UserNotExist() {
        // Arrange
        when(userService.getByUsername("notexist")).thenReturn(null);
        when(userService.getByEmail("notexist")).thenReturn(null);
        loginRequest.setUsername("notexist");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(loginRequest, "127.0.0.1");
        });

        assertEquals(ResultCodeEnum.USER_NOT_EXIST.getCode(), exception.getCode());
        assertEquals(ResultCodeEnum.USER_NOT_EXIST.getMessage(), exception.getMessage());

        verify(userService).getByUsername("notexist");
        verify(userService).getByEmail("notexist");
        verify(passwordEncoder, never()).matches(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("登录失败 - 密码错误")
    void testLogin_PasswordError() {
        // Arrange
        when(userService.getByUsername("testuser")).thenReturn(normalUser);
        when(passwordEncoder.matches("wrongpassword", "$2a$10$encodedPassword")).thenReturn(false);
        loginRequest.setPassword("wrongpassword");

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(loginRequest, "127.0.0.1");
        });

        assertEquals(ResultCodeEnum.USER_PASSWORD_ERROR.getCode(), exception.getCode());
        assertEquals(ResultCodeEnum.USER_PASSWORD_ERROR.getMessage(), exception.getMessage());

        verify(passwordEncoder).matches("wrongpassword", "$2a$10$encodedPassword");
        verify(jwtUtil, never()).generateToken(any(), any(String.class), any(String.class));
    }

    @Test
    @DisplayName("登录失败 - 账号被锁定")
    void testLogin_AccountLocked() {
        // Arrange
        loginRequest.setUsername("lockeduser");
        when(userService.getByUsername("lockeduser")).thenReturn(lockedUser);
        when(passwordEncoder.matches("123456", "$2a$10$encodedPassword")).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(loginRequest, "127.0.0.1");
        });

        assertEquals(ResultCodeEnum.USER_ACCOUNT_LOCKED.getCode(), exception.getCode());
        assertEquals(ResultCodeEnum.USER_ACCOUNT_LOCKED.getMessage(), exception.getMessage());

        verify(passwordEncoder).matches("123456", "$2a$10$encodedPassword");
        verify(jwtUtil, never()).generateToken(any(), any(String.class), any(String.class));
    }

    @Test
    @DisplayName("注册成功")
    void testRegister_Success() {
        // Arrange
        when(userService.getByUsername("newuser")).thenReturn(null);
        when(userService.getByEmail("new@example.com")).thenReturn(null);
        when(passwordEncoder.encode("123456")).thenReturn("$2a$10$encodedNewPassword");
        // 使用 doAnswer 来设置保存后的用户ID
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(100L); // 模拟数据库生成的ID
            return true;
        }).when(userService).save(any(User.class));
        when(jwtUtil.generateToken(eq(100L), eq("newuser"), eq("USER"))).thenReturn("mock.jwt.token");

        // Act
        var response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());
        assertEquals("newuser", response.getUsername());

        verify(userService).getByUsername("newuser");
        verify(userService).getByEmail("new@example.com");
        verify(passwordEncoder).encode("123456");
        verify(userService).save(any(User.class));
        verify(jwtUtil).generateToken(eq(100L), eq("newuser"), eq("USER"));
    }

    @Test
    @DisplayName("注册成功 - 使用默认昵称")
    void testRegister_Success_WithDefaultNickname() {
        // Arrange
        registerRequest.setNickname(null);
        when(userService.getByUsername("newuser")).thenReturn(null);
        when(userService.getByEmail("new@example.com")).thenReturn(null);
        when(passwordEncoder.encode("123456")).thenReturn("$2a$10$encodedNewPassword");
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(101L);
            return true;
        }).when(userService).save(any(User.class));
        when(jwtUtil.generateToken(eq(101L), eq("newuser"), eq("USER"))).thenReturn("mock.jwt.token");

        // Act
        var response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("newuser", response.getNickname());
    }

    @Test
    @DisplayName("注册失败 - 用户名已存在")
    void testRegister_UsernameExist() {
        // Arrange
        when(userService.getByUsername("newuser")).thenReturn(normalUser);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.register(registerRequest);
        });

        assertEquals(ResultCodeEnum.USER_ACCOUNT_EXIST.getCode(), exception.getCode());
        assertEquals(ResultCodeEnum.USER_ACCOUNT_EXIST.getMessage(), exception.getMessage());

        verify(userService).getByUsername("newuser");
        verify(userService, never()).save(any(User.class));
    }

    @Test
    @DisplayName("注册失败 - 邮箱已存在")
    void testRegister_EmailExist() {
        // Arrange
        when(userService.getByUsername("newuser")).thenReturn(null);
        when(userService.getByEmail("new@example.com")).thenReturn(normalUser);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.register(registerRequest);
        });

        assertEquals(ResultCodeEnum.USER_EMAIL_EXIST.getCode(), exception.getCode());
        assertEquals(ResultCodeEnum.USER_EMAIL_EXIST.getMessage(), exception.getMessage());

        verify(userService).getByUsername("newuser");
        verify(userService).getByEmail("new@example.com");
        verify(userService, never()).save(any(User.class));
    }

    @Test
    @DisplayName("注册失败 - 保存用户失败")
    void testRegister_SaveFailed() {
        // Arrange
        when(userService.getByUsername("newuser")).thenReturn(null);
        when(userService.getByEmail("new@example.com")).thenReturn(null);
        when(passwordEncoder.encode("123456")).thenReturn("$2a$10$encodedNewPassword");
        when(userService.save(any(User.class))).thenReturn(false);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.register(registerRequest);
        });

        assertEquals(ResultCodeEnum.ERROR.getCode(), exception.getCode());
        assertEquals("注册失败，请稍后重试", exception.getMessage());

        verify(userService).save(any(User.class));
        verify(jwtUtil, never()).generateToken(any(), any(String.class), any(String.class));
    }
}
