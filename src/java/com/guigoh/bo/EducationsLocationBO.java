/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.EducationsLocationDAO;
import com.guigoh.dao.EducationsNameDAO;
import com.guigoh.entity.Educations;
import com.guigoh.entity.EducationsLocation;
import com.guigoh.entity.EducationsName;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joe
 */
public class EducationsLocationBO implements Serializable {

    EducationsLocationDAO educationsLocationDAO;

    public EducationsLocationBO() {
        educationsLocationDAO = new EducationsLocationDAO();
    }

    public List<EducationsLocation> getAll() {
        try {
            return educationsLocationDAO.findEducationsLocationEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void create(EducationsLocation educationsLocation) {
        try {
            educationsLocationDAO.create(educationsLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EducationsLocation findEducationsLocationByName(EducationsLocation locationId) {
        try {
            return educationsLocationDAO.findEducationsByName(locationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
