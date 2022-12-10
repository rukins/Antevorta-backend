package com.antevorta.model.converter;

import com.antevorta.model.onlinestorecredentials.OnlineStoreCredentials;
import com.antevorta.security.encryptor.Encryptor;
import com.antevorta.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
@Component
public class OnlineStoreCredentialsConverter implements AttributeConverter<OnlineStoreCredentials, String> {

    private final Encryptor encryptor;

    @Autowired
    public OnlineStoreCredentialsConverter(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public String convertToDatabaseColumn(OnlineStoreCredentials onlineStoreCredentials) {
        return encryptor.encrypt(
                JsonUtils.convertToString(onlineStoreCredentials)
        );
    }

    @Override
    public OnlineStoreCredentials convertToEntityAttribute(String json) {
        return JsonUtils.convertToObject(encryptor.decrypt(json), OnlineStoreCredentials.class);
    }
}
