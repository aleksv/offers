package at.neseri.offers.main.offer;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import at.neseri.offers.main.customer.Customer;
import at.neseri.offers.main.db.IIdentity;
import at.neseri.offers.main.utils.Reference;

public class Offer implements IIdentity {
	private int id;
	private String note;
	private String subject;
	private LocalDate created = LocalDate.now();
	private Reference<Integer, Customer> customer;
	private int customerId;
	private Reference<Integer, List<OfferPosition>> offerPositions;

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
		offerPositions.setCacheId(id);
		customer.setCacheId(id);
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
		return Optional.ofNullable(customer).map(c -> c.get(customerId)).orElse(null);
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public Offer withCustomerId(int customerId) {
		this.customerId = customerId;
		return this;
	}

	public Offer withCustomerReference(Reference<Integer, Customer> customer) {
		this.customer = customer;
		return this;
	}

	public LocalDate getCreated() {
		return created;
	}

	public void setCreated(LocalDate date) {
		this.created = date;
	}

	public Offer withCreated(LocalDate date) {
		this.created = date;
		return this;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Offer withSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public Offer withOfferPositionsReference(Reference<Integer, List<OfferPosition>> offerPositions) {
		this.offerPositions = offerPositions;
		return this;
	}

	public List<OfferPosition> getOfferPositions() {
		return offerPositions.get(getId());
	}

	@Override
	public String toString() {
		return subject + " (" + id + ")";
	}
}
