package at.neseri.offers.main.utils;

import java.util.Objects;
import java.util.function.Function;

public class Reference<TIn, TOut> {

	private Function<TIn, TOut> supp;
	private TOut cache;
	private TIn cacheId;

	public Reference(Function<TIn, TOut> supp) {
		this.supp = supp;
	}

	public TOut get(TIn id) {
		if (Objects.equals(cacheId, id)) {
			return cache;
		}
		cache = supp.apply(id);
		cacheId = id;
		return cache;
	}
}
