/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.dao;

import com.guigoh.dao.exceptions.NonexistentEntityException;
import com.guigoh.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.entity.DiscussionTopic;
import java.util.ArrayList;
import java.util.Collection;
import com.guigoh.entity.EducationalObject;
import com.guigoh.entity.Tags;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.transaction.UserTransaction;

/**
 *
 * @author ipti008
 */
public class TagsDAO implements Serializable {

    private transient EntityManagerFactory emf = JPAUtil.getEMF();
    
    public TagsDAO() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tags tags) throws RollbackFailureException, Exception {
        if (tags.getDiscussionTopicCollection() == null) {
            tags.setDiscussionTopicCollection(new ArrayList<DiscussionTopic>());
        }
        if (tags.getEducationalObjectCollection() == null) {
            tags.setEducationalObjectCollection(new ArrayList<EducationalObject>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<DiscussionTopic> attachedDiscussionTopicCollection = new ArrayList<DiscussionTopic>();
            for (DiscussionTopic discussionTopicCollectionDiscussionTopicToAttach : tags.getDiscussionTopicCollection()) {
                discussionTopicCollectionDiscussionTopicToAttach = em.getReference(discussionTopicCollectionDiscussionTopicToAttach.getClass(), discussionTopicCollectionDiscussionTopicToAttach.getId());
                attachedDiscussionTopicCollection.add(discussionTopicCollectionDiscussionTopicToAttach);
            }
            tags.setDiscussionTopicCollection(attachedDiscussionTopicCollection);
            Collection<EducationalObject> attachedEducationalObjectCollection = new ArrayList<EducationalObject>();
            for (EducationalObject educationalObjectCollectionEducationalObjectToAttach : tags.getEducationalObjectCollection()) {
                educationalObjectCollectionEducationalObjectToAttach = em.getReference(educationalObjectCollectionEducationalObjectToAttach.getClass(), educationalObjectCollectionEducationalObjectToAttach.getId());
                attachedEducationalObjectCollection.add(educationalObjectCollectionEducationalObjectToAttach);
            }
            tags.setEducationalObjectCollection(attachedEducationalObjectCollection);
            em.persist(tags);
            for (DiscussionTopic discussionTopicCollectionDiscussionTopic : tags.getDiscussionTopicCollection()) {
                discussionTopicCollectionDiscussionTopic.getTagsCollection().add(tags);
                discussionTopicCollectionDiscussionTopic = em.merge(discussionTopicCollectionDiscussionTopic);
            }
            for (EducationalObject educationalObjectCollectionEducationalObject : tags.getEducationalObjectCollection()) {
                educationalObjectCollectionEducationalObject.getTagsCollection().add(tags);
                educationalObjectCollectionEducationalObject = em.merge(educationalObjectCollectionEducationalObject);
            }
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

    public void edit(Tags tags) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tags persistentTags = em.find(Tags.class, tags.getId());
            Collection<DiscussionTopic> discussionTopicCollectionOld = persistentTags.getDiscussionTopicCollection();
            Collection<DiscussionTopic> discussionTopicCollectionNew = tags.getDiscussionTopicCollection();
            Collection<EducationalObject> educationalObjectCollectionOld = persistentTags.getEducationalObjectCollection();
            Collection<EducationalObject> educationalObjectCollectionNew = tags.getEducationalObjectCollection();
            Collection<DiscussionTopic> attachedDiscussionTopicCollectionNew = new ArrayList<DiscussionTopic>();
            for (DiscussionTopic discussionTopicCollectionNewDiscussionTopicToAttach : discussionTopicCollectionNew) {
                discussionTopicCollectionNewDiscussionTopicToAttach = em.getReference(discussionTopicCollectionNewDiscussionTopicToAttach.getClass(), discussionTopicCollectionNewDiscussionTopicToAttach.getId());
                attachedDiscussionTopicCollectionNew.add(discussionTopicCollectionNewDiscussionTopicToAttach);
            }
            discussionTopicCollectionNew = attachedDiscussionTopicCollectionNew;
            tags.setDiscussionTopicCollection(discussionTopicCollectionNew);
            Collection<EducationalObject> attachedEducationalObjectCollectionNew = new ArrayList<EducationalObject>();
            for (EducationalObject educationalObjectCollectionNewEducationalObjectToAttach : educationalObjectCollectionNew) {
                educationalObjectCollectionNewEducationalObjectToAttach = em.getReference(educationalObjectCollectionNewEducationalObjectToAttach.getClass(), educationalObjectCollectionNewEducationalObjectToAttach.getId());
                attachedEducationalObjectCollectionNew.add(educationalObjectCollectionNewEducationalObjectToAttach);
            }
            educationalObjectCollectionNew = attachedEducationalObjectCollectionNew;
            tags.setEducationalObjectCollection(educationalObjectCollectionNew);
            tags = em.merge(tags);
            for (DiscussionTopic discussionTopicCollectionOldDiscussionTopic : discussionTopicCollectionOld) {
                if (!discussionTopicCollectionNew.contains(discussionTopicCollectionOldDiscussionTopic)) {
                    discussionTopicCollectionOldDiscussionTopic.getTagsCollection().remove(tags);
                    discussionTopicCollectionOldDiscussionTopic = em.merge(discussionTopicCollectionOldDiscussionTopic);
                }
            }
            for (DiscussionTopic discussionTopicCollectionNewDiscussionTopic : discussionTopicCollectionNew) {
                if (!discussionTopicCollectionOld.contains(discussionTopicCollectionNewDiscussionTopic)) {
                    discussionTopicCollectionNewDiscussionTopic.getTagsCollection().add(tags);
                    discussionTopicCollectionNewDiscussionTopic = em.merge(discussionTopicCollectionNewDiscussionTopic);
                }
            }
            for (EducationalObject educationalObjectCollectionOldEducationalObject : educationalObjectCollectionOld) {
                if (!educationalObjectCollectionNew.contains(educationalObjectCollectionOldEducationalObject)) {
                    educationalObjectCollectionOldEducationalObject.getTagsCollection().remove(tags);
                    educationalObjectCollectionOldEducationalObject = em.merge(educationalObjectCollectionOldEducationalObject);
                }
            }
            for (EducationalObject educationalObjectCollectionNewEducationalObject : educationalObjectCollectionNew) {
                if (!educationalObjectCollectionOld.contains(educationalObjectCollectionNewEducationalObject)) {
                    educationalObjectCollectionNewEducationalObject.getTagsCollection().add(tags);
                    educationalObjectCollectionNewEducationalObject = em.merge(educationalObjectCollectionNewEducationalObject);
                }
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
                Integer id = tags.getId();
                if (findTags(id) == null) {
                    throw new NonexistentEntityException("The tags with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tags tags;
            try {
                tags = em.getReference(Tags.class, id);
                tags.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tags with id " + id + " no longer exists.", enfe);
            }
            Collection<DiscussionTopic> discussionTopicCollection = tags.getDiscussionTopicCollection();
            for (DiscussionTopic discussionTopicCollectionDiscussionTopic : discussionTopicCollection) {
                discussionTopicCollectionDiscussionTopic.getTagsCollection().remove(tags);
                discussionTopicCollectionDiscussionTopic = em.merge(discussionTopicCollectionDiscussionTopic);
            }
            Collection<EducationalObject> educationalObjectCollection = tags.getEducationalObjectCollection();
            for (EducationalObject educationalObjectCollectionEducationalObject : educationalObjectCollection) {
                educationalObjectCollectionEducationalObject.getTagsCollection().remove(tags);
                educationalObjectCollectionEducationalObject = em.merge(educationalObjectCollectionEducationalObject);
            }
            em.remove(tags);
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

    public List<Tags> findTagsEntities() {
        return findTagsEntities(true, -1, -1);
    }

    public List<Tags> findTagsEntities(int maxResults, int firstResult) {
        return findTagsEntities(false, maxResults, firstResult);
    }

    private List<Tags> findTagsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tags.class));
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

    public Tags findTags(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tags.class, id);
        } finally {
            em.close();
        }
    }

    public int getTagsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tags> rt = cq.from(Tags.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Tags findTagsByName(String tags) {
        EntityManager em = getEntityManager();
        try {
            if (tags.equals("") || tags == null) {
                return new Tags();
            }
            String ttemp = tags.toUpperCase().replaceAll(" ", "");
            List<Tags> tagstemp = (List<Tags>) em.createNativeQuery("select * from tags "
                    + "where UPPER(regexp_replace(name,'\\s*', '', 'g')) like '" + ttemp + "' ", Tags.class).getResultList();
            if (tagstemp.isEmpty()) {
                return new Tags();
            }
            return tagstemp.get(0);
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<Tags> findTagsByText(String tags) {
        EntityManager em = getEntityManager();
        try {
            if (tags.equals("") || tags == null) {
                return new ArrayList<Tags>();
            }
            String ttemp = tags.toUpperCase().replaceAll(" ", "");
            List<Tags> tagstemp = (List<Tags>) em.createNativeQuery("select * from tags "
                    + "where UPPER(name) like '" + ttemp.toUpperCase() + "%' ", Tags.class).getResultList();
            if (tagstemp.isEmpty()) {
                return new ArrayList<Tags>();
            }
            return tagstemp;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public void createTagsDiscussionTopic(Tags tags, DiscussionTopic discussionTopic) throws RollbackFailureException, Exception {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            String sql = "INSERT INTO topic_tags (tags_id, discussion_topic_id) "
                    + "VALUES(?1,?2)";

            Query query = em.createNativeQuery(sql);

            query.setParameter(1, tags.getId().longValue());
            query.setParameter(2, discussionTopic.getId().longValue());

            query.executeUpdate();


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
}
