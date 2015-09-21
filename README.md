## Stubble - Simple Rest API stubs

### What is it?
This is a simple Java based library that eases the development of Restful API stubs without having to write any code.
This is the purpose of this app.
It aims to simplify the process of creating the stubs for the restful services purely by configuration and templating.

#### Add this to your pom (Push to maven repo Pending):

```
<dependency>
    <groupId>com.github.saiprasadkrishnamurthy</groupId>
    <artifactId>stubble</artifactId>
    <version>1.0</version>
</dependency>
```

#### Restful API Definitions yml file

You'll need to define your restful services (endpoints) in one or more yml files. These yml file(s) contain the behaviour of the services
for these endpoints. It's easier to explain it with an example:

```
- api:                  Get customer billing status
  httpverb:             get
  uri:                  /:customer/:phone/billingstatus?verbose=:verbose
  responseStatus:       201
  responseCookies:      cookie1=cookievalue1,cookie2=cookievalue2
  responseHeaders:      header2=headervalue2,header3=headervalue3
  responseTemplateFile: customer_billing_status.hbs
  responseContentType:  application/json

- api:                  Update customer details
  httpverb:             put
  uri:                  /customer/:customerId/
  responseStatus:       200
  responseCookies:      cookie1=cookievalue1,cookie2=cookievalue2,apiToken=XXAASSWWEERR
  responseHeaders:      header2=headervalue2,header3=headervalue3
  responseTemplateFile: update_customer.hbs
  responseContentType:  application/json
```

The above definition file contains the definition for 2 services (apis).

Where:

FIELD       |   DESCRIPTION |
------------|---------------|
api         |  Some readable description of the service (for better maintainability).|
httpverb    |  The http request method of this service.     |
uri | The uri mapping of this service. All the variables (both path variables and query string variables) are prefixed with a ':' and can be accessed in the templates. Details about the message templates are coming up in the below sections.  
responsestatus| The response status code that this service should return    |
responseCookies |   a comma separated key value pair of the cookies that the response from this service should contain. |
responseHeaders |   a comma separated key value pair of the headers that the response from this service should contain. |
responseTemplateFile | name of the handlebar template file to construct the response. More on the response templates in the below sections.|
responseContentType | The content type header to be set in the response by this service.|


#### Examples based on the above definitions file:
INPUT REQUEST           |   MATCHED API | VARIABLES AND VALUES |
------------------------|---------------| ---------------------|
GET /saikris/112233/billingstatus?verbose=yes |'Get customer billing status' | uri.customer = sai, uri.phone=112233, uri.verbose=yes|
GET /saikris/112233/billingstatus |'Get customer billing status' | uri.customer = sai, uri.phone=112233, uri.verbose=empty|
GET /saikris/112233/billingstatusss | ERROR | NA |


#### Response Templates

** Handlebar Templates are chosen to be the response templating language because of their simplicity **

The template file is specified in the api definition (per api).

The templates get full access to the variables that are defined in the definitions
 
The access rules are pretty simple:

You access the fields with <yml_property_name>.<variable_name>

A few examples, 
* Input request: /customer/sai, Definition: /customer/:name. Then the variable uri.name contains the value 'sai'.
* If you want to access requestHeaders, you may access it using the key **requestHeaders.<header_name>**
* If you want to access the Request Body, you may access it using the key **requestBody** [Note that 
if the request body is a JSON, then you could access the JSON properties as usual to any level of nesting. For example: requestBody.customer.name

#### Sample response template file:

```
{
    indexname: {{uri.customer}},
    body: {
            city: {{requestBody.city}}
            phoneNumber: {{uri.phone}}
        }
}
```

#### To run the app as a standalone java application

* Download the JAR from the maven repo (or use maven exec plugin or anyother runner of your choice).
``` 
java -jar stubble-jar-with-dependencies.jar --templatesdir=<root_dir_of_handlebar_templates> --definitions=<root_dir_of_yml_definitions> 
```
* By default the app listens to the port 4567. If you wish to use a different port, then override with a JVM system property -Dport
 for example -Dport=8080
 
 
#### To run the app programatically using the Java API

* Add the maven dependency.
* You could fork the below lines of code in a separate thread.
```
main.Main stubbleMain = new main.Main();
stubbleMain.run("<apiDefinitionsRootDirectory>", "<templatesRootDirectory>")
```
#### Best Practices

* Use one yml file per api For example, all the billing related services can be defined in billing_api_defs.yml.
* Name the yml and template files sensibly.
* Don't complicate the templates (be explicit as much as possible).





  





 



  
  
  
  







