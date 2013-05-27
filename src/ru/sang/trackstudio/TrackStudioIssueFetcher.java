package ru.sang.trackstudio;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import jetbrains.buildServer.issueTracker.AbstractIssueFetcher;
import jetbrains.buildServer.issueTracker.IssueData;
import jetbrains.buildServer.util.CollectionsUtil;
import jetbrains.buildServer.util.cache.EhCacheUtil;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.sang.trackstudio.restapi.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TrackStudioIssueFetcher extends AbstractIssueFetcher {

    private final static Log LOGGER = LogFactory.getLog(TrackStudioIssueFetcher.class);
    private Pattern pattern;

    public TrackStudioIssueFetcher(@NotNull EhCacheUtil cacheUtil) {
        super(cacheUtil);
    }

    @NotNull
    @Override
    public IssueData getIssue(@NotNull final String _host, @NotNull final String _id, @Nullable final Credentials _credentials) throws Exception {
        String url = getUrl(_host, _id);
        return getFromCacheOrFetch(url, new TrackStudioFetchFunction(_host, _id, _credentials));
    }

    @NotNull
    @Override
    public String getUrl(@NotNull final String _host, @NotNull final String _id) {
        return String.format("%stask/%s", _host, _id);
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    @NotNull
    protected String extractId(@NotNull String match) {
        Matcher matcher = pattern.matcher(match);
        if (matcher.find()) {
            return matcher.group(3);
        }

        return match.toUpperCase();
    }

    public class TrackStudioFetchFunction implements FetchFunction {
        private String host;
        private String id;
        private UsernamePasswordCredentials credentials;

        public TrackStudioFetchFunction(final String host, final String id, final Credentials credentials) {

            if (credentials == null) {
                throw new IllegalArgumentException(String.format("You must set username and password!"));
            }

            this.host = host;
            this.id = id;
            this.credentials = (UsernamePasswordCredentials) credentials;
        }

        @NotNull
        @Override
        public IssueData fetch() {
            String url = String.format("%srest/task/info/%s?login=%s&password=%s", host, extractId(id), credentials.getUserName(), credentials.getPassword());
            try {
                InputStream json = fetchHttpFile(url, credentials);
                return parseIssue(json);
            } catch (IOException e) {
                LOGGER.fatal(e);
                throw new RuntimeException("Error fetching issue data", e);
            }
        }

        private IssueData parseIssue(final InputStream json) {
            try {
                Gson gson = new Gson();
                Task task = gson.fromJson(new InputStreamReader(json, Charset.forName("UTF-8")), Task.class);

                String summary = task.getName();
                String state = task.getResolutionName();
                String type = task.getWorkflowName();
                String priority = task.getPriorityName();

                Map<String, String> data = CollectionsUtil.asMap(IssueData.SUMMARY_FIELD, summary,
                        IssueData.STATE_FIELD, state,
                        IssueData.TYPE_FIELD, type,
                        IssueData.PRIORITY_FIELD, priority);
                // todo add additional parameters

                Boolean resolved = task.isFinishedState();
                Boolean isFeature = false;
                String url = getUrl(host, id);

                return new IssueData(id, data, resolved, isFeature, url);
            } catch (JsonIOException e) {
                LOGGER.fatal(e);
                throw new RuntimeException(String.format("Error reading JSON for issue '%s' on '%s'.", id, host));
            } catch (JsonParseException e) {
                LOGGER.fatal(e);
                throw new RuntimeException(String.format("Error parsing JSON for issue '%s' on '%s'.", id, host));
            }
        }
    }
}
