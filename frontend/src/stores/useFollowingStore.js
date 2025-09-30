import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { MOCK_ARTISTS } from '@/data/MockData.js'; // 🟢 MockData Import

export const useFollowingStore = defineStore('following', () => {
    // 1. 상태: 팔로우 작가 목록 (MockData 기반 초기화)
    const artists = ref([]);

    // 2. 액션: 초기화 (API 호출 대체)
    const initializeFollowing = () => {
        // ⭐ [구조 개선]: MOCK_ARTISTS 중 101, 103, 106번을 팔로우한 상태로 가정
        const followingIds = [101, 103, 106];
        artists.value = MOCK_ARTISTS
            .filter(a => followingIds.includes(a.id))
            .map(a => ({
                id: a.id,
                name: a.name,
                bio: a.bio,
                avatar: a.profileImage
            }));
    }

    // 3. 액션: 언팔로우 처리
    const unfollowArtist = (id) => {
        artists.value = artists.value.filter((a) => a.id !== id)
    }

    // 4. Getter
    const getArtists = computed(() => artists.value)
    const getArtistCount = computed(() => artists.value.length)
    const getArtistsForPreview = computed(() => artists.value.slice(0, 4))

    initializeFollowing(); // Store 정의 시 초기화

    return {
        artists,
        unfollowArtist,
        getArtists,
        getArtistCount,
        getArtistsForPreview,
        initializeFollowing // 뷰에서 명시적으로 호출 가능
    }
})