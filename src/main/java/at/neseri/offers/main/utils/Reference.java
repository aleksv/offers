package at.neseri.offers.main.utils;

import java.util.function.Function;

public class Reference<TIn, TOut> {

	private Function<TIn, TOut> supp;

	public Reference(Function<TIn, TOut> supp) {
		this.supp = supp;
	}

	public TOut get(TIn id) {
		return supp.apply(id);
	}
}
