<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Облачная обработка изображений</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    </head>

    <body>
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

      <table width="80%" heigth="80%" border="0" align="center">
         <tr>
            <td>
                <nav class="navbar navbar-expand-lg navbar-light bg-light">
                  <a class="navbar-brand" href="#">Navbar</a>
                  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                  </button>

                  <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav mr-auto">
                      <li class="nav-item active">
                        <a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
                      </li>
                      <li class="nav-item">
                        <a class="nav-link" href="#">Link</a>
                      </li>
                      <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                          Dropdown
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                          <a class="dropdown-item" href="#">Action</a>
                          <a class="dropdown-item" href="#">Another action</a>
                          <div class="dropdown-divider"></div>
                          <a class="dropdown-item" href="#">Something else here</a>
                        </div>
                      </li>
                    </ul>
                  </div>
                </nav>
            </td>
         </tr>

         <tr valign = "top">
            <td bgcolor = "#eee">
                   <form action = "imageProcess" method = "post" enctype = "multipart/form-data">
                     <div class="form-group">
                       <label for="exampleFormControlFile1">Select a file to upload:</label>
                       <input type="file" class="form-control-file" name="file" id="formFileUpload" accept=".jpg, *.jpeg">
                       <input type = "submit" value = "Upload File" />
                     </div>
                   </form>
                   <br />

                   <%
                              String msg = (String)request.getAttribute("message");
                              String filesize = (String)request.getAttribute("filesize");
                              if (msg != null && msg.length() > 0) {

                                out.println("<div class=\"toast\" role=\"alert\" aria-live=\"assertive\" aria-atomic=\"true\" data-delay=\"5000\" id=\"uploadMsg\">");
                                out.println("  <div class=\"toast-header\">");
                                out.println("    <img class=\"rounded mr-2\">");
                                out.println("    <strong class=\"mr-auto\">Response</strong>");
                                out.println("    <small class=\"text-muted\">"+filesize+"</small>");
                                out.println("    <button type=\"button\" class=\"ml-2 mb-1 close\" data-dismiss=\"toast\" aria-label=\"Close\">");
                                out.println("      <span aria-hidden=\"true\">&times;</span>");
                                out.println("    </button>");
                                out.println("  </div>");
                                out.println("  <div class=\"toast-body\">");
                                out.println(msg);
                                out.println("  </div>");
                                out.println("</div>");

                                out.println("<script>");
                                out.println("     $(document).ready(function(){ $(\"#uploadMsg\").toast('show'); }); ");
                                out.println("</script>");
                              }
                           %>
            </td>
         </tr>

         <tr>
            <td colspan = "2" bgcolor = "#b5dcb3">
               <center>
                  Copyright © 2020 project.edu.kz
               </center>
            </td>
         </tr>
      </table>

    </body>
</html>