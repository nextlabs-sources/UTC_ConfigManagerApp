package com.nextlabs.utc.conf.hibernate;

// Generated May 9, 2013 1:12:51 PM by Hibernate Tools 3.4.0.CR1

/**
 * CountryMasterId generated by hbm2java
 */
public class CountryMasterId{

	private String countryCode;
	private String shortName;
	private String name;

	public CountryMasterId() {
	}

	public CountryMasterId(String countryCode, String shortName,
			String name) {
		this.countryCode = countryCode;
		this.shortName = shortName;
		this.name = name;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CountryMasterId))
			return false;
		CountryMasterId castOther = (CountryMasterId) other;

		return ((this.getCountryCode() == castOther.getCountryCode()) || (this
				.getCountryCode() != null && castOther.getCountryCode() != null && this
				.getCountryCode().equals(castOther.getCountryCode())))
				&& ((this.getShortName() == castOther.getShortName()) || (this
						.getShortName() != null
						&& castOther.getShortName() != null && this
						.getShortName().equals(castOther.getShortName())))
				&& ((this.getName() == castOther.getName()) || (this.getName() != null
						&& castOther.getName() != null && this.getName()
						.equals(castOther.getName())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getCountryCode() == null ? 0 : this.getCountryCode()
						.hashCode());
		result = 37 * result
				+ (getShortName() == null ? 0 : this.getShortName().hashCode());
		result = 37 * result
				+ (getName() == null ? 0 : this.getName().hashCode());
		return result;
	}

}