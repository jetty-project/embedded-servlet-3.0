> :warning: **This uses the end-of-life Jetty version 8.**   
> Do not use in production.  
> For Jetty 9 / Servlet 3.1 version see https://github.com/jetty-project/embedded-servlet-3.1

Example: Embedded Jetty 8 w/ Servlet 3.0 and Annotation
=====================================================

This is a maven project setup as a WAR packaging, with an EmbedMe class in
the test scope that starts an embedded jetty of the WAR file being
produced by this project.

Quick Start
-----------

    $ mvn clean install exec:exec

Open your web browser to

    http://localhost:8080/  for the root of the project

    http://localhost:8080/test/  to show the annotation working as expected


