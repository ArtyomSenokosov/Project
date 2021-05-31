package ru.mail.senokosov.artem.service.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDTO<T> {
    private List<T> objects = new ArrayList<>();
    private List<Integer> numbersOfPage = new ArrayList<>();
}
