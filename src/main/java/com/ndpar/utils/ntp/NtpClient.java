package com.ndpar.utils.ntp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

/**
 * Simple client to calculate time offset between localhost and remote NTP
 * server. On the remote site NTP server has to be running, otherwise timeout
 * exception will be thrown.
 *
 * @author Andrey Paramonov
 * @author Isaac Levin
 */
public class NtpClient {

    protected final Log log = LogFactory.getLog(getClass());

    private NTPUDPClient ntpClient = new NTPUDPClient();
    private InetAddress serverHost;

    /**
     * Sets remote NTP server host name.
     *
     * @param serverHostName remote NTP server host name or IP
     */
    public void setServerHost(String serverHostname) {
        try {
            serverHost = InetAddress.getByName(serverHostname);

        } catch (UnknownHostException e) {
            throw new TimeClientException(e);
        }
    }

    /**
     * Creates local NTP client and opens connection to remote NTP server.
     *
     * @param clientNtpPort any available port on localhost
     * @param defaultTimeout default timeout in milliseconds
     */
    public NtpClient(int clientNtpPort, int defaultTimeout) {
        try {
            ntpClient.setDefaultTimeout(defaultTimeout);
            ntpClient.open(clientNtpPort);
            log.debug("NTP client created: " + ntpClient);

        } catch (SocketException e) {
            throw new TimeClientException(e);
        }
    }

    /**
     * Closes connection to remote NTP server.
     */
    public void destroy() {
        ntpClient.close();
    }

    /**
     * Returns time offset between local and remote host.
     */
    public Long calculateTimeOffset() {
        try {
            TimeInfo info = ntpClient.getTime(serverHost);
            info.computeDetails();
            return info.getOffset();

        } catch (IOException e) {
            throw new TimeClientException(e);
        }
    }
}
