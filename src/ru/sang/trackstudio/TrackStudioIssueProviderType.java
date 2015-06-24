package ru.sang.trackstudio;

import jetbrains.buildServer.issueTracker.IssueProviderType;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

public class TrackStudioIssueProviderType extends IssueProviderType {

    @NotNull
    private final String myConfigUrl;

    @NotNull
    private final String myPopupUrl;

    public TrackStudioIssueProviderType(@NotNull final PluginDescriptor pluginDescriptor) {
        myConfigUrl = pluginDescriptor.getPluginResourcesPath("admin/editIssueProvider.jsp");
        myPopupUrl = pluginDescriptor.getPluginResourcesPath("popup.jsp");
    }

    @NotNull
    @Override
    public String getType() {
        return "trackstudio";
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "TrackStudio";
    }

    @NotNull
    @Override
    public String getEditParametersUrl() {
        return myConfigUrl;
    }

    @NotNull
    @Override
    public String getIssueDetailsUrl() {
        return myPopupUrl;
    }
}
