package ua.training.controller;

public interface RegistrationValidation {
    String FIRST_NAME_REGEX = "[A-Z][a-z]{2,20}";
    String FIRST_NAME_CYR_REGEX = "[А-ЩЮЯҐІЇЄ][а-щьюяґіїє']{2,20}";
    ;
    String LAST_NAME_REGEX = "[a-zA-Z]{1,}'?-?[a-zA-Z]{1,}\\s?([a-zA-Z]{1,})?";
    String LAST_NAME_CYR_REGEX = "[А-ЩЮЯҐІЇЄ][а-щьюяґіїє']{1,25}([-][А-ЩЮЯҐІЇЄ][а-щьюяґіїє']{1,25})?";
    String LOGIN_REGEX = "[A-Za-z0-9_-]{3,20}";
}
