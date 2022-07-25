package org.arkaan.simpleatm.repository;

import org.arkaan.simpleatm.error.DuplicateAccountNumberException;
import org.arkaan.simpleatm.model.Account;
import org.arkaan.simpleatm.repository.Repository.AccountRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepoDB implements AccountRepository {

    @PersistenceContext
    private final EntityManager em;

    public AccountRepoDB(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Account> findOne(int id) {
        em.getTransaction().begin();
        Account account;
        try {
            account = em.createQuery("select a from Account a where a.accountNumber=:account_number", Account.class)
                .setParameter("account_number", id)
                .getSingleResult();
            em.getTransaction().commit();
        } catch(NoResultException e) {
            account = null;
            em.getTransaction().rollback();
        }
        return Optional.ofNullable(account);
    }

    @Override
    public Account save(Account data) {
        em.getTransaction().begin();
        try {
            em.persist(data);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new DuplicateAccountNumberException(data.getAccountNumber());
            }
        }
        return data;
    }

    @Override
    public Account remove(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveAll() {
        throw new UnsupportedOperationException();
    }

    public List<Account> findAll() {
        em.getTransaction().begin();
        List<Account> accounts = em.createQuery("select a from Account a", Account.class)
                .getResultList();
        em.getTransaction().commit();
        return accounts;
    }
}
