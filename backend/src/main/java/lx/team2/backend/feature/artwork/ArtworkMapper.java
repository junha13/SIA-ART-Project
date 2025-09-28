package lx.team2.backend.feature.artwork;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArtworkMapper {
    void insertArtwork(ArtworkVO vo);
    List<ArtworkVO> selectAllArtworks();
}
