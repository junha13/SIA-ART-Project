package lx.team2.backend.feature.publicart;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PublicArtDAO {

	@Autowired
	SqlSession session;
	
	// ========== 공공예술 insert ==========
	public int insertPublicArtDB(PublicArtVO publicArt) {
		return session.insert("insertPublicArtDB", publicArt);
	}
}
