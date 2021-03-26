package at.neseri.offers.main.item;

import java.util.Arrays;

public enum Unit {
	NONE("-"), HOUR("Stunde"), LITER("Liter"), KG("Kilogramm");

	private String name;

	private Unit(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public static Unit byString(String string) {
		return Arrays.asList(values()).stream().filter(o -> o.toString().equalsIgnoreCase(string)).findFirst()
				.orElse(null);
	}
}
