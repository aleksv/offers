package at.neseri.offers.main.customer;

import java.util.Optional;

import at.neseri.offers.main.db.IIdentity;

public class Customer implements IIdentity {
	private int id = 0;
	private String vorname;
	private String nachname;
	private String strasse;
	private String plz;
	private String ort;
	private String firma;

	@Override
	public int getId() {
		return id;
	}

	@Override
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
		return (Optional.ofNullable(firma).orElse("").isBlank() ? nachname + " " + vorname
				: firma + " (" + nachname + " " + vorname + ")");
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public Customer withStrasse(String strasse) {
		this.strasse = strasse;
		return this;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public Customer withPlz(String plz) {
		this.plz = plz;
		return this;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public Customer withOrt(String ort) {
		this.ort = ort;
		return this;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public Customer withFirma(String firma) {
		this.firma = firma;
		return this;
	}

}
