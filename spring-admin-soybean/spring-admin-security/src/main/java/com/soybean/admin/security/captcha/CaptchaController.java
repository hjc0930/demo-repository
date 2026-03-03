package com.soybean.admin.security.captcha;

import com.soybean.admin.common.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 验证码控制器
 */
@RestController
@RequestMapping("/api/auth/captcha")
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    /**
     * 获取验证码
     */
    @GetMapping
    public Result<CaptchaService.CaptchaResult> getCaptcha() {
        CaptchaService.CaptchaResult captcha = captchaService.generateCaptcha();
        return Result.success(captcha);
    }
}
