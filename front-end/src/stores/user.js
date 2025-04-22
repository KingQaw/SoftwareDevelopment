import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
    state: () => ({
        id: null,
        nickname: '',
        avatar: '',
        account: '',
        role: '',
        token: localStorage.getItem('token') || null
    }),

    getters: {
        // 判断用户是否登录
        isLoggedIn: (state) => !!state.token,

        // 获取用户角色
        userRole: (state) => state.role,

        // 获取用户基本信息
        userInfo: (state) => ({
            id: state.id,
            nickname: state.nickname,
            avatar: state.avatar,
            account: state.account,
            role: state.role
        })
    },

    actions: {
        // 设置用户信息
        setUser(userData) {
            this.id = userData.id
            this.nickname = userData.nickname
            this.avatar = userData.avatar
            this.account = userData.account
            this.role = userData.role
            this.token = userData.token

            // 持久化 token 到 localStorage
            if (userData.token) {
                localStorage.setItem('token', userData.token)
            }
        },

        // 清除用户信息
        clearUser() {
            this.id = null
            this.data = null
            this.nickname = ''
            this.avatar = ''
            this.account = ''
            this.role = ''
            this.token = null
            localStorage.removeItem('token')
        },

        // 更新用户信息（部分更新）
        updateUserInfo(partialData) {
            if (partialData.nickname !== undefined) {
                this.nickname = partialData.nickname
            }
            if (partialData.avatar !== undefined) {
                this.avatar = partialData.avatar
            }
            // 可以继续添加其他需要更新的字段
        }
    }
})