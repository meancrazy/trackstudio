package ru.sang.trackstudio;


import jetbrains.buildServer.issueTracker.AbstractIssueProvider;
import jetbrains.buildServer.issueTracker.IssueFetcher;
import jetbrains.buildServer.issueTracker.IssueProviderType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.regex.Pattern;


public class TrackStudioIssueProvider extends AbstractIssueProvider {

    //private final static Log LOGGER = LogFactory.getLog(TrackStudioIssueProvider.class);

    public TrackStudioIssueProvider(@NotNull final IssueProviderType type, @NotNull IssueFetcher fetcher) {
        super(type.getType(), fetcher);
    }

    @NotNull
    @Override
    protected Pattern compilePattern(@NotNull final Map<String, String> properties) {
        String pattern = properties.get("pattern");
        Pattern result = EMPTY_PATTERN;

        if (pattern != null) {
            result = safeCompile(pattern);
        } else {
            String idPrefix = properties.get("idPrefix");
            if (useIdPrefix() && idPrefix != null) {
                result = compileDisjunction(idPrefix, "-(\\d+)");
            }
        }

        ((TrackStudioIssueFetcher) myFetcher).setPattern(result);

        return result;
    }

    @Override
    protected boolean useIdPrefix() {
        return true;
    }

    @Nullable
    @Override
    protected String checkIssueIdBeforeTestConnection(@NotNull String issueId) {
        if (!myPattern.matcher(issueId).matches()) {
            return "The specified issue id can't be matched. Please enter a suitable issue id, or change the settings.";
        }

        return super.checkIssueIdBeforeTestConnection(issueId);
    }

    @NotNull
    @Override
    protected String extractId(@NotNull String id) {
        return ((TrackStudioIssueFetcher) myFetcher).extractId(id);
    }
}
