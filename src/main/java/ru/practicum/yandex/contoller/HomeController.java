package ru.practicum.yandex.contoller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/home")
public class HomeController {
    @GetMapping
    public String home() {
        return "<h1>Приветствуем вас, в приложении Котограм<h1>";
    }
}
