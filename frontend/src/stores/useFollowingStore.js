import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useFollowingStore = defineStore('following', () => {
    // 1. 상태: 팔로우 작가 목록 (더미 데이터)
    const artists = ref([
        { id: 1, name: '김작가', bio: '추상화 전문 예술가', avatar: 'https://via.placeholder.com/80x80/F64E60/fff?text=A1' },
        { id: 2, name: '이예술', bio: '현대 미술과 설치미술 작가', avatar: 'https://via.placeholder.com/80x80/1BC5BD/fff?text=A2' },
        { id: 3, name: '박 작가', bio: '조각가', avatar: 'assets/media/avatars/300-11.jpg' },
        { id: 4, name: '정 작가', bio: '사진작가', avatar: 'assets/media/avatars/300-14.jpg' },
    ])

    // 2. 액션: 언팔로우 처리
    const unfollowArtist = (id) => {
        artists.value = artists.value.filter((a) => a.id !== id)
    }

    // 3. Getter
    const getArtists = computed(() => artists.value)
    const getArtistCount = computed(() => artists.value.length)
    // 마이페이지 미리보기를 위한 슬라이스 Getter
    const getArtistsForPreview = computed(() => artists.value.slice(0, 4))

    return {
        artists,
        unfollowArtist,
        getArtists,
        getArtistCount,
        getArtistsForPreview
    }
})