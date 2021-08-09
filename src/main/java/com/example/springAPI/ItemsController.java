package com.example.springAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ItemsController {

    private final ItemsService itemsService;

    @PostMapping("/items")
    @ResponseBody
    public Long save(@RequestBody ItemsSaveDto itemsSaveDto) {
        return itemsService.save(itemsSaveDto);
    }

//  ResponesBody 는 view resolver 대신 httpMessageConverter가 실행된다.
//  문자열 같은 경우는  StringConverter가 동작하고, json 형식일 경우 MappingJackson2HttpMessageConverter에 의해 response body에 응답을 보낸다.
    @GetMapping("/items/{id}")
    @ResponseBody
    public ItemsResponseDto findById(@PathVariable Long id) {
        return itemsService.findById(id);
    }
}
