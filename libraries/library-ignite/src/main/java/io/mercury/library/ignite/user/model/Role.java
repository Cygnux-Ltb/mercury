package io.mercury.library.ignite.user.model;

/**
 * Roles：
 * ADMIN & MEMBER
 **/
public enum Role {

    ADMIN, MEMBER;

    public String authority() {
        return "ROLE_" + this.name();
    }

    @Override
    public String toString() {
        return this.name();
    }
}
