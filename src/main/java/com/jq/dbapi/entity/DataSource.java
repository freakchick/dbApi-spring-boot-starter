package com.jq.dbapi.entity;

import lombok.Data;

@Data
public class DataSource {

    String id;
    String url;
    String username;
    String password;
    String type;

}
