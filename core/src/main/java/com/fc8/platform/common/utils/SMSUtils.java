package com.fc8.platform.common.utils;

import com.fc8.platform.common.properties.KeyProperties;
import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SMSUtils {

    @Value("${coolsms.api.key}")
    private String key;

    @Value("${coolsms.api.secret}")
    private String secret;

    private DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        messageService = NurigoApp.INSTANCE.initialize(key, secret, KeyProperties.SMS_DOMAIN);
    }

    public void sendVerificationMessage(String to, String verificationCode) {
        Message message = new Message();
        message.setFrom(KeyProperties.SMS_SENDER);
        message.setTo(to);
        message.setText("[아파트너] 인증 번호 : [ " + verificationCode + " ]");

        this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }

}
