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
<%@ taglib prefix="font" uri="/WEB-INF/font-functions.tld" %>
<%@ taglib prefix="collection" uri="/WEB-INF/collection-functions.tld" %>
<%@ taglib prefix="category" uri="/WEB-INF/category-functions.tld" %>
<jsp:useBean id="userSession" class="com.simisinc.platform.presentation.controller.UserSession" scope="session"/>
<jsp:useBean id="widgetContext" class="com.simisinc.platform.presentation.controller.WidgetContext" scope="request"/>
<jsp:useBean id="collection" class="com.simisinc.platform.domain.model.items.Collection" scope="request"/>
<jsp:useBean id="itemList" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="category" class="com.simisinc.platform.domain.model.items.Category" scope="request"/>
<jsp:useBean id="recordPaging" class="com.simisinc.platform.infrastructure.database.DataConstraints" scope="request"/>
<jsp:useBean id="smallGridCount" class="java.lang.String" scope="request"/>
<jsp:useBean id="mediumGridCount" class="java.lang.String" scope="request"/>
<jsp:useBean id="largeGridCount" class="java.lang.String" scope="request"/>
<jsp:useBean id="showImage" class="java.lang.String" scope="request"/>
<jsp:useBean id="showIcon" class="java.lang.String" scope="request"/>
<jsp:useBean id="showLink" class="java.lang.String" scope="request"/>
<jsp:useBean id="useItemLink" class="java.lang.String" scope="request"/>
<jsp:useBean id="showLaunchLink" class="java.lang.String" scope="request"/>
<jsp:useBean id="launchLabel" class="java.lang.String" scope="request"/>
<jsp:useBean id="isSearchResults" class="java.lang.String" scope="request"/>
<jsp:useBean id="searchName" class="java.lang.String" scope="request"/>
<jsp:useBean id="searchLocation" class="java.lang.String" scope="request"/>
<style>
  .card-catalog .item-name {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    word-break: break-word;
    position: static!important;
  }
  .card-catalog .card-top {
    padding: .5rem;
  }
  .card-catalog .item-name {
    min-height: 60px;
  }
  .card-catalog .card-section {
      padding: 0.5rem 1rem;
  }
  .card-catalog .card-bottom {
    position: relative;
    bottom: 0;
    padding: 0.5rem 1rem;
  }
</style>
<c:if test="${!empty title}">
  <h4><c:if test="${!empty icon}"><i class="fa ${icon}"></i> </c:if><c:out value="${title}" /></h4>
</c:if>
<%@include file="../page_messages.jspf" %>
<c:if test="${isSearchResults eq 'true'}">
  <p class="search-results subheader">
    <c:choose>
      <c:when test="${recordPaging.totalRecordCount == 0}">
        No search results found
      </c:when>
      <c:when test="${recordPaging.totalRecordCount == 1}">
        Found 1 result
      </c:when>
      <c:when test="${recordPaging.totalRecordCount < 0}">
        Found <fmt:formatNumber value="${itemList.size()}" /> results
      </c:when>
      <c:otherwise>
        Found <fmt:formatNumber value="${recordPaging.totalRecordCount}" /> results
      </c:otherwise>
    </c:choose>
    for <strong>&quot;<c:out value="${searchName}" />&quot;</strong><c:if test="${!empty searchLocation}">
      near <strong>&quot;<c:out value="${searchLocation}" />&quot;</strong>
    </c:if><c:if test="${itemList.size() < recordPaging.totalRecordCount}">, showing the first ${recordPaging.pageSize}...</c:if>
  </p>
</c:if>
<c:choose>
  <c:when test="${!empty itemList}">
    <div class="grid-x grid-margin-x align-stretch card-catalog">
      <c:forEach items="${itemList}" var="item">
        <c:set var="categoryIcon" scope="request" value="${category:icon(item.categoryId)}"/>
        <c:set var="categoryHeaderCSS" scope="request" value="${category:headerColorCSS(item.categoryId)}"/>
        <div class="small-<c:out value="${smallGridCount}" /> medium-<c:out value="${mediumGridCount}" /> large-<c:out value="${largeGridCount}" /> cell card">
          <c:choose>
            <c:when test="${showImage eq 'true' && !empty item.imageUrl}">
              <div class="card-top no-gap text-center image-browser" style="<c:out value="${categoryHeaderCSS}" />">
              <c:choose>
                <c:when test="${useItemLink eq 'true' && !empty item.url && (fn:startsWith(item.url, 'http://') || fn:startsWith(item.url, 'https://'))}">
                  <a target="_blank" href="${item.url}"><img src="${item.imageUrl}" /></a>
                </c:when>
                <c:otherwise>
                  <a href="${ctx}/show/${item.uniqueId}"><img src="${item.imageUrl}" /></a>
                </c:otherwise>
              </c:choose>
              </div>
            </c:when>
            <c:otherwise>
              <div class="card-top no-gap no-border text-center" style="<c:out value="${categoryHeaderCSS}" />;height: 175px">
                <div class="text-middle">
                  <c:set var="thisIcon" scope="request" value="bookmark-o" />
                  <c:if test="${showIcon eq 'true' && !empty categoryIcon}">
                    <c:set var="thisIcon" scope="request" value="${categoryIcon}" />
                  </c:if>
                  <c:choose>
                    <c:when test="${useItemLink eq 'true' && !empty item.url && (fn:startsWith(item.url, 'http://') || fn:startsWith(item.url, 'https://'))}">
                      <a target="_blank" href="${item.url}" style="<c:out value="${categoryHeaderCSS}" />"><i class="fa fa-4x fa-<c:out value="${thisIcon}" />"></i></a>
                    </c:when>
                    <c:otherwise>
                      <a href="${ctx}/show/${item.uniqueId}" style="<c:out value="${categoryHeaderCSS}" />"><i class="fa fa-4x fa-<c:out value="${thisIcon}" />"></i></a>
                    </c:otherwise>
                  </c:choose>
                </div>
              </div>
            </c:otherwise>
          </c:choose>
          <div class="card-section">
            <div class="item-name">
              <c:choose>
                <c:when test="${showLink eq 'false'}">
                  <c:out value="${item.name}" />
                </c:when>
                <c:when test="${useItemLink eq 'true' && (fn:startsWith(item.url, 'http://') || fn:startsWith(item.url, 'https://'))}">
                  <a target="_blank" href="${item.url}"><c:out value="${item.name}"/></a>
                </c:when>
                <c:otherwise>
                  <a href="${ctx}/show/${item.uniqueId}"><c:out value="${item.name}" /></a>
                </c:otherwise>
              </c:choose>
            </div>
            <c:if test="${!empty item.address}"><div class="item-city"><small><c:out value="${item.address}" /></small></div></c:if>
            <c:if test="${!empty item.keywords}"><div class="item-keywords"><small><c:out value="${item.keywords}" /></small></div></c:if>
          </div>
          <div class="card-bottom">
            <div class="grid-x align-bottom">
              <div class="auto cell category-icon">
                <c:set var="categoryIcon" scope="request" value="${category:icon(item.categoryId)}"/>
                <c:choose>
                  <c:when test="${!empty categoryIcon}">
                    <i class="${font:fad()} fa-<c:out value="${categoryIcon}" />"></i>
                  </c:when>
                  <c:when test="${!empty collection:icon(item.collectionId)}">
                    <i class="${font:fad()} fa-<c:out value="${collection:icon(item.collectionId)}" />"></i>
                  </c:when>
                  <c:otherwise>
                    <i class="fa fa-blackboard"></i>
                  </c:otherwise>
                </c:choose>
                <c:out value="${category:name(item.categoryId)}" />
              </div>
              <c:if test="${showLink eq 'false'}">
                <div class="shrink cell item-url text-right">
                  <c:choose>
                    <c:when test="${useItemLink eq 'true' && !empty item.url && (fn:startsWith(item.url, 'http://') || fn:startsWith(item.url, 'https://'))}">
                      <a target="_blank" href="${item.url}"><c:out value="${launchLabel}"/></a>
                    </c:when>
                    <c:otherwise>
                      <a href="${ctx}/show/${item.uniqueId}">View</a>
                    </c:otherwise>
                  </c:choose>
                </div>
              </c:if>
            </div>
          </div>
        </div>
      </c:forEach>
    </div>
    <c:if test="${isSearchResults ne 'true'}">
      <%-- Paging Control --%>
      <c:if test="${category.id gt 0}">
        <c:set var="recordPagingParams" scope="request" value="categoryId=${category.id}"/>
      </c:if>
      <%@include file="../paging_control.jspf" %>
    </c:if>
  </c:when>
  <c:when test="${isSearchResults ne 'true'}">
    <p class="subheader">
      No <c:out value="${fn:toLowerCase(collection.name)}"/> were found
    </p>
  </c:when>
</c:choose>