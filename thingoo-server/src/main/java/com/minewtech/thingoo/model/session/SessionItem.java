package com.minewtech.thingoo.model.session;

import lombok.Data;

import java.util.List;

@Data
public class SessionItem {
    private String  token;
    private String  userId;
    private String  name;
    private String  email;
    private String  role;
}
