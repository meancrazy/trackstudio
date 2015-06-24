package ru.sang.trackstudio;


import jetbrains.buildServer.issueTracker.AbstractIssueProviderFactory;
import jetbrains.buildServer.issueTracker.IssueFetcher;
import jetbrains.buildServer.issueTracker.IssueProvider;
import jetbrains.buildServer.issueTracker.IssueProviderType;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;


public class TrackStudioProviderFactory extends AbstractIssueProviderFactory {

    protected TrackStudioProviderFactory(@NotNull final IssueProviderType type,  @NotNull IssueFetcher fetcher) {
        super(type, fetcher);
    }

    @NotNull
    public IssueProvider createProvider() {
        return new TrackStudioIssueProvider(myType, myFetcher);
    }
}

