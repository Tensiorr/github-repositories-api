# GitHub Repositories API

A simple proxy app that lists GitHub repositories for a given user, filtering out forks.

## How to run

```bash
./gradlew bootRun
```

## Running tests

```bash
./gradlew test
```

## Endpoint
```
GET /users/{username}/repos
```

Returns all non-fork repositories for the given GitHub user, along with their branches and last commit SHA.

**Success (200):**
```json
[
  {
    "repositoryName": "BudgetApp",
    "ownerLogin": "Tensiorr",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "08aca13f5eedc0abe49d72e3d122a2aef2bd0dad"
      }
    ]
  },
  {
    "repositoryName": "Weather_data_pipeline",
    "ownerLogin": "Tensiorr",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "28bc0a44dcf407c0a5891d78c3773a70a3e94ded"
      }
    ]
  }
]
```

**User not found (404):**
```json
{
  "status": 404,
  "message": "User nonexistentuser1 not found"
}
```

## Tech stack

- Java 25
- Spring Boot 4
- Gradle (Kotlin DSL)