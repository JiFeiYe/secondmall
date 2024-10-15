package com.tu.mall.common.utils;

import cn.hutool.core.util.StrUtil;
import com.tu.mall.common.exception.CustomException;
import com.tu.mall.common.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author JiFeiYe
 * @since 2024/10/10
 */
@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailSender;

    /**
     * 校验参数
     *
     * @param to      收信人
     * @param subject 主题
     * @param text    内容
     */
    private void checkMailProperties(String to, String subject, String text) {
        if (StrUtil.isEmpty(to))
            throw new RuntimeException("收信人不能为空。");
        if (StrUtil.isEmpty(subject))
            throw new RuntimeException("主题不能为空。");
        if (StrUtil.isEmpty(text))
            throw new RuntimeException("内容不能为空。");
    }

    /**
     * 发送普通文本邮件（测试用）
     *
     * @param to      收信人
     * @param subject 主题
     * @param text    内容
     */
    public void sendTextMail(String to, String subject, String text) {
        try {
            checkMailProperties(to, subject, text);
            // true表示支持复杂类型
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);
            // 发信人
            mimeMessageHelper.setFrom(new InternetAddress(mailSender, "张三"));
            // 收信人
            mimeMessageHelper.setTo(to.split(","));
            // 设置主题
            mimeMessageHelper.setSubject(subject);
            // 设置内容
            mimeMessageHelper.setText(text);
            // 设置发送时间（现在）
            mimeMessageHelper.setSentDate(new Date());

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            System.out.println("邮件发送成功：" + mailSender + " --> " + to);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("邮件发送失败" + e.getMessage());
        }
    }

    /**
     * 发送HTML邮件
     *
     * @param to         收件人
     * @param verifyCode 验证码
     */
    public void sendHTMLMail(String to, String verifyCode) {
        try {
            // 创建 MimeMessageHelper 对象
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);
            // 设置发件人
            mimeMessageHelper.setFrom(new InternetAddress(mailSender, "张三"));
            // 设置收件人
            mimeMessageHelper.setTo(to.split(","));
            // 设置邮件主题
            mimeMessageHelper.setSubject("易电脑");
            // 设置邮件正文（HTML 格式）
            String htmlContent = "<html>" +
                    "<body>" +
                    "<p>尊敬的用户，</p>" +
                    "<p>您好！</p>" +
                    "<p>您正在尝试使用验证码功能。以下是您的验证码，请妥善保管并在有效期内使用。</p>" +
                    "<p><strong>验证码：<span style='color: blue;'>" + verifyCode + "</span></strong></p>" +
                    "<p>有效期限：5分钟</p>" +
                    "<p>请注意，为了您的账户安全，请勿将此验证码泄露给任何人。</p>" +
                    "</body>" +
                    "</html>";
            mimeMessageHelper.setText(htmlContent, true); // 第二个参数设置为 true 表示正文是 HTML 格式
            // 发送邮件
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            System.out.println("邮件发送成功：" + mailSender + " --> " + to);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new CustomException(ResultCodeEnum.EMAIL_FAIL);
        }
    }
}
