package vn.hcmute.appfood.services.Impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import vn.hcmute.appfood.utils.Constant;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {
    private final JavaMailSender mailSender;
    private final StringRedisTemplate sRedisTemplate;

    @Value("${spring.mail.username}")
    private String fromMail;

    public OtpService(JavaMailSender mailSender, StringRedisTemplate sRedisTemplate) {
        this.mailSender = mailSender;
        this.sRedisTemplate = sRedisTemplate;
    }

    public String generateOtp(String key) {
        //Xóa OTP cũ nếu có
        sRedisTemplate.delete(key);
        SecureRandom random = new SecureRandom();
        String otp = String.valueOf(random.nextInt(900000) + 100000); // 6 so OTP
        sRedisTemplate.opsForValue().set(key, otp, Constant.OTP_EXPIRE_TIME, TimeUnit.MINUTES);
        return otp;
    }

    public boolean validateOtp(String key, String otp) {
        String storedOtp = sRedisTemplate.opsForValue().get(key);
        if (storedOtp != null && storedOtp.equals(otp)) {
            sRedisTemplate.delete(key); // Xoa OTP khi xac thuc thanh cong
            return true;
        }
        return false;
    }

    public void sendOTP(String toMail, String otp) throws MessagingException, UnsupportedEncodingException {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            //Câu hinh noi dung mail
            helper.setFrom(fromMail, "NN Food App"); //Ten nguoi gui
            helper.setTo(toMail.trim()); //mail nguoi nhan
            helper.setSubject("YOUR OTP CODE"); //Tieu de mail
            helper.setText("Your OTP code: "+otp); //Noi dung mail

            //Gui mail
            mailSender.send(message);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
