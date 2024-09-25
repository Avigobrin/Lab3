package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // TODO Task: pick appropriate instance variables for this class
    private List<String> countryCodes;
    private Map<String, List<String>> countryLanguages;
    private Map<String, Map<String, String>> translations;
    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */

    public JSONTranslator() {
        this("sample.json");

    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        countryCodes = new ArrayList<>();
        countryLanguages = new HashMap<>();
        translations = new HashMap<>();
        String jsonString = null;
        try {
            jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        JSONArray jsonArray = new JSONArray(jsonString);
        // TODO Task: CHECK THIS WORKS
        // Note: this will likely be one of the most substantial pieces of code you write in this lab.
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject countryData = jsonArray.getJSONObject(i);

            String countryCode = countryData.getString("countryCode");
            countryCodes.add(countryCode);

            // Create a map for this country's translations
            Map<String, String> languageMap = new HashMap<>();
            List<String> languageCodes = new ArrayList<>();

            // Add language translations for this country
            for (String key : countryData.keySet()) {
                if (!key.equals(countryCode)) {
                    languageMap.put(key, countryData.getString(key));
                    languageCodes.add(key);
                }
            }

            // Store translations and languages for this country
            countryLanguages.put(countryCode, languageCodes);
            translations.put(countryCode, languageMap);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        // TODO Task: return an appropriate list of language codes,
        //            but make sure there is no aliasing to a mutable object
        return new ArrayList<>(countryLanguages.getOrDefault(country, new ArrayList<>()));
    }

    @Override
    public List<String> getCountries() {
        // TODO Task: return an appropriate list of country codes,
        //            but make sure there is no aliasing to a mutable object
        return new ArrayList<>(countryCodes);
    }

    @Override
    public String translate(String country, String language) {
        // TODO Task: complete this method using your instance variables as needed
        if (translations.containsKey(country)) {
            Map<String, String> languageMap = translations.get(country);
            return languageMap.getOrDefault(language, "Translation not available");
        }
        return "Country not found";
    }
}
