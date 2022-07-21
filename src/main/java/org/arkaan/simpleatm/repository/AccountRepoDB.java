package org.arkaan.simpleatm.repository;

import org.arkaan.simpleatm.model.Account;
import org.springframework.stereotype.Repository;
import org.arkaan.simpleatm.repository.Repository.AccountRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepoDB implements AccountRepository {

    @PersistenceContext
    private EntityManager em;

    public AccountRepoDB(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Account> findOne(int id) {
        em.getTransaction().begin();
        Account account = em.find(Account.class, id);
        em.getTransaction().commit();
        return Optional.ofNullable(account);
    }

    @Override
    public Account save(Account data) {
        em.getTransaction().begin();
        em.persist(data);
        em.getTransaction().commit();
        return data;
    }

    @Override
    public Account update(int id, Account data) {
        return null;
    }

    @Override
    public Account remove(int id) {
        return null;
    }

    @Override
    public void saveAll() {

    }

    public List<Account> findAll() {
        em.getTransaction().begin();
        List<Account> accounts = em.createQuery("select a from Account a", Account.class)
                .getResultList();
        em.getTransaction().commit();
        return accounts;
    }
}
