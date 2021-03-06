# tenancy-spring-data-mongodb

This sample application shows differences between SpEL expression evaluation on the example of a multi-tenant MongoDB application.

## Starting Point

With Stamplets project we had the requirement to separate data based on incoming tenant information. This tenant information is sent
by mobile as well as web clients as part of the http header as an attribute. Based on this context information the proper set of MongoDB
collection is chosen in the persistence layer. In addition the tenant information can change dynamically. Tenants may be created on demand.
This can be realized in different ways. Our first approach was to go with alt. 1 but we realized that we ran into performance issues, that's
why we were looking for other approaches and discussed those with Thomas Darimont and Oliver Gierke (both Spring Data team members).

## (1) Separate Collections using the T-operator

Springs SpEL provides a nice `#{T(..)}` operator to resolve full-qualified Java classes that are then used to invoke methods on. It was
the first approach to get it to work. The flow is as follows:
- An incoming client request hold the tenant information as http header attribute
- A servlet filter expects this header attribute and stores the current tenant in a thread-local context object
- At the persistence layer this context information is resolved with the T-operator to separate the MongoDB collection.

## (2) Using pre-compiled SpEL expressions

...

## (3) Call Java methods from SpEL Expressions

...

## (4) Using a Discriminator Property

Using a discriminator property value is different to the approaches we've seen so far in that all tenants share the same collection but are
differentiated by a more technical property on each Document. This approach is takes from the area of relational databases where a dedicated
column value distinguishes between different tenants. This solution does not fulfill the requirement of data separation by collections and
should only be mentioned here for the sake of completeness.