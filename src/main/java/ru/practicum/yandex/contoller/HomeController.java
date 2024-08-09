package ru.practicum.yandex.contoller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {
    @GetMapping
    public String home() {
        return "Приветствуем вас, в приложении Котограм";
    }
}
