package logic.Users;

import ocsf.server.ConnectionToClient;

public class ClientUser {
    private String ipAddress, hostName, status;
    private String username;
    private ConnectionToClient connection;

    public ClientUser(String ipAddress, String hostName, String status, ConnectionToClient connection) {
        this.ipAddress = ipAddress;
        this.hostName = hostName;
        this.status = status;
        this.connection = connection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public String getStatus() {
        return status;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ConnectionToClient getConnection() {
        return connection;
    }

    public void setConnection(ConnectionToClient connection) {
        this.connection = connection;
    }

    public boolean isEqual(ClientUser other) {
        return this.ipAddress.equals(other.ipAddress) && this.hostName.equals(other.hostName);
    }

    @Override
    public String toString() {
        return "Client [ipAddress=" + ipAddress + ", hostName=" + hostName + ", status=" + status + ", username=" + username + "]\n";
    }
}
