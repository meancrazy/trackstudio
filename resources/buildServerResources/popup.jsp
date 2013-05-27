<%@include file="/include.jsp"%>
<jsp:useBean id="issue" scope="request" type="jetbrains.buildServer.issueTracker.IssueEx"/>
<c:set var="issueData" value="${issue.issueDataOrNull}"/>
<c:set var="fields" value="${issueData.allFields}"/>
<bs:issueDetailsPopup issue="${issue}" popupClass="yt" priorityClass="p${fields[priorityValueField]}">
	<jsp:attribute name="otherFields">
		<c:set var="fixVersion" value="${fields[fixVersionField]}"/>
		<c:if test="${not empty fixVersion}">
            <td title="Fix versions">${fixVersion}</td>
        </c:if>
    </jsp:attribute>
</bs:issueDetailsPopup>