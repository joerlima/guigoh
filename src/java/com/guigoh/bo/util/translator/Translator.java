/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo.util.translator;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author ipti
 */
public class Translator implements Serializable {

    private Locale locale;

    public void setLocale(String acronym) {
        if (acronym != null) {
            String acronymLang = acronym.substring(0, 2);
            String acronymCountry = acronym.substring(2, 4);
            locale = new Locale(acronymLang, acronymCountry);
        }
    }

    public String getWord(String keyword) {
        return ResourceBundle.getBundle("com.guigoh.bo.util.translator.Labels", locale).getString(keyword);
    }
}