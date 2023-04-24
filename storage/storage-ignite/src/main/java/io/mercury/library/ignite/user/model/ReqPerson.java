package io.mercury.library.ignite.user.model;

/**
 * Used for getting request for login and register
 * Because the request only has username and password info
 *
 * @param username Request username
 * @param password Request password
 */
public record ReqPerson(
        String username,
        String password) {
}
