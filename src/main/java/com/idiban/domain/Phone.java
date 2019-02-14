package main.java.com.idiban.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Phone {

	@Id
	@GeneratedValue
	@JsonIgnore
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private User userOne;

	private String number;
	private String prefix;

	public Phone() {
		super();
	}

	public Phone(String number, String prefix) {
		this.number = number;
		this.prefix = prefix;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUser(User userOne) {
		this.userOne = userOne;
	}

	@Override
	public String toString() {
		return "phone [number=" + number + ", prefix=" + prefix + "]";
	}

}
