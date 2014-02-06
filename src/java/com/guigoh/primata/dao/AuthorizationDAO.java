/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.dao;

import com.guigoh.primata.dao.exceptions.IllegalOrphanException;
import com.guigoh.primata.dao.exceptions.NonexistentEntityException;
import com.guigoh.primata.dao.exceptions.PreexistingEntityException;
import com.guigoh.primata.dao.exceptions.RollbackFailureException;
import com.guigoh.primata.entity.Authorization;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.primata.entity.Users;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Joe
 */
public class AuthorizationDAO implements Serializable {

     private EntityManagerFactory emf = JPAUtil.getEMF();
    
    public AuthorizationDAO() {

    }

   

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Authorization authorization) throws IllegalOrphanException, PreexistingEntityException, RollbackFailureException, Exception {
        List<String> illegalOrphanMessages = null;
        Users usersOrphanCheck = authorization.getUsers();
        if (usersOrphanCheck != null) {
            Authorization oldAuthorizationOfUsers = usersOrphanCheck.getAuthorization();
            if (oldAuthorizationOfUsers != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Users " + usersOrphanCheck + " already has an item of type Authorization whose users column cannot be null. Please make another selection for the users field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users = authorization.getUsers();
            if (users != null) {
                users = em.getReference(users.getClass(), users.getUsername());
                authorization.setUsers(users);
            }
            em.persist(authorization);
            if (users != null) {
                users.setAuthorization(authorization);
                users = em.merge(users);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAuthorization(authorization.getTokenId()) != null) {
                throw new PreexistingEntityException("Authorization " + authorization + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Authorization authorization) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Authorization persistentAuthorization = em.find(Authorization.class, authorization.getTokenId());
            Users usersOld = persistentAuthorization.getUsers();
            Users usersNew = authorization.getUsers();
            List<String> illegalOrphanMessages = null;
            if (usersNew != null && !usersNew.equals(usersOld)) {
                Authorization oldAuthorizationOfUsers = usersNew.getAuthorization();
                if (oldAuthorizationOfUsers != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Users " + usersNew + " already has an item of type Authorization whose users column cannot be null. Please make another selection for the users field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usersNew != null) {
                usersNew = em.getReference(usersNew.getClass(), usersNew.getUsername());
                authorization.setUsers(usersNew);
            }
            authorization = em.merge(authorization);
            if (usersOld != null && !usersOld.equals(usersNew)) {
                usersOld.setAuthorization(null);
                usersOld = em.merge(usersOld);
            }
            if (usersNew != null && !usersNew.equals(usersOld)) {
                usersNew.setAuthorization(authorization);
                usersNew = em.merge(usersNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = authorization.getTokenId();
                if (findAuthorization(id) == null) {
                    throw new NonexistentEntityException("The authorization with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Authorization authorization;
            try {
                authorization = em.getReference(Authorization.class, id);
                authorization.getTokenId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The authorization with id " + id + " no longer exists.", enfe);
            }
            Users users = authorization.getUsers();
            if (users != null) {
                users.setAuthorization(null);
                users = em.merge(users);
            }
            em.remove(authorization);
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Authorization> findAuthorizationEntities() {
        return findAuthorizationEntities(true, -1, -1);
    }

    public List<Authorization> findAuthorizationEntities(int maxResults, int firstResult) {
        return findAuthorizationEntities(false, maxResults, firstResult);
    }

    private List<Authorization> findAuthorizationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Authorization.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Authorization findAuthorization(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Authorization.class, id);
        } finally {
            em.close();
        }
    }

    public int getAuthorizationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Authorization> rt = cq.from(Authorization.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
     public List<Authorization> findExperiencesByTokenId(Integer subnetwork) {
        EntityManager em = getEntityManager();
        try {
            List<Authorization> authorizationList = (List<Authorization>) em.createNativeQuery("select a.* from primata_authorization a "
                    + "join primata_social_profile s on a.token_id = s.token_id "
                    + "where a.status = 'PC' and s.subnetwork_id = '" + subnetwork + "'", Authorization.class).getResultList();
            return authorizationList;
        } finally {
            em.close();
        }
    }
    
}
