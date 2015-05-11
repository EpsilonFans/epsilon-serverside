# Epsilon Web Server-side
Epsilon Web is a web runtime to execute Epsilon as a server-side scripting language.

### What is this ?

Epsilon Web is a web application that works as a runtime able to execute Epsilon EGL scripts to generate content. Using this web application, a developer can add EGL scripts (or create an overlay with these files) to create Epsilon-based applications.

This project is based on the [Epsilon EGL servlet][egl-servlet].

[egl-servlet]: https://www.eclipse.org/epsilon/doc/articles/egl-server-side/

### Usage

Using Maven, you can create a web application including only Epsilon-compatible models (such as Ecore models, XML and GraphML files), EOL libraries and EGL scripts. You must include a dependency to the epsilon.web in you ``pom.xml``

```xml
		<dependency>
			<groupId>org.eclipse.epsilon</groupId>
			<artifactId>org.eclipse.epsilon.web</artifactId>
			<version>1.2-SNAPSHOT</version>
			<type>war</type>
		</dependency>
```

### Example Application

The project includes an example application with the demo scripts provided in the [Epsilon EGL servlet][egl-servlet].

Using Maven, you can run the application using the ``jetty:run`` goal in the ``org.eclipse.epsilon.web.example`` project.

```
    $ cd org.eclipse.epsilon.web.example
    $ mvn jetty:run
``` 

Using Eclipse m2e, you can run the application using a provided launcher file.

1. Select the ``org.eclipse.epsilon.web.example`` project.
2. Select the ``Run epsilon.web.example.launch`` file
3. Right click on the file, and execute "Run As > Run epsilon.web.example"
 
 
### Branches in the Git repository

There are many branches in the project's Git:

* ``1.x``, is the original Epsilon EGL servlet organized as a Maven project
* ``1.2.0``, is the project updated to the Epsilon libraries version 1.2  