/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo;

import com.guigoh.primata.dao.InterestsDAO;
import com.guigoh.primata.entity.Interests;
import com.guigoh.primata.entity.SocialProfile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class InterestsBO implements Serializable {

    public List<Interests> findInterests(Integer socialprofile_id) {
        InterestsDAO interestsDAO = new InterestsDAO();
        List<Interests> interestsList = interestsDAO.findInterestsBySocialProfileId(socialprofile_id);
        if (interestsList == null) {
            return new ArrayList<Interests>();
        }
        return interestsList;
    }

    public List<Interests> findInterestsByInterestsTypeName(String interestsType) {
        InterestsDAO interestsDAO = new InterestsDAO();
        List<Interests> interestsList = interestsDAO.findInterestsByInterestsTypeName(interestsType);
        if (interestsList == null) {
            return new ArrayList<Interests>();
        }
        return interestsList;
    }
    
    public List<Interests> findInterestsByInterestsTypeId(Integer interestsType) {
        InterestsDAO interestsDAO = new InterestsDAO();
        List<Interests> interestsList = interestsDAO.findInterestsByInterestsTypeId(interestsType);
        if (interestsList == null) {
            return new ArrayList<Interests>();
        }
        return interestsList;
    }

    public Interests findInterestsByInterestsName(String interestsName) {
        InterestsDAO interestsDAO = new InterestsDAO();
        Interests interests = interestsDAO.findInterestsByInterestsName(interestsName);
        if (interests == null) {
            return new Interests();
        }
        return interests;
    }

    public List<Interests> getAll() {
        InterestsDAO interestsDAO = new InterestsDAO();
        List<Interests> interestsList = interestsDAO.findInterestsEntities();
        if (interestsList == null) {
            return new ArrayList<Interests>();
        }
        return interestsList;
    }

    public void destroyInterestsBySocialProfile(SocialProfile socialprofile) {
        try {
            InterestsDAO interestsDAO = new InterestsDAO();
            interestsDAO.destroyInterestsSocialProfile(socialprofile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroyInterestsBySocialProfileInterestsType(SocialProfile socialprofile, String interestsType) {
        try {
            InterestsDAO interestsDAO = new InterestsDAO();
            List<Interests> interestsListT = interestsDAO.findInterestsBySocialProfileId(socialprofile.getSocialProfileId());
            for (Interests interests : interestsListT) {
                if (interests.getTypeId().getName().equalsIgnoreCase(interestsType)) {
                    interestsDAO.destroyInterestsBySocialProfileInterestsType(socialprofile, interests);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createInterestsBySocialProfileByInterest(List<Interests> interestsList, SocialProfile socialprofile) {
        try {
            InterestsDAO interestsDAO = new InterestsDAO();
            interestsDAO.createInterestsBySocialProfileByInterest(interestsList, socialprofile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createInterestsBySocialProfileByIds(Integer[] interestsIds, SocialProfile socialprofile) {
        try {
            InterestsDAO interestsDAO = new InterestsDAO();
            interestsDAO.createInterestsBySocialProfileByIds(interestsIds, socialprofile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void create(Interests intetests) {
        try {
            InterestsDAO interestsDAO = new InterestsDAO();
            interestsDAO.create(intetests);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public Interests findInterestsByID(Integer interestsID) {
        InterestsDAO interestsDAO = new InterestsDAO();
        Interests interests = interestsDAO.findInterestsByID(interestsID);
        if (interests == null) {
            return new Interests();
        }
        return interests;
    }
}
