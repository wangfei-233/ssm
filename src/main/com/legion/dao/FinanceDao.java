package legion.dao;
import legion.entity.Finance;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
@Repository
public interface FinanceDao {
    Integer addFinance(Finance finance);

    Integer deleteFinance(@Param("id") Integer id);

    Integer updateFinance(Finance finance);

    Finance listFinanceByID(@Param("id") Integer id);

    ArrayList<Finance> listFinance(@Param("type") String type,@Param("page") Integer page);

    Integer listallpage(@Param("type") String type);

}
