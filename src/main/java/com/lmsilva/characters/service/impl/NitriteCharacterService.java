package com.lmsilva.characters.service.impl;

import com.lmsilva.characters.service.ICharacterService;
import com.lmsilva.characters.swagger.model.*;
import com.lmsilva.characters.swagger.model.Character;
import org.dizitart.no2.Nitrite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

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
        return db.getRepository(Comic.class).find().toList(); // TODO: add filtering and pagination
    }

    @Override
    public List<Event> eventsByCharacterId(Integer characterId, String name, String nameStartsWith,
                                           String modifiedSince, List<Integer> creators, List<Integer> series,
                                           List<Integer> comics, List<Integer> stories, List<String> orderBy,
                                           Integer limit, Integer offset) {
        return db.getRepository(Event.class).find().toList(); // TODO: add filtering and pagination
    }

    @Override
    public List<Series> seriesByCharacterId(Integer characterId, String title, String titleStartsWith,
                                            Integer startYear, String modifiedSince, List<Integer> comics,
                                            List<Integer> stories, List<Integer> events, List<Integer> creators,
                                            String seriesType, List<String> contains, List<String> orderBy,
                                            Integer limit, Integer offset) {
        return db.getRepository(Series.class).find().toList(); // TODO: add filtering and pagination
    }

    @Override
    public List<Story> storiesbyCharacterId(Integer characterId, String modifiedSince, List<Integer> comics,
                                            List<Integer> series, List<Integer> events, List<Integer> creators,
                                            List<String> orderBy, Integer limit, Integer offset) {
        return db.getRepository(Story.class).find().toList(); // TODO: add filtering and pagination
    }
}
