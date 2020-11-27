package at.neseri.offers.main.utils;

import java.util.function.Supplier;

public class Reference<T> {

	private Supplier<T> supp;

	public Reference(Supplier<T> supp) {
		this.supp = supp;
	}

	public T get() {
		return supp.get();
	}
}
