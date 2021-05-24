package at.neseri.offers.main.item;

import at.neseri.offers.main.db.IIdentity;

public class Item implements IIdentity {
	private int id;
	private String name;

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public Item withId(int id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Item withName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public String toString() {
		return name;
	}
}
