package pl.isa.hadoop;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class HostnameInterceptor implements Interceptor {
    Logger logger = Logger.getLogger(HostnameInterceptor.class);

    @Override
    public void initialize() {
        logger.info("Starting hostname interceptor");
    }

    @Override
    public Event intercept(Event event) {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            event.getHeaders().put("hostname", hostname);
        } catch (UnknownHostException e) {
            logger.error("could not set hostname", e);
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
        logger.info("Closing hostname interceptor");
    }

    public static class Builder implements Interceptor.Builder {
        @Override
        public Interceptor build() {
            return new HostnameInterceptor();
        }

        @Override
        public void configure(Context context) {
            
        }
    }
}
