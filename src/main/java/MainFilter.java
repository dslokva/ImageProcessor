import javax.servlet.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFilter implements Filter {

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        SettingsStore settings = SettingsStore.getInstance();
        req.setAttribute("blurRatio", settings.getBlurRatio());
        req.setAttribute("compressRatio", settings.getCompressionRatio());
        req.setAttribute("blurEnabled", settings.getBlurEnabled());
        req.setAttribute("compressEnabled", settings.getCompressEnabled());
        req.setAttribute("lightUpEnabled", settings.getLightUpEnabled());
        req.setAttribute("histogramUpEnabled", settings.getHistogramUpEnabled());

        String absoluteDiskPath = req.getServletContext().getRealPath("/output/");

        settings.updateGalleryList(absoluteDiskPath);
        req.setAttribute("galleryList", settings.getGalleryList());

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
