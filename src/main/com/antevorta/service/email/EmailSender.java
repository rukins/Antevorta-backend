package com.antevorta.service.email;

import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {
    @Getter
    @Value("${mailgun.domain}")
    private String domain;

    private final MailgunMessagesApi mailgunMessagesApi;

    @Autowired
    public EmailSender(@Value("${mailgun.base-url}") String baseUrl, @Value("${mailgun.private-api-key}") String privateApiKey) {
        this.mailgunMessagesApi = MailgunClient.config(baseUrl, privateApiKey)
                .createApi(MailgunMessagesApi.class);
    }
    

    public void send(Message message) {
        mailgunMessagesApi.sendMessage(domain, message);
    }
}
