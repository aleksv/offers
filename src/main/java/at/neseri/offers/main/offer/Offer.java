package at.neseri.offers.main.offer;

import java.time.LocalDate;
import java.util.ArrayList;
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
	private final List<OfferPosition> offerPositions = new ArrayList<>();

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
	
	public List<OfferPosition> getOfferPositions() {
		return offerPositions;
	}

	public class OfferPosition {
		private int position;
		private String posTitle;
		private double cost;
		private String details;

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public String getPosTitle() {
			return posTitle;
		}

		public void setPosTitle(String posTitle) {
			this.posTitle = posTitle;
		}

		public double getCost() {
			return cost;
		}

		public void setCost(double cost) {
			this.cost = cost;
		}

		public String getDetails() {
			return details;
		}

		public void setDetails(String details) {
			this.details = details;
		}

	}
}
