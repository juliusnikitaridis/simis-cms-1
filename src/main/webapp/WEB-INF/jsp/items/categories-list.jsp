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
<%@ taglib prefix="font" uri="/WEB-INF/tlds/font-functions.tld" %>
<jsp:useBean id="userSession" class="com.simisinc.platform.presentation.controller.UserSession" scope="session"/>
<jsp:useBean id="widgetContext" class="com.simisinc.platform.presentation.controller.WidgetContext" scope="request"/>
<jsp:useBean id="categoryList" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="collection" class="com.simisinc.platform.domain.model.items.Collection" scope="request"/>
<jsp:useBean id="category" class="com.simisinc.platform.domain.model.items.Category" scope="request"/>
<jsp:useBean id="listingsLink" class="java.lang.String" scope="request"/>
<c:if test="${!empty title}">
  <h4><c:if test="${!empty icon}"><i class="fa ${icon}"></i> </c:if><c:out value="${title}" /></h4>
</c:if>
<ul class="no-bullet" style="text-indent: -11px; margin-left: 21px !important;">
  <c:choose>
    <c:when test="${!empty listingsLink}">
      <c:set var="listingsLinkPrefix" scope="request" value="${listingsLink}"/>
    </c:when>
    <c:otherwise>
      <c:set var="listingsLinkPrefix" scope="request" value="${ctx}/directory/${collection.uniqueId}"/>
    </c:otherwise>
  </c:choose>
  <c:choose>
    <c:when test="${category.id eq -1}">
      <li><a href="${listingsLinkPrefix}"><i class="${font:fas()} fa-circle-check"></i> All <c:out value="${collection.name}" /></a>&nbsp;<small class="subheader"><fmt:formatNumber value="${collection.itemCount}" /></small></li>
    </c:when>
    <c:otherwise>
      <li><a href="${listingsLinkPrefix}"><i class="${font:far()} fa-circle"></i> All <c:out value="${collection.name}" /></a>&nbsp;<small class="subheader"><fmt:formatNumber value="${collection.itemCount}" /></small></li>
    </c:otherwise>
  </c:choose>
  <c:forEach items="${categoryList}" var="thisCategory">
    <c:choose>
      <c:when test="${category.id eq thisCategory.id}">
        <li><a href="${listingsLinkPrefix}?categoryId=${thisCategory.id}"><i class="${font:fas()} fa-circle-check"></i> <c:out value="${thisCategory.name}" /></a>&nbsp;<small class="subheader"><fmt:formatNumber value="${thisCategory.itemCount}" /></small></li>
      </c:when>
      <c:otherwise>
        <li><a href="${listingsLinkPrefix}?categoryId=${thisCategory.id}"><i class="${font:far()} fa-circle"></i> <c:out value="${thisCategory.name}" /></a>&nbsp;<small class="subheader"><fmt:formatNumber value="${thisCategory.itemCount}" /></small></li>
      </c:otherwise>
    </c:choose>
  </c:forEach>
</ul>
