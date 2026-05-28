package ch.hevs.businessobject;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;



@Entity
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;
	private String lastname;
	private String firstname;

	// relations
	@OneToMany(mappedBy = "owner")
	private List<Account> accounts;
	
	// id
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	// firstname
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	// lastname
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	// accounts (From Account)
	public List<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	
	// helper methods
	public void addAccount(Account a) {
		accounts.add(a);
		a.setOwner(this);
	}

	// constructors
	public Client() {
	}
	public Client(String firstname, String lastname) {
		this.lastname = lastname;
		this.firstname = firstname;
	}

	@Override
	public String toString() {
		String result = id + "-" + lastname + "-" + firstname;
		return result;
	}
}
