package ua.training.dto;

public interface RegexConstants {

    String firstNameRegexp = "[A-Z][a-z]{2,20}";

    String firstNameCyrRegexp = "[А-ЩЮЯҐІЇЄ][а-щьюяґіїє']{2,20}";

    String lastNameRegexp = "[a-zA-Z]+'?-?[a-zA-Z]+\\s?([a-zA-Z]+)?";

    String lastNameCyrRegexp = "[А-ЩЮЯҐІЇЄ][а-щьюяґіїє']{1,25}([-][А-ЩЮЯҐІЇЄ][а-щьюяґіїє']{1,25})?";

    String loginRegexp = "[a-z0-9_-]{3,20}";

}
