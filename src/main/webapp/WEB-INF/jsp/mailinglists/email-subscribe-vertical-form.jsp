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
<%@ taglib prefix="js" uri="/WEB-INF/tlds/javascript-escape.tld" %>
<%@ taglib prefix="url" uri="/WEB-INF/tlds/url-functions.tld" %>
<%@ taglib prefix="text" uri="/WEB-INF/tlds/text-functions.tld" %>
<jsp:useBean id="userSession" class="com.simisinc.platform.presentation.controller.UserSession" scope="session"/>
<jsp:useBean id="widgetContext" class="com.simisinc.platform.presentation.controller.WidgetContext" scope="request"/>
<jsp:useBean id="sitePropertyMap" class="java.util.HashMap" scope="request"/>
<script type="text/javascript">
    function validateEmail${widgetContext.uniqueId}(email) {
        let re = /\S+@\S+\.\S+/;
        return re.test(email);
    }
    function emailSignUp${widgetContext.uniqueId}() {
        let email = document.getElementById("email${widgetContext.uniqueId}").value;
        let name = document.getElementById("name${widgetContext.uniqueId}").value;
        <c:if test="${showName eq 'true'}">
          if (name === undefined || name.trim().length === 0) {
            document.getElementById('nameHelpText${widgetContext.uniqueId}').innerHTML = "Please enter your name";
            return false;
          } else {
            document.getElementById('nameHelpText${widgetContext.uniqueId}').innerHTML = "";
          }
        </c:if>
        if (email === undefined || email.trim().length === 0) {
            document.getElementById('emailHelpText${widgetContext.uniqueId}').innerHTML = "Please enter your email address";
            return false;
        }
        if (!validateEmail${widgetContext.uniqueId}(email)) {
            document.getElementById('emailHelpText${widgetContext.uniqueId}').innerHTML = "Please re-enter your email address using a proper format.";
            return false;
        }
        $.getJSON("${ctx}/json/emailSubscribe?token=${userSession.formToken}&email=" + encodeURIComponent(email) + "&name=" + encodeURIComponent(name), function(data) {
            if (data.status === undefined || data.status !== '0') {
                document.getElementById('emailHelpText${widgetContext.uniqueId}').innerHTML = "Please re-enter your email address using a proper format.";
                return false;
            }
            document.getElementById('emailHelpText${widgetContext.uniqueId}').innerHTML = "Thanks for signing up for <c:out value="${js:escape(sitePropertyMap['site.name'])}"/> emails!";
        });
        return false;
    }
</script>
<form method="get" onsubmit="return emailSignUp${widgetContext.uniqueId}()">
  <c:if test="${!empty title}">
    <h4><c:if test="${!empty icon}"><i class="fa ${icon}"></i> </c:if><c:out value="${title}" /></h4>
  </c:if>
  <c:if test="${!empty introHtml}">
    <p>${introHtml}</p>
  </c:if>
  <c:if test="${showName eq 'true'}">
    <div class="small-12 cell">
      <input type="text" id="name${widgetContext.uniqueId}" name="name${widgetContext.uniqueId}" placeholder="Your first name" required>
      <p class="help-text" id="nameHelpText${widgetContext.uniqueId}"></p>
    </div>
  </c:if>
  <div class="small-12 cell">
    <input type="text" id="email${widgetContext.uniqueId}" name="email${widgetContext.uniqueId}" placeholder="Your email address" required>
    <p class="help-text" id="emailHelpText${widgetContext.uniqueId}"></p>
  </div>
  <c:if test="${!empty footerHtml}">
    <div class="small-12 cell">
      <p>${footerHtml}</p>
    </div>
  </c:if>
  <button type="submit" class="button no-gap expanded"><c:out value="${buttonName}" /></button>
</form>
