<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Облачная обработка изображений</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <%String pathWebcontent = request.getContextPath();%>

    <link rel="stylesheet" href="<%=pathWebcontent%>/resources/bootstrap.min.css">
    <link rel="stylesheet" href="<%=pathWebcontent%>/resources/bootstrap-slider.css">
    <link rel="stylesheet" href="<%=pathWebcontent%>/resources/other.css">
    <link rel="stylesheet" href="<%=pathWebcontent%>/resources/fileinput.min.css" media="all" type="text/css"/>
    <link rel="stylesheet" href="<%=pathWebcontent%>/resources/fa-all.css">

    <script src="<%=pathWebcontent%>/resources/bootstrap-slider.js"></script>
    <script src="<%=pathWebcontent%>/resources/jquery-3.4.1.min.js"></script>
    <script src="<%=pathWebcontent%>/resources/popper.min.js"></script>
    <script src="<%=pathWebcontent%>/resources/bootstrap.min.js"></script>
    <script src="<%=pathWebcontent%>/resources/piexif.min.js" type="text/javascript"></script>
    <script src="<%=pathWebcontent%>/resources/sortable.min.js" type="text/javascript"></script>
    <script src="<%=pathWebcontent%>/resources/purify.min.js" type="text/javascript"></script>
    <script src="<%=pathWebcontent%>/resources/fileinput.min.js"></script>
    <script src="<%=pathWebcontent%>/resources/fileinput-ru.js"></script>
    <script src="<%=pathWebcontent%>/resources/theme.min.js"></script>

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
                                    <div aria-live="polite" aria-atomic="true"
                                         class="d-flex justify-content-center align-items-center">
                                        <%
                                            String msg = (String) request.getAttribute("message");
                                            String filesize = (String) request.getAttribute("filesize");
                                            if (msg != null && msg.length() > 0) {
                                                out.println("<div class=\"toast\" role=\"alert\" aria-live=\"assertive\" aria-atomic=\"true\" data-delay=\"8000\" id=\"uploadMsg\">");
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
                                            }
                                        %>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="tab-pane fade" id="v-pills-profile" role="tabpanel"
                             aria-labelledby="v-pills-profile-tab">
                            <div class="container">
                                <div class="row">
                                    <div class="col-12">
                                        <table class="table table-image">
                                            <thead>
                                            <tr>
                                                <th scope="col">Дата</th>
                                                <th scope="col">Изображение</th>
                                                <th scope="col">Оригинальный размер</th>
                                                <th scope="col">Сжатый размер</th>
                                            </tr>
                                            </thead>
                                            <tbody>

                                            <%
                                                HashMap<String, HashMap> galleryList = (HashMap<String, HashMap>) request.getAttribute("galleryList");
                                                if (galleryList != null) {
                                                    Iterator it = galleryList.entrySet().iterator();
                                                    while (it.hasNext()) {
                                                        Map.Entry pair = (Map.Entry) it.next();
                                                        String folderName = (String) pair.getKey();
                                                        HashMap<String, HashMap> detailsMap = (HashMap<String, HashMap>) pair.getValue();

                                                        Iterator it2 = detailsMap.entrySet().iterator();
                                                        while (it2.hasNext()) {
                                                            Map.Entry pair2 = (Map.Entry) it2.next();
                                                            String fileName = (String) pair2.getKey();
                                                            HashMap<String, String> fileDetails = (HashMap<String, String>) pair2.getValue();


                                                            if (fileName.endsWith("original.jpg")) {
                                                                out.println("<tr>");
                                                                out.println("<td>" +fileDetails.get("createDateTime")+ "</td>");

                                                                out.println("<td class=\"w-25\">");
                                                                out.println("<img src=\"" + fileDetails.get("imgLink") + "\" class=\"img-fluid img-thumbnail\" alt=\"Sheep\" data-toggle=\"modal\" data-target=\"#exampleModal\">");
                                                                out.println("</td>");

                                                                out.println("<td>"+fileDetails.get("compressedSize")+"</td>");
                                                                out.println("<td>"+fileDetails.get("size")+"</td>");
                                                                out.println("</tr>");
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    out.println("<tr><td> No items. </td></tr>");
                                                }
                                            %>


                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane fade" id="v-pills-settings" role="tabpanel"
                             aria-labelledby="v-pills-settings-tab">
                            <div class="container">
                                <form name="settingForm" action="<%=request.getContextPath()%>/settingsProcess"
                                      method="post">
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="custom-control custom-checkbox">
                                                <input type="checkbox" class="custom-control-input"
                                                       name="compressEnabled" id="chkCompressEnabled">
                                                <label class="custom-control-label" for="chkCompressEnabled">Сжимать
                                                    изображение</label>
                                            </div>
                                            <label id="label1" for="compressRatioSlider">Уровень компрессии:</label>
                                            <input id="compressRatioSlider" data-slider-id="crSlider" type="text"
                                                   data-slider-min="10" name="compressRatio"
                                                   data-slider-max="90" data-slider-step="1"
                                                   data-slider-value="30"/>
                                            <span class="badge badge-secondary"
                                                  id="compressRatioSliderVal">30</span>
                                        </div>
                                        <div class="col">
                                            <div class="custom-control custom-checkbox">
                                                <input type="checkbox" class="custom-control-input"
                                                       name="blurEnabled"
                                                       id="chkBlurEnabled">
                                                <label class="custom-control-label" for="chkBlurEnabled">Применять
                                                    размытие</label>
                                            </div>
                                            <label for="blurRatioSlider">Уровень размытия (blur)</label>
                                            <input id="blurRatioSlider" data-slider-id="blurSlider" type="text"
                                                   data-slider-min="2" name="blurRatio"
                                                   data-slider-max="5" data-slider-step="1" data-slider-value="3"/>
                                            <span class="badge badge-secondary" id="blurRatioSliderVal">3</span>
                                        </div>

                                        <div class="col">

                                        </div>
                                    </div>
                                    <br/>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="custom-control custom-checkbox">
                                                <input type="checkbox" class="custom-control-input"
                                                       name="lightUpEnabled"
                                                       id="chkLightUpEnabled">
                                                <label class="custom-control-label" for="chkLightUpEnabled">Выравнивание
                                                    яркости</label>
                                            </div>
                                        </div>

                                        <div class="col">
                                            <div class="custom-control custom-checkbox">
                                                <input type="checkbox" class="custom-control-input"
                                                       name="histogramUpEnabled"
                                                       id="chkHistogramUpEnabled">
                                                <label class="custom-control-label" for="chkHistogramUpEnabled">Засветка
                                                    изображения</label>
                                            </div>
                                        </div>

                                        <div class="col">

                                        </div>
                                    </div>

                                    <br/>
                                    <div class="form-row">
                                        <div class="col">
                                            <button type="submit" class="btn btn-success btn-sm">Сохранить
                                                настройки
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
        var compressSlider = new Slider("#compressRatioSlider", {
            tooltip_position: 'left'
        });

        var blurSlider = new Slider("#blurRatioSlider", {
            tooltip_position: 'left'
        });

        compressSlider.on("slide", function (sliderValue) {
            document.getElementById("compressRatioSliderVal").textContent = sliderValue;
        });

        blurSlider.on("slide", function (sliderValue) {
            document.getElementById("blurRatioSliderVal").textContent = sliderValue;
        });

        $("#compressRatioSliderVal").text(<%= request.getAttribute("compressRatio") %>);
        compressSlider.setValue(<%= request.getAttribute("compressRatio") %>);

        $("#blurRatioSliderVal").text(<%= request.getAttribute("blurRatio") %>);
        blurSlider.setValue(<%= request.getAttribute("blurRatio") %>);

        $('#chkCompressEnabled').prop('checked', '<%= request.getAttribute("compressEnabled") %>');
        $('#chkBlurEnabled').prop('checked', '<%= request.getAttribute("blurEnabled") %>');
        $('#chkHistogramUpEnabled').prop('checked', '<%= request.getAttribute("histogramUpEnabled") %>');
        $('#chkLightUpEnabled').prop('checked', '<%= request.getAttribute("lightUpEnabled") %>');

        console.log("ready!");
    });

    function chkCompressClick() {

    }

</script>

<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Результат обработки</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                ...
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">Закрыть</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Удалить</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>