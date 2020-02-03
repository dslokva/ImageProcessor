<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Облачная обработка изображений</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <%String pathWebcontent = request.getContextPath();%>

    <link rel="stylesheet" href="<%=pathWebcontent%>/resources/bootstrap.min.css">
    <link rel="stylesheet" href="<%=pathWebcontent%>/resources/bootstrap-slider.css">
    <link rel="stylesheet" href="<%=pathWebcontent%>/resources/other.css">

    <script src="<%=pathWebcontent%>/resources/bootstrap-slider.js"></script>
    <script src="<%=pathWebcontent%>/resources/jquery-3.4.1.min.js"></script>
    <script src="<%=pathWebcontent%>/resources/popper.min.js"></script>
    <script src="<%=pathWebcontent%>/resources/bootstrap.min.js"></script>

    <!-- bootstrap 4.x is supported. You can also use the bootstrap css 3.3.x versions -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/5.0.1/css/fileinput.min.css" media="all"
          rel="stylesheet" type="text/css"/>

    <!-- the font awesome icon library if using with `fas` theme (or Bootstrap 4.x). Note that default icons used in the plugin are glyphicons that are bundled only with Bootstrap 3.x. -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" crossorigin="anonymous">

    <!-- piexif.min.js is needed for auto orienting image files OR when restoring exif data in resized images and when you
        wish to resize images before upload. This must be loaded before fileinput.min.js -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/5.0.1/js/plugins/piexif.min.js"
            type="text/javascript"></script>
    <!-- sortable.min.js is only needed if you wish to sort / rearrange files in initial preview.
        This must be loaded before fileinput.min.js -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/5.0.1/js/plugins/sortable.min.js"
            type="text/javascript"></script>
    <!-- purify.min.js is only needed if you wish to purify HTML content in your preview for
        HTML files. This must be loaded before fileinput.min.js -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/5.0.1/js/plugins/purify.min.js"
            type="text/javascript"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/5.0.1/js/fileinput.min.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/5.0.1/js/locales/ru.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/5.0.1/themes/fas/theme.min.js"></script>


</head>

<body>
<meta charset="utf-8">

<script>
    $('#myTab a').on('click', function (e) {
        e.preventDefault()
        $(this).tab('show')
    });
</script>


<table width="80%" heigth="80%" border="0" align="center">
    <tr>
        <td colspan="2" bgcolor="#b5dcb3">
            <br/>
        </td>
    </tr>
    <tr valign="top">
        <td bgcolor="#eee">
            <div class="row">
                <div class="col-3">
                    <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
                        <a class="nav-link active" id="v-pills-home-tab" data-toggle="pill" href="#v-pills-home"
                           role="tab"
                           aria-controls="v-pills-home" aria-selected="true">Загрузка изображения</a>
                        <a class="nav-link" id="v-pills-profile-tab" data-toggle="pill" href="#v-pills-profile"
                           role="tab"
                           aria-controls="v-pills-profile" aria-selected="false">Галерея загрузок</a>
                        <a class="nav-link" id="v-pills-settings-tab" data-toggle="pill" href="#v-pills-settings"
                           role="tab"
                           aria-controls="v-pills-settings" aria-selected="false">Настройки обработки</a>
                    </div>
                </div>
                <div class="col-9">
                    <div class="tab-content" id="v-pills-tabContent">
                        <div class="tab-pane fade show active" id="v-pills-home" role="tabpanel"
                             aria-labelledby="v-pills-home-tab">
                            <div class="row">
                                <div class="col-10">
                                    <form name="imageForm" action="imageProcess" method="post"
                                          enctype="multipart/form-data">

                                        <div class="form-group">
                                            <input id="input-b3" name="input-b3[]" type="file" class="file" multiple
                                                   data-show-upload="false" data-show-caption="true" data-theme="fas"
                                                   data-language="ru"
                                                   data-msg-placeholder="Выберите файлы для обработки...">
                                            <br/>
                                            <div style="text-align: center;">
                                                <input class="btn btn-success" type="submit"
                                                       value="Загрузить файл (ы)"/>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-10">
                                    <br/>
                                    <%
                                        String msg = (String) request.getAttribute("message");
                                        String filesize = (String) request.getAttribute("filesize");
                                        if (msg != null && msg.length() > 0 && filesize != null && !filesize.equals("")) {
                                            out.println("<div style=\"text-align: center;\"><div class=\"toast\" role=\"alert\" aria-live=\"assertive\" aria-atomic=\"true\" data-delay=\"8000\" id=\"uploadMsg\">");
                                            out.println("  <div class=\"toast-header\">");
                                            out.println("    <img class=\"rounded mr-2\">");
                                            out.println("    <strong class=\"mr-auto\">Ответ от сервера</strong>");
                                            out.println("    <small class=\"text-muted\">" + filesize + "</small>");
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
                                            out.println("</div>");
                                        }
                                    %>
                                </div>
                            </div>
                        </div>

                        <div class="tab-pane fade" id="v-pills-profile" role="tabpanel"
                             aria-labelledby="v-pills-profile-tab">
                            ...в разработке
                        </div>
                        <div class="tab-pane fade" id="v-pills-settings" role="tabpanel"
                             aria-labelledby="v-pills-settings-tab">
                            <div class="container">
                                <form name="settingForm" action="<%=request.getContextPath()%>/settingsProcess"
                                      method="post">
                                    <div class="form-row">
                                        <div class="col">
                                            <label for="compressRatioSlider">Уровень компрессии (сжатие)</label>
                                            <input id="compressRatioSlider" data-slider-id="crSlider" type="text"
                                                   data-slider-min="10" name="compressRatio"
                                                   data-slider-max="90" data-slider-step="1" data-slider-value="30"/>
                                            <span class="badge badge-secondary" id="compressRatioSliderVal">30</span>
                                        </div>

                                        <div class="col">
                                            <label for="blurRatioSlider">Уровень размытия (blur)</label>
                                            <input id="blurRatioSlider" data-slider-id="blurSlider" type="text"
                                                   data-slider-min="10" name="blurRatio"
                                                   data-slider-max="90" data-slider-step="1" data-slider-value="30"/>
                                            <span class="badge badge-secondary" id="blurRatioSliderVal">30</span>
                                        </div>

                                        <div class="col">

                                        </div>
                                    </div>
                                    <br/>
                                    <div class="form-row">
                                        <div class="col">
                                            <button type="submit" class="btn btn-success btn-sm">Сохранить настройки
                                            </button>
                                        </div>
                                        <div class="col">

                                        </div>
                                        <div class="col">

                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </td>
    </tr>

    <tr>
        <td colspan="2" bgcolor="#b5dcb3">
            <div style="text-align: center;">
                Copyright © 2020 cloudproject.edu.kz
            </div>
        </td>
    </tr>
</table>

<script>
    $(document).ready(function () {
        console.log("ready!");

        var compressSlider = new Slider("#compressRatioSlider", {
            tooltip_position: 'left'
        });

        var blurSlider = new Slider("#blurRatioSlider", {
            tooltip_position: 'left'
        });

        compressSlider.on("slide", function (sliderValue) {
            document.getElementById("compressRatioSliderVal").textContent = sliderValue;
            // localStorage.setItem("compressRatioValue", sliderValue);
        });

        blurSlider.on("slide", function (sliderValue) {
            document.getElementById("blurRatioSliderVal").textContent = sliderValue;
            // localStorage.setItem("blurRatioValue", sliderValue);
        });

        // $("#compressRatioSliderVal").text(localStorage.getItem("compressRatioValue"));
        // compressSlider.setValue(localStorage.getItem("compressRatioValue"));
        //
        // $("#blurRatioSliderVal").text(localStorage.getItem("blurRatioValue"));
        // blurSlider.setValue(localStorage.getItem("blurRatioValue"));
    });
</script>
</body>
</html>