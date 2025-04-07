package example.day04.controller;


import example.day04.model.dto.TodoDto;
import example.day04.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/day04/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    // 1. 등록
    @PostMapping // http://localhost:8080/day04/todos
    // {"title" : "회의","content" : "강의관련회의진행","done" : "false"}
    public TodoDto todoSave(@RequestBody TodoDto todoDto) {
        return todoService.todoSave(todoDto);
    } // end todoSave

    // 2. 전체조회
    @GetMapping // http://localhost:8080/day04/todos
    public List<TodoDto> todoFindAll() {
        return todoService.todoFindAll();
    } // end todoFindAll

    // 3. 개별 조회
    @GetMapping("view") // http://localhost:8080/day04/todos/view?id=1
    public TodoDto todoFindById(@RequestParam int id) {
        return todoService.todoFindById(id);
    } // end todoFindById

    // 4. 개별 수정
    @PutMapping // http://localhost:8080/day04/todos
    // {"id" : 1,"title" : "수정-회의","content" : "수정-강의관련회의진행","done" : "false"}
    public TodoDto todoUpdate(@RequestBody TodoDto todoDto) {
        return todoService.todoSave(todoDto);
    } // end todoUpdate

    // 5. 개별 삭제
    @DeleteMapping // http://localhost:8080/day04/todos?id=1
    public boolean todoDelete(@RequestParam int id) {
        return todoService.todoDelete(id);
    } // end todoDelete

    // 6. 전체 조회( + 페이징처리 )
    @GetMapping("/page") // http://localhost:8080/day04/todos/page?page=1&size=3
        // @RequestParam(defaultValue = "초기값") : 만약에 매개변수값이 존재하지 않으면 초기값 대입
    public List<TodoDto> todoFindByPage(
            @RequestParam int page, // page : 현재 조회할 페이지 번호 , 초기값 = 1
            @RequestParam int size) { //size : 현재 조회할 페이지당 자료 개수 , 초기값 = 3
        return todoService.todoFindByPage(page, size);
    } // end todoFindByPage

} // end class
