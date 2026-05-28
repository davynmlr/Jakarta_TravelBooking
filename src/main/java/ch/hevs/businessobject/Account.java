package ch.hevs.businessobject;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private String number;
	private long saldo;	
	private String description;
	
	// relations
	@ManyToOne
	private Client owner;

	// id 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	// number
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	// saldo	
	public long getSaldo() {
		return saldo;
	}
	public void setSaldo(long saldo) {
		this.saldo = saldo;
	}
	
	// description
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	// owner (from Client)
	public Client getOwner() {
		return owner;
	}
	public void setOwner(Client owner) {
		this.owner = owner;
	}
	
	// methods
	public void debit(int amount) {
		long newAmount = getSaldo() - amount;
		setSaldo(newAmount);
	}
	
	public void credit(int amount) {
		setSaldo(getSaldo() + amount);
	}

	// constructors
	public Account() {
	}
	public Account(String number, long saldo, Client owner,
			String description) {
		this.number = number;
		this.saldo = saldo;
		this.owner = owner;
		this.description = description;
	}
}