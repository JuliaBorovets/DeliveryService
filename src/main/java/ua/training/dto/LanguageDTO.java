package ua.training.dto;

import ua.training.controller.SupportedLanguages;

import java.util.Arrays;
import java.util.Locale;

public class LanguageDTO {

    private String name;
    private String choice;

    public void setChoice(String language) {
        this.choice = language;
        this.name = findNameByCode(language);
    }

    public String getChoice() {
        return choice;
    }

    public String getName() {
        return name;
    }

    public String findNameByCode(String code) {
        String codeLC = code.toLowerCase();

        for (SupportedLanguages lang : SupportedLanguages.values()) {
            if (lang.getCode().equals(codeLC)) {
                return lang.getName();
            }
        }

        return "";
    }

    public SupportedLanguages[] getSupportedLanguages() {
        return SupportedLanguages.values();
    }

    public String[] getSupportedCodes() {
        return Arrays.stream(SupportedLanguages.values()).map(SupportedLanguages::getCode).toArray(String[]::new);
    }

    public Locale getLocale() {
        return SupportedLanguages.determineLocale(choice);
    }

    public boolean isLocaleCyrillic() {
        return SupportedLanguages.CYRILLICS.contains(choice);
    }

}
