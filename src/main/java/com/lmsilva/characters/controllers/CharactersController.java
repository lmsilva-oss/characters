package com.lmsilva.characters.controllers;

import com.lmsilva.characters.service.ICharacterService;
import com.lmsilva.characters.swagger.model.*;
import com.lmsilva.characters.swagger.model.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/v1/public/characters")
public class CharactersController {
    public static final String FORMATS = "(comic|magazine|trade paperback|hardcover|digest|graphic novel|digital comic|infinite comic)";
    public static final String INVALID_OR_UNRECOGNIZED_ORDERING_PARAMETER = "Invalid or unrecognized ordering parameter";
    public static final String INVALID_OR_UNRECOGNIZED_PARAMETER = "Invalid or unrecognized parameter";
    @Autowired
    @Qualifier("Nitrite")
    ICharacterService characterService;

    private static void validateLimit(Integer limit) {
        if (limit != null) {
            if (limit > 100) {
                throw new BadRequestException("Limit greater than 100");
            } else if (limit < 1) {
                throw new BadRequestException("Limit below 1");
            }
        }
    }

    private static void validateListAgainstRegex(List<String> orderBy, String regex, String errorMsg) {
        Pattern pattern = Pattern.compile(regex);
        for (String order : orderBy) {
            validateStringAgainstRegex(order, pattern, errorMsg);
        }
    }

    private static void validateStringAgainstRegex(String string, Pattern pattern, String errorMsg) {
        Matcher matcher = pattern.matcher(string);
        if (!matcher.matches()) {
            throw new BadRequestException(errorMsg);
        }
    }

    @RequestMapping("/")
    public List<Character> get(
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
        validateLimit(limit);
        validateListAgainstRegex(orderBy, "-?(name|modified)", INVALID_OR_UNRECOGNIZED_ORDERING_PARAMETER);

        return characterService.allCharacters(name, nameStartsWith, modifiedSince, comics,
                series, events, stories, orderBy, limit, offset);
    }

    @RequestMapping("/{characterId}")
    public Character getCharacterIndividual(@PathVariable Integer characterId) {
        return characterService.characterById(characterId);
    }

    @RequestMapping("/{characterId}/comics")
    public List<Comic> getCharacterComics(@PathVariable Integer characterId,
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
        validateLimit(limit);
        validateListAgainstRegex(orderBy, "-?(focDate|onsaleDate|title|issueNumber|modified)", INVALID_OR_UNRECOGNIZED_ORDERING_PARAMETER);
        validateStringAgainstRegex(format, Pattern.compile(FORMATS), INVALID_OR_UNRECOGNIZED_PARAMETER);
        validateStringAgainstRegex(formatType, Pattern.compile("(comic|collection)"), INVALID_OR_UNRECOGNIZED_PARAMETER);
        validateStringAgainstRegex(dateDescriptor, Pattern.compile("((last|this|next)Week|thisMonth)"), INVALID_OR_UNRECOGNIZED_PARAMETER);

        return characterService.comicsByCharacterId(characterId, format, formatType, noVariants, dateDescriptor, dateRange, title, titleStartsWith,
                startYear, issueNumber, diamondCode, digitalId, upc, isbn, ean, issn,
                hasDigitalIssue, modifiedSince, creators, series, events, stories, sharedAppearances,
                collaborators, orderBy, limit, offset);
    }

    @RequestMapping("/{characterId}/events")
    public List<Event> getCharacterEvents(@PathVariable Integer characterId, @RequestParam(required = false) String name,
                                          @RequestParam(required = false) String nameStartsWith,
                                          @RequestParam(required = false) String modifiedSince,
                                          @RequestParam(required = false) List<Integer> creators,
                                          @RequestParam(required = false) List<Integer> series,
                                          @RequestParam(required = false) List<Integer> comics,
                                          @RequestParam(required = false) List<Integer> stories,
                                          @RequestParam(required = false) List<String> orderBy,
                                          @RequestParam(required = false) Integer limit,
                                          @RequestParam(required = false) Integer offset) {
        validateLimit(limit);
        validateListAgainstRegex(orderBy, "-?(name|startDate|modified)", INVALID_OR_UNRECOGNIZED_ORDERING_PARAMETER);

        return characterService.eventsByCharacterId(characterId, name, nameStartsWith, modifiedSince, creators, series,
                comics, stories, orderBy, limit, offset);
    }

    @RequestMapping("/{characterId}/series")
    public List<Series> getCharacterSeries(@PathVariable Integer characterId,
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
        validateLimit(limit);
        validateListAgainstRegex(orderBy, "-?(title|startYear|modified)", INVALID_OR_UNRECOGNIZED_ORDERING_PARAMETER);
        validateListAgainstRegex(contains, FORMATS, INVALID_OR_UNRECOGNIZED_PARAMETER);

        return characterService.seriesByCharacterId(characterId, title, titleStartsWith, startYear, modifiedSince,
                comics, stories, events, creators, seriesType, contains, orderBy, limit, offset);
    }

    @RequestMapping("/{characterId}/stories")
    public List<Story> getCharacterStories(@PathVariable Integer characterId,
                                           @RequestParam(required = false) String modifiedSince,
                                           @RequestParam(required = false) List<Integer> comics,
                                           @RequestParam(required = false) List<Integer> series,
                                           @RequestParam(required = false) List<Integer> events,
                                           @RequestParam(required = false) List<Integer> creators,
                                           @RequestParam(required = false) List<String> orderBy,
                                           @RequestParam(required = false) Integer limit,
                                           @RequestParam(required = false) Integer offset) {
        validateLimit(limit);
        validateListAgainstRegex(orderBy, "-?(id|modified)", INVALID_OR_UNRECOGNIZED_ORDERING_PARAMETER);

        return characterService.storiesbyCharacterId(characterId, modifiedSince, comics, series, events, creators,
                orderBy, limit, offset);
    }
}
