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
edit service section in the /usr/java/tomcat/conf/server.xml. add a <Context docBase="/opt/simis/files/images/carfixImages/" path="/images2"/>
```
 <Service name="ConnectApps">
        <Connector port="80" protocol="HTTP/1.1" maxThreads="150" scheme="http" clientAuth="false" />
        <Connector port="443" protocol="HTTP/1.1" SSLEnabled="true" maxThreads="150" scheme="https" secure="true" keystoreFile="/root/.keystore" keystorePass="Indigo@18." clientAuth="false" sslProtocol="TLS" />
        <Engine name="ConnectEngine" defaultHost="localhost">
            <Realm className="org.apache.catalina.realm.LockOutRealm">
                <Realm className="org.apache.catalina.realm.UserDatabaseRealm" resourceName="UserDatabase" />
            </Realm>
            <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs" prefix="access_log." suffix=".txt" pattern="%h %l %v %t &quot;%r&quot; %s %b" resolveHosts="false" />

            <Host name="localhost"  appBase="/home/webapps/connect"
                        unpackWARs="true" autoDeploy="true"
                        xmlValidation="false" xmlNamespaceAware="false">
                <Context path="/contrib" docBase="contrib" debug="0" />
                <Context docBase="/opt/simis/files/images/carfixImages/" path="/images2"/>
            </Host>

            <!-- <Host name="name-manager.onsocialcloud.com"  appBase="/home/webapps/manager"
                unpackWARs="true" autoDeploy="true"
                xmlValidation="false" xmlNamespaceAware="false">
            </Host> -->

            <!-- <Host name="localhost"  appBase="/home/webapps/crm"
                unpackWARs="true" autoDeploy="true"
                xmlValidation="false" xmlNamespaceAware="false">
            </Host> -->

        </Engine>
    </Service>


```

### SchedulerManager.java 

*  BackgroundJob.scheduleRecurrently(SEND_SP_REMINDERS_JOB, Cron.daily(17), SendReminderToServiceProviderJob::execute);
*  BackgroundJob.scheduleRecurrently(PROCESS_SP_BATCH_PAYENTS, Cron.daily(17), ProcessServiceProviderPaymentsJob::execute);

### CORS configuration.

The Access-Control-Allow-Origin headers are added by the nginx instance on the loadbalancer. This 
header was added by RestServlet.java , line 123 but has been removed since site_url is not being set.

RestRequestFilter.java also adds these headers if the site_url is set in the site_properties table - maybe the config should be 
removed from the LB ?



