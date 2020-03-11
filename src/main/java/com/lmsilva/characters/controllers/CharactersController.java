package com.lmsilva.characters.controllers;

import com.lmsilva.characters.swagger.ApiException;
import com.lmsilva.characters.swagger.api.PublicApi;
import com.lmsilva.characters.swagger.model.*;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
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
        MarvelAuth auth = new MarvelAuth(PUBLIC_KEY, PRIVATE_KEY);

        return apiClient
                .getCreatorCollection(auth.getPublicKey(), auth.getHash(), auth.getTimestamp(),
                        null, null, null, null, null,
                        null, null, null, null, null)
                .getData();
    }

    @RequestMapping("/v1/public/characters/{characterId}")
    public CharacterDataContainer getCharacterIndividual(@PathVariable Integer characterId) throws NoSuchAlgorithmException, UnsupportedEncodingException, ApiException {
        MarvelAuth auth = new MarvelAuth(PUBLIC_KEY, PRIVATE_KEY);

        return apiClient
                .getCharacterIndividual(characterId, auth.getPublicKey(), auth.getHash(), auth.getTimestamp())
                .getData();
    }

    @RequestMapping("/v1/public/characters/{characterId}/comics")
    public ComicDataContainer getCharacterComics(@PathVariable Integer characterId) throws ApiException, UnsupportedEncodingException, NoSuchAlgorithmException {
        MarvelAuth auth = new MarvelAuth(PUBLIC_KEY, PRIVATE_KEY);
        return apiClient.getComicsCharacterCollection(characterId, auth.getPublicKey(), auth.getHash(), auth.getTimestamp(),
                null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, null, null, null).getData();
    }

    @RequestMapping("/v1/public/characters/{characterId}/events")
    public EventDataContainer getCharacterEvents(@PathVariable Integer characterId) throws UnsupportedEncodingException, NoSuchAlgorithmException, ApiException {
        MarvelAuth auth = new MarvelAuth(PUBLIC_KEY, PRIVATE_KEY);

        return apiClient
                .getCharacterEventsCollection(characterId, auth.getPublicKey(), auth.getHash(), auth.getTimestamp(),
                        null, null, null, null, null, null,
                        null, null, null, null)
                .getData();
    }

    @RequestMapping("/v1/public/characters/{characterId}/series")
    public SeriesDataContainer getCharacterSeries(@PathVariable Integer characterId) throws UnsupportedEncodingException, NoSuchAlgorithmException, ApiException {
        MarvelAuth auth = new MarvelAuth(PUBLIC_KEY, PRIVATE_KEY);

        return apiClient
                .getCharacterSeriesCollection(characterId, auth.getPublicKey(), auth.getHash(), auth.getTimestamp(),
                        null, null, null, null, null, null,
                        null, null, null, null, null, null, null)
                .getData();
    }

    @RequestMapping("/v1/public/characters/{characterId}/stories")
    public StoryDataContainer getCharacterStories(@PathVariable Integer characterId) throws UnsupportedEncodingException, NoSuchAlgorithmException, ApiException {
        MarvelAuth auth = new MarvelAuth(PUBLIC_KEY, PRIVATE_KEY);

        return apiClient
                .getCharacterStoryCollection(characterId, auth.getPublicKey(), auth.getHash(), auth.getTimestamp(),
                        null, null, null, null, null, null, null, null)
                .getData();
    }

    private static class MarvelAuth {
        private String hash;
        private Integer timestamp;
        private String publicKey;

        public MarvelAuth(String publicKey, String privateKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            long timestamp = System.currentTimeMillis()/1000; // ugly hack to fit into an int
            // ugly hack to get an Integer
            String ts = String.valueOf(timestamp);
            String hashBase = ts + privateKey + publicKey;

            this.publicKey = publicKey;
            this.timestamp = Integer.parseInt(ts);
            this.hash = Hex.encodeHexString(md5.digest(hashBase.getBytes("UTF-8")));
        }

        public String getHash() {
            return hash;
        }

        public Integer getTimestamp() {
            return timestamp;
        }

        public String getPublicKey() {
            return publicKey;
        }
    }
}
