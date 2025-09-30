// src/data/MockData.js

/**
 * 🧑‍🎨 아티스트 (Artist) 및 사용자 더미 데이터 (총 6명)
 */
export const MOCK_ARTISTS = [
    {
        id: 101, // 김준하
        name: "김준하",
        role: "예술가님",
        profileImage: "https://via.placeholder.com/110?text=JH",
        bio: "2조의 팀장이자 자랑이자 버팀목이자 기타등등인 고급인력",
        followers: 29,
        following: 29,
        career: [
            { year: '2001년', desc: '예술적인 걸음마' },
            { year: '2014년', desc: '경력2: 마포구 시민회관 그룹전' },
            { year: '2015년', desc: '경력1: 한국 디지털 아트 협회 입회' },
            { year: '2016년', desc: '경력3: 개인 작품 "고독한 코드" 발표' },
            { year: '2020년', desc: '예술적인 입학: 청년작가 과정 수료' },
            { year: '2025년', desc: '최종팀프로젝트 1등 (예상)' }
        ]
    },
    {
        id: 102, // 허지서
        name: "허지서",
        role: "아티스트",
        profileImage: "https://via.placeholder.com/110?text=HS",
        bio: "전문가를 위한 고급 유화 기법의 대가. 느슨한 예술계를 뒤집어놓았다.",
        followers: 550,
        following: 120,
        career: [
            { year: '2022년', desc: '최고급 유화 마스터 과정 수료' },
            { year: '2023년', desc: '제15회 신진 작가 공모전 대상 수상' },
            { year: '2024년', desc: '국제 조각 심포지엄 참가 및 초청 작가 활동' }
        ]
    },
    {
        id: 103, // 박정훈 (New: 기존 Artwork/History에 있던 작가)
        name: "박정훈",
        role: "아티스트",
        profileImage: "https://via.placeholder.com/110?text=PJH",
        bio: "인터랙티브 미디어를 활용한 새로운 예술 형태를 탐구합니다.",
        followers: 180,
        following: 50,
        career: [
            { year: '2023년', desc: '미디어 아트 공모전 은상' }
        ]
    },
    {
        id: 104, // 고예원 (New: 기존 Artwork에 있던 작가)
        name: "고예원",
        role: "아티스트",
        profileImage: "https://via.placeholder.com/110?text=GYW",
        bio: "백토와 청자를 주재료로, 한국적 미를 현대적으로 재해석합니다.",
        followers: 85,
        following: 30,
        career: [
            { year: '2021년', desc: '도예 공방 운영 시작' }
        ]
    },
    {
        id: 105, // 주영민 (New: 기존 Artwork/History에 있던 작가)
        name: "주영민",
        role: "아티스트",
        profileImage: "https://via.placeholder.com/110?text=JYM",
        bio: "일상 속 찰나의 감정을 담은 감성 회화 작가.",
        followers: 320,
        following: 100,
        career: [
            { year: '2020년', desc: '감성 미술 협회 정회원' }
        ]
    },
    {
        id: 106, // 박민호 (New: 기존 Artwork/History에 있던 작가)
        name: "박민호",
        role: "아티스트",
        profileImage: "https://via.placeholder.com/110?text=PMH",
        bio: "느림의 미학을 추구하는 필름 사진가.",
        followers: 900,
        following: 40,
        career: [
            { year: '2024년', desc: '개인 사진전 "틈새 시간" 개최' }
        ]
    },
    {
        id: 107, // 이화가 (FavoriteProductsView에서 사용)
        name: "이화가",
        role: "아티스트",
        profileImage: "https://via.placeholder.com/110?text=LYG",
        bio: "자연의 아름다움을 담는 화가.",
        followers: 150,
        following: 50,
        career: []
    },
    {
        id: 108, // 홍길동 (FavoriteProductsView에서 사용)
        name: "홍길동",
        role: "아티스트",
        profileImage: "https://via.placeholder.com/110?text=HGD",
        bio: "개성 강한 추상화 작가.",
        followers: 500,
        following: 10,
        career: []
    }
];

/**
 * 🖼️ 작품 (Artwork) 및 상품 (Product) 더미 데이터 (총 10개 이상)
 */
export const MOCK_ARTWORKS = [
    // 기존에 ArtworkListView에 있던 더미
    { id: 1, title: '놀러가고 싶어요', subtitle: '느슨한 예술계를 뒤집어놓았다', artist: '주영민', artistId: 105, location: '상당구', price: 150000, likes: 6, category: '회화', image: 'https://placehold.co/200x180/5DADE2/fff?text=Art1', following: false, info: { size: '45cm x 53cm', material: '캔버스 유화물감', year: '2023', weight: '1.5kg' }, description: '이건 진짜 엄청난 명작이다.' },
    { id: 2, title: '팝업스토어 가는 사람', subtitle: '보고 싶다 김준하', artist: '박정훈', artistId: 103, location: '곤지암동', price: 420000, likes: 18, category: '회화', image: 'https://placehold.co/200x180/A3E4D7/000?text=Art2', following: true, info: { size: '30cm x 30cm', material: '아크릴', year: '2024', weight: '0.5kg' }, description: '팝업 스토어의 즐거움을 담았습니다.' },
    { id: 3, title: '피그마 그만 만질래', subtitle: '디자인 고통 승화', artist: '허지서', artistId: 102, location: '송도동', price: 990000, likes: 42, category: '공예', image: 'https://placehold.co/200x180/F8C471/000?text=Comedian', following: false, info: { size: '10cm x 10cm', material: '나무, 레진', year: '2025', weight: '0.1kg' }, description: '디자인 고통을 승화시킨 공예품입니다.' },
    { id: 4, title: '알바하러가는 예원', artist: '고예원', artistId: 104, location: '북가좌동', price: 85000, likes: 12, category: '도예', image: 'https://placehold.co/200x180/EC7063/fff?text=Art4', following: false, info: { size: '30cm x 30cm', material: '도자기 유약', year: '2024', weight: '1.2kg' }, description: '알바의 고통을 담았습니다.' },
    { id: 5, title: '집에가고 싶은 민호', artist: '박민호', artistId: 106, location: '서대문구', price: 250000, likes: 25, category: '사진', image: 'https://placehold.co/200x180/3699FF/fff?text=Photo', following: false, info: { size: 'A4', material: '필름 인화지', year: '2024', weight: '0.1kg' }, description: '퇴근을 염원하는 사진입니다.' },
    { id: 6, title: '학원가고 싶어요', artist: '김준하', artistId: 101, location: '만안구', price: 1200000, likes: 8, category: '조각', image: 'https://placehold.co/200x180/FFA800/fff?text=Sculpture', following: true, info: { size: '50cm x 50cm', material: '청동', year: '2022', weight: '10kg' }, description: '팀장의 유일한 취미.' },
    // 추가 작품 (다양성 증진)
    { id: 7, title: '가을의 서정', subtitle: '일상의 찰나', artist: '주영민', artistId: 105, location: '상당구', price: 210000, likes: 60, category: '회화', image: 'https://placehold.co/200x180/F0F0F0/000?text=Art7', following: true, info: { size: '50cm x 40cm', material: '아크릴', year: '2023', weight: '1kg' }, description: '따뜻한 감성이 돋보이는 작품입니다.' },
    { id: 8, title: '흐르는 시간', subtitle: '필름의 질감', artist: '박민호', artistId: 106, location: '용산구', price: 120000, likes: 88, category: '사진', image: 'https://placehold.co/200x180/000000/fff?text=Photo1', following: false, info: { size: 'A2', material: '캔버스 인화', year: '2024', weight: '0.5kg' }, description: '하늘을 담은 대형 캔버스 인화.' },
    // FavoriteProductsView의 Mock Data 통합
    { id: 9, title: '추상화 작품', artist: '홍길동', artistId: 108, location: '강남구', price: 500000, likes: 10, category: '회화', image: 'public/assets/media/stock/600x600/img-45.jpg', shortDescription: '강렬한 색채가 특징인 현대 추상화', info: { size: '100cm', material: '유화', year: '2024' }, description: '추상화입니다.' },
    { id: 10, title: '풍경화', artist: '이화가', artistId: 107, location: '은평구', price: 300000, likes: 5, category: '회화', image: 'public/assets/media/stock/600x600/img-46.jpg', shortDescription: '자연의 아름다움을 담은 풍경화', info: { size: '80cm', material: '수채화', year: '2023' }, description: '풍경화입니다.' }
];

/**
 * 📰 게시글 (Board) 및 댓글 (Comment) 더미 데이터
 */
export const MOCK_POSTS = [
    // BoardListView의 기존 데이터
    { id: 1, category: "공지", title: "게시판 규정 관련 공지사항입니다.[12]", content: "게시판 이용에 관한 주요 규정입니다. 모두 필독해주세요.", comments: "게시판 규정 댓글", author: "관리자", authorId: 999, likes: 15, views: 200, date: "2025.09.27", tags: ["공지", "필독"], image: "https://placehold.co/600x300/F5A9A9/fff?text=NOTICE+IMAGE" },
    { id: 2, category: "미술", title: "미술 빡x 요즘 미술하기 힘드네요...", content: "아이디어가 고갈되어서 힘듭니다.", comments: "아이디어가 좋네요", author: "허지서", authorId: 102, likes: 5, views: 120, date: "2025.09.28", tags: ["미술", "고민"], image: "https://placehold.co/600x300/F5A9A9/fff?text=ATTACHED+IMAGE" },
    { id: 3, category: "음악", title: "인기글 예시 - 조회수 높음", content: "최근 발표된 새로운 음악 트렌드 분석입니다.", comments: "노래 좋아요", author: "음악가", authorId: 103, likes: 50, views: 500, date: "2025.09.29", tags: ["음악", "트렌드"], image: "" },
    { id: 4, category: "정보", title: "일반 정보글 예시", content: "유용한 웹사이트 정보를 공유합니다.", comments: "새로운 정보 감사합니다", author: "정보통", authorId: 105, likes: 2, views: 50, date: "2025.09.20", tags: ["정보", "공유"], image: "" },
    // 추가 게시글
    { id: 5, category: "미술", title: "최종 발표 D-Day", content: "오늘은 발표날입니다. 모두 화이팅!", author: "김준하", authorId: 101, likes: 15, views: 200, date: "2025.12.01", tags: ["발표", "팀플"], image: "https://placehold.co/600x300/F0A0A0/fff?text=SUCCESS" },
    { id: 6, category: "공예", title: "레진 공예 시작했어요", content: "취미로 시작했는데 빠져드네요.", author: "허지서", authorId: 102, likes: 25, views: 300, date: "2025.11.15", tags: ["공예", "취미"], image: "" },
];

export const MOCK_COMMENTS = [
    { postId: 2, author: "이관람", text: "디자인이 훨씬 깔끔하고 보기 좋아졌네요!", date: "2025.09.29" },
    { postId: 2, author: "박수집", text: "추천 버튼이 눈에 확 들어와서 클릭하고 싶어져요. 수고하셨습니다!", date: "2025.09.29" }
];


/**
 * 🎓 클래스 (Class) 더미 데이터
 */
export const MOCK_CLASSES = [
    // 기존 데이터
    {
        id: 1, title: '수채화 기초', subtitle: '처음 시작하는 수채화, 기본기부터 차근차근', category: '미술', level: '입문',
        instructor: '김준하', instructorId: 101, instructorAvatar: 'https://via.placeholder.com/64x64.png?text=KJ',
        description: '강좌 목표, 철학, 수강생이 얻을 수 있는 가치', duration: '일일', location: '구로구 구로동', days: '일요일', capacity: 10, price: 100000,
        materialsIncluded: false, curriculum: ['붓 잡는 법', '물감 이해', '정물화'], image: 'https://placehold.co/900x540/ddd/000?text=Watercolor+Basic'
    },
    {
        id: 2, title: '유화 마스터', subtitle: '전문가를 위한 고급 유화 기법', category: '미술', level: '고급',
        instructor: '허지서', instructorId: 102, instructorAvatar: 'https://via.placeholder.com/64x64.png?text=HS',
        description: '고급 유화 기법 클래스입니다.', duration: '4주', location: '서울 구로구 구로동', days: '토, 일', capacity: 5, price: 200000,
        materialsIncluded: true, curriculum: ['색채의 이해', '형태 탐구', '창작 실습', '작품 발표'], image: 'https://placehold.co/900x540/ddd/000?text=Oil+Master'
    },
    // 추가 데이터
    {
        id: 3, title: '나만의 도자기 빚기', subtitle: '도예 체험 클래스', category: '도예', level: '입문',
        instructor: '고예원', instructorId: 104, instructorAvatar: 'https://via.placeholder.com/64x64.png?text=GYW',
        description: '흙을 만지며 힐링하는 시간.', duration: '2시간', location: '종로구 삼청동', days: '수요일', capacity: 8, price: 50000,
        materialsIncluded: true, curriculum: ['물레 사용법', '빚기', '유약 바르기'], image: 'https://placehold.co/900x540/A9CCE3/000?text=Pottery+Class'
    },
    {
        id: 4, title: '사진 보정 기초', subtitle: '라이트룸 사용법 완벽 마스터', category: '사진', level: '중급',
        instructor: '박민호', instructorId: 106, instructorAvatar: 'https://via.placeholder.com/64x64.png?text=PM',
        description: '사진 보정의 기본 원리를 이해하고 실습합니다.', duration: '8주', location: '온라인', days: '금요일', capacity: 30, price: 300000,
        materialsIncluded: false, curriculum: ['색감 조정', '명암비 이해', '인물 보정'], image: 'https://placehold.co/900x540/85C1E9/000?text=Photo+Class'
    }
];

/**
 * 📍 지도 위치 (Location) 더미 데이터 (ArchiveMapView 용)
 */
export const MOCK_MAP_DATA = [
    // ArchiveMapView의 기존 데이터
    { id: 106, type: 'art', title: '미래 도시', category: '디지털', image: 'assets/media/avatars/300-17.jpg', lat: 37.4950, lng: 126.8870, loc: '구로구' },
    { id: 205, type: 'studio', name: '정조각 공방', bio: '현대 조각 작업 공간', image: 'assets/media/avatars/300-5.jpg', lat: 37.4952, lng: 126.8872, loc: '구로구' },
    { id: 305, type: 'gallery', title: '구로 디지털 아트홀', category: '미디어 아트', image: 'assets/media/avatars/300-20.jpg', lat: 37.4954, lng: 126.8874, loc: '구로구' },
    // 추가 데이터
    { id: 107, type: 'art', title: '가을의 서정', category: '회화', image: 'https://placehold.co/60x60/F0F0F0/000?text=Art7', lat: 37.5615, lng: 126.9750, loc: '중구' },
    { id: 206, type: 'studio', name: '홍대 작업실', bio: '팝아트 스튜디오', image: 'assets/media/avatars/300-1.jpg', lat: 37.5500, lng: 126.9200, loc: '마포구' },
];

/**
 * 🛒 구매/판매 내역 더미 데이터
 */
export const MOCK_HISTORY = {
    // PurchaseHistoryView의 기존 데이터
    purchases: [
        { id: 1, artworkId: 1, title: '팀장의 고뇌', date: '2025-09-02', price: 150000, seller: '김준하', image: 'https://placehold.co/150x150/007bff/ffffff?text=Art1' },
        { id: 2, artworkId: 7, title: '가을의 서정', date: '2025-09-02', price: 210000, seller: '주영민', image: 'https://placehold.co/150x150/007bff/ffffff?text=Art7' },
    ],
    // SalesHistoryView의 기존 데이터
    sales: [
        { id: 1, artworkId: 2, title: '팝업스토어 가는 사람', date: '2025-09-01', price: 420000, buyer: '구매자A', image: 'https://placehold.co/150x150/28a745/ffffff?text=Art2' },
        { id: 2, artworkId: 5, title: '승리 직전의 미소', date: '2025-11-20', price: 890000, buyer: '구매자B', image: 'https://placehold.co/150x150/C39BD3/fff?text=Art5' },
    ]
}

/**
 * ❤️ 즐겨찾기 더미 데이터
 */
export const MOCK_FAVORITES = {
    // FavoriteProductsView의 기존 데이터
    products: [
        { id: 3, title: '피그마 그만 만질래', artist: '허지서', shortDescription: '디자인 고통을 승화시킨 공예품입니다.', image: 'https://placehold.co/400x250/F8C471/000?text=Comedian' },
        { id: 10, title: '풍경화', artist: '이화가', shortDescription: '자연의 아름다움을 담은 풍경화', image: 'public/assets/media/stock/600x600/img-46.jpg' },
        { id: 9, title: '추상화 작품', artist: '홍길동', shortDescription: '강렬한 색채가 특징인 현대 추상화', image: 'public/assets/media/stock/600x600/img-45.jpg' },
    ],
    // FavoritePlacesView의 기존 데이터
    places: [
        { id: 1, name: '예술의전당', address: '서울 서초구', description: '다양한 공연과 전시가 열리는 복합 문화 공간' },
        { id: 2, name: '홍대 예술 거리', address: '서울 마포구', description: '젊은 예술가들의 작품과 거리 공연을 즐길 수 있는 공간' }
    ]
}