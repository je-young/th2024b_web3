package example.day04.model.repository;


import example.day04.model.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity , Integer> {

    // JPA Reopsitory
    // 1.save() 2.findById() 3.findAll() 4. deleteById() 등등 미리 만들어진 CRUD 메소드 제공.
    // 2. 쿼리메서드(JPQL 이용한 메서드 이름 기반으로 자동 생성) // =================================
    // Spring JPA 에서 SQL 문장을 직접 작성하지 않고 메서드 이름으로 쿼리 생성한다. <카멜표기법>
    // ***메서드 명명 규칙***

    List<TodoEntity> findByTitle(String title);
        // List<TodoEntity> : 조회 결과를 List 타입
        // findByTitle : title 필드를 select(조회) 한다. * 주의할점 : <카멜표기법>
        // (String title) : 조회 조건
        // mybatis : select * from todo where title = ${title}
    List<TodoEntity> findByTitleContaining(String Keyword);
        // mybatis : select * from todo where title like '%${title}%'

    // 3. 네이티브쿼리(*SQL 직접 작성*) // ======================================================
    // Spring JPA에서 SQL 문장을 직접 작성하는 메서드.
    // *** @Query(value = "SQL 문법", nativeQuery = true) ***
    //      -> Query 는 select 위한 어노테이션 이므로 insetr, update, delete 할 경우에는 @ModifyQuery 같이 사용한다.
    // SQL 문의 매개변수를 작성시에는 : 매개변수명 작성하여 매개변수를 대입할 수 있다.
    @Query(value = "select * from todo where title = :title", nativeQuery = true)
    List<TodoEntity> findByTitleNative(String title);
        // List<TodoEntity> : 조회 결과를 List 타입
        // findByTitleNative : 규칙 없으므로 아무거나
        // (String title) : 조회 조건으로 SQL 문법의 매개변수
    @Query(value = "select * from todo where title like %:title%", nativeQuery = true)
    List<TodoEntity> findByTitleNativeSearch(String Keyword);

} // end interface


// https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html // 참고 : url