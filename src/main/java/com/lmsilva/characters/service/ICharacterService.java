package com.lmsilva.characters.service;

import com.lmsilva.characters.swagger.model.*;
import com.lmsilva.characters.swagger.model.Character;

import java.util.List;

public interface ICharacterService {

    public List<Character> allCharacters(String name, String nameStartsWith, String modifiedSince, List<Integer> comics, List<Integer> series, List<Integer> events, List<Integer> stories, List<String> orderBy, Integer limit, Integer offset);

    public Character characterById(Integer characterId);

    public List<Comic> comicsByCharacterId(Integer characterId, String format, String formatType, Boolean noVariants, String dateDescriptor, List<Integer> dateRange, String title, String titleStartsWith, Integer startYear, Integer issueNumber, String diamondCode, Integer digitalId, String upc, String isbn, String ean, String issn, Boolean hasDigitalIssue, String modifiedSince, List<Integer> creators, List<Integer> series, List<Integer> events, List<Integer> stories, List<Integer> sharedAppearances, List<Integer> collaborators, List<String> orderBy, Integer limit, Integer offset);

    public List<Event> eventsByCharacterId(Integer characterId, String name, String nameStartsWith, String modifiedSince, List<Integer> creators, List<Integer> series, List<Integer> comics, List<Integer> stories, List<String> orderBy, Integer limit, Integer offset);

    public List<Series> seriesByCharacterId(Integer characterId, String title, String titleStartsWith, Integer startYear, String modifiedSince, List<Integer> comics, List<Integer> stories, List<Integer> events, List<Integer> creators, String seriesType, List<String> contains, List<String> orderBy, Integer limit, Integer offset);

    public List<Story> storiesbyCharacterId(Integer characterId, String modifiedSince, List<Integer> comics, List<Integer> series, List<Integer> events, List<Integer> creators, List<String> orderBy, Integer limit, Integer offset);
}
