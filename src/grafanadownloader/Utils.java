package grafanadownloader;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Sergey Sukhorukov
 */
public class Utils {

    /***
     * 
     * @param date for example "2020-04-22T21:00"
     * @param format for example "yyyy-MM-dd'T'HH:mm"
     * @return 
     */
    public static String DateIntoTimestamp(String date, String format) {
        Calendar cal = GregorianCalendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            cal.setTime(sdf.parse(date));
            Timestamp tstamp = new Timestamp(cal.getTimeInMillis());
            System.out.println("DateIntoTimestamp:" + tstamp + " = " + cal.getTimeInMillis());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return String.valueOf(cal.getTimeInMillis());
    }

    /***
     * Build link to rendered image 
     * Mask: "@protocol://@host:@port/render/d-solo/@dashID/@dashboardName?orgId=@orgID&panelId=@panelId&from=@timeFrom&to=@timeTo&width=@width&height=@height"
     * Result: "http://localhost:3000/render/d-solo/9b8LgEJZz/local-util?orgId=1&panelId=2&from=1587534812088&to=1587545906762&width=1000&height=500"
     */
    public static String URLBuilder(String dashboardName, String dashID, String panelId, String orgID, String timeFrom, String timeTo, String host, String port, String protocol,  String height,String width) {
        return ""+protocol+"://" + host + ":" + port + "/render/d-solo/" + dashID + "/" + dashboardName + "?orgId=" + orgID + "&panelId=" + panelId + "&from=" + timeFrom + "&to=" + timeTo + "&width="+width+"&height="+height+"";
    }
}
