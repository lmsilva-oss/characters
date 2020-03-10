package com.lmsilva.characters.controllers;

import com.lmsilva.characters.swagger.ApiException;
import com.lmsilva.characters.swagger.api.PublicApi;
import com.lmsilva.characters.swagger.model.CharacterDataContainer;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@RestController
@RequestMapping("/v1/public/characters")
public class CharactersController {
    private static final PublicApi apiClient = new PublicApi();
    @Value("${marvel.api.publicKey}")
    private String PUBLIC_KEY;
    @Value("${marvel.api.privateKey}")
    private String PRIVATE_KEY;

    @RequestMapping("/")
    // TODO: add query parameters
    public CharacterDataContainer get() throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        long timestamp = System.currentTimeMillis()/1000; // ugly hack to fit into an int
        // ugly hack to get an Integer
        String ts = String.valueOf(timestamp);
        Integer tsInt = Integer.parseInt(ts);

        String hashBase = ts + PRIVATE_KEY + PUBLIC_KEY;
        String hash = Hex.encodeHexString(md5.digest(hashBase.getBytes("UTF-8")));

        return apiClient
                .getCreatorCollection(PUBLIC_KEY, hash, tsInt,
                        null, null, null, null, null,
                        null, null, null, null, null)
                .getData();
    }

    // TODO: @RequestMapping('/v1/public/characters/{characterId}')
    // TODO: @RequestMapping('/v1/public/characters/{characterId}/comics')
    // TODO: @RequestMapping('/v1/public/characters/{characterId}/events')
    // TODO: @RequestMapping('/v1/public/characters/{characterId}/series')
    // TODO: @RequestMapping('/v1/public/characters/{characterId}/stories')
}
