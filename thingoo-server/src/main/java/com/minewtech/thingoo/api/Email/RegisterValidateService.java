package com.minewtech.thingoo.api.Email;

import com.minewtech.thingoo.util.Md5Util;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;

public class RegisterValidateService {
    public static void  processregister(String email) {

        ///如果处于安全，可以将激活码处理的更复杂点，这里我稍做简单处理
        //user.setValidateCode(MD5Tool.MD5Encrypt(email));
        String validateCode = Md5Util.encode2hex(email);
        ///邮件的内容
        StringBuffer sb = new StringBuffer("点击下面链接激活账号，48小时生效，否则重新注册账号，链接只能使用一次，请尽快激活！：");
        sb.append("\r\n");
        sb.append("http://iot.beaconyun.com/#/activate?email=");
        sb.append(email);
        sb.append("&validateCode=");
        sb.append(validateCode);
        //发送邮件
        try {
            SendEmail.send(email, sb.toString());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println("发送邮件");
    }
}
