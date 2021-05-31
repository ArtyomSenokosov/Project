package ru.mail.senokosov.artem.service.model.show;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShowArticleDTO {

    private Long id;
    private String date;
    private String title;
    private String firstName;
    private String lastName;
    private String shortContent;
    private String fullContent;
    private List<ShowCommentDTO> comments = new ArrayList<>();
}
