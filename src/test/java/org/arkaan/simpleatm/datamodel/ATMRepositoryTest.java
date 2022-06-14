package org.arkaan.simpleatm.datamodel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class ATMRepositoryTest {

    private static final ATMRepository atmRepository = new ATMRepository();
    
    private static Customer customer1 = mock(Customer.class);
    private static Customer customer2 = mock(Customer.class);
    private static Customer customer3 = mock(Customer.class);
    private static Card card1 = mock(Card.class);
    private static Card card2 = mock(Card.class);
    private static Card card3 = mock(Card.class);
    
    @BeforeAll
    static void setUp() {
	when(card1.getAccountNumber()).thenReturn(776643);
	when(card1.getAccountNumber()).thenReturn(774921);
	when(card1.getAccountNumber()).thenReturn(777106);
	when(customer1.getBalance()).thenReturn(1000);
	when(customer2.getBalance()).thenReturn(1000);
	when(customer3.getBalance()).thenReturn(1000);
	when(card1.getCustomer()).thenReturn(customer1);
	when(card2.getCustomer()).thenReturn(customer2);
	when(card3.getCustomer()).thenReturn(customer3);
	when(card1.getPin()).thenReturn(123456);
	when(card2.getPin()).thenReturn(123456);
	when(card3.getPin()).thenReturn(123456);
	when(card1.getAccountBalance()).thenReturn(1000);
	when(card2.getAccountBalance()).thenReturn(1000);
	when(card3.getAccountBalance()).thenReturn(1000);
	atmRepository.addAccount(card1);
	atmRepository.addAccount(card2);
	atmRepository.addAccount(card3);
    }
    
    @Test
    void testFailFindAccountIfNotFound() {
	Optional<Card> result = atmRepository.findOne(999999);
	assertFalse(result.isPresent());
    }
    
    @Test
    void testFailWithdrawInsufficientBalance() {
	Transaction.Status result = atmRepository
		.withdrawMoney(999999, card1, "2022-02-02 22:00:00");
	assertEquals(Transaction.Status.FAILED, result);
    }
    
    @Test
    void testSuccessWithdraw() {
	Transaction.Status result = atmRepository
		.withdrawMoney(200, card1, "2022-02-02 22:00:00");
	assertEquals(Transaction.Status.SUCCESS, result);
    }
    
    @Test
    void testSuccessDeposit() {
	Transaction.Status result = atmRepository
		.depositMoney(100, card1);
	assertEquals(Transaction.Status.SUCCESS, result);
    }
    
    @Test
    void testFailTransferInvalidDestionation() {
	Transaction.Status result = atmRepository
		.transferMoney(100, card1, "999999", null, null);
	assertEquals(Transaction.Status.FAILED, result);
    }
    
    @Test
    void testFailTransferInsufficientBalance() {
	Transaction.Status result = atmRepository
		.transferMoney(1000000, card1, "777106", "645831", "2022-02-02 22:00:00");
	assertEquals(Transaction.Status.FAILED, result);
    }
    
    @Test
    void testSuccessTransfer() {
	Transaction.Status result = atmRepository
		.transferMoney(100, card1, "777106", "645831", "2022-02-02 22:00:00");
	assertEquals(Transaction.Status.SUCCESS, result);
    }
}
