/*
 * Copyright 2025-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fcuillandre.chessbot.gui;

import com.fcuillandre.chessbot.utils.ChessAppUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Centralized message provider for internationalization (i18n).
 * Supports English and French locales.
 *
 * @author fcuillandre
 * @since 1.0
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class Messages {

    private static final String BUNDLE_BASE = "messages";

    @Getter
    private static Locale currentLocale = Locale.ENGLISH;
    private static ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASE, currentLocale);

    /**
     * Switches the application locale and reloads the bundle.
     *
     * @param locale the new locale to apply
     */
    public static void setLocale(final Locale locale) {
        currentLocale = locale;
        ResourceBundle.clearCache();
        bundle = ResourceBundle.getBundle(BUNDLE_BASE, currentLocale);
    }

    /**
     * Returns the translated string for the given key.
     *
     * @param key the message key
     * @return the translated string, or the key itself if not found
     */
    public static String get(final String key) {
        try {
            return bundle.getString(key);
        } catch (java.util.MissingResourceException e) {
            ChessAppUtils.log("Missing translation for key: " + key);
            return key;
        }
    }

    /**
     * Returns the translated string for the given key, formatted with the provided arguments.
     *
     * @param key  the message key
     * @param args the arguments to format into the message
     * @return the formatted translated string
     */
    public static String get(final String key, final Object... args) {
        return MessageFormat.format(get(key), args);
    }
}

