package org.arkaan.simpleatm.repository;

import org.arkaan.simpleatm.error.AddTransactionException;
import org.arkaan.simpleatm.model.Transaction;
import org.arkaan.simpleatm.repository.Repository.TransactionRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Repository
public class TransactionRepoDB implements TransactionRepository {

    @PersistenceContext
    private final EntityManager em;

    public TransactionRepoDB(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Transaction> findOne(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Transaction save(Transaction data) {
        em.getTransaction().begin();
        try {
            em.persist(data);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new AddTransactionException();
        }
        return data;
    }

    @Override
    public Transaction remove(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Transaction> findByAccountNumber(int accountNumber) {
        throw new UnsupportedOperationException();
    }
}
