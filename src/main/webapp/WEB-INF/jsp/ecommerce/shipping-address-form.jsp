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
<%@ taglib prefix="product" uri="/WEB-INF/tlds/product-functions.tld" %>
<jsp:useBean id="userSession" class="com.simisinc.platform.presentation.controller.UserSession" scope="session"/>
<jsp:useBean id="widgetContext" class="com.simisinc.platform.presentation.controller.WidgetContext" scope="request"/>
<jsp:useBean id="address" class="com.simisinc.platform.domain.model.ecommerce.Address" scope="request"/>
<jsp:useBean id="shippingCountryList" class="java.util.ArrayList" scope="request"/>
<%-- Form body--%>
<script>
  function checkForm${widgetContext.uniqueId}() {
    if (document.getElementById("shippingCountry").value.trim() == "") {
      alert("Please choose a Country");
      return false;
    }
    if (document.getElementById("shippingState").value.trim() == "") {
      alert("Please choose a State or Province");
      return false;
    }
    return true;
  }
</script>
<%@include file="../page_messages.jspf" %>
<form method="post" onsubmit="return checkForm${widgetContext.uniqueId}()" autocomplete="on">
  <%-- Required by controller --%>
  <input type="hidden" name="widget" value="${widgetContext.uniqueId}"/>
  <input type="hidden" name="token" value="${userSession.formToken}"/>
  <%-- The form --%>
  <%--   <span class="required">*</span>Shipping is limited to United States addresses only--%>
  <div class="grid-x">
    <div class="small-12 cell">
      <fieldset>
        <label for="country">Country <span class="required">*</span>
          <select id="country" name="country" onchange="updateCountry${widgetContext.uniqueId}()">
            <option value="">Choose</option>
            <c:forEach items="${shippingCountryList}" var="shippingCountry">
              <option value="<c:out value="${shippingCountry.title}" />"<c:if test="${address.country eq shippingCountry.title}"> selected</c:if>><c:out value="${shippingCountry.title}" /></option>
            </c:forEach>
          </select>
        </label>
      </fieldset>
      <fieldset>
        <div class="grid-x grid-margin-x">
          <div class="small-6 cell">
            <label for="firstName">First Name <span class="required">*</span>
              <input type="text" id="firstName" name="firstName" placeholder="First Name" value="<c:out value="${address.firstName}" />" autocomplete="given-name" required/>
            </label>
          </div>
          <div class="small-6 cell">
            <label for="lastName">Last Name <span class="required">*</span>
              <input type="text" id="lastName" name="lastName" placeholder="Last Name" value="<c:out value="${address.lastName}" />" autocomplete="family-name" required/>
            </label>
          </div>
        </div>
      </fieldset>

      <%--
        <label for="organization">Company
          <input type="text" id="organization" name="organization" placeholder="Company (optional)" value="<c:out value="${address.organization}" />" autocomplete="organization"/>
        </label>
        --%>

      <fieldset>
        <div class="grid-x grid-margin-x">
          <div class="small-6 cell">
            <label for="street">Street Address <span class="required">*</span>
              <input type="text" id="street" name="street" placeholder="Address" value="<c:out value="${address.street}" />" autocomplete="address-line1" required/>
            </label>
          </div>
          <div class="small-6 cell">
            <label for="addressLine2">Apartment/Suite/Other
              <input type="text" id="addressLine2" name="addressLine2" placeholder="Apartment, Suite, etc. (optional)" value="<c:out value="${address.addressLine2}" />" autocomplete="address-line2"/>
            </label>
          </div>
        </div>
      </fieldset>

      <fieldset>
        <div class="grid-x grid-margin-x">
          <div class="small-4 cell">
            <label for="city">City <span class="required">*</span>
              <input type="text" id="city" name="city" placeholder="City" value="<c:out value="${address.city}" />" autocomplete="address-level2" required/>
            </label>
          </div>
          <div class="small-4 cell">
            <label for="state">State/Province <span class="required">*</span>
              <input style="<c:if test="${address.country eq 'United States'}">display:none</c:if>" type="text" id="province" name="province" value="<c:out value="${address.state}" />" />
              <select style="<c:if test="${address.country ne 'United States'}">display:none</c:if>" id="state" name="state">
                <option value="">Choose</option>
                <option value="AL"<c:if test="${address.state eq 'AL'}"> selected</c:if>>Alabama (AL)</option>
                <option value="AK"<c:if test="${address.state eq 'AK'}"> selected</c:if>>Alaska (AK)</option>
                <option value="AZ"<c:if test="${address.state eq 'AZ'}"> selected</c:if>>Arizona (AZ)</option>
                <option value="AR"<c:if test="${address.state eq 'AR'}"> selected</c:if>>Arkansas (AR)</option>
                <option value="CA"<c:if test="${address.state eq 'CA'}"> selected</c:if>>California (CA)</option>
                <option value="CO"<c:if test="${address.state eq 'CO'}"> selected</c:if>>Colorado (CO)</option>
                <option value="CT"<c:if test="${address.state eq 'CT'}"> selected</c:if>>Connecticut (CT)</option>
                <option value="DE"<c:if test="${address.state eq 'DE'}"> selected</c:if>>Delaware (DE)</option>
                <option value="DC"<c:if test="${address.state eq 'DC'}"> selected</c:if>>District Of Columbia (DC)</option>
                <option value="FL"<c:if test="${address.state eq 'FL'}"> selected</c:if>>Florida (FL)</option>
                <option value="GA"<c:if test="${address.state eq 'GA'}"> selected</c:if>>Georgia (GA)</option>
                <option value="HI"<c:if test="${address.state eq 'HI'}"> selected</c:if>>Hawaii (HI)</option>
                <option value="ID"<c:if test="${address.state eq 'ID'}"> selected</c:if>>Idaho (ID)</option>
                <option value="IL"<c:if test="${address.state eq 'IL'}"> selected</c:if>>Illinois (IL)</option>
                <option value="IN"<c:if test="${address.state eq 'IN'}"> selected</c:if>>Indiana (IN)</option>
                <option value="IA"<c:if test="${address.state eq 'IA'}"> selected</c:if>>Iowa (IA)</option>
                <option value="KS"<c:if test="${address.state eq 'KS'}"> selected</c:if>>Kansas (KS)</option>
                <option value="KY"<c:if test="${address.state eq 'KY'}"> selected</c:if>>Kentucky (KY)</option>
                <option value="LA"<c:if test="${address.state eq 'LA'}"> selected</c:if>>Louisiana (LA)</option>
                <option value="ME"<c:if test="${address.state eq 'ME'}"> selected</c:if>>Maine (ME)</option>
                <option value="MD"<c:if test="${address.state eq 'MD'}"> selected</c:if>>Maryland (MD)</option>
                <option value="MA"<c:if test="${address.state eq 'MA'}"> selected</c:if>>Massachusetts (MA)</option>
                <option value="MI"<c:if test="${address.state eq 'MI'}"> selected</c:if>>Michigan (MI)</option>
                <option value="MN"<c:if test="${address.state eq 'MN'}"> selected</c:if>>Minnesota (MN)</option>
                <option value="MS"<c:if test="${address.state eq 'MS'}"> selected</c:if>>Mississippi (MS)</option>
                <option value="MO"<c:if test="${address.state eq 'MO'}"> selected</c:if>>Missouri (MO)</option>
                <option value="MT"<c:if test="${address.state eq 'MT'}"> selected</c:if>>Montana (MT)</option>
                <option value="NE"<c:if test="${address.state eq 'NE'}"> selected</c:if>>Nebraska (NE)</option>
                <option value="NV"<c:if test="${address.state eq 'NV'}"> selected</c:if>>Nevada (NV)</option>
                <option value="NH"<c:if test="${address.state eq 'NH'}"> selected</c:if>>New Hampshire (NH)</option>
                <option value="NJ"<c:if test="${address.state eq 'NJ'}"> selected</c:if>>New Jersey (NJ)</option>
                <option value="NM"<c:if test="${address.state eq 'NM'}"> selected</c:if>>New Mexico (NM)</option>
                <option value="NY"<c:if test="${address.state eq 'NY'}"> selected</c:if>>New York (NY)</option>
                <option value="NC"<c:if test="${address.state eq 'NC'}"> selected</c:if>>North Carolina (NC)</option>
                <option value="ND"<c:if test="${address.state eq 'ND'}"> selected</c:if>>North Dakota (ND)</option>
                <option value="OH"<c:if test="${address.state eq 'OH'}"> selected</c:if>>Ohio (OH)</option>
                <option value="OK"<c:if test="${address.state eq 'OK'}"> selected</c:if>>Oklahoma (OK)</option>
                <option value="OR"<c:if test="${address.state eq 'OR'}"> selected</c:if>>Oregon (OR)</option>
                <option value="PA"<c:if test="${address.state eq 'PA'}"> selected</c:if>>Pennsylvania (PA)</option>
                <option value="RI"<c:if test="${address.state eq 'RI'}"> selected</c:if>>Rhode Island (RI)</option>
                <option value="SC"<c:if test="${address.state eq 'SC'}"> selected</c:if>>South Carolina (SC)</option>
                <option value="SD"<c:if test="${address.state eq 'SD'}"> selected</c:if>>South Dakota (SD)</option>
                <option value="TN"<c:if test="${address.state eq 'TN'}"> selected</c:if>>Tennessee (TN)</option>
                <option value="TX"<c:if test="${address.state eq 'TX'}"> selected</c:if>>Texas (TX)</option>
                <option value="UT"<c:if test="${address.state eq 'UT'}"> selected</c:if>>Utah (UT)</option>
                <option value="VT"<c:if test="${address.state eq 'VT'}"> selected</c:if>>Vermont</option>
                <option value="VA"<c:if test="${address.state eq 'VA'}"> selected</c:if>>Virginia</option>
                <option value="WA"<c:if test="${address.state eq 'WA'}"> selected</c:if>>Washington</option>
                <option value="WV"<c:if test="${address.state eq 'WV'}"> selected</c:if>>West Virginia</option>
                <option value="WI"<c:if test="${address.state eq 'WI'}"> selected</c:if>>Wisconsin</option>
                <option value="WY"<c:if test="${address.state eq 'WY'}"> selected</c:if>>Wyoming</option>
                <option value="AS"<c:if test="${address.state eq 'AS'}"> selected</c:if>>American Samoa (AS)</option>
                <option value="GU"<c:if test="${address.state eq 'GU'}"> selected</c:if>>Guam (GU)</option>
                <option value="MP"<c:if test="${address.state eq 'MP'}"> selected</c:if>>Northern Mariana Islands (MP)</option>
                <option value="PR"<c:if test="${address.state eq 'PR'}"> selected</c:if>>Puerto Rico (PR)</option>
                <option value="UM"<c:if test="${address.state eq 'UM'}"> selected</c:if>>United States Minor Outlying Islands (UM)</option>
                <option value="VI"<c:if test="${address.state eq 'VI'}"> selected</c:if>>Virgin Islands (VI)</option>
                <option value="AA"<c:if test="${address.state eq 'AA'}"> selected</c:if>>Armed Forces Americas (AA)</option>
                <option value="AP"<c:if test="${address.state eq 'AP'}"> selected</c:if>>Armed Forces Pacific (AP)</option>
                <option value="AE"<c:if test="${address.state eq 'AE'}"> selected</c:if>>Armed Forces Others (AE)</option>
              </select>
            </label>
          </div>
          <div class="small-4 cell">
            <label for="postalCode">Zip/Postal Code <span class="required">*</span>
              <input type="text" id="postalCode" name="postalCode" placeholder="Zip/Postal Code" value="<c:out value="${address.postalCode}" />" autocomplete="postal-code" required/>
            </label>
          </div>
        </div>
      </fieldset>
    </div>
  </div>
  <div class="button-container">
    <button class="button primary" name="button" value="save">Save &amp; Continue</button>
  </div>
</form>
<script>
  function updateCountry${widgetContext.uniqueId}() {
    var countryElement = document.getElementById("country");
    var country = countryElement.options[countryElement.selectedIndex].value;
    if (country === 'United States') {
      $('#province').hide();
      $('#state').show();
    } else {
      $('#state').hide();
      $('#province').show();
    }
  }
</script>