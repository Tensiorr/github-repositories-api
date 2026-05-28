package com.tensiorr.github_repositories_api;


import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "github.api.url=http://localhost:8090")
public class GitHubControllerIntegrationTest {

    private RestTemplate restTemplate;
    static WireMockServer wireMockServer;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(HttpStatusCode statusCode) {
                return false;
            }
        });
        wireMockServer = new WireMockServer(8090);
        wireMockServer.start();
        configureFor("localhost", 8090);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void shouldReturnRepositoriesForExistingUser() {
        stubFor(get("/users/testuser/repos")
                .willReturn(okJson("""
                    [
                        {
                            "name": "my-repo",
                            "fork": false,
                            "owner": { "login": "testuser" }
                        }
                    ]
                    """)));

        stubFor(get("/repos/testuser/my-repo/branches")
                .willReturn(okJson("""
                    [
                        {
                            "name": "main",
                            "commit": { "sha": "abc123" }
                        }
                    ]
                    """)));

        var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/users/testuser/repos",
                RepoResponse[].class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].repositoryName()).isEqualTo("my-repo");
        assertThat(response.getBody()[0].ownerLogin()).isEqualTo("testuser");
        assertThat(response.getBody()[0].branches()).hasSize(1);
        assertThat(response.getBody()[0].branches().get(0).name()).isEqualTo("main");
        assertThat(response.getBody()[0].branches().get(0).lastCommitSha()).isEqualTo("abc123");
    }


    @Test
    void shouldReturnEmptyListWhenAllReposAreForks() {
        stubFor(get("/users/testuser/repos")
                .willReturn(okJson("""
                    [
                        {
                            "name": "forked-repo",
                            "fork": true,
                            "owner": { "login": "testuser" }
                        }
                    ]
                    """)));

        var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/users/testuser/repos",
                RepoResponse[].class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void shouldReturn404WhenUserDoesNotExist() {
        stubFor(get("/users/nonexistentuser/repos")
                .willReturn(aResponse().withStatus(404)));

        var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/users/nonexistentuser/repos",
                ErrorResponse.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody().message()).isEqualTo("User nonexistentuser not found");
    }
}