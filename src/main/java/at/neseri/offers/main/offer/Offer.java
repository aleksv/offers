package at.neseri.offers.main.offer;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import at.neseri.offers.main.customer.Customer;
import at.neseri.offers.main.db.IIdentity;
import at.neseri.offers.main.item.Item;
import at.neseri.offers.main.utils.Reference;

public class Offer implements IIdentity {
	private int id;
	private String note;
	private LocalDate created = LocalDate.now();
	private Reference<Integer, Customer> customer;
	private Reference<Set<Integer>, List<Item>> items;
	private int customerId;
	private Set<Integer> itemIds;

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
		return customer.get(customerId);
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

	public Offer withItemReference(Reference<Set<Integer>, List<Item>> items) {
		this.items = items;
		return this;
	}

	public List<Item> getItems() {
		return items.get(itemIds);
	}

	public Offer withItemIds(Set<Integer> itemIds) {
		this.itemIds = itemIds;
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

}
