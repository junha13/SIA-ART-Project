// src/data/MockData.js (새로 업데이트)

/**
 * 🧑‍🎨 아티스트 (Artist) 및 사용자 더미 데이터
 */
export const MOCK_ARTISTS = [
    {
        id: 101, // 🟢 김준하 (ArtistProfileView의 주인공)
        name: "김준하",
        role: "예술가님",
        profileImage: "https://via.placeholder.com/110?text=JH",
        bio: "2조의 팀장이자 자랑이자 버팀목이자 기타등등인 고급인력",
        followers: 29,
        following: 29,
        career: [
            { year: '2001년', desc: '예술적인 걸음마' },
            { year: '2025년', desc: '최종팀프로젝트 1등' }
        ]
    },
    {
        id: 102, // 🟢 허지서
        name: "허지서",
        role: "아티스트",
        profileImage: "https://via.placeholder.com/110?text=HS",
        bio: "전문가를 위한 고급 유화 기법의 대가. 느슨한 예술계를 뒤집어놓았다.",
        followers: 550,
        following: 120,
        career: []
    }
];

/**
 * 🖼️ 작품 (Artwork) 및 상품 (Product) 더미 데이터
 */
export const MOCK_ARTWORKS = [
    { id: 1, title: '놀러가고 싶어요', subtitle: '느슨한 예술계를 뒤집어놓았다', artist: '주영민', artistId: 105, location: '상당구', price: 150000, likes: 6, category: '회화', image: 'https://placehold.co/200x180/5DADE2/fff?text=Art1', following: false, info: { size: '45cm x 53cm', material: '캔버스 유화물감', year: '2023', weight: '1.5kg' }, description: '이건 진짜 엄청난 명작이다.' },
    { id: 2, title: '팝업스토어 가는 사람', subtitle: '보고 싶다 김준하', artist: '박정훈', artistId: 106, location: '곤지암동', price: 420000, likes: 18, category: '회화', image: 'https://placehold.co/200x180/A3E4D7/000?text=Art2', following: true, info: { size: '30cm x 30cm', material: '아크릴', year: '2024', weight: '0.5kg' }, description: '팝업 스토어의 즐거움을 담았습니다.' },
    { id: 3, title: '피그마 그만 만질래', artist: '허지서', artistId: 102, location: '송도동', price: 990000, likes: 42, category: '공예', image: 'https://placehold.co/200x180/F8C471/000?text=Comedian', following: false, info: { size: '10cm x 10cm', material: '나무, 레진', year: '2025', weight: '0.1kg' }, description: '디자인 고통을 승화시킨 공예품입니다.' },
    { id: 4, title: '알바하러가는 예원', artist: '고예원', location: '북가좌동', price: 85000, likes: 12, category: '도예', image: 'https://placehold.co/200x180/EC7063/fff?text=Art4', following: false, info: { size: '30cm x 30cm', material: '도자기 유약', year: '2024', weight: '1.2kg' }, description: '알바의 고통을 담았습니다.' },
    // 🟢 김준하 작가 작품 2개 추가
    { id: 5, title: '팀장님의 의무', subtitle: '총대를 멘 리더의 고뇌', artist: '김준하', artistId: 101, location: '구로구', price: 120000, likes: 25, category: '회화', image: 'https://placehold.co/200x180/16A085/fff?text=Art5', following: false, info: { size: '40cm x 40cm', material: '유화', year: '2025', weight: '1kg' }, description: '책임감을 표현한 작품.' },
    { id: 6, title: '자랑스러운 깃발', subtitle: '최종 프로젝트 1등의 쾌거', artist: '김준하', artistId: 101, location: '송도동', price: 300000, likes: 30, category: '조각', image: 'https://placehold.co/200x180/5499C7/fff?text=Art6', following: false, info: { size: '20cm x 20cm', material: '레진', year: '2025', weight: '0.5kg' }, description: '승리의 순간.' },
];

/**
 * 📰 게시글 (Board) 및 댓글 (Comment) 더미 데이터
 */
export const MOCK_POSTS = [
    { id: 1, category: "공지", title: "게시판 규정 관련 공지사항입니다.[12]", content: "게시판 이용에 관한 주요 규정입니다. 모두 필독해주세요.", comments: "게시판 규정 댓글", author: "관리자", likes: 15, views: 200, date: "2025.09.27", tags: ["공지", "필독"], image: "https://placehold.co/600x300/F5A9A9/fff?text=NOTICE+IMAGE" },
    { id: 2, category: "미술", title: "미술 빡x 요즘 미술하기 힘드네요...", content: "아이디어가 고갈되어서 힘듭니다.", comments: "아이디어가 좋네요", author: "김춘화", likes: 5, views: 120, date: "2025.09.28", tags: ["미술", "고민"], image: "https://placehold.co/600x300/F5A9A9/fff?text=ATTACHED+IMAGE" },
    { id: 3, category: "음악", title: "인기글 예시 - 조회수 높음", content: "최근 발표된 새로운 음악 트렌드 분석입니다.", comments: "노래 좋아요", author: "음악가", likes: 50, views: 500, date: "2025.09.29", tags: ["음악", "트렌드"], image: "" },
    { id: 4, category: "정보", title: "일반 정보글 예시", content: "유용한 웹사이트 정보를 공유합니다.", comments: "새로운 정보 감사합니다", author: "정보통", likes: 2, views: 50, date: "2025.09.20", tags: ["정보", "공유"], image: "" },
    // 🟢 김준하 작가 게시글 1개 추가
    { id: 5, category: "미술", title: "김준하입니다. 포폴 작업 공유해요!", content: "최종 프로젝트 작업 과정 중입니다.", comments: "힘내세요", author: "김준하", likes: 10, views: 80, date: "2025.12.01", tags: ["작업공유", "유화"], image: "" },
];

export const MOCK_COMMENTS = [
    { postId: 2, author: "이관람", text: "디자인이 훨씬 깔끔하고 보기 좋아졌네요! 특히 댓글 영역이 마음에 듭니다.", date: "2025.09.29" },
    { postId: 2, author: "박수집", text: "추천 버튼이 눈에 확 들어와서 클릭하고 싶어져요. 수고하셨습니다!", date: "2025.09.29" }
];


/**
 * 🎓 클래스 (Class) 더미 데이터
 */
export const MOCK_CLASSES = [
    {
        id: 1, title: '수채화 기초', subtitle: '처음 시작하는 수채화, 기본기부터 차근차근', category: '미술', level: '입문',
        instructor: '김준하', instructorId: 101, instructorAvatar: 'https://via.placeholder.com/64x64.png?text=KJ',
        description: '강좌 목표, 철학, 수강생이 얻을 수 있는 가치',
        duration: '일일', location: '구로구 구로동', days: '일요일', capacity: 10, price: 100000,
        materialsIncluded: false, curriculum: ['붓 잡는 법', '물감 이해', '정물화'],
        image: 'https://placehold.co/900x540/ddd/000?text=Watercolor+Basic'
    },
    {
        id: 2, title: '유화 마스터', subtitle: '전문가를 위한 고급 유화 기법', category: '미술', level: '고급',
        instructor: '허지서', instructorId: 102, instructorAvatar: 'https://via.placeholder.com/64x64.png?text=HS',
        description: '고급 유화 기법 클래스입니다.',
        duration: '4주', location: '서울 구로구 구로동', days: '토, 일', capacity: 5, price: 200000,
        materialsIncluded: true, curriculum: ['색채의 이해', '형태 탐구', '창작 실습', '작품 발표'],
        image: 'https://placehold.co/900x540/ddd/000?text=Oil+Master'
    }
];

/**
 * 📍 지도 위치 (Location) 더미 데이터 (ArchiveMapView 용)
 */
export const MOCK_MAP_DATA = [
    { id: 106, type: 'art', title: '미래 도시', category: '디지털', image: 'assets/media/avatars/300-17.jpg', lat: 37.4950, lng: 126.8870, loc: '구로구' },
    { id: 205, type: 'studio', name: '정조각 공방', bio: '현대 조각 작업 공간', image: 'assets/media/avatars/300-5.jpg', lat: 37.4952, lng: 126.8872, loc: '구로구' },
    { id: 305, type: 'gallery', title: '구로 디지털 아트홀', category: '미디어 아트', image: 'assets/media/avatars/300-20.jpg', lat: 37.4954, lng: 126.8874, loc: '구로구' },
    { id: 306, type: 'gallery', title: '서울 시립 미술관', category: '상설 전시', image: 'assets/media/avatars/300-21.jpg', lat: 37.5615, lng: 126.9750, loc: '중구' },
    // 🟢 맵 데이터 4개 추가
    { id: 107, type: 'art', title: '밤의 꿈', category: '회화', image: 'assets/media/avatars/300-18.jpg', lat: 37.5610, lng: 126.9745, loc: '중구' },
    { id: 206, type: 'studio', name: '기타 연주 스튜디오', bio: '음악 레슨 공간', image: 'assets/media/avatars/300-10.jpg', lat: 37.4953, lng: 126.8873, loc: '구로구' },
    { id: 307, type: 'gallery', title: '홍대 길거리 전시', category: '야외 설치', image: 'assets/media/avatars/300-22.jpg', lat: 37.5505, lng: 126.9205, loc: '마포구' },
    { id: 108, type: 'art', title: '고요한 아침의 빛', category: '회화', image: 'assets/media/avatars/300-11.jpg', lat: 37.5665, lng: 126.9780, loc: '종로구' },
];

/**
 * 🛒 구매/판매 내역 더미 데이터
 */
export const MOCK_HISTORY = {
    purchases: [
        { id: 1, artworkId: 1, title: '놀러가고 싶어요', date: '2025-09-02', price: 150000, seller: '주영민', image: 'https://placehold.co/150x150/007bff/ffffff?text=Art1' },
        // 🟢 구매 내역 1개 추가
        { id: 2, artworkId: 3, title: '피그마 그만 만질래', date: '2025-10-15', price: 990000, seller: '허지서', image: 'https://placehold.co/150x150/F8C471/000?text=Comedian' },
    ],
    sales: [
        { id: 1, artworkId: 2, title: '팝업스토어 가는 사람', date: '2025-09-01', price: 420000, buyer: '구매자A', image: 'https://placehold.co/150x150/28a745/ffffff?text=Art2' },
        // 🟢 판매 내역 1개 추가
        { id: 2, artworkId: 5, title: '팀장님의 의무', date: '2025-12-05', price: 120000, buyer: '구매자B', image: 'https://placehold.co/150x150/16A085/fff?text=Art5' },
    ]
}

/**
 * ❤️ 즐겨찾기 더미 데이터
 */
export const MOCK_FAVORITES = {
    products: [
        { id: 3, title: '피그마 그만 만질래', artist: '허지서', shortDescription: '디자인 고통을 승화시킨 공예품입니다.', image: 'https://placehold.co/400x250/F8C471/000?text=Comedian' },
        // 🟢 즐겨찾기 작품 1개 추가
        { id: 4, title: '알바하러가는 예원', artist: '고예원', shortDescription: '알바의 고통을 담았습니다.', image: 'https://placehold.co/400x250/EC7063/fff?text=Art4' },
    ],
    places: [
        { id: 1, name: '예술의전당', address: '서울 서초구', description: '다양한 공연과 전시가 열리는 복합 문화 공간' },
        // 🟢 즐겨찾기 장소 1개 추가
        { id: 2, name: '구로디지털 아트홀', address: '서울 구로구', description: '미디어 아트 중심의 전시 공간' },
    ]
}