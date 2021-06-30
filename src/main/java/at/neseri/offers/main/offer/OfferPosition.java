package at.neseri.offers.main.offer;

import java.util.Optional;

public class OfferPosition {
	private String posTitle;
	private double cost;
	private double singleCost;
	private String details = "";

	public String getPosTitle() {
		return posTitle;
	}

	public void setPosTitle(String posTitle) {
		this.posTitle = posTitle;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = Optional.ofNullable(details).orElse("");
	}

	public double getSingleCost() {
		return singleCost;
	}

	public void setSingleCost(double singleCost) {
		this.singleCost = singleCost;
	}

}