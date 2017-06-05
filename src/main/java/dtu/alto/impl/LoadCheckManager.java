package dtu.alto.impl;

import dtu.alto.linkload.PortStats;
import dtu.alto.core.ALTOService;
import dtu.alto.core.LoadCheckService;
import org.apache.felix.scr.annotations.*;
import org.onlab.packet.IpAddress;
import org.onlab.util.SafeRecurringTask;
import org.onosproject.incubator.net.PortStatisticsService;
import org.onosproject.net.*;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.device.PortStatistics;
import org.onosproject.net.host.HostService;
import org.onosproject.net.link.LinkService;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by s150924 on 6/5/17.
 */

@Component(immediate = true)
@Service
public class LoadCheckManager implements LoadCheckService {

    private final Logger log = getLogger(getClass());

    public static final int STATS_CHECK_INTERVAL = 10 * 1000; // seconds

    private final long statsInterval = STATS_CHECK_INTERVAL;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected HostService hostService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected DeviceService deviceService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected LinkService linkService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected PortStatisticsService portStatisticsService;


    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected ALTOService altoService;


    HashMap<IpAddress, PortStats> serverStatistics;

    private final ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(1);


    @Activate
    public void activate() {

        log.info("Started ALTO Service");

        this.serverStatistics = new HashMap<>();

        scheduledExecutorService.scheduleAtFixedRate(
                SafeRecurringTask.wrap(
                        () -> getStats()),
                0, statsInterval, TimeUnit.MILLISECONDS);
    }

    @Deactivate
    public void deactivate() {

        log.info("Stopped Load Checker Service");
    }

    @Override
    public HashMap<IpAddress, PortStats> getLoadReport() {

        return serverStatistics;
    }


    public void getStats() {

        PortStats serverStats;
        long latestTimestamp;
        List<IpAddress> cdnServers = altoService.getContentServers();

        for (IpAddress ip : cdnServers) {

            if(!serverStatistics.containsKey(ip)) {
                serverStats = new PortStats();
                serverStatistics.put(ip, serverStats);
            }

            serverStats = serverStatistics.get(ip);

            Set<Host> possibleHosts = hostService.getHostsByIp(ip);
            Host host = possibleHosts.iterator().next();
            DeviceId attachedDeviceId = host.location().deviceId();
            PortNumber attachedDevicePort = host.location().port();

            PortStatistics latestStats = deviceService.getStatisticsForPort(attachedDeviceId, attachedDevicePort);

            if(latestStats == null)
                return;

            latestTimestamp = System.currentTimeMillis() % 1000;
            long recBytes = latestStats.bytesReceived();
            int lastNumSamples = serverStats.getNumSamples();

            if(serverStats.getNumSamples() == 0){
                serverStats.setFirstSample(recBytes);
                serverStats.setLastSample(recBytes);
                serverStats.setFirstTimestamp(latestTimestamp);
                serverStats.setLastTimestamp(latestTimestamp);
                serverStats.setNumSamples(lastNumSamples+1);
                return;
            }

            if(serverStats.getNumSamples() > 0){
                long timeDiff = latestTimestamp - serverStats.getLastTimestamp();
                long byteDiff = recBytes - serverStats.getLastSample();
                float latestRate = (byteDiff / timeDiff) / 1000;

                serverStats.setRate(latestRate);

                timeDiff = latestTimestamp - serverStats.getFirstTimestamp();
                byteDiff = recBytes - serverStats.getFirstSample();
                float newAvg = (byteDiff / timeDiff) / 1000;
                //float newAvg = ((lastNumSamples * avgR) + latestRate) / (lastNumSamples + 1);

                serverStats.setAvgRate(newAvg);
                serverStats.setLastSample(recBytes);
                serverStats.setLastTimestamp(latestTimestamp);
                serverStats.setNumSamples(serverStats.getNumSamples()+1);
            }
        }
    }
}
