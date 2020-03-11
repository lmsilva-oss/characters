package com.lmsilva.characters.controllers;

import com.lmsilva.characters.service.ICharacterService;
import com.lmsilva.characters.swagger.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/v1/public/characters")
public class CharactersController {
    @Autowired
    @Qualifier("MarvelApi")
    ICharacterService characterService;

    @RequestMapping("/")
    // TODO: add query parameters
    public CharacterDataWrapper get(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String nameStartsWith,
            @RequestParam(required = false) String modifiedSince,
            @RequestParam(required = false) List<Integer> comics,
            @RequestParam(required = false) List<Integer> series,
            @RequestParam(required = false) List<Integer> events,
            @RequestParam(required = false) List<Integer> stories,
            @RequestParam(required = false) List<String> orderBy,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer offset) {
        return characterService.allCharacters(name, nameStartsWith, modifiedSince, comics,
                series, events, stories, orderBy, limit, offset);
    }

    @RequestMapping("/{characterId}")
    public CharacterDataWrapper getCharacterIndividual(@PathVariable Integer characterId) {
        return characterService.characterById(characterId);
    }

    @RequestMapping("/{characterId}/comics")
    public ComicDataWrapper getCharacterComics(@PathVariable Integer characterId,
                                               @RequestParam(required = false) String format,
                                               @RequestParam(required = false) String formatType,
                                               @RequestParam(required = false) Boolean noVariants,
                                               @RequestParam(required = false) String dateDescriptor,
                                               @RequestParam(required = false) List<Integer> dateRange,
                                               @RequestParam(required = false) String title,
                                               @RequestParam(required = false) String titleStartsWith,
                                               @RequestParam(required = false) Integer startYear,
                                               @RequestParam(required = false) Integer issueNumber,
                                               @RequestParam(required = false) String diamondCode,
                                               @RequestParam(required = false) Integer digitalId,
                                               @RequestParam(required = false) String upc,
                                               @RequestParam(required = false) String isbn,
                                               @RequestParam(required = false) String ean,
                                               @RequestParam(required = false) String issn,
                                               @RequestParam(required = false) Boolean hasDigitalIssue,
                                               @RequestParam(required = false) String modifiedSince,
                                               @RequestParam(required = false) List<Integer> creators,
                                               @RequestParam(required = false) List<Integer> series,
                                               @RequestParam(required = false) List<Integer> events,
                                               @RequestParam(required = false) List<Integer> stories,
                                               @RequestParam(required = false) List<Integer> sharedAppearances,
                                               @RequestParam(required = false) List<Integer> collaborators,
                                               @RequestParam(required = false) List<String> orderBy,
                                               @RequestParam(required = false) Integer limit,
                                               @RequestParam(required = false) Integer offset) {
        return characterService.comicsByCharacterId(characterId, format, formatType, noVariants, dateDescriptor, dateRange, title, titleStartsWith,
                startYear, issueNumber, diamondCode, digitalId, upc, isbn, ean, issn,
                hasDigitalIssue, modifiedSince, creators, series, events, stories, sharedAppearances,
                collaborators, orderBy, limit, offset);
    }

    @RequestMapping("/{characterId}/events")
    public EventDataWrapper getCharacterEvents(@PathVariable Integer characterId, @RequestParam(required = false) String name,
                                               @RequestParam(required = false) String nameStartsWith,
                                               @RequestParam(required = false) String modifiedSince,
                                               @RequestParam(required = false) List<Integer> creators,
                                               @RequestParam(required = false) List<Integer> series,
                                               @RequestParam(required = false) List<Integer> comics,
                                               @RequestParam(required = false) List<Integer> stories,
                                               @RequestParam(required = false) List<String> orderBy,
                                               @RequestParam(required = false) Integer limit,
                                               @RequestParam(required = false) Integer offset) {
        return characterService.eventsByCharacterId(characterId, name, nameStartsWith, modifiedSince, creators, series,
                comics, stories, orderBy, limit, offset);
    }

    @RequestMapping("/{characterId}/series")
    public SeriesDataWrapper getCharacterSeries(@PathVariable Integer characterId,
                                                @RequestParam(required = false) String title,
                                                @RequestParam(required = false) String titleStartsWith,
                                                @RequestParam(required = false) Integer startYear,
                                                @RequestParam(required = false) String modifiedSince,
                                                @RequestParam(required = false) List<Integer> comics,
                                                @RequestParam(required = false) List<Integer> stories,
                                                @RequestParam(required = false) List<Integer> events,
                                                @RequestParam(required = false) List<Integer> creators,
                                                @RequestParam(required = false) String seriesType,
                                                @RequestParam(required = false) List<String> contains,
                                                @RequestParam(required = false) List<String> orderBy,
                                                @RequestParam(required = false) Integer limit,
                                                @RequestParam(required = false) Integer offset) {
        return characterService.seriesByCharacterId(characterId, title, titleStartsWith, startYear, modifiedSince,
                comics, stories, events, creators, seriesType, contains, orderBy, limit, offset);
    }

    @RequestMapping("/{characterId}/stories")
    public StoryDataWrapper getCharacterStories(@PathVariable Integer characterId,
                                                @RequestParam(required = false) String modifiedSince,
                                                @RequestParam(required = false) List<Integer> comics,
                                                @RequestParam(required = false) List<Integer> series,
                                                @RequestParam(required = false) List<Integer> events,
                                                @RequestParam(required = false) List<Integer> creators,
                                                @RequestParam(required = false) List<String> orderBy,
                                                @RequestParam(required = false) Integer limit,
                                                @RequestParam(required = false) Integer offset) {
        return characterService.storiesbyCharacterId(characterId, modifiedSince, comics, series, events, creators,
                orderBy, limit, offset);
    }
}
