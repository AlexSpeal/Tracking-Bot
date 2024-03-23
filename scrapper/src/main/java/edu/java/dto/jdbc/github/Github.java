package edu.java.dto.jdbc.github;

public record Github(Repository repository, Branch[] branches,
                     PullRequest[] pullRequests) {

}
