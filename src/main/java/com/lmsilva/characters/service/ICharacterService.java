package com.lmsilva.characters.service;

import com.lmsilva.characters.swagger.model.*;

import java.util.List;

public interface ICharacterService {

    public CharacterDataWrapper allCharacters(String name, String nameStartsWith, String modifiedSince, List<Integer> comics, List<Integer> series, List<Integer> events, List<Integer> stories, List<String> orderBy, Integer limit, Integer offset);

    public CharacterDataWrapper characterById(Integer characterId);

    public ComicDataWrapper comicsByCharacterId(Integer characterId, String format, String formatType, Boolean noVariants, String dateDescriptor, List<Integer> dateRange, String title, String titleStartsWith, Integer startYear, Integer issueNumber, String diamondCode, Integer digitalId, String upc, String isbn, String ean, String issn, Boolean hasDigitalIssue, String modifiedSince, List<Integer> creators, List<Integer> series, List<Integer> events, List<Integer> stories, List<Integer> sharedAppearances, List<Integer> collaborators, List<String> orderBy, Integer limit, Integer offset);

    public EventDataWrapper eventsByCharacterId(Integer characterId, String name, String nameStartsWith, String modifiedSince, List<Integer> creators, List<Integer> series, List<Integer> comics, List<Integer> stories, List<String> orderBy, Integer limit, Integer offset);

    public SeriesDataWrapper seriesByCharacterId(Integer characterId, String title, String titleStartsWith, Integer startYear, String modifiedSince, List<Integer> comics, List<Integer> stories, List<Integer> events, List<Integer> creators, String seriesType, List<String> contains, List<String> orderBy, Integer limit, Integer offset);

    public StoryDataWrapper storiesbyCharacterId(Integer characterId, String modifiedSince, List<Integer> comics, List<Integer> series, List<Integer> events, List<Integer> creators, List<String> orderBy, Integer limit, Integer offset);
}
