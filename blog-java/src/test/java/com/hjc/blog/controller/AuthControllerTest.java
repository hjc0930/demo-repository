package com.hjc.blog.controller;

import com.hjc.blog.dto.LoginDto;
import com.hjc.blog.dto.RegisterDto;
import com.hjc.blog.service.AuthService;
import com.hjc.blog.vo.LoginVo;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * AuthController 单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("认证控制器测试")
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AuthController authController;

    private LoginDto loginRequest;
    private RegisterDto registerRequest;
    private LoginVo loginResponse;

    @BeforeEach
    void setUp() {
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

        // 登录响应
        loginResponse = LoginVo.builder()
                .token("mock.jwt.token")
                .userId(1L)
                .username("testuser")
                .nickname("测试用户")
                .avatar("http://example.com/avatar.jpg")
                .role("USER")
                .build();

        // 模拟request获取IP
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
    }

    @Test
    @DisplayName("登录成功")
    void testLogin_Success() {
        // Arrange
        when(authService.login(any(LoginDto.class), any(String.class))).thenReturn(loginResponse);

        // Act
        var result = authController.login(loginRequest, request);

        // Assert
        assertNotNull(result);
        assertEquals("20000", result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertNotNull(result.getData());
        assertEquals("mock.jwt.token", result.getData().getToken());
        assertEquals(1L, result.getData().getUserId());
        assertEquals("testuser", result.getData().getUsername());
        assertEquals("测试用户", result.getData().getNickname());
        assertEquals("USER", result.getData().getRole());

        verify(authService).login(any(LoginDto.class), any(String.class));
    }

    @Test
    @DisplayName("注册成功")
    void testRegister_Success() {
        // Arrange
        when(authService.register(any(RegisterDto.class))).thenReturn(loginResponse);

        // Act
        var result = authController.register(registerRequest);

        // Assert
        assertNotNull(result);
        assertEquals("20000", result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertNotNull(result.getData());
        assertEquals("mock.jwt.token", result.getData().getToken());
        assertEquals(1L, result.getData().getUserId());
        assertEquals("testuser", result.getData().getUsername());

        verify(authService).register(any(RegisterDto.class));
    }

    @Test
    @DisplayName("获取客户端IP - 从X-Forwarded-For获取")
    void testGetClientIp_FromXForwardedFor() {
        // Arrange
        when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.1");

        // Act
        var result = authController.login(loginRequest, request);

        // Assert
        assertNotNull(result);
        verify(authService).login(any(LoginDto.class), eq("192.168.1.1"));
    }

    @Test
    @DisplayName("获取客户端IP - 从X-Real-IP获取")
    void testGetClientIp_FromXRealIp() {
        // Arrange
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn("192.168.1.2");

        // Act
        var result = authController.login(loginRequest, request);

        // Assert
        assertNotNull(result);
        verify(authService).login(any(LoginDto.class), eq("192.168.1.2"));
    }

    @Test
    @DisplayName("获取客户端IP - 从RemoteAddr获取")
    void testGetClientIp_FromRemoteAddr() {
        // Arrange
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("192.168.1.3");

        // Act
        var result = authController.login(loginRequest, request);

        // Assert
        assertNotNull(result);
        verify(authService).login(any(LoginDto.class), eq("192.168.1.3"));
    }

    @Test
    @DisplayName("获取客户端IP - 处理多级代理")
    void testGetClientIp_MultipleProxies() {
        // Arrange
        when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.4, 192.168.1.5, 192.168.1.6");

        // Act
        var result = authController.login(loginRequest, request);

        // Assert
        assertNotNull(result);
        verify(authService).login(any(LoginDto.class), eq("192.168.1.4"));
    }

    @Test
    @DisplayName("获取客户端IP - 处理未知IP")
    void testGetClientIp_UnknownIp() {
        // Arrange
        when(request.getHeader("X-Forwarded-For")).thenReturn("unknown");
        when(request.getHeader("X-Real-IP")).thenReturn("unknown");
        when(request.getRemoteAddr()).thenReturn("192.168.1.7");

        // Act
        var result = authController.login(loginRequest, request);

        // Assert
        assertNotNull(result);
        verify(authService).login(any(LoginDto.class), eq("192.168.1.7"));
    }
}
