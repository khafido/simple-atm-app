package org.arkaan.simpleatm.model;

import javax.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {

    public enum Status {
        SUCCESS,
        FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private String detail;

    @Column(nullable = false)
    private Status status;

    @Transient
    private  int accountNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Transaction() {}

    public Transaction(int id, Type type, String detail, Status status, int accountNumber) {
        this.type = type;
        this.detail = detail;
        this.status = status;
        this.accountNumber = accountNumber;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

    public Status getStatus() {
        return status;
    }
    
    public int getAccountNumber() {
        return accountNumber;
    }
    
    @Override
    public String toString() {
        return String.join(",", type.toString(), detail, status.toString(), String.valueOf(accountNumber), "\n");
    }
}
