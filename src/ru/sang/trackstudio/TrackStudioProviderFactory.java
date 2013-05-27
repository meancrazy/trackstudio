package ru.sang.trackstudio;


import jetbrains.buildServer.issueTracker.AbstractIssueProviderFactory;
import jetbrains.buildServer.issueTracker.IssueFetcher;
import jetbrains.buildServer.issueTracker.IssueProvider;
import org.jetbrains.annotations.NotNull;


public class TrackStudioProviderFactory extends AbstractIssueProviderFactory {

    protected TrackStudioProviderFactory(@NotNull IssueFetcher fetcher) {
        super(fetcher, "TrackStudio");
    }

    @NotNull
    public IssueProvider createProvider() {
        return new TrackStudioIssueProvider(myFetcher);
    }
}
