/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bean;

import com.guigoh.primata.bo.LanguageBO;
import com.guigoh.primata.bo.SocialProfileBO;
import com.guigoh.primata.bo.util.translator.ConfigReader;
import com.guigoh.primata.bo.util.translator.Translator;
import com.guigoh.primata.entity.Language;
import com.guigoh.primata.entity.SocialProfile;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ipti004
 */
@SessionScoped
@ManagedBean(name = "localeBean")
public class LocaleBean implements Serializable {

    private Translator trans = new Translator();
    private ConfigReader cr = new ConfigReader();
    private String acronym = "pt";
    private String token = "";
    private SocialProfile socialProfile;
    private LanguageBO languageBO = new LanguageBO();
    private SocialProfileBO socialProfileBO = new SocialProfileBO();

    public LocaleBean() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().trim().equalsIgnoreCase("token")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (!token.equals("")) {
            socialProfile = socialProfileBO.findSocialProfile(token);
            acronym = languageBO.findById(socialProfile.getLanguageId().getId()).getAcronym();
        }
        if (acronym.equals("ptBR")) {
            changeToPTBR("");
        } else if (acronym.equals("enUS")) {
            changeToENUS("");
        } else if (acronym.equals("frFR")) {
            changeToFRFR("");
        }

    }

    public String getString(String string) {
        setLocale();
        return trans.getWord(string);
    }

    public String changeToPTBR(String url) {
        cr.editarTag("locale", "ptBR");
        if (!token.equals("")) {
            socialProfile.setLanguageId(languageBO.findByAcronym("ptBR"));
            socialProfileBO.edit(socialProfile);
        }
        acronym = "ptBR";
        return url + "?faces-redirect=true&includeViewParams=true";
    }

    public String changeToENUS(String url) {
        cr.editarTag("locale", "enUS");
        if (!token.equals("")) {
            socialProfile.setLanguageId(languageBO.findByAcronym("enUS"));
            socialProfileBO.edit(socialProfile);
        }
        acronym = "enUS";
        return url + "?faces-redirect=true&includeViewParams=true";
    }

    public String changeToFRFR(String url) {
        cr.editarTag("locale", "frFR");
        if (!token.equals("")) {
            socialProfile.setLanguageId(languageBO.findByAcronym("frFR"));
            socialProfileBO.edit(socialProfile);
        }
        acronym = "frFR";
        return url + "?faces-redirect=true&includeViewParams=true";
    }

    public void setLocale() {
        if (cr.getTag("locale").equals("enUS")) {
            trans.setLocaleENUS();
        } else if (cr.getTag("locale").equals("ptBR")) {
            trans.setLocalePTBR();
        } else if (cr.getTag("locale").equals("frFR")) {
            trans.setLocaleFRFR();
        }
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getAcronym() {
        return acronym;
    }
}