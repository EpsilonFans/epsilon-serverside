# Notes #

The Epsilon Server-side languages are configured to be compiled using [Maven](http://maven.apache.org) and/or [Eclipse with the Web tooling](http://eclipse.org).

## Creating applications using Epsilon Web ##

You can create new applications using Maven and adding dependencies to the Epsilon web. A project with dependencies to the Epsilon Web will include the java libraries and configurations to support the EGL as a templating language.

### Including dependencies to the Epsilon Web ###

To include the Epsilon languages in your application, first you must add the dependency to the epsilon.egl.servlet project.

	<dependencies>

		<!-- Web support for Epsilon Languages -->
		<dependency>
		    <groupId>org.eclipse.epsilon</groupId>
		    <artifactId>org.eclipse.epsilon.egl.web</artifactId>
		    <version>1.x</version>
		    <type>war</type>
		</dependency>	
		:
	</dependencies>
		
In addition, you must configure the WAR overlay to include first the Epsilon EGL content and later the content of the current project. (In this way, you can overwrite the files provided by default) 

	<build>
		:
		<plugins>

			<!-- WAR overlay configuration -->
			<plugin>
			    <groupId>org.apache.maven.plugins</groupId>
			    <artifactId>maven-war-plugin</artifactId>
			    <version>2.6</version>
			    <configuration>
			        <overlays>
			            <overlay>
						       <groupId>org.eclipse.epsilon</groupId>
						       <artifactId>org.eclipse.epsilon.egl.web</artifactId>
						   </overlay>
						   <overlay>
						   	    <!-- include last the current project -->
						   </overlay>
			        </overlays>
			    </configuration>
			</plugin>            
			:
		</plugins>
		:
	</build>

### Modifying the server.xml configuration file ###

Using the recent Java EE APIs, usually you do not need to change the `server.xml` file. However, if you create a new `web.xml` and want to use the Epsilon languages, you must include some configuration options for the EGL Servlet.    

    <servlet>
        <servlet-name>egl</servlet-name>
        <servlet-class>org.eclipse.epsilon.egl.servlet.EglServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>egl</servlet-name>
        <url-pattern>*.egl</url-pattern>
    </servlet-mapping>


NOTE: The overlay configuration included before ensures that the web.xml provided by your project will overwrite the web.xml included by default by the Epsilon Web. You must include this EGL servlet configuration to make Epsilon work in the final application. Without this configuration Epsilon will not work.


## Running the Project using Maven ##

You can run the application using the Embedded Jetty server. You can run the application using `mvn jetty:run-war`.

The project will run (by default) in `http://localhost:8080/firewell.web`

## Running the Project in Eclipse ##

### Using Maven integration (m2e) 

Two launcher files have been created to run the application in Eclipse using Maven. 

* The `Run egl.web.launch` runs the application. You can select that file and use the option `Run As` -> `Run egl.web`.  

### Using WTP

You can deploy the application in Eclipse using the "J2EE Preview" server without any problem. You can create the server in the Eclipse `Servers` view and then use the `Run As` -> `Run in Server` menu options.

The project will run (by default) in `http://localhost:8080/org.eclipse.epsilon.egl.servlet/`

Each time you modify the application, the application must be re-published to the "J2EE Preview" server. You can use the button in the toolbar of the "Servers" view.

### Project Facets for Eclipse ##

In order to run the application in the Eclipse WTP, proper project facets are required. The facets for the project are the following:

* Dynamic Web Module version 3.0 (i.e. facet="jst.web" version="3.0")
* Java version 1.7 (i.e. facet="java" version="1.7") 
* Javascript version 1.0 (i.e. facet="wst.jsdt.web" version="1.0")
  
If you have some problema and cannot change the facets using the IDE (in the Project Properties), you can try changing directly the values in the `.settings/org.eclipse.wst.common.project.facet.core.xml` file

## Support for Google App Engine ##

Google App Engine uses Servlet API 2.5. Probably, this is the reason why original Epsilon EGL servlet uses that (old) version. 

A profile is provided to compile the application using the Servlet API 2.5. You can compile or run the application using the Servlet 2.5 API using `mvn -Pservlet-2.5 ...`

* To compile the web application, you can use `mvn -Pservlet-2.5 package `
* To run the web application, you can execute `mvn -Pservlet-2.5 jetty:run-war `  
   
