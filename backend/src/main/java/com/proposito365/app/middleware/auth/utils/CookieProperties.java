package com.proposito365.app.middleware.auth.utils;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.security.cookie")
public class CookieProperties {
	private String name;
	private String nameRefresh;
	private boolean httpOnly;
	private boolean secure;
	private Duration maxAge;
	private Duration maxAgeRefresh;
	private String sameSite;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameRefresh() {
		return nameRefresh;
	}

	public void setNameRefresh(String nameRefresh) {
		this.nameRefresh = nameRefresh;
	}

	public boolean isHttpOnly() {
		return httpOnly;
	}

	public void setHttpOnly(boolean httpOnly) {
		this.httpOnly = httpOnly;
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public Duration getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Duration maxAge) {
		this.maxAge = maxAge;
	}

	public Duration getMaxAgeRefresh() {
		return maxAgeRefresh;
	}

	public void setMaxAgeRefresh(Duration maxAgeRefresh) {
		this.maxAgeRefresh = maxAgeRefresh;
	}
	
	public String getSameSite() {
		return sameSite;
	}

	public void setSameSite(String sameSite) {
		this.sameSite = sameSite;
	}

	@Override
	public String toString() {
		return "CookieProperties [name=" + name + ", nameRefresh=" + nameRefresh + ", httpOnly=" + httpOnly
				+ ", secure=" + secure + ", maxAge=" + maxAge + ", maxAgeRefresh=" + maxAgeRefresh + ", sameSite="
				+ sameSite + "]";
	}

	
}
