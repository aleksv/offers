package at.neseri.offers.main.customer;

public class Customer {
	private int id;
	private String vorname;
	private String nachname;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Customer withId(int id) {
		setId(id);
		return this;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public Customer withVorname(String vorname) {
		setVorname(vorname);
		return this;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public Customer withNachname(String nachname) {
		setNachname(nachname);
		return this;
	}

	@Override
	public String toString() {
		return nachname + " " + vorname + " (" + id + ")";
	}
}
