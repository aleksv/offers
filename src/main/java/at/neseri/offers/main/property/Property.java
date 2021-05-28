package at.neseri.offers.main.property;

import at.neseri.offers.main.db.IIdentity;

public class Property implements IIdentity {

	private int id;
	private String key;
	private String value;

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
