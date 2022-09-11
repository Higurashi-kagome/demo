<template>
	<view class="content">
		<image class="logo" src="/static/logo.png"></image>
		<view class="text-area">
			<text class="title">{{title}}</text>
		</view>
		<button v-if="canIUseGetUserProfile" style="margin-bottom: 40rpx;" type="primary" lang="zh_CN" @tap="getUserProfile">新版登录方式</button>
		<button v-else type="primary" lang="zh_CN" open-type="getUserInfo" @getuserinfo="bindgetuserinfo">旧版登录方式</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				title: '登录配适',
				canIUseGetUserProfile: false
			}
		},
		onLoad() {
			if (wx.getUserProfile) {
				this.canIUseGetUserProfile = true
			}
		},
		methods: {
			// 旧版登录方式
			bindgetuserinfo(e) {
				console.log(e)
				if (e.detail.userInfo) { // 有的时候获取不到 userInfo
					uni.showLoading({
						title: '登录中'
					})
					// 业务逻辑
					this.wxLoginCode(e.detail.userInfo)
				}
			},
			// 新版登录方式
			getUserProfile() {
				uni.getUserProfile({
					lang: 'zh_CN',
					desc: '用于获取您的个人信息',
					success: res => {
						console.log(res)
						if (res.userInfo) {
							this.wxLoginCode(res.userInfo)
						} else {
							console.log('登录失败！' + res.errMsg)
						}
					},
					fail: err => {
						console.log(err)
						uni.showToast({
							title:'您已拒绝登录',
							icon:"error",
							duration:2000
						})
					}
				})
			},
			// 请求后端的方法
			wxLoginCode(e) {
				console.log(e)
				wx.login({
					success: res => {
						if (res.code) {
							console.log(res)
						} else {
							console.log('登录失败！' + res.errMsg)
						}
						uni.hideLoading();
					}
				})
			}
		}
	}
</script>

<style>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.logo {
		height: 200rpx;
		width: 200rpx;
		margin-top: 200rpx;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 50rpx;
	}

	.text-area {
		display: flex;
		justify-content: center;
	}

	.title {
		font-size: 36rpx;
		color: #8f8f94;
	}
</style>
