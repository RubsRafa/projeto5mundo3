/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import cadastroserver.CadastroThreadV2;
import model.Movimento;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author rubia
 */
public class MovimentoJpaController {
    private EntityManagerFactory emf;

    public MovimentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Movimento movimento) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(movimento);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Movimento> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Movimento> query = em.createQuery("SELECT m FROM Movimento m", Movimento.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Movimento findOne(Integer idMovimento) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Movimento.class, idMovimento);
        } finally {
            em.close();
        }
    }

    public void edit(Movimento movimento) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(movimento);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Movimento movimento = em.find(Movimento.class, id);
            if (movimento != null) {
                em.remove(movimento);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void createV2(Movimento movimento) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(movimento);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

}
