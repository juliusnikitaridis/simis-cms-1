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
<jsp:useBean id="name" class="java.lang.String" scope="request"/>
<jsp:useBean id="link" class="java.lang.String" scope="request"/>
<jsp:useBean id="icon" class="java.lang.String" scope="request"/>
<jsp:useBean id="leftIcon" class="java.lang.String" scope="request"/>
<jsp:useBean id="linkClass" class="java.lang.String" scope="request"/>
<jsp:useBean id="target" class="java.lang.String" scope="request"/>
<a <c:if test="${!empty target}">target="${target}" </c:if><c:if test="${!empty linkClass}">class="${linkClass}" </c:if>href="${link}"><c:if test="${!empty leftIcon}"><i class="fa ${leftIcon}"></i> </c:if><c:out value="${name}"/><c:if test="${!empty icon}"> <i class="fa ${icon}"></i></c:if></a>