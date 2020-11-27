package at.neseri.offers.main.item;

import at.neseri.offers.main.db.IIdentity;

public class Item implements IIdentity {
	private int id;
	private String name;
	private Unit unit;
	private double price;

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

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Item withUnit(String unit) {
		this.unit = unit == null ? null : Unit.byString(unit);
		return this;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Item withPrice(double price) {
		this.price = price;
		return this;
	}

	@Override
	public String toString() {
		return name + " ( " + id + ")";
	}
}
