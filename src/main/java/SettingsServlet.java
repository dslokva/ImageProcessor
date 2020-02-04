import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class SettingsServlet extends HttpServlet {

    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        httpServletRequest.getRequestDispatcher("/index.jsp").forward(httpServletRequest, httpServletResponse);
    }

    public SettingsServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String compressParam = request.getParameter("compressRatio");
        String blurParam = request.getParameter("blurRatio");

        SettingsStore settings = SettingsStore.getInstance();
        if (blurParam != null)
            settings.setBlurRatio(Integer.valueOf(blurParam));
        if (compressParam != null)
            settings.setCompressionRatio(Integer.valueOf(compressParam));

        request.setAttribute("blurRatio", settings.getBlurRatio());
        request.setAttribute("compressRatio", settings.getCompressionRatio());
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

}