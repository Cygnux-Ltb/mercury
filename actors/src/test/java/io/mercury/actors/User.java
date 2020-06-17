package io.mercury.actors;

public final class User {

	private final long userId;
	private final String userName;
	private final int age;

	public User(long userId, String userName, int age) {
		this.userId = userId;
		this.userName = userName;
		this.age = age;
	}

	public long getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public int getAge() {
		return age;
	}

}