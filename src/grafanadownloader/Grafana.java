package grafanadownloader;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author @suhoy Sergey Sukhorukov
 */
public class Grafana {

    public Props conf;
    public String start;
    public String finish;

    public Grafana(Props conf, String start, String finish) {
        this.conf = conf;
        this.start = Utils.DateIntoTimestamp(start, conf.params.get("java.timeMask"));
        this.finish = Utils.DateIntoTimestamp(finish, conf.params.get("java.timeMask"));
    }

    public void DownloadAll() {
        System.out.println("\r\nDownloading: ");
        String host = conf.params.get("grafana.host");
        String port = conf.params.get("grafana.port");
        String protocol = conf.params.get("grafana.protocol");
        String height = conf.params.get("panels.height");
        String width = conf.params.get("panels.width");
        String urls;
        HttpURLConnection conn;

        File file = new File(System.getProperty("user.dir") + "/" + conf.outFolder);
        file.mkdir();

        for (int i = 1; i < conf.metrics.size() + 1; i++) {
            try {
                List<String> vars = new ArrayList<>();
                int j = 1;
                while (true) {
                    String var = conf.metrics.get(i - 1).get("var" + j);

                    if (var == null) {
                        break;
                    }
                    vars.add(var);
                    j++;
                }
                urls = Utils.URLBuilder(
                        conf.metrics.get(i - 1).get("dashboardName"),
                        conf.metrics.get(i - 1).get("dashID"),
                        conf.metrics.get(i - 1).get("panelId"),
                        conf.metrics.get(i - 1).get("orgID"),
                        start,
                        finish,
                        host,
                        port,
                        protocol,
                        height,
                        width,
                        vars);

                System.out.println("Link" + i + "=" + urls);
                URL url = new URL(urls);

                
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(1_000 * 60 * 5);
                conn.setConnectTimeout(1_000 * 60 * 5);
                conn.setRequestMethod("GET");
                

                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + conf.params.get("grafana.token"));

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedImage image = ImageIO.read(conn.getInputStream());
                String outImage = System.getProperty("user.dir") + "/" + conf.outFolder + "/" + conf.metrics.get(i - 1).get("output");
                //Also extension for image : conf.metrics.get(i - 1).get("output").split("\\.")[1]
                ImageIO.write(image, "png", new File(outImage));
                System.out.println("Succesfull for: " + outImage);
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
