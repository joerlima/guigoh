/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.bo;

import com.guigoh.dao.AuthorDAO;
import com.guigoh.entity.Author;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ipti008
 */
public class AuthorBO implements Serializable{
    
    private AuthorDAO authorDAO;
    
    public AuthorBO(){
        authorDAO = new AuthorDAO();
    }
    
    public void create(Author author){
        try{
            authorDAO.create(author);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void edit(Author author){
        try{
            authorDAO.edit(author);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public List<Author> getAuthorsByEducationalObject(Integer educationalObjectId){
        try{
            return authorDAO.getAuthorsByEducationalObject(educationalObjectId);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
