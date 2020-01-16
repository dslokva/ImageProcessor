<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Облачная обработка изображений</title>
</head>
    <body>

        <h3>File Upload:</h3>
              Select a file to upload: <br />
              <form action = "imageProcess" method = "post" enctype = "multipart/form-data">
                 <input type = "file" name = "file" size = "50" />
                 <br />
                 <input type = "submit" value = "Upload File" />
              </form>

        <%
           String msg = (String)request.getAttribute("message");
           if (msg != null && msg.length() > 0) {
             out.println(msg);
           }
        %>

    </body>
</html>