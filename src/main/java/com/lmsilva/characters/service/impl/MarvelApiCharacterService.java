package com.lmsilva.characters.service.impl;

import com.lmsilva.characters.service.ICharacterService;
import com.lmsilva.characters.swagger.ApiException;
import com.lmsilva.characters.swagger.api.PublicApi;
import com.lmsilva.characters.swagger.model.*;
import com.lmsilva.characters.swagger.model.Character;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service(value="MarvelApi")
public class MarvelApiCharacterService implements ICharacterService {

    private static final PublicApi apiClient = new PublicApi();
    @Value("${marvel.api.publicKey}")
    private String PUBLIC_KEY;
    @Value("${marvel.api.privateKey}")
    private String PRIVATE_KEY;

    @Override
    public List<Character> allCharacters(String name, String nameStartsWith, String modifiedSince, List<Integer> comics, List<Integer> series, List<Integer> events, List<Integer> stories, List<String> orderBy, Integer limit, Integer offset) {
        MarvelAuth auth = new MarvelAuth(PUBLIC_KEY, PRIVATE_KEY);

        try {
            return apiClient
                    .getCreatorCollection(auth.getPublicKey(), auth.getHash(), auth.getTimestamp(),
                            null, null, null, null, null,
                            null, null, null, null, null)
                    .getData().getResults();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Character characterById(Integer characterId) {
        MarvelAuth auth = new MarvelAuth(PUBLIC_KEY, PRIVATE_KEY);

        try {
            return apiClient
                    .getCharacterIndividual(characterId, auth.getPublicKey(), auth.getHash(), auth.getTimestamp())
                    .getData().getResults().get(0);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Comic> comicsByCharacterId(Integer characterId, String format, String formatType,
                                           Boolean noVariants, String dateDescriptor, List<Integer> dateRange, String title,
                                           String titleStartsWith, Integer startYear, Integer issueNumber, String diamondCode,
                                           Integer digitalId, String upc, String isbn, String ean, String issn, Boolean hasDigitalIssue,
                                           String modifiedSince, List<Integer> creators, List<Integer> series, List<Integer> events,
                                           List<Integer> stories, List<Integer> sharedAppearances, List<Integer> collaborators,
                                           List<String> orderBy, Integer limit, Integer offset) {

        MarvelAuth auth = new MarvelAuth(PUBLIC_KEY, PRIVATE_KEY);

        try {
            return apiClient.getComicsCharacterCollection(characterId, auth.getPublicKey(), auth.getHash(), auth.getTimestamp(),
                    format, formatType, noVariants, dateDescriptor, dateRange, title, titleStartsWith,
                    startYear, issueNumber, diamondCode, digitalId, upc, isbn, ean, issn,
                    hasDigitalIssue, modifiedSince, creators, series, events, stories, sharedAppearances,
                    collaborators, orderBy, limit, offset)
                    .getData().getResults();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Event> eventsByCharacterId(Integer characterId, String name, String nameStartsWith,
                                           String modifiedSince, List<Integer> creators, List<Integer> series, List<Integer> comics,
                                           List<Integer> stories, List<String> orderBy, Integer limit, Integer offset) {
        MarvelAuth auth = new MarvelAuth(PUBLIC_KEY, PRIVATE_KEY);

        try {
            return apiClient
                    .getCharacterEventsCollection(characterId, auth.getPublicKey(), auth.getHash(), auth.getTimestamp(),
                        name, nameStartsWith, modifiedSince, creators, series, comics, stories, orderBy, limit, offset)
                    .getData().getResults();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Series> seriesByCharacterId(Integer characterId, String title, String titleStartsWith,
                                            Integer startYear, String modifiedSince, List<Integer> comics, List<Integer> stories,
                                            List<Integer> events, List<Integer> creators, String seriesType, List<String> contains,
                                            List<String> orderBy, Integer limit, Integer offset) {
        MarvelAuth auth = new MarvelAuth(PUBLIC_KEY, PRIVATE_KEY);

        try {
            return apiClient
                    .getCharacterSeriesCollection(characterId, auth.getPublicKey(), auth.getHash(), auth.getTimestamp(),
                            title, titleStartsWith, startYear, modifiedSince, comics, stories, events, creators,
                            seriesType, contains, orderBy, limit, offset)
                    .getData().getResults();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Story> storiesbyCharacterId(Integer characterId, String modifiedSince, List<Integer> comics,
                                            List<Integer> series, List<Integer> events, List<Integer> creators, List<String> orderBy,
                                            Integer limit, Integer offset) {
        MarvelAuth auth = new MarvelAuth(PUBLIC_KEY, PRIVATE_KEY);

        try {
            return apiClient
                    .getCharacterStoryCollection(characterId, auth.getPublicKey(), auth.getHash(), auth.getTimestamp(),
                            modifiedSince, comics, series, events, creators, orderBy, limit, offset)
                    .getData().getResults();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    private static class MarvelAuth {
        private String hash;
        private Integer timestamp;
        private String publicKey;

        public MarvelAuth(String publicKey, String privateKey) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                long timestamp = System.currentTimeMillis() / 1000; // ugly hack to fit into an int
                // ugly hack to get an Integer
                String ts = String.valueOf(timestamp);
                String hashBase = ts + privateKey + publicKey;

                this.publicKey = publicKey;
                this.timestamp = Integer.parseInt(ts);
                this.hash = Hex.encodeHexString(md5.digest(hashBase.getBytes(StandardCharsets.UTF_8)));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
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
