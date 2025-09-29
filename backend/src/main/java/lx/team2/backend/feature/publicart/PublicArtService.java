package lx.team2.backend.feature.publicart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublicArtService {

	@Autowired
	PublicArtDAO dao;
	
	// ========== 공공예술 핑찍은거 정보 받으면 db insert ==========	
	@Transactional
	public boolean insertPublicArtDB(PublicArtVO publicArt) {
		/*
		// 1. 공공예술 정보 추출 후 저장
		PublicArtVO publicArt = dto.getPublicArt();
		
		// 2. 위치 정보 추출 후 저장, PK 추출
		LocationVO location = dto.getLocation();
		dao.insertPublicArtLocationDB(location);
		
		publicArt.setLocationNo(location.getLocationNo());
		
		// 3. 공공예술 insert
		dao.insertPublicArtDB(publicArt);
		*/
		int num = dao.insertPublicArtDB(publicArt);
		return (num == 1) ? true : false;
	}
}
