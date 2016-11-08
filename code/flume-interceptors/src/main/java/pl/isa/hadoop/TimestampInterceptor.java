package pl.isa.hadoop;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by trener on 08.11.16.
 */
public class TimestampInterceptor implements Interceptor {
    Logger logger = Logger.getLogger(TimestampInterceptor.class);

    @Override
    public void initialize() {
        logger.info("Starting timestamp interceptor");
    }

    @Override
    public Event intercept(Event event) {
        //119.68.172.239|-|-|2016-11-06 12:36:29|GET /explore HTTP/1.0|200|4950|http://www.roberts.biz/privacy.php|Mozilla/5.0 (X11; Linux x86_64; rv:1.9.7.20) Gecko/2014-01-29 18:29:41 Firefox/3.8
        String body = new String(event.getBody());
        String[] parts = body.split("\\|");
        String timeString = parts[3];

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long timestamp = sdf.parse(timeString).getTime();
            event.getHeaders().put("timestamp", String.valueOf(timestamp));
        } catch (ParseException e) {
            logger.error("error parsing date: " + body);
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        for(Event e : list) {
            intercept(e);
        }
        return list;
    }

    @Override
    public void close() {

    }

    public static class Builder implements Interceptor.Builder {

        @Override
        public Interceptor build() {
            return new TimestampInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
