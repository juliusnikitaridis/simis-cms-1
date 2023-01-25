<%--
  ~ Copyright 2022 SimIS Inc.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="font" uri="/WEB-INF/tlds/font-functions.tld" %>
<%@ taglib prefix="js" uri="/WEB-INF/tlds/javascript-escape.tld" %>
<%@ taglib prefix="url" uri="/WEB-INF/tlds/url-functions.tld" %>
<%@ taglib prefix="date" uri="/WEB-INF/tlds/date-functions.tld" %>
<%@ taglib prefix="number" uri="/WEB-INF/tlds/number-functions.tld" %>
<jsp:useBean id="userSession" class="com.simisinc.platform.presentation.controller.UserSession" scope="session"/>
<jsp:useBean id="widgetContext" class="com.simisinc.platform.presentation.controller.WidgetContext" scope="request"/>
<jsp:useBean id="datasetList" class="java.util.ArrayList" scope="request"/>
<c:if test="${!empty title}">
  <h4><c:if test="${!empty icon}"><i class="fa ${icon}"></i> </c:if><c:out value="${title}" /></h4>
</c:if>
<%@include file="../page_messages.jspf" %>
<a class="button small radius primary" href="${ctx}/admin/datasets/new"><i class="fa fa-cloud-upload"></i> Add a Dataset</a>
<table class="unstriped">
  <thead>
    <tr>
      <th>Name</th>
      <th width="180" class="text-center">Data Date</th>
      <th width="180" class="text-center">Schedule Status</th>
      <th width="180" class="text-center">Sync Status</th>
      <th width="180" class="text-center">Records</th>
      <th class="text-center">File Type</th>
      <th class="text-center">Size</th>
      <th width="80">Action</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach items="${datasetList}" var="dataset">
    <tr>
      <td>
        <a href="${ctx}/admin/dataset-preview?datasetId=${dataset.id}"><c:out value="${dataset.name}" /></a>
        <br />
        <small>
          <c:if test="${dataset.rowsProcessed gt -1}"><fmt:formatNumber value="${dataset.rowsProcessed}" /> /</c:if>
          <c:if test="${dataset.rowCount gt -1}">          
            <fmt:formatNumber value="${dataset.rowCount}" /> record<c:if test="${dataset.rowCount ne 1}">s</c:if>
          </c:if>
        </small>
      </td>
      <td class="text-center">
        <c:choose>
          <c:when test="${!empty dataset.lastDownload}">
            <c:choose>
              <c:when test="${fn:contains(date:relative(dataset.lastDownload), 'an hour') || fn:contains(date:relative(dataset.lastDownload), 'minute') || fn:contains(date:relative(dataset.lastDownload), 'now')}">
                <span class="label round tiny success"><c:out value="${date:relative(dataset.lastDownload)}" /></span>
              </c:when>
              <c:otherwise>
                <span class="label round tiny"><c:out value="${date:relative(dataset.lastDownload)}" /></span>
              </c:otherwise>
            </c:choose>
          </c:when>
          <c:otherwise>
            <small><c:out value="${date:relative(dataset.created)}" /></small>
          </c:otherwise>
        </c:choose>
      </td>
      <td class="text-center">
        <c:if test="${dataset.scheduleEnabled}">
          <c:choose>
            <c:when test="${dataset.queueStatus eq 1}">
              <span class="label round primary">Queued</span>
            </c:when>
            <c:when test="${dataset.queueStatus gt 1}">
              <span class="label round alert">Failed</span>
            </c:when>
            <c:when test="${dataset.queueAttempts gt 1}">
              <span class="label round warning">Retrying</span>
            </c:when>
            <c:otherwise>
              <span class="label round success">Scheduled</span>
            </c:otherwise>
          </c:choose>
          <br />
          <small><c:out value="${dataset.scheduleFrequency}" /></small>
        </c:if>
      </td>
      <td class="text-center">
        <c:if test="${dataset.syncEnabled}">
          <c:choose>
            <c:when test="${dataset.syncStatus eq 1}">
              <span class="label round primary">Processing</span>
            </c:when>
            <c:when test="${dataset.syncStatus gt 1}">
              <span class="label round alert">Failed</span>
            </c:when>
            <c:otherwise>
              <span class="label round success">Enabled</span>
            </c:otherwise>
          </c:choose>
        </c:if>
      </td>
      <td class="text-center">
        <c:choose>
          <c:when test="${dataset.processStatus eq 1}">
            <span class="label round success"><i class="fa fa-spinner fa-spin fa-fw"></i> Processing</span><br />
            <fmt:formatNumber value="${dataset.rowsProcessed}" /> / <fmt:formatNumber value="${dataset.rowCount}" />
          </c:when>
          <c:when test="${dataset.processStatus gt 1}">
            <span class="label round alert"><i class="fa fa-spinner fa-spin fa-fw"></i> Processing</span><br />
            <fmt:formatNumber value="${dataset.rowsProcessed}" /> / <fmt:formatNumber value="${dataset.rowCount}" />
          </c:when>
          <c:when test="${!empty dataset.processed}">
            <span class="label round success"><i class="fa fa-check"></i> Processed</span><br />
            <small class="subheader"><fmt:formatNumber value="${dataset.totalProcessTime}" /> ms</small>
          </c:when>
          <c:when test="${dataset.rowCount gt -1}">
            <span class="label round secondary">Ready</span>
          </c:when>
          <c:otherwise>
            <span class="label round warning" id="rowCount">Data Not Found</span>
          </c:otherwise>
        </c:choose>
      </td>
      <td class="text-center">
        <small><c:out value="${dataset.fileType}" /></small>
      </td>
      <td class="text-center"><small><c:out value="${number:suffix(dataset.fileLength)}"/></small></td>
      <td>
        <a title="Modify dataset" href="${ctx}/admin/dataset-mapper?datasetId=${dataset.id}"><small><i class="${font:fas()} fa-edit"></i></small></a>
        <a href="${ctx}/assets/dataset/${dataset.url}"><i class="fa fa-download"></i></a>
        <a href="${widgetContext.uri}?command=delete&widget=${widgetContext.uniqueId}&token=${userSession.formToken}&datasetId=${dataset.id}" onclick="return confirm('Are you sure you want to delete <c:out value="${js:escape(dataset.name)}" />?');"><i class="fa fa-remove"></i></a>
        <%--<a href="${ctx}/admin/dataset?datasetId=${dataset.id}"><i class="fas fa-edit"></i></a>--%>
      </td>
    </tr>
    </c:forEach>
    <c:if test="${empty datasetList}">
      <tr>
        <td colspan="8">No datasets were found</td>
      </tr>
    </c:if>
  </tbody>
</table>
