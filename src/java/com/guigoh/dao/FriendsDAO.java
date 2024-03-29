/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.dao;

import com.guigoh.dao.exceptions.NonexistentEntityException;
import com.guigoh.dao.exceptions.PreexistingEntityException;
import com.guigoh.dao.exceptions.RollbackFailureException;
import com.guigoh.entity.Friends;
import com.guigoh.entity.FriendsPK;
import com.guigoh.entity.SocialProfile;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.guigoh.entity.Users;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author IPTI
 */
public class FriendsDAO implements Serializable {

    private transient EntityManagerFactory emf = JPAUtil.getEMF();

    public FriendsDAO() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Friends friends) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (friends.getFriendsPK() == null) {
            friends.setFriendsPK(new FriendsPK());
        }
        friends.getFriendsPK().setTokenFriend2(friends.getTokenFriend2().getToken());
        friends.getFriendsPK().setTokenFriend1(friends.getTokenFriend1().getToken());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users tokenRecommender = friends.getTokenRecommender();
            if (tokenRecommender != null) {
                tokenRecommender = em.getReference(tokenRecommender.getClass(), tokenRecommender.getUsername());
                friends.setTokenRecommender(tokenRecommender);
            }
            Users tokenFriend2 = friends.getTokenFriend2();
            if (tokenFriend2 != null) {
                tokenFriend2 = em.getReference(tokenFriend2.getClass(), tokenFriend2.getUsername());
                friends.setTokenFriend2(tokenFriend2);
            }
            Users tokenFriend1 = friends.getTokenFriend1();
            if (tokenFriend1 != null) {
                tokenFriend1 = em.getReference(tokenFriend1.getClass(), tokenFriend1.getUsername());
                friends.setTokenFriend1(tokenFriend1);
            }
            em.persist(friends);
            if (tokenRecommender != null) {
                tokenRecommender.getFriendsCollection().add(friends);
                tokenRecommender = em.merge(tokenRecommender);
            }
            if (tokenFriend2 != null) {
                tokenFriend2.getFriendsCollection().add(friends);
                tokenFriend2 = em.merge(tokenFriend2);
            }
            if (tokenFriend1 != null) {
                tokenFriend1.getFriendsCollection().add(friends);
                tokenFriend1 = em.merge(tokenFriend1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findFriends(friends.getFriendsPK()) != null) {
                throw new PreexistingEntityException("Friends " + friends + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Friends friends) throws NonexistentEntityException, RollbackFailureException, Exception {
        friends.getFriendsPK().setTokenFriend2(friends.getTokenFriend2().getToken());
        friends.getFriendsPK().setTokenFriend1(friends.getTokenFriend1().getToken());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Friends persistentFriends = em.find(Friends.class, friends.getFriendsPK());
            Users tokenRecommenderOld = persistentFriends.getTokenRecommender();
            Users tokenRecommenderNew = friends.getTokenRecommender();
            Users tokenFriend2Old = persistentFriends.getTokenFriend2();
            Users tokenFriend2New = friends.getTokenFriend2();
            Users tokenFriend1Old = persistentFriends.getTokenFriend1();
            Users tokenFriend1New = friends.getTokenFriend1();
            if (tokenRecommenderNew != null) {
                tokenRecommenderNew = em.getReference(tokenRecommenderNew.getClass(), tokenRecommenderNew.getUsername());
                friends.setTokenRecommender(tokenRecommenderNew);
            }
            if (tokenFriend2New != null) {
                tokenFriend2New = em.getReference(tokenFriend2New.getClass(), tokenFriend2New.getUsername());
                friends.setTokenFriend2(tokenFriend2New);
            }
            if (tokenFriend1New != null) {
                tokenFriend1New = em.getReference(tokenFriend1New.getClass(), tokenFriend1New.getUsername());
                friends.setTokenFriend1(tokenFriend1New);
            }
            friends = em.merge(friends);
            if (tokenRecommenderOld != null && !tokenRecommenderOld.equals(tokenRecommenderNew)) {
                tokenRecommenderOld.getFriendsCollection().remove(friends);
                tokenRecommenderOld = em.merge(tokenRecommenderOld);
            }
            if (tokenRecommenderNew != null && !tokenRecommenderNew.equals(tokenRecommenderOld)) {
                tokenRecommenderNew.getFriendsCollection().add(friends);
                tokenRecommenderNew = em.merge(tokenRecommenderNew);
            }
            if (tokenFriend2Old != null && !tokenFriend2Old.equals(tokenFriend2New)) {
                tokenFriend2Old.getFriendsCollection().remove(friends);
                tokenFriend2Old = em.merge(tokenFriend2Old);
            }
            if (tokenFriend2New != null && !tokenFriend2New.equals(tokenFriend2Old)) {
                tokenFriend2New.getFriendsCollection().add(friends);
                tokenFriend2New = em.merge(tokenFriend2New);
            }
            if (tokenFriend1Old != null && !tokenFriend1Old.equals(tokenFriend1New)) {
                tokenFriend1Old.getFriendsCollection().remove(friends);
                tokenFriend1Old = em.merge(tokenFriend1Old);
            }
            if (tokenFriend1New != null && !tokenFriend1New.equals(tokenFriend1Old)) {
                tokenFriend1New.getFriendsCollection().add(friends);
                tokenFriend1New = em.merge(tokenFriend1New);
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
                FriendsPK id = friends.getFriendsPK();
                if (findFriends(id) == null) {
                    throw new NonexistentEntityException("The friends with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(FriendsPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Friends friends;
            try {
                friends = em.getReference(Friends.class, id);
                friends.getFriendsPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The friends with id " + id + " no longer exists.", enfe);
            }
            Users tokenRecommender = friends.getTokenRecommender();
            if (tokenRecommender != null) {
                tokenRecommender.getFriendsCollection().remove(friends);
                tokenRecommender = em.merge(tokenRecommender);
            }
            Users tokenFriend2 = friends.getTokenFriend2();
            if (tokenFriend2 != null) {
                tokenFriend2.getFriendsCollection().remove(friends);
                tokenFriend2 = em.merge(tokenFriend2);
            }
            Users tokenFriend1 = friends.getTokenFriend1();
            if (tokenFriend1 != null) {
                tokenFriend1.getFriendsCollection().remove(friends);
                tokenFriend1 = em.merge(tokenFriend1);
            }
            em.remove(friends);
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

    public List<Friends> findFriendsEntities() {
        return findFriendsEntities(true, -1, -1);
    }

    public List<Friends> findFriendsEntities(int maxResults, int firstResult) {
        return findFriendsEntities(false, maxResults, firstResult);
    }

    private List<Friends> findFriendsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Friends.class));
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

    public Friends findFriends(FriendsPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Friends.class, id);
        } finally {
            em.close();
        }
    }

    public List<Friends> findFriendsByToken(String id) {
        EntityManager em = getEntityManager();
        try {
            List<Friends> friendList = (List<Friends>) em.createNativeQuery("select * from friends "
                    + "where status = 'AC' and (token_friend_1 = '" + id + "' or token_friend_2 = '" + id + "')", Friends.class).getResultList();
            return friendList;
        } finally {
            em.close();
        }
    }

    public List<SocialProfile> findFriendsOnlineByToken(String id) {
        EntityManager em = getEntityManager();
        try {
            List<SocialProfile> onlineList = (List<SocialProfile>) em.createNativeQuery("select sp.* from messenger_status ms "
                    + "join social_profile sp on ms.social_profile_id = sp.social_profile_id "
                    + "join friends f on (sp.token_id = f.token_friend_2 or sp.token_id = f.token_friend_1) "
                    + "where (f.token_friend_1 = '"+id+"' or f.token_friend_2 = '"+id+"') "
                    + "and sp.token_id <> '"+id+"' "
                    + "and f.status = 'AC' "
                    + "and (select round( date_part( 'epoch', now() ) )) - ms.last_ping < 90", SocialProfile.class).getResultList();
            return onlineList;
        } finally {
            em.close();
        }
    }
    
    public List<SocialProfile> findFriendsOfflineByToken(String id) {
        EntityManager em = getEntityManager();
        try {
            List<SocialProfile> offlineList = (List<SocialProfile>) em.createNativeQuery("select sp.* from messenger_status ms "
                    + "join social_profile sp on ms.social_profile_id = sp.social_profile_id "
                    + "join friends f on (sp.token_id = f.token_friend_2 or sp.token_id = f.token_friend_1) "
                    + "where (f.token_friend_1 = '"+id+"' or f.token_friend_2 = '"+id+"') "
                    + "and sp.token_id <> '"+id+"' "
                    + "and f.status = 'AC' "
                    + "and (select round( date_part( 'epoch', now() ) )) - ms.last_ping > 90", SocialProfile.class).getResultList();
            return offlineList;
        } finally {
            em.close();
        }
    }

    public List<Friends> findPendingFriendsByToken(String id) {
        EntityManager em = getEntityManager();
        try {
            List<Friends> friendList = (List<Friends>) em.createNativeQuery("select * from friends "
                    + "where status = 'PE' and token_friend_2 = '" + id + "'", Friends.class).getResultList();
            return friendList;
        } finally {
            em.close();
        }
    }

    public List<Friends> loadFriendSearchList(String token, String str) {
        EntityManager em = getEntityManager();
        try {
            List<Friends> friendsList = (List<Friends>) em.createNativeQuery("select distinct f.* from friends f "
                    + "join social_profile sp on ((f.token_friend_2 = sp.token_id) or (f.token_friend_1 = sp.token_id)) "
                    + "where (UPPER(sp.name) like '" + str.toUpperCase() + "%') "
                    + "and f.status = 'AC' "
                    + "and (f.token_friend_1 = '" + token + "' or f.token_friend_2 = '" + token + "')", Friends.class).getResultList();
            return friendsList;
        } finally {
            em.close();
        }
    }

    public List<SocialProfile> loadUserSearchList(String str) {
        EntityManager em = getEntityManager();
        try {
            List<SocialProfile> usersList = (List<SocialProfile>) em.createNativeQuery("select * from social_profile "
                    + "where (UPPER(name) like '" + str.toUpperCase() + "%') "
                    + "", SocialProfile.class).getResultList();
            return usersList;
        } finally {
            em.close();
        }
    }

    public void updateRecommendQut(Friends friend) {
        EntityManager em = getEntityManager();
        try {
            String sql = "update friends set recommenders = (recommenders+1) where "
                    + "token_friend_1 = '" + friend.getTokenFriend1().getToken() + "' "
                    + "and token_friend_2 = '" + friend.getTokenFriend2().getToken() + "'";
            Query query = em.createNamedQuery(sql);
            query.executeUpdate();
        } finally {
            em.close();
        }
    }

    public int getFriendsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Friends> rt = cq.from(Friends.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
