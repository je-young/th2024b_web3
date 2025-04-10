package todo.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import todo.model.dto.TodoDto;
import todo.model.entity.TodoEntity;
import todo.repository.TodoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    // 1. 등록
    public TodoDto todoSave(TodoDto todoDto) {
        // (1) dto 를 entity 변환하기
        TodoEntity todoEntity = todoDto.toEntity();
        // (2) entity 를 save(영속화/db레코드 매칭/등록) 한다.
        TodoEntity saveEntity = todoRepository.save(todoEntity);
        // (3) save 로 부터 반환된 엔티티(영속화)된 결과가 존재하면
        if (saveEntity.getId() > 1) {
            return saveEntity.toDto(); // entity 를 dto 로 변환하여 반환
        }else { // 결과가 존재하지 않으면
            return null; // null 반환
        } // end if
    } // end todoSave

    // 2. 전체조회
    public List<TodoDto> todoFindAll() {

        // [방법1]. 일반 반복문 ============================================================================= //
        // (1) 모든 entity 조회 , findAll()
        List<TodoEntity> todoEntityList = todoRepository.findAll();
        // (2) 모든 entity 리스트 를 dto 리스트 반환한다.
        List<TodoDto> todoDtoList = new ArrayList<>(); // (2-1) : dto 리스트 생성한다.
        for (int index = 0; index < todoEntityList.size(); index++) { // (2-2) : entity 리스트를 순회
            TodoDto todoDto = todoEntityList.get(index).toDto(); // (2-3) : index번째 entity를 dto로 변환
            todoDtoList.add(todoDto); // (2-4) : dto 리스트에 추가
        } // end for
        return todoDtoList;

        // [방법2]. 스트림(stream)방법 ============================================================================= //
        // return todoRepository.findAll().stream().map(TodoEntity::toDto).collect(Collectors.toList());
    } // end todoFindAll

    // 3. 개별조회
    public TodoDto todoFindById(int id) {
        // [방법1]. 일반적인 ============================================================================= //
        // (1) pk(식별번호) 이용한 entity 조회하기 , .findById()
        // Optional 클래스 : null 제어하는 메소드들을 제공하는 클래스
        Optional<TodoEntity> optional = todoRepository.findById(id);
        // (2) 조회한 결과가 존재하면 , .isPresent()
        if (optional.isPresent()) {
            // (3) Optional 객체에서 (영속된)엔티티 꺼내기
            TodoEntity todoEntity = optional.get();
            // (4) dto로 변환
            TodoDto todoDto = todoEntity.toDto();
            // (5) dto 반환
            return todoDto;
        } // end if
        return null;
        // [방법2]. 스트림(stream)방법 ============================================================================= //
        // return todoRepository.findById(id).map(TodoEntity::toDto).orElse(null);

        // .map(TodoEntity::toDto) : Optional 의 데이터가 null 이 아니면 map 실행하여 dto 로 변환후 반환
        // .orElse(null) : Optional 의 데이터가 null 이면 null 반환
    } // end todofindById

    // 4. 개별 수정 + @Transactional
    public TodoDto todoUpdate(int id, TodoDto todoDto) {
        // [방법1]. 일반적인 ============================================================================= //
        // (1) 수정할 id 로 엔티티를 조회한다.
        Optional<TodoEntity> optional = todoRepository.findById(todoDto.getId());
        // (2) 존재하면 수정하고 존재하지 않으면 null 반환 , .isPresent()
        if (optional.isPresent()) {
            // (3) 엔티티 꺼내기
            TodoEntity todoEntity = optional.get();
            // (4) 엔티티 수정하기 , 입력받은 dto 값을 엔티티에 대입하기
            todoEntity.setTitle(todoDto.getTitle());
            todoEntity.setContent(todoDto.getContent());
            todoEntity.setDone(todoDto.isDone()); // boolean 의 getter를 사용
            return todoEntity.toDto(); // dto로 변환후 반환
        } // end if
        return null;
        // [방법2]. 스트림(stream)방법 ============================================================================= //
//        return todoRepository.findById(todoDto.getId()) Optional<TodoEntity>
//                // findById()로 조회한 결과의 optional 객체가 존재하면
//                .map(todoEntity -> {
//                    todoEntity.setTitle(todoDto.getTitle());
//                    todoEntity.setContent(todoDto.getContent());
//                    todoEntity.setDone(todoDto.isDone());
//                    return todoEntity.toDto();
//                }) Optional<TodoDto>
//                // findById 결과의 optional 데이터가 존재하지 않으면
//                .orElse(null);

    } // end todoUpdate

    // 5. 개별 삭제
    public boolean todoDelete(int id) {
        // [방법1]. 일반적인 ============================================================================= //
        // (1) id 를 이용하여 엔티티 조회하기
            // findById 반환타입 : Optional vs existsById : boolean
        boolean result = todoRepository.existsById(id);
        // (2) 만약에 존재하면
        if (result == true) {
            // (3) 영속성 제거 , deleteById(pk번호);
            todoRepository.deleteById(id);
            return true; // 삭제 성공
        } // end if
        return false; // 존재하지 않으면 삭제 취소
        // [방법2]. 스트림(stream)방법 ============================================================================= //
//        return todoRepository.existsById(id);
//        .map(entity -> {
//            todoRepository.deleteById(id);
//            return true;
//        })
//        .orElse(false);
    } // end todoDelete

    // 6. 전체 조회( + 페이징처리 )
    public List<TodoDto> todoFindByPage(int page , int size) {
        // page : 현재 조회중인 페이지 번호
        // size : 페이지 1개당 조회할 자료 개수
        // (1) PageRequest 클래스 이용한 페이징 처리 설정
            // - PageRequest.of( page : 조회할페이지번호 , size : 전체자료개수 ); - 조회할 페이지 번호는 1페이지 가 0부터 시작
            // - 페이지당 조회할 자료 개수
            // - Sort.by(정렬기준)
                // - Sort.by(Sort.Direction.ASC, "필드명") : 오름차순
                // - Sort.by(Sort.Direction.DESC, "필드명") : 내림차순(최신순)
        PageRequest pageRequest = PageRequest.of(page-1, size , Sort.by(Sort.Direction.DESC, "id"));
        // [방법2] stream 이용한 페이징처리
        return todoRepository.findAll(pageRequest).stream()
                .map(TodoEntity::toDto)
                .collect(Collectors.toList());
    } // end todoFindByPage

    // 7. 제목 검색 조회1 ( 입력한 값이 *일치* 한 제목 조회 )
    public List<TodoDto> search1(String title ){
        // [1 쿼리메소드 ]. JPA 리포지토리에서 내가 만든 추상메소드 사용한다.
//        return todoRepository.findByTitle( title )
//                .stream().map( TodoEntity::toDto )
//                .collect( Collectors.toList() );
        // [2 네이티브쿼리 ] JPA 리포지토리에서 내가 만든 추상메소드 사용한다.
        return todoRepository.findByTitleNative( title )
                .stream().map( TodoEntity::toDto )
                .collect( Collectors.toList() );
    } // end search1
    // 8. 제목 검색 조회2 ( 입력한 값이 *포함* 된 제목 조회 )
    public List<TodoDto> search2(String title ){
        // [1 쿼리메소드 ]. JPA 리포지토리에서 내가 만든 추상메소드 사용한다.
//        return todoRepository.findByTitleContaining( title )
//                .stream().map( TodoEntity::toDto )
//                .collect( Collectors.toList() );
        // [2 네이티브쿼리 ] JPA 리포지토리에서 내가 만든 추상메소드 사용한다.
        return todoRepository.findByTitleNativeSearch( title )
                .stream().map( TodoEntity::toDto )
                .collect( Collectors.toList() );
    } // end search2

} // end class

/**
// (2) pageRequest 객체를 findXX 에 매개변수로 대입한다. .findAll(페이징객체);
Page<TodoEntity> todoEntityPage = todoRepository.findAll(pageRequest);
            System.out.println("todoEntityPage : " + todoEntityPage);
            System.out.println("todoEntityPage.getTotalPages() : " + todoEntityPage.getTotalPages()); // 전체 페이지 개수
        System.out.println("todoEntityPage.getTotalElements() : " + todoEntityPage.getTotalElements()); // 전체 자료 개수
        System.out.println("todoEntityPage.getContent() : " + todoEntityPage.getContent()); // 조회된 page 타입 --> List 타입으로 변환

 // (3) page 타입의 entity 를 dto 로 변환
 for (int index = 0; index < todoEntityPage.getContent().size() ; index++) {
 TodoDto todoDto = todoEntityPage.getContent().get(index).toDto();
 todoDtoList.add(todoDto);
 } // end for
 return todoDtoList;
 */