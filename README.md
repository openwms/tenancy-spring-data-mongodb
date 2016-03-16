# tenancy-spring-data-mongodb

This sample application shows differences between SpEL expression evaluation on the example of a multi-tenant MongoDB application.

## Starting Point

With the Stamplets product we had the requirement to separate data based on incoming tenant information. This tenant information is sent
by mobile as well as web clients as part of the http header as an attribute. Based on this context information the proper set of MongoDB
collection is chosen in the persistence layer. In addition the tenant information can change dynamically. Tenants may be created on demand.
This can be realized in different ways.

## Separate Collections using the T-operator

Springs SpEL provides a nice operator (#{T(..)}) that resolves full-qualified Java classes that can be used to invoke methods by using
Java Reflection. It is the first approach to bring in a dynamic selection of MongoDB Collection.