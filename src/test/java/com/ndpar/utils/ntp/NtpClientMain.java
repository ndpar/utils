package com.ndpar.utils.ntp;

public class NtpClientMain {

    public static void main(String[] args) {
        int clientPort = Integer.parseInt(System.getProperty("ntp.client.port"));
        NtpClient client = new NtpClient(clientPort, 20000);

        String hostName = System.getProperty("ntp.server.hostname");
        client.setServerHost(hostName);

        System.out.println("Time offset between localhost and " + hostName + ": " + client.calculateTimeOffset());
        client.destroy();
    }
}
