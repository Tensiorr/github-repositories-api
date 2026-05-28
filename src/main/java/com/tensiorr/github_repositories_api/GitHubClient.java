package com.tensiorr.github_repositories_api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
class GitHubClient {

    private final RestClient restClient;

    public GitHubClient(RestClient.Builder builder, @Value("${github.api.url}") String githubApiUrl) {
        this.restClient = builder
                .baseUrl(githubApiUrl)
                .build();
    }

    public List<GitHubRepoDto> getRepos(String username) {
        return restClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new UserNotFoundException(username);
                })
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<GitHubBranchDto> getBranches(String username, String repo) {
        return restClient.get()
                .uri("/repos/{username}/{repo}/branches", username, repo)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
