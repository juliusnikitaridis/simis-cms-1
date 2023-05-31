# CARFIX release notes 1.0



## site.properties config

the following need to be set in the site_properties table

* site.header.line1 = CarFix
* site.registrations = true
* site.name = true
* site.login
* site.online = true
* site.description = portal for booking car services..etc
* site.image

* mail.from_address = admin@carfixsa.com
* mail.ssl = false
* mail.port = 587
* mail.from_name = CarFiX
* mail.host_name = smtp.sendgrid.net
* mail.password = passwordxcxxxx
* mail.username = apikey
* site.registration.confirm.link = https://carfix.connectmobiles24.com/
* site.carfix.images.base.dir = /home/webapps/connect/ROOT/images/carfixImages

* peachpayments.paymentlinks.api.url = https://testsecure.peachpayments.com/checkout/initiate
* peachpayments.paymentlinks.api.authenticationEntityId = 8ac7a4c98694e687018696fe5bdd024f 
* peachpayments.payment.api.url = https://test.peachpay.co.za/API/Payments
* peachpayments.payment.api.key = 062199b6-1460-4feb-a3df-699808377e07

* peachpayments.paymentstatus.api.url = https://testsecure.peachpayments.com/status



### ADDITIONAL TOMCAT SERVER CONFIGURATION
```aidl
<servlet>
    <servlet-name>images</servlet-name>
    <servlet-class>com.example.images.ImageServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>images</servlet-name>
    <url-pattern>/images/*</url-pattern>
</servlet-mapping>

```

### SchedulerManager.java 

*  BackgroundJob.scheduleRecurrently(SEND_SP_REMINDERS_JOB, Cron.daily(17), SendReminderToServiceProviderJob::execute);
*  BackgroundJob.scheduleRecurrently(PROCESS_SP_BATCH_PAYENTS, Cron.daily(17), ProcessServiceProviderPaymentsJob::execute);

### CORS configuration.

The Access-Control-Allow-Origin headers are added by the nginx instance on the loadbalancer. This 
header was added by RestServlet.java , line 123 but has been removed since site_url is not being set.

RestRequestFilter.java also adds these headers if the site_url is set in the site_properties table - maybe the config should be 
removed from the LB ?



