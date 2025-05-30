package com.example.login.service.Impl;





import com.example.login.service.VerificationCodeService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;



import java.util.concurrent.TimeUnit;


@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String senderEmail;
    

    // 验证码缓存的键前缀
    private static final String VERIFICATION_CODE_PREFIX = "VerificationCodeWithEmail:";
    // 日志记录器
    private static final Logger logger = LogManager.getLogger(VerificationCodeServiceImpl.class);

    @Autowired
    public VerificationCodeServiceImpl(JavaMailSender javaMailSender,
                                       RedisTemplate<String, Object> redisTemplate,
                                        SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.redisTemplate = redisTemplate;
        this.templateEngine = templateEngine;
    }

    // 发送邮件验证码
    public void sendVerificationCodeWithEmail(String target_email, String random_code) {
        try {
            // 创建 MimeMessage 对象用于发送 HTML 邮件
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            // 设置发件人、收件人、主题等
            helper.setFrom(senderEmail);
            helper.setTo(target_email);
            helper.setSubject("验证您的电子邮件");

            // 创建 Thymeleaf 上下文并设置变量
            Context context = new Context();
            context.setVariable("code", random_code);

            // 使用 Thymeleaf 模板引擎渲染 HTML
            String htmlContent = templateEngine.process("email", context); // email 是模板名称，对应 email.html

            // 发送 HTML 邮件
            helper.setText(htmlContent, true); // 第二个参数为 true 表示内容是 HTML 格式
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            logger.error("邮件发送失败", e);
            throw new RuntimeException("邮件验证码发送失败，请稍后再试。", e);
        }
    }



    // 发送短信验证码
    @Override
    public void sendVerificationCodeWithPhone(String targetPhone, String random_code) {

    }


    // 存储验证码
    // 设置有效期为3分钟, 设置键值对  键:"VerificationCodeWithEmail:" + email_phone  值:random_code
    @Override
    public void storeVerificationCodeToRedis(String email_phone, String random_code){
        redisTemplate.opsForValue().set(
                "VerificationCodeWithEmail:" + email_phone,
                random_code,
                300, TimeUnit.SECONDS
        );

    }

    // 验证验证码
    @Override
    public boolean validateVerificationCode(String email_phone, String input_code) {
    // 根据键:VerificationCodeWithEmail+email_phone  取出存在redis中的验证码
    Object storedCodeObject = redisTemplate.opsForValue().get(VERIFICATION_CODE_PREFIX + email_phone);
    String storedCodeString = storedCodeObject.toString();

    if (!storedCodeString.equals(input_code)) {
        logger.warn("验证码错误");
        return false;
    }

    // 验证码验证成功并清除缓存
    redisTemplate.delete(VERIFICATION_CODE_PREFIX + email_phone);
    logger.info("账号:{}验证成功",  email_phone);

    return true;
}




































}
