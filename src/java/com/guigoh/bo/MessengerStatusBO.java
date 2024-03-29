/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.MessengerStatusDAO;
import com.guigoh.dao.exceptions.RollbackFailureException;
import com.guigoh.entity.MessengerStatus;
import java.io.Serializable;

/**
 *
 * @author IPTI
 */
public class MessengerStatusBO implements Serializable{

    private MessengerStatusDAO messengerStatusDAO;

    public MessengerStatusBO() {
        messengerStatusDAO = new MessengerStatusDAO();
    }

    public Double getServerTime() {
        try {
            return messengerStatusDAO.getServerTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long getUsersOnline() {
        try {
            return messengerStatusDAO.getUsersOnline();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void pingUser(MessengerStatus ms) throws RollbackFailureException, Exception {
        try {
            if (messengerStatusDAO.findMessengerStatus(ms.getSocialProfileId()) == null) {
                messengerStatusDAO.create(ms);
            } else {
                messengerStatusDAO.edit(ms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
