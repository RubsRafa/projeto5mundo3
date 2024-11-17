/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.PessoaJuridica;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author rubia
 */
public class PessoaJuridicaJpaController {
    private EntityManagerFactory emf;

    public PessoaJuridicaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PessoaJuridica pj) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pj);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<PessoaJuridica> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<PessoaJuridica> query = em.createQuery("SELECT p FROM PessoaJuridica p", PessoaJuridica.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void edit(PessoaJuridica pj) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pj);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            PessoaJuridica pj = em.find(PessoaJuridica.class, id);
            if (pj != null) {
                em.remove(pj);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

}
