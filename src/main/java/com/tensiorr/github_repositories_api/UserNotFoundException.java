package com.tensiorr.github_repositories_api;

class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User " + username + " not found");
    }
}
