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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="url" uri="/WEB-INF/tlds/url-functions.tld" %>
<%@ taglib prefix="text" uri="/WEB-INF/tlds/text-functions.tld" %>
<jsp:useBean id="userSession" class="com.simisinc.platform.presentation.controller.UserSession" scope="session"/>
<jsp:useBean id="widgetContext" class="com.simisinc.platform.presentation.controller.WidgetContext" scope="request"/>
<jsp:useBean id="fieldList" class="java.util.ArrayList" scope="request"/>
<c:if test="${!empty title}">
  <h4><c:if test="${!empty icon}"><i class="fa ${icon}"></i> </c:if><c:out value="${title}" /></h4>
</c:if>
<%@include file="../page_messages.jspf" %>
<c:forEach items="${fieldList}" var="field" varStatus="status">
  <c:choose>
    <c:when test="${'html' eq field.type}">
      <c:if test="${!empty field.label}">
        <strong><c:out value="${field.label}"/></strong>:
      </c:if>
      <p>${field.value}</p>
    </c:when>
    <c:when test="${'url' eq field.type && (fn:startsWith(field.value, 'http://') || fn:startsWith(field.value, 'https://'))}">
      <p><a class="button small no-gap" href="${url:encode(field.value)}" target="_blank" rel="nofollow">${field.label} <i class="fa fa-external-link"></i></a></p>
    </c:when>
    <c:otherwise>
      <c:if test="${!empty field.label}">
        <strong><c:out value="${field.label}"/></strong>:
      </c:if>
      <p><c:out value="${field.value}"/></p>
    </c:otherwise>
  </c:choose>
</c:forEach>