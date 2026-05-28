package ch.hevs.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import ch.hevs.businessobject.Account;
import ch.hevs.businessobject.Client;
import ch.hevs.service.Bank;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * TransferBean.java
 * 
 */

@SessionScoped
@Named("transferBean")
public class TransferBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Client> clients;
    private List<String> clientNames;
    private List<String> sourceAccountDescriptions;
    private List<String> destinationAccountDescriptions;
    private String sourceAccountDescription;
    private String destinationAccountDescription;
    private String sourceClientName;
    private String destinationClientName;
    private String transactionResult;
    private int transactionAmount;
    
    @Inject
    private Bank bank;
    
    @PostConstruct
    public void initialize() {
    	
    	// get clients
		List<Client> clientList = bank.getClients();
		this.clientNames = new ArrayList<String>();
		for (Client client : clientList) {
			this.clientNames.add(client.getLastname());
		}
		
		// initialize account descriptions
		this.sourceAccountDescriptions = new ArrayList<String>();
		this.destinationAccountDescriptions = new ArrayList<String>();
		List<Account> accounts = bank.getAccountListFromClientLastname(clientList.get(0).getLastname());
		this.sourceAccountDescriptions.add(accounts.get(0).getDescription());
		this.destinationAccountDescriptions.add(accounts.get(0).getDescription());
    }
    
    // transactionAmount
    public int getTransactionAmount () {
    	return transactionAmount;
    }
    public void setTransactionAmount (final int transactionAmount) {
    	this.transactionAmount=transactionAmount;
    }
    
    // sourceClientName
    public String getSourceClientName () {
    	return sourceClientName;
    }
    public void setSourceClientName (final String sourceClientName) {
    	this.sourceClientName=sourceClientName;
    }
    
    // sourceAccountDescriptions
    public List<String> getSourceAccountDescriptions () {
    	return sourceAccountDescriptions;
    }
    
    // destinationAccountDescriptions
    public List<String> getDestinationAccountDescriptions () {
    	return destinationAccountDescriptions;
    }
    
    // destinationClientName
    public String getDestinationClientName () {
    	return destinationClientName;
    }
    public void setDestinationClientName (final String destinationClientName) {
    	this.destinationClientName=destinationClientName;
    }
    
    // transactionResult
    public String getTransactionResult () {
    	return transactionResult;
    }
	public void setTransactionResult(String transactionResult) {
		this.transactionResult = transactionResult;
	}
    
	// sourceAccountDescription
    public String getSourceAccountDescription() {
		return sourceAccountDescription;
	}
	public void setSourceAccountDescription(String sourceAccountDescription) {
		this.sourceAccountDescription = sourceAccountDescription;
	}

	// destinationAccountDescription
	public String getDestinationAccountDescription() {
		return destinationAccountDescription;
	}
	public void setDestinationAccountDescription(
			String destinationAccountDescription) {
		this.destinationAccountDescription = destinationAccountDescription;
	}

	public void updateSourceAccounts(ValueChangeEvent event) {
    	this.sourceClientName = (String)event.getNewValue();
    	
	    List<Account> accounts = bank.getAccountListFromClientLastname(this.sourceClientName);
	    this.sourceAccountDescriptions = new ArrayList<String>();
		for (Account account : accounts) {
			this.sourceAccountDescriptions.add(account.getDescription());
		}
    }
	public void updateDestinationAccounts(ValueChangeEvent event) {
    	this.destinationClientName = (String)event.getNewValue();
			
	    List<Account> accounts = bank.getAccountListFromClientLastname(this.destinationClientName);
	    this.destinationAccountDescriptions = new ArrayList<String>();
		for (Account account : accounts) {
			this.destinationAccountDescriptions.add(account.getDescription());
		}
    }

    public List<Client> getClients() {
		return clients;
    }
    
    public List<String> getClientNames() {
    	return clientNames;
    }
    
    
    public String performTransfer() {
    	
    	try {
			if (sourceClientName.equals(destinationClientName) && sourceAccountDescription.equals(destinationAccountDescription)) {
				
				this.transactionResult="Error: accounts are identical!";
			} 
			else {
				
				Account compteSrc = bank.getAccount(sourceAccountDescription, sourceClientName);
				Account compteDest = bank.getAccount(destinationAccountDescription, destinationClientName);
	
				// Transfer
				bank.transfer(compteSrc, compteDest, transactionAmount);
				this.transactionResult="Success!";
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

		return "showTransferResult"; //  the String value returned represents the outcome used by the navigation handler to determine what page to display next.
	} 
}