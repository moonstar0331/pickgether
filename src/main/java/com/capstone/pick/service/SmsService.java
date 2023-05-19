package com.capstone.pick.service;

import com.capstone.pick.repository.cache.SmsCacheRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.MessageType;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class SmsService {

    @Value("${spring.cool.api.key}")
    private String api_key;

    @Value("${spring.cool.api.secret}")
    private String api_secret;

    @Value("${spring.cool.from.number}")
    private String fromPhoneNumber;

    private final SmsCacheRepository smsCacheRepository;

    public void send(String toPhoneNumber) {
        Random rand = new Random();
        String certNum = "";

        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            certNum += ran;
        }

        DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(api_key, api_secret, "https://api.coolsms.co.kr");
        Message message = new Message();
        message.setFrom(fromPhoneNumber); // TODO: 문자인증을 진행하기 위해서는 자신의 핸드폰 번호를 입력하여 테스트 진행
        message.setType(MessageType.SMS);
        message.setTo(toPhoneNumber);
        message.setText("인증번호는 [" + certNum + "] 입니다.");

        try {
            messageService.sendOne(new SingleMessageSendingRequest(message));
        } catch (Exception e) {
            e.printStackTrace();
        }

        smsCacheRepository.setSmsCertification(toPhoneNumber, certNum);
    }

    public void certification(String toPhoneNumber, String userCheckNumber) throws Exception {
        if(isVerify(toPhoneNumber, userCheckNumber)) {
            throw new Exception("인증번호가 일치하지 않습니다.");
        }

        smsCacheRepository.deleteSmsCertification(toPhoneNumber);
    }

    public boolean isVerify(String toPhoneNumber, String userCheckNumber) {
        return !(smsCacheRepository.hasKey(toPhoneNumber))
                && smsCacheRepository.getSmsCertification(toPhoneNumber).equals(userCheckNumber);
    }
}
