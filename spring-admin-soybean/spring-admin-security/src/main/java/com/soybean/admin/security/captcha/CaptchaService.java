package com.soybean.admin.security.captcha;

import com.soybean.admin.security.utils.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final RedisCache redisCache;

    private static final String CAPTCHA_KEY_PREFIX = "captcha:";
    private static final int CAPTCHA_EXPIRE_MINUTES = 5;
    private static final int CAPTCHA_WIDTH = 120;
    private static final int CAPTCHA_HEIGHT = 40;
    private static final int CAPTCHA_CODE_COUNT = 4;
    private static final int CAPTCHA_LINE_COUNT = 20;

    /**
     * 生成验证码
     */
    public CaptchaResult generateCaptcha() {
        // 生成UUID作为验证码ID
        String uuid = UUID.randomUUID().toString();
        String key = CAPTCHA_KEY_PREFIX + uuid;

        // 生成验证码文本
        String code = generateCode();

        // 存储到Redis
        redisCache.setCacheObject(key, code.toLowerCase(), CAPTCHA_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 生成验证码图片
        String imageBase64 = generateImage(code);

        log.debug("Captcha generated: uuid={}", uuid);

        return new CaptchaResult(uuid, imageBase64);
    }

    /**
     * 验证验证码
     */
    public boolean verifyCaptcha(String uuid, String code) {
        if (uuid == null || code == null) {
            return false;
        }

        String key = CAPTCHA_KEY_PREFIX + uuid;
        String cachedCode = redisCache.getCacheObject(key);

        if (cachedCode == null) {
            log.warn("Captcha expired or not found: uuid={}", uuid);
            return false;
        }

        // 验证成功后删除验证码
        redisCache.deleteObject(key);

        boolean result = cachedCode.equalsIgnoreCase(code);
        if (!result) {
            log.warn("Captcha verification failed: uuid={}, input={}, expected={}", uuid, code, cachedCode);
        }

        return result;
    }

    /**
     * 生成验证码文本
     */
    private String generateCode() {
        String chars = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < CAPTCHA_CODE_COUNT; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

    /**
     * 生成验证码图片
     */
    private String generateImage(String code) {
        BufferedImage image = new BufferedImage(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 设置抗锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 填充背景
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, CAPTCHA_WIDTH, CAPTCHA_HEIGHT);

        // 绘制干扰线
        Random random = new Random();
        for (int i = 0; i < CAPTCHA_LINE_COUNT; i++) {
            g.setColor(getRandomColor(150, 250));
            int x1 = random.nextInt(CAPTCHA_WIDTH);
            int y1 = random.nextInt(CAPTCHA_HEIGHT);
            int x2 = random.nextInt(CAPTCHA_WIDTH);
            int y2 = random.nextInt(CAPTCHA_HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        // 绘制验证码
        int codeWidth = CAPTCHA_WIDTH / (code.length() + 1);
        for (int i = 0; i < code.length(); i++) {
            g.setColor(getRandomColor(50, 150));
            g.setFont(new Font("Arial", Font.BOLD, 30));
            int x = (i + 1) * codeWidth - 10;
            int y = CAPTCHA_HEIGHT / 2 + 10;
            g.drawString(String.valueOf(code.charAt(i)), x, y);
        }

        g.dispose();

        // 转换为Base64
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            log.error("Failed to generate captcha image", e);
            throw new RuntimeException("Failed to generate captcha image", e);
        }
    }

    /**
     * 获取随机颜色
     */
    private Color getRandomColor(int min, int max) {
        Random random = new Random();
        int r = min + random.nextInt(max - min);
        int g = min + random.nextInt(max - min);
        int b = min + random.nextInt(max - min);
        return new Color(r, g, b);
    }

    /**
     * 验证码结果
     */
    public static class CaptchaResult {
        private final String uuid;
        private final String image;

        public CaptchaResult(String uuid, String image) {
            this.uuid = uuid;
            this.image = image;
        }

        public String getUuid() {
            return uuid;
        }

        public String getImage() {
            return image;
        }
    }
}
