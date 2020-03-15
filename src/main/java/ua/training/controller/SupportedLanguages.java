package ua.training.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Getter
@AllArgsConstructor
public enum SupportedLanguages {
    ENGLISH("en", "English"),
    UKRAINIAN("uk", "Українська");

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public static final Set<String> CYRILLICS = new HashSet<>(Arrays.asList(UKRAINIAN.getCode()));

    private String code;
    private String name;

    public static SupportedLanguages getDefault() {
        return ENGLISH;
    }

    public static Locale determineLocale(SupportedLanguages lang) {
        return new Locale(lang.getCode());
    }

    public static Locale determineLocale(String code) {
        String codeLocale = code.toLowerCase();
        for (SupportedLanguages lang : SupportedLanguages.values()) {
            if (lang.getCode().equals(codeLocale)) {
                return determineLocale(lang);
            }
        }
        return determineLocale(getDefault());
    }

}
