package com.lmsilva.characters.service.impl;

import com.lmsilva.characters.service.ICharacterService;
import com.lmsilva.characters.swagger.model.*;
import com.lmsilva.characters.swagger.model.Character;
import org.dizitart.no2.Nitrite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;
import static org.dizitart.no2.objects.filters.ObjectFilters.in;

@Service(value="Nitrite")
public class NitriteCharacterService implements ICharacterService {
    @Autowired
    Nitrite db;

    @Override
    public List<Character> allCharacters(String name, String nameStartsWith, String modifiedSince,
                                         List<Integer> comics, List<Integer> series, List<Integer> events,
                                         List<Integer> stories, List<String> orderBy,
                                         Integer limit, Integer offset) {
        return db.getRepository(Character.class).find().toList(); // TODO: add filtering and pagination
    }

    @Override
    public Character characterById(Integer characterId) {
        return db.getRepository(Character.class).find(eq("id", characterId)).firstOrDefault();
    }

    @Override
    public List<Comic> comicsByCharacterId(Integer characterId, String format, String formatType,
                                           Boolean noVariants, String dateDescriptor, List<Integer> dateRange,
                                           String title, String titleStartsWith, Integer startYear,
                                           Integer issueNumber, String diamondCode, Integer digitalId, String upc,
                                           String isbn, String ean, String issn, Boolean hasDigitalIssue,
                                           String modifiedSince, List<Integer> creators, List<Integer> series,
                                           List<Integer> events, List<Integer> stories,
                                           List<Integer> sharedAppearances, List<Integer> collaborators,
                                           List<String> orderBy, Integer limit, Integer offset) {
        Character character = characterById(characterId);
        List<ComicSummary> comics = character.getComics().getItems();
        Set<Integer> ids = new HashSet<>();
        Pattern pattern = Pattern.compile(".*/v1/public/comics/(\\d+)");
        for (ComicSummary comic : comics) {
            String baseUri = comic.getResourceURI();
            Matcher matcher = pattern.matcher(baseUri);
            if (!matcher.find()) {
                throw new RuntimeException("Looks like Marvel changed their URL structure");
            }
            ids.add(Integer.valueOf(matcher.group(1)));
        }
        List<Comic> results = db.getRepository(Comic.class).find(in("id", ids.toArray())).toList();
        return results; // TODO: add filtering and pagination
    }

    @Override
    public List<Event> eventsByCharacterId(Integer characterId, String name, String nameStartsWith,
                                           String modifiedSince, List<Integer> creators, List<Integer> series,
                                           List<Integer> comics, List<Integer> stories, List<String> orderBy,
                                           Integer limit, Integer offset) {
        Character character = characterById(characterId);
        List<EventSummary> events = character.getEvents().getItems();
        Set<Integer> ids = new HashSet<>();
        Pattern pattern = Pattern.compile(".*/v1/public/events/(\\d+)");
        for (EventSummary event : events) {
            String baseUri = event.getResourceURI();
            Matcher matcher = pattern.matcher(baseUri);
            if (!matcher.find()) {
                throw new RuntimeException("Looks like Marvel changed their URL structure");
            }
            ids.add(Integer.valueOf(matcher.group(1)));
        }
        List<Event> results = db.getRepository(Event.class).find(in("id", ids.toArray())).toList();
        return results; // TODO: add filtering and pagination
    }

    @Override
    public List<Series> seriesByCharacterId(Integer characterId, String title, String titleStartsWith,
                                            Integer startYear, String modifiedSince, List<Integer> comics,
                                            List<Integer> stories, List<Integer> events, List<Integer> creators,
                                            String seriesType, List<String> contains, List<String> orderBy,
                                            Integer limit, Integer offset) {
        Character character = characterById(characterId);
        List<SeriesSummary> seriesList = character.getSeries().getItems();
        Set<Integer> ids = new HashSet<>();
        Pattern pattern = Pattern.compile(".*/v1/public/series/(\\d+)");
        for (SeriesSummary series : seriesList) {
            String baseUri = series.getResourceURI();
            Matcher matcher = pattern.matcher(baseUri);
            if (!matcher.find()) {
                throw new RuntimeException("Looks like Marvel changed their URL structure");
            }
            ids.add(Integer.valueOf(matcher.group(1)));
        }
        List<Series> results = db.getRepository(Series.class).find(in("id", ids.toArray())).toList();
        return results; // TODO: add filtering and pagination
    }

    @Override
    public List<Story> storiesbyCharacterId(Integer characterId, String modifiedSince, List<Integer> comics,
                                            List<Integer> series, List<Integer> events, List<Integer> creators,
                                            List<String> orderBy, Integer limit, Integer offset) {
        Character character = characterById(characterId);
        List<StorySummary> stories = character.getStories().getItems();
        Set<Integer> ids = new HashSet<>();
        Pattern pattern = Pattern.compile(".*/v1/public/stories/(\\d+)");
        for (StorySummary story : stories) {
            String baseUri = story.getResourceURI();
            Matcher matcher = pattern.matcher(baseUri);
            if (!matcher.find()) {
                throw new RuntimeException("Looks like Marvel changed their URL structure");
            }
            ids.add(Integer.valueOf(matcher.group(1)));
        }
        List<Story> results = db.getRepository(Story.class).find(in("id", ids.toArray())).toList();
        return results; // TODO: add filtering and pagination
    }
}
