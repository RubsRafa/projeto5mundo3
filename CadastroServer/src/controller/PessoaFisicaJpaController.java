/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.PessoaFisica;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author rubia
 */
public class PessoaFisicaJpaController {
    private EntityManagerFactory emf;

    public PessoaFisicaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PessoaFisica pf) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pf);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<PessoaFisica> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<PessoaFisica> query = em.createQuery("SELECT p FROM PessoaFisica p", PessoaFisica.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void edit(PessoaFisica pf) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pf);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            PessoaFisica pf = em.find(PessoaFisica.class, id);
            if (pf != null) {
                em.remove(pf);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

}
