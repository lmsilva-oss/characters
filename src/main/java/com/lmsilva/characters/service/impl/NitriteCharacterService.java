package com.lmsilva.characters.service.impl;

import com.lmsilva.characters.service.ICharacterService;
import com.lmsilva.characters.swagger.model.Character;
import com.lmsilva.characters.swagger.model.*;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.SortOrder;
import org.dizitart.no2.objects.ObjectFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;
import static org.dizitart.no2.objects.filters.ObjectFilters.in;

@Service(value="Nitrite")
public class NitriteCharacterService implements ICharacterService {
    public static final String COMICS_ID_REGEX_BASE = ".*/v1/public/comics/";
    public static final String EVENTS_ID_REGEX_BASE = ".*/v1/public/events/";
    public static final String SERIES_ID_REGEX_BASE = ".*/v1/public/series/";
    public static final String STORIES_ID_REGEX_BASE = ".*/v1/public/stories/";
    @Autowired
    Nitrite db;

    private static FindOptions createFindOptions(List<String> orderBy, Integer limit, Integer offset) {
        FindOptions options = null;
        if (orderBy != null) {
            String orderByField = orderBy.get(0);
            if (orderByField.startsWith("-")) {
                options = FindOptions.sort(orderByField, SortOrder.Descending);
            } else {
                options = FindOptions.sort(orderByField, SortOrder.Ascending);
            }
        }

        if (limit != null) {
            if (offset == null) {
                offset = 0;
            }
            if (options != null) {
                options = options.thenLimit(offset, limit);
            } else {
                options = FindOptions.limit(offset, limit);
            }
        }
        return options;
    }

    @Override
    public List<Character> allCharacters(String name, String nameStartsWith, String modifiedSince,
                                         List<Integer> comics, List<Integer> series, List<Integer> events,
                                         List<Integer> stories, List<String> orderBy,
                                         Integer limit, Integer offset) {
        FindOptions options = createFindOptions(orderBy, limit, offset);

        List<Character> characters;
        if (options != null) {
            characters = db.getRepository(Character.class).find(options).toList();
        } else {
            characters = db.getRepository(Character.class).find().toList();
        }

        // filtering after the database has returned already
        // since nitrite isn't playing nicely with the current document nesting
        List<Character> auxList = new ArrayList<>();
        if (comics != null && !comics.isEmpty()) {
            int i = 0;
            while (i < comics.size()) {
                Pattern pattern = Pattern.compile(COMICS_ID_REGEX_BASE + comics.get(i++));
                auxList.addAll(characters.stream()
                        .filter(character -> character.getComics().getItems().stream()
                            .anyMatch(comicSummary ->
                                pattern.matcher(comicSummary.getResourceURI()).matches()))
                        .collect(Collectors.toList()));
            }
        }

        if (series != null && !series.isEmpty()) {
            int i = 0;
            while (i < series.size()) {
                Pattern pattern = Pattern.compile(SERIES_ID_REGEX_BASE + series.get(i++));
                auxList.addAll(characters.stream()
                        .filter(character -> character.getSeries().getItems().stream()
                                .anyMatch(seriesSummary ->
                                        pattern.matcher(seriesSummary.getResourceURI()).matches()))
                        .collect(Collectors.toList()));
            }
        }

        if (events != null && !events.isEmpty()) {
            int i = 0;
            while (i < events.size()) {
                Pattern pattern = Pattern.compile(EVENTS_ID_REGEX_BASE + events.get(i++));
                auxList.addAll(characters.stream()
                        .filter(character -> character.getEvents().getItems().stream()
                                .anyMatch(eventSummary ->
                                        pattern.matcher(eventSummary.getResourceURI()).matches()))
                        .collect(Collectors.toList()));
            }
        }

        if (stories != null && !stories.isEmpty()) {
            int i = 0;
            while (i < stories.size()) {
                Pattern pattern = Pattern.compile(STORIES_ID_REGEX_BASE + stories.get(i++));
                auxList.addAll(characters.stream()
                        .filter(character -> character.getStories().getItems().stream()
                                .anyMatch(storySummary ->
                                        pattern.matcher(storySummary.getResourceURI()).matches()))
                        .collect(Collectors.toList()));
            }
        }

        if (!auxList.isEmpty()) {
            characters = auxList;
        }

        return characters;
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
        Pattern pattern = Pattern.compile(COMICS_ID_REGEX_BASE + "(\\d+)");
        for (ComicSummary comic : comics) {
            String baseUri = comic.getResourceURI();
            Matcher matcher = pattern.matcher(baseUri);
            if (!matcher.find()) {
                throw new RuntimeException("Looks like Marvel changed their URL structure");
            }
            ids.add(Integer.valueOf(matcher.group(1)));
        }
        ObjectFilter filter = in("id", ids.toArray());
        FindOptions options = createFindOptions(orderBy, limit, offset);
        List<Comic> results;
        if (options != null) {
            results = db.getRepository(Comic.class).find(filter, options).toList();
        } else {
            results = db.getRepository(Comic.class).find(filter).toList();
        }
        return results; // TODO: still missing most parameter checks
    }

    @Override
    public List<Event> eventsByCharacterId(Integer characterId, String name, String nameStartsWith,
                                           String modifiedSince, List<Integer> creators, List<Integer> series,
                                           List<Integer> comics, List<Integer> stories, List<String> orderBy,
                                           Integer limit, Integer offset) {
        Character character = characterById(characterId);
        List<EventSummary> events = character.getEvents().getItems();
        Set<Integer> ids = new HashSet<>();
        Pattern pattern = Pattern.compile(EVENTS_ID_REGEX_BASE + "(\\d+)");
        for (EventSummary event : events) {
            String baseUri = event.getResourceURI();
            Matcher matcher = pattern.matcher(baseUri);
            if (!matcher.find()) {
                throw new RuntimeException("Looks like Marvel changed their URL structure");
            }
            ids.add(Integer.valueOf(matcher.group(1)));
        }
        ObjectFilter filter = in("id", ids.toArray());
        FindOptions options = createFindOptions(orderBy, limit, offset);
        List<Event> results;
        if (options != null) {
            results = db.getRepository(Event.class).find(filter, options).toList();
        } else  {
            results = db.getRepository(Event.class).find(filter).toList();
        }
        return results; // TODO: still missing most parameter checks
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
        Pattern pattern = Pattern.compile(SERIES_ID_REGEX_BASE + "(\\d+)");
        for (SeriesSummary series : seriesList) {
            String baseUri = series.getResourceURI();
            Matcher matcher = pattern.matcher(baseUri);
            if (!matcher.find()) {
                throw new RuntimeException("Looks like Marvel changed their URL structure");
            }
            ids.add(Integer.valueOf(matcher.group(1)));
        }
        ObjectFilter filter = in("id", ids.toArray());
        FindOptions options = createFindOptions(orderBy, limit, offset);
        List<Series> results;
        if (options != null) {
            results = db.getRepository(Series.class).find(filter, options).toList();
        }
        else {
            results = db.getRepository(Series.class).find(filter).toList();
        }
        return results; // TODO: still missing most parameter checks
    }

    @Override
    public List<Story> storiesbyCharacterId(Integer characterId, String modifiedSince, List<Integer> comics,
                                            List<Integer> series, List<Integer> events, List<Integer> creators,
                                            List<String> orderBy, Integer limit, Integer offset) {
        Character character = characterById(characterId);
        List<StorySummary> stories = character.getStories().getItems();
        Set<Integer> ids = new HashSet<>();
        Pattern pattern = Pattern.compile(STORIES_ID_REGEX_BASE + "(\\d+)");
        for (StorySummary story : stories) {
            String baseUri = story.getResourceURI();
            Matcher matcher = pattern.matcher(baseUri);
            if (!matcher.find()) {
                throw new RuntimeException("Looks like Marvel changed their URL structure");
            }
            ids.add(Integer.valueOf(matcher.group(1)));
        }
        ObjectFilter filter = in("id", ids.toArray());
        FindOptions options = createFindOptions(orderBy, limit, offset);
        List<Story> results;
        if (options!= null) {
            results = db.getRepository(Story.class).find(filter, options).toList();
        } else {
            results = db.getRepository(Story.class).find(filter).toList();
        }
        return results; // TODO: still missing most parameter checks
    }
}
