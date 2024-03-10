//
//package org.example;
//
//import java.util.List;
//import javax.annotation.Generated;
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//@Generated("net.hexar.json2pojo")
//@SuppressWarnings("unused")
//public class PrivateKey {
//
//    @Expose
//    private String commit;
//    @SerializedName("local_address")
//    private List<String> localAddress;
//    @Expose
//    private Long mtu;
//    @SerializedName("peer_public_key")
//    private String peerPublicKey;
//    @SerializedName("private_key")
//    private String privateKey;
//    @Expose
//    private List<Long> reserved;
//    @Expose
//    private String server;
//    @SerializedName("server_port")
//    private Long serverPort;
//    @Expose
//    private String type;
//
//    public String getCommit() {
//        return commit;
//    }
//
//    public void setCommit(String commit) {
//        this.commit = commit;
//    }
//
//    public List<String> getLocalAddress() {
//        return localAddress;
//    }
//
//    public void setLocalAddress(List<String> localAddress) {
//        this.localAddress = localAddress;
//    }
//
//    public Long getMtu() {
//        return mtu;
//    }
//
//    public void setMtu(Long mtu) {
//        this.mtu = mtu;
//    }
//
//    public String getPeerPublicKey() {
//        return peerPublicKey;
//    }
//
//    public void setPeerPublicKey(String peerPublicKey) {
//        this.peerPublicKey = peerPublicKey;
//    }
//
//    public String getPrivateKey() {
//        return privateKey;
//    }
//
//    public void setPrivateKey(String privateKey) {
//        this.privateKey = privateKey;
//    }
//
//    public List<Long> getReserved() {
//        return reserved;
//    }
//
//    public void setReserved(List<Long> reserved) {
//        this.reserved = reserved;
//    }
//
//    public String getServer() {
//        return server;
//    }
//
//    public void setServer(String server) {
//        this.server = server;
//    }
//
//    public Long getServerPort() {
//        return serverPort;
//    }
//
//    public void setServerPort(Long serverPort) {
//        this.serverPort = serverPort;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//}
