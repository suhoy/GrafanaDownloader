package grafanadownloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sergey Sukhorukov Works with Grafana v6.4.4 on Java 8
 */
public class GrafanaDownloader {

    /**
     * *
     * List for CLI
     */
    final static Map<String, List<String>> args = new HashMap<>();

    /**
     * *
     *
     * @param arg Args example "-start 2020-02-20T20:00 -finish 2020-02-20T23:00 -config C:\Users\Sergey\config.txt -out report" 
     * Where: 
     * -start : Start time with mask set in config 
     * -finish : End time with mask set in config
     * -config : Full path to config file mask set in config 
     * -out : Subfolder name for downloaded images
     */
    public static void main(String[] arg) {
        try {
            ReadParams(arg);
            Props p = new Props(args.get("config").get(0), args.get("out").get(0));
            Grafana g = new Grafana(p, args.get("start").get(0), args.get("finish").get(0));
            g.DownloadAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }

    /**
     * *
     *
     * @param args CLI into List
     */
    public static void ReadParams(String[] arg) {
        List<String> options = null;
        for (int i = 0; i < arg.length; i++) {
            final String a = arg[i];

            if (a.charAt(0) == '-') {
                if (a.length() < 2) {
                    System.err.println("Error at argument " + a);
                    return;
                }

                options = new ArrayList<>();
                args.put(a.substring(1), options);
            } else if (options != null) {
                options.add(a);
            } else {
                System.err.println("Illegal parameter usage");
                return;
            }
        }
        System.out.println("Input:");
        System.out.println("Start=" + args.get("start").get(0));
        System.out.println("Finish=" + args.get("finish").get(0));
        System.out.println("Config=" + args.get("config").get(0));
    }
}
