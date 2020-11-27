package at.neseri.offers.main.offer;

import at.neseri.offers.main.customer.Customer;
import at.neseri.offers.main.db.IIdentity;
import at.neseri.offers.main.utils.Reference;

public class Offer implements IIdentity {
	private int id;
	private String note;
	private Reference<Customer> customer;

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public Offer withId(int id) {
		this.id = id;
		return this;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Offer withNote(String note) {
		this.note = note;
		return this;
	}

	public Customer getCustomer() {
		return customer.get();
	}

	public void setCustomer(Reference<Customer> customer) {
		this.customer = customer;
	}

	public Offer withCustomer(Reference<Customer> customer) {
		this.customer = customer;
		return this;
	}

}
