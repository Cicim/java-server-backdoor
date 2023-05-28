package com.example;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class MyServlet extends HttpServlet {
    PrintWriter out;

    private void startHtml() {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Terminal</title>");
        out.println("<link rel=\"stylesheet\" href=\"style.css\" />");
        out.println("</head>");
        out.println("<body>");
    }
    private void endHtml() {
        out.println("</body>");
        out.println("</html>");
    }

    private void printForm(String command) {
        command = command.replace("'", "&#39;");

        out.println("<form method=\"GET\">");
        out.println("<input spellcheck=\"false\" type=\"text\" name=\"cmd\" value='" + command + "' />");
        out.println("<input type=\"submit\" value=\"Send\" />");
        out.println("<a href=\".\" id=\"home\">Back to Home</a>");
        out.println("</form>");
    }

    private String[] splitTerminalArgs(String raw) {
        // Split a string of arguments in array, taking into account
        // the quoted values stuff
        // Example: "a b" c "d e" -> [a b, c, d e]
        
        // Split by spaces
        String[] args = raw.split(" ");
        
        // Join the quoted values
        java.util.ArrayList<String> newArgs = new java.util.ArrayList<String>();
        String current = "";
        boolean inQuotes = false;

        for (String arg : args) {
            if (arg.startsWith("\"")) {
                inQuotes = true;
                arg = arg.substring(1);
            }

            if (arg.endsWith("\"")) {
                inQuotes = false;
                arg = arg.substring(0, arg.length() - 1);
            }

            if (inQuotes) {
                current += arg + " ";
            } else {
                if (!current.isEmpty()) {
                    current += arg;
                    newArgs.add(current);
                    current = "";
                } else {
                    newArgs.add(arg);
                }
            }
        }

        return newArgs.toArray(new String[0]);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        out = response.getWriter();

        startHtml();
        // Get the command from the request
        String command = request.getParameter("cmd");
        if (command == null || command.isEmpty()) {
            printForm("");
            out.println("<h1 class='error'>Enter a command</h1>");
            endHtml();
            return;
        }

        printForm(command);

        // Split the command by spaces
        String[] cmd = splitTerminalArgs(command);
        // Run the command
        Process p = null;
        
        try {
            p = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            out.println("<h1 class='error'>The program could not be executed</h1>");
            out.println("<pre class='preerror'>" + e.getMessage() + "</pre>");
            endHtml();
            return;
        }

        // Wait for 1 second for the command to finish
        for (int i = 0; i < 1000; i++) {
            try {
                int status = p.exitValue();

                // Save the output to a string
                java.io.InputStream is = p.getInputStream();
                java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";

                // Print the result
                if (status == 0)
                    out.println("<h1 class='success'>The program exited successfully</h1>");
                else
                    out.println("<h1 class='error'>The program exited with status code " + status + "</h1>");

                if (result.isEmpty())
                    out.println("<h1 class='nothing'>The program did not output anything to stdout</h1>");
                else
                    out.println("<pre>" + result + "</pre>");

                // Save the error to a string
                is = p.getErrorStream();
                s = new java.util.Scanner(is).useDelimiter("\\A");
                String error = s.hasNext() ? s.next() : "";

                if (!error.isEmpty())
                    out.println("<h1 class='stderr'>The program printed to stderr</h1><pre>" + error + "</pre>");
                
                endHtml();
                return;
            } catch (IllegalThreadStateException e) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }

        // If the command didn't finish in 1 second, kill it
        out.println("<h1 class='hang'>The program did not finish within 1 second</h1>");
        endHtml();
    }
}
