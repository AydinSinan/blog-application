package com.microservice.userservice.dao.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBlogRequest {
    private String title;
    private String text;
    private String email;
}
