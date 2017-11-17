# Grails Controller LocalDate test project

Project created to demonstrate issue with `java.time.LocalDate` in Controllers.

Using the Grails Java8 plugin, the application/framework tries to create objects to participate in Data Binding. 
Due to the fact `LocalDate` doesn't have a public constructor, the code never makes it into Data Binding.

```
Could not find matching constructor for: java.time.LocalDate(). Stacktrace follows:

java.lang.reflect.InvocationTargetException: null
	at org.grails.core.DefaultGrailsControllerClass$ReflectionInvoker.invoke(DefaultGrailsControllerClass.java:211)
	at org.grails.core.DefaultGrailsControllerClass.invoke(DefaultGrailsControllerClass.java:188)
	at org.grails.web.mapping.mvc.UrlMappingsInfoHandlerAdapter.handle(UrlMappingsInfoHandlerAdapter.groovy:90)
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:967)
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:901)
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970)
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)
	at org.springframework.boot.web.filter.ApplicationContextHeaderFilter.doFilterInternal(ApplicationContextHeaderFilter.java:55)
	at org.grails.web.servlet.mvc.GrailsWebRequestFilter.doFilterInternal(GrailsWebRequestFilter.java:77)
	at org.grails.web.filters.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:67)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at java.lang.Thread.run(Thread.java:745)
Caused by: groovy.lang.GroovyRuntimeException: Could not find matching constructor for: java.time.LocalDate()
	at grails.artefact.Controller$Trait$Helper.initializeCommandObject(Controller.groovy:425)
	... 14 common frames omitted
```

# Reproducing the issue

1. `./gradlew bootRun`
2. Try calling a method with a `LocalDate` controller param
   ```bash
   curl -X GET 'http://localhost:8080/localDate/birthdays?birthday=2017-01-01'
   ```
   * Throws a 500 error calling `newInstance()` on a `LocalDate`
3. Try calling a method with a command object wrapping a `LocalDate`
   ```bash
   curl -X GET 'http://localhost:8080/command/birthdays?birthday=2017-01-01'
   ```
   * Is unable to bind (See: issue in [Grails Java8](https://github.com/grails-plugins/grails-java8/issues/7))
4. Try calling a method with a `java.util.Date` for fun
   ```bash
   curl -X GET 'http://localhost:8080/date/birthdays?birthday=2017-01-01'
   ```
   * 'Works'