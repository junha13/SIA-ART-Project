import { createRouter, createWebHistory } from "vue-router"
import { useAppStore } from '@/stores/useAppStore'

// View Imports
import MainView from "../views/MainView.vue"
import ArchiveMapView from "../views/ArchiveMapView.vue" // 새 지도 뷰
import LoginView from "../views/LoginView.vue"
import RegisterView from "../views/RegisterView.vue"
import ArtistProfileView from "../views/ArtistProfileView.vue"
import ProductRegistrationView from "../views/ProductRegistrationView.vue"
import ClassListView from "../views/ClassListView.vue"
import MyPageView from "../views/MyPageView.vue"
import SalesHistoryView from "../views/SalesHistoryView.vue"
import PurchaseHistoryView from "../views/PurchaseHistoryView.vue"
import FavoriteProductsView from "../views/FavoriteProductsView.vue"
import FavoritePlacesView from "../views/FavoritePlacesView.vue"
import LocationSettingsView from "../views/LocationSettingsView.vue"
import ArtworkDetailView from "../views/ArtworkDetailView.vue"
import ClassDetailView from "../views/ClassDetailView.vue"
import FollowingArtistsView from "../views/FollowingArtistsView.vue"
import ArtworkListView from "../views/ArtworkListView.vue"
import BoardListView from "../views/BoardListView.vue"
import BoardWriteView from "../views/BoardWriteView.vue"
import BoardDetailView from "../views/BoardDetailView.vue"
import ArtistListView from "../views/ArtistListView.vue"

const router = createRouter({
    history: createWebHistory(),
    routes: [
        // === App.vue 레이아웃을 숨길 페이지 ===
        {
            path: "/login",
            name: "login",
            component: LoginView,
            meta: { requiresNoLayout: true } // 메타 필드 추가
        },
        {
            path: "/register",
            name: "register",
            component: RegisterView,
            meta: { requiresNoLayout: true } // 메타 필드 추가
        },

        // === 기존 레이아웃을 사용할 페이지 ===
        {
            path: "/archive-map", // 기존 메인 경로를 지도 아카이빙으로 변경
            name: "archive-map",
            component: ArchiveMapView,
            meta: { title: '지역 예술 지도' }
        },
        {
            path: "/", // 홈 버튼이 이동할 실제 메인 뷰 (콘텐츠 강조 버전)
            name: "home",
            component: MainView,
            meta: { title: '홈' }
        },
        {
            path: "/mypage", name: "mypage",
            component: MyPageView,
            meta: { title: '마이페이지' }
        },
        {
            path: "/artist/:id", name: "artist-profile",
            component: ArtistProfileView
        },
        {
            path: "/product/register", name: "product-registration",
            component: ProductRegistrationView
        },
        {
            path: "/classes", name: "classes",
            component: ClassListView
        },
        {
            path: "/class/:id", name: "class-detail",
            component: ClassDetailView
        },
        {
            path: "/following-artists", name: "following-artists",
            component: FollowingArtistsView
        },
        {
            path: "/sales-history", name: "sales-history",
            component: SalesHistoryView
        },
        {
            path: "/purchase-history", name: "purchase-history",
            component: PurchaseHistoryView
        },
        {
            path: "/favorite-products", name: "favorite-products",
            component: FavoriteProductsView
        },
        {
            path: "/favorite-places", name: "favorite-places",
            component: FavoritePlacesView
        },
        {
            path: "/location-settings", name: "location-settings",
            component: LocationSettingsView
        },
        {
            path: "/artwork/:id", name: "artwork-detail",
            component: ArtworkDetailView
        },
        {
            path: "/artworks", name: "artworks-list",
            component: ArtworkListView
        },
        {
            path: "/board", name: "board-list",
            component: BoardListView
        },
        {
            path: "/board/write", name: "board-write",
            component: BoardWriteView
        },
        {
            path: "/board/:id", name: "board-detail",
            component: BoardDetailView, props: true
        },
        {
            path: "/artist/list", name: "artist-list",
            component: ArtistListView, props: true
        },
         {
    path: '/board/edit', name: 'board-edit',
    component: BoardWriteView // ⭐ BoardWriteView 재사용
  }
        ,
    ],
})

// 네비게이션 가드: Pinia 스토어를 사용하여 레이아웃 표시 여부 결정
router.beforeEach((to, from, next) => {
    const store = useAppStore()
    if (to.meta.requiresNoLayout) {
        // 로그인/회원가입 페이지일 경우 레이아웃 숨김
        store.setShowLayout(false)
    } else {
        // 그 외 페이지일 경우 레이아웃 표시
        store.setShowLayout(true)
    }
    next()
})

export default router
