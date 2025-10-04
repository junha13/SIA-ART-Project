package lx.team2.backend.feature.classes;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClassDAO {
    void getClasses(ClassVO vo);
}