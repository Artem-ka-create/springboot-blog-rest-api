package com.springboot.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
public class CommentDto {

    private long id;

    @NotEmpty(message = "name should not be null or empty")
    private String name;

    @NotEmpty(message = "Email should not be null or empty")
    @Email
    private String email;

    @NotEmpty
    @Size(min = 10, message = "body has to by more than 10 characters")
    private String body;
}
