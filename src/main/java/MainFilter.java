import javax.servlet.*;
import java.io.IOException;

public class MainFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        SettingsStore settings = SettingsStore.getInstance();
        req.setAttribute("blurRatio", settings.getBlurRatio());
        req.setAttribute("compressRatio", settings.getCompressionRatio());

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
