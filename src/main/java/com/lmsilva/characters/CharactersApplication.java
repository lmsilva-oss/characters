package com.lmsilva.characters;

import com.lmsilva.characters.service.impl.MarvelApiCharacterService;
import com.lmsilva.characters.swagger.model.*;
import com.lmsilva.characters.swagger.model.Character;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.PersistentCollection;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.tool.ExportOptions;
import org.dizitart.no2.tool.Exporter;
import org.dizitart.no2.tool.Importer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.*;

@SpringBootApplication
public class CharactersApplication {
	private static Logger LOGGER = LoggerFactory.getLogger(CharactersApplication.class);
	private static String EXPORT_PATH = System.getProperty("com.lmsilva.characters.exportPath");
	private static String IMPORT_PATH = System.getProperty("com.lmsilva.characters.importPath");
	private static String[] files = {".character", ".comic", ".series", ".story", ".event"};

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(CharactersApplication.class, args);
		// Export from Marvel API
		Nitrite db = context.getBean(Nitrite.class);
		if (EXPORT_PATH != null) {
			// TODO: improve progress reporting
			LOGGER.info("Starting export");
			exportToFile(context.getBean(MarvelApiCharacterService.class), db);
			LOGGER.info("Export finished");
		}

		// Import from Nitrite-generated JSON file
		if (IMPORT_PATH != null) {
			// TODO: improve progress reporting
			LOGGER.info("Starting import");
			importFromFile(db);
			LOGGER.info("Import finished");
		}
	}

	public static void exportToFile(MarvelApiCharacterService marvelApiCharacterService, Nitrite db) {
		// TODO: increase characters fetched (currently 20)
		List<Character> characters = marvelApiCharacterService.allCharacters(null, null,
				null, null, null, null, null, null, null, null);
		ObjectRepository<Character> characterObjectRepository = db.getRepository(Character.class);
		ObjectRepository<Comic> comicObjectRepository = db.getRepository(Comic.class);
		ObjectRepository<Event> eventObjectRepository = db.getRepository(Event.class);
		ObjectRepository<Series> seriesObjectRepository = db.getRepository(Series.class);
		ObjectRepository<Story> storyObjectRepository = db.getRepository(Story.class);

		characters.forEach(character -> characterObjectRepository.insert(character));

		for (Character character : characters) {
			Integer characterId = character.getId();
			List<Comic> comics = marvelApiCharacterService.comicsByCharacterId(characterId, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			List<Event> events = marvelApiCharacterService.eventsByCharacterId(characterId, null, null, null, null, null, null, null, null, null, null);
			List<Series> series = marvelApiCharacterService.seriesByCharacterId(characterId, null, null, null, null, null, null, null, null, null, null, null, null, null);
			List<Story> stories = marvelApiCharacterService.storiesbyCharacterId(characterId, null, null, null, null, null, null, null, null);

			comics.forEach(comic -> comicObjectRepository.insert(comic));
			events.forEach(event -> eventObjectRepository.insert(event));
			series.forEach(series1 -> seriesObjectRepository.insert(series1));
			stories.forEach(story -> storyObjectRepository.insert(story));
		}

		Exporter exporter = Exporter.of(db);
		exporter.withOptions(new ExportOptions() {{
			setCollections(new ArrayList<PersistentCollection<?>>() {{
				add(characterObjectRepository);
			}});
		}});
		exporter.exportTo(EXPORT_PATH + ".character");

		exporter.withOptions(new ExportOptions() {{
			setCollections(new ArrayList<PersistentCollection<?>>() {{
				add(comicObjectRepository);
			}});
		}});
		exporter.exportTo(EXPORT_PATH + ".comic");

		exporter.withOptions(new ExportOptions() {{
			setCollections(new ArrayList<PersistentCollection<?>>() {{
				add(eventObjectRepository);
			}});
		}});
		exporter.exportTo(EXPORT_PATH + ".event");

		exporter.withOptions(new ExportOptions() {{
			setCollections(new ArrayList<PersistentCollection<?>>() {{
				add(seriesObjectRepository);
			}});
		}});
		exporter.exportTo(EXPORT_PATH + ".series");

		exporter.withOptions(new ExportOptions() {{
			setCollections(new ArrayList<PersistentCollection<?>>() {{
				add(storyObjectRepository);
			}});
		}});
		exporter.exportTo(EXPORT_PATH + ".story");
	}

	public static void importFromFile(Nitrite db) {
		Importer importer = Importer.of(db);

		for (String file : files) {
			importer.importFrom(IMPORT_PATH + file);
		}
	}

}
