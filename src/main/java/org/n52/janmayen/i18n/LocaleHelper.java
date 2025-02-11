/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.janmayen.i18n;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import org.n52.janmayen.function.Functions;
import org.n52.janmayen.function.Predicates;

public final class LocaleHelper {
    private static final Map<String, Locale> CACHE = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, String> ISO_COUNTRY_ALPHA_3_TO_ALPHA_2 = Arrays
            .stream(Locale.getISOCountries()).map(Functions.curryFirst(Locale::new, "")).distinct()
            .collect(toMap(Locale::getISO3Country, Locale::getCountry));
    private static final Map<String, String> ISO_LANGUAGE_ALPHA_3_TO_ALPHA_2 = Arrays
            .stream(Locale.getISOLanguages()).map(Locale::new).distinct()
            .collect(toMap(Locale::getISO3Language, Locale::getLanguage));

    private LocaleHelper() {
    }

    public static Locale decode(String locale) {
        if (locale == null) {
            throw new NullPointerException();
        }
        if (locale.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        return decode(locale, null);
    }

    public static Locale decode(String locale, @Nullable Locale defaultLocale) {
        return Optional.ofNullable(locale)
                .map(String::trim)
                .filter(Predicates.not(String::isEmpty))
                .map(l -> CACHE.computeIfAbsent(l, LocaleHelper::decode1))
                .orElse(defaultLocale);
    }

    public static String encode(Locale input) {
        if (input == null) {
            return null;
        }
        String country = input.getISO3Country();
        String language = input.getISO3Language();
        StringBuilder sb = new StringBuilder(language);
        if (!country.isEmpty()) {
            sb.append("-").append(country);
        }
        return sb.toString();
    }

    private static Locale decode1(String locale) {
        String[] tokens = locale.split("[-_# ]");
        String language = tokens.length > 0 ? tokens[0].toLowerCase() : "";
        String country = tokens.length > 1 ? tokens[1].toUpperCase() : "";
        String variant = tokens.length > 2 ? tokens[2] : "";
        country = ISO_COUNTRY_ALPHA_3_TO_ALPHA_2.getOrDefault(country, country);
        language = ISO_LANGUAGE_ALPHA_3_TO_ALPHA_2.getOrDefault(language, language);
        return new Locale(language, country, variant);
    }
}
