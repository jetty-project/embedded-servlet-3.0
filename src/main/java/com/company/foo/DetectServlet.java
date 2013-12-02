package com.company.foo;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is provided as an example to a question about how to detect that
 * Jetty is running a servlet.
 * 
 * http://stackoverflow.com/questions/17151551/detect-if-running-servlet-container-is-eclipse-jetty/17152192#17152192
 */
@WebServlet(urlPatterns =
{ "/detect" })
@SuppressWarnings("serial")
public class DetectServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        detect(out,"org.eclipse.jetty.server.Server");
        detect(out,"/org/eclipse/jetty/server/Server.class");
        detect(out,"org.eclipse.jetty.server.Request");
        detect(out,"/org/eclipse/jetty/server/Request.class");
        detect(out,"org.mortbay.jetty.Server");
        detect(out,"/org/mortbay/jetty/Server.class");
        
        detectDefaultServlet(out, req, "org.eclipse.jetty");
        detectDefaultServlet(out, req, "org.apache.catalina");
        
        String version = getReflectedMethodValue("org.eclipse.jetty.server.Server", "getVersion");
        out.printf("Server.getVersion() = %s%n", version);
    }

    private void detectDefaultServlet(PrintWriter out, HttpServletRequest req, String keyword)
    {
        // Request the default servlet (its pretty safe to say it will always be there)
        RequestDispatcher dispatcher = req.getServletContext().getNamedDispatcher("default");
        if(dispatcher == null) {
            out.printf("detectDefaultServlet(out, req, \"%s\") = <no default servlet>%n", keyword);
            return;
        }
        
        // If the request dispatcher implementation contains the keyword, we can claim a match
        boolean detected = dispatcher.getClass().getName().contains(keyword);
        out.printf("detectDefaultServlet(out, req, \"%s\") = %b (%s)%n", keyword, detected, dispatcher.getClass().getName());
    }

    private String getReflectedMethodValue(String clazzName, String methodName)
    {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        try
        {
            Class<?> clazz = Class.forName(clazzName,false,loader);
            Class<?> parameterTypes[] = new Class<?>[0];
            Method method = clazz.getDeclaredMethod(methodName,parameterTypes);
            Object args[] = new Object[0];
            return (String)method.invoke(clazz,args);
        }
        catch (ClassNotFoundException e)
        {
            return "<class-not-found>";
        }
        catch (Throwable t)
        {
            return "<" + t.getClass().getName() + ": " + t.getMessage() + ">";
        }
    }

    private void detect(PrintWriter out, String resource)
    {
        out.printf("detect(\"%s\") = %b%n",resource,detected(resource));
    }

    private boolean detected(String clazz)
    {
        try
        {
            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

            systemClassLoader.loadClass(clazz);

            return true;
        }
        catch (ClassNotFoundException cnfe)
        {
            Class<?> classObj = getClass();

            if (classObj.getResource(clazz) != null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
