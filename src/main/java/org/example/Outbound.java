package org.example;

import java.util.List;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Outbound {
    @Expose
    private String server;

    @Expose
    private String detour;

    private String peer_public_key;

    @Expose
    private List<Long> reserved;

    private String fake_packets;

    private List<String> local_address;

    private String private_key;

    private Long server_port;

    @Expose
    private String tag;

    @Expose
    private String type;

    @Expose
    private int mtu;

}