package ch.hevs.test;

import java.sql.SQLException;

import org.junit.Test;

import ch.hevs.businessobject.Account;
import ch.hevs.businessobject.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import junit.framework.TestCase;

public class PopulateDB extends TestCase {

	@Test
	public void test() throws SQLException, ClassNotFoundException {
		
		EntityTransaction tx = null;
		try {
			
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("bankPU_unitTest");
			EntityManager em = emf.createEntityManager();
			tx = em.getTransaction();
			tx.begin();
		
			Client c1 = new Client("Zinedine", "Zidane");
			Account a1 = new Account("1000", 10000, c1, "Compte Courant");
			
			Client c2 = new Client("Michel", "Platini");
			Account a2 = new Account("1001", 20000, c2, "Compte Courant");
			Account a3 = new Account("1003", 1000, c2, "Livret A");
	
			Client c3 = new Client("Jean-Pierre", "Papin");
			Account a4 = new Account("1002", 30000, c3, "Compte Courant");
	
			em.persist(c1);
			em.persist(c2);
			em.persist(c3);
			
			em.persist(a1);
			em.persist(a2);
			em.persist(a3);
			em.persist(a4);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
