package lx.team2.backend.feature.artwork;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface ArtworkMapper {

    int insertArtwork(ArtworkVO artwork);

    List<ArtworkVO> selectMarker(Map<String, Object> params);

    ArtworkVO selectArtwork(long artworkNumber);

    int updateArtwork(ArtworkVO artwork);

    int deleteArtwork(long artworkNumber);
}