package main.java.com.idiban.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import main.java.com.idiban.PassHash;

@Entity
public class User {

	@Id
	@GeneratedValue
	private long id;

	private String name;
	private String email;
	private String password;

	@OneToMany(mappedBy = "userOne")
	private List<Phone> phones;

	@OneToOne(mappedBy = "userTwo")
	@JsonIgnore
	private UserLog userLog;

	public User() {
	}

	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = PassHash.getInstance().toHash(password);
		this.phones = new ArrayList<Phone>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(ArrayList<Phone> phones) {
		this.phones = phones;
	}

	public UserLog getUserLog() {
		return userLog;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", email=" + email + ", password=" + password + ", phones=" + phones + "]";
	}

	
	public void addphone(String number, String prefix) {
		this.phones.add(new Phone(number, prefix));
	}

}
