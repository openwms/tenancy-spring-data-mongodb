# tenancy-spring-data-mongodb

This sample application shows differences between SpEL expression evaluation on the example of a multi-tenant MongoDB application.

## Starting Point

With the Stamplets product we had the requirement to separate data based on incoming tenant information. This tenant information is sent
by mobile as well as web clients as part of the http header as an attribute. Based on this context information the proper set of MongoDB
collection is chosen in the persistence layer. This can be realized in different ways.

