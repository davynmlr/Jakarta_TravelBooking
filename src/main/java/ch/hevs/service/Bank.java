package ch.hevs.service;

import java.io.Serializable;
import java.util.List;

import ch.hevs.businessobject.Account;
import ch.hevs.businessobject.Client;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@RequestScoped
public class Bank implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName = "bankPU", type=PersistenceContextType.TRANSACTION)
	private EntityManager em;

	public Account getAccount(String accountDescription, String lastnameOwner) {
		
		Query query = em.createQuery("FROM Account a WHERE a.description=:description AND a.owner.lastname=:lastname");
		query.setParameter("description", accountDescription);
		query.setParameter("lastname", lastnameOwner);
		
		Account account = (Account) query.getSingleResult();
		
		return account;
	}
	
	public List<Account> getAccountListFromClientLastname(String lastname) {
		return (List<Account>) em.createQuery("SELECT c.accounts FROM Client c where c.lastname=:lastname").setParameter("lastname", lastname).getResultList();
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void transfer(Account srcAccount, Account destAccount, int amount) {
		
		System.out.println("ID source account called from transfer(): " + srcAccount.getId());
		System.out.println("ID destination account called from transfer(): " + destAccount.getId());
		
		em.persist(srcAccount);
		em.persist(destAccount);
		srcAccount.debit(amount);
		destAccount.credit(amount); 
	}

	public List<Client> getClients() {
		return em.createQuery("FROM Client").getResultList();
	}
	
	public Client getClient(long clientid) {
		return (Client) em.createQuery("FROM Client c where c.id=:id").setParameter("id", clientid).getSingleResult();
	}
}
