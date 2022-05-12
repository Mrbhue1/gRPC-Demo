package com.grpc.user.entity;


import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@ToString
public class User {

    @Id
    private String login;
    private String name;
    private String genre;

}