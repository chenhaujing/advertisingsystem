<style  lang="scss">
@import '~assets/css/base.scss';
$menuBgColor: #32323a;
$leftWidth: 220px;
$topHeight: 54px; // 顶部标题高度
// 最外层容器
.container {
    position: relative;
    width: 100%;
    height: 100%;
    .ivu-menu-dark {
        background: $menuBgColor;
    }
    .ivu-menu-dark.ivu-menu-vertical .ivu-menu-opened .ivu-menu-submenu-title {
        background: $menuBgColor;
    }
    .ivu-menu-dark.ivu-menu-vertical .ivu-menu-opened {
        background: $menuBgColor;
    } // 菜单选中效果样式覆盖
    .ivu-menu-dark.ivu-menu-vertical .ivu-menu-item-active:not(.ivu-menu-submenu),
    .ivu-menu-dark.ivu-menu-vertical .ivu-menu-submenu-title-active:not(.ivu-menu-submenu) {
        color: #fff; // border-right: 0;
        // border-left: 4px solid $mainColor;
    } // 行
    .flex {
        height: 100%;
    } // 面包屑
    .layout-breadcrumb {
        clear: both;
        margin-left: $leftWidth;
        padding: 10px 15px 0;
    }


    .layout-left {
        z-index: 999;
        min-height: 100%;
        // overflow:auto;
        position: absolute;
        left: 0;
        background-color: $menuBgColor;
        width: $leftWidth;
        top: $topHeight;
        bottom: 0;
        .tac {
            text-align: center;
        }
    }

    .layout-menu-left {
        background: $menuBgColor;
    }

    .layout-header {
        height: 96px;
        background: #fff;
        background-color: rgb(247, 251, 252);
        box-shadow: 0 1px 1px rgba(0, 0, 0, .1);
    }

    .layout-headerLogoBox {
        height: $topHeight;
        width: $leftWidth;
        padding: 11px 0px;
        margin: 0;
        float: left;
        box-sizing: border-box;
        background-color: #28282e;
    } // 左侧logo
    .layout-headerLogo {
        width: 27px;
        height: 30px;
        padding: 0;
        margin: 0 auto;
        display: block;
        background-image: url("~assets/img/ads_logo.png");
        background-size: 100% 100%;
    } // 底部版权信息
    .layout-copy {
        height: 66px;
        line-height: 66px;
        text-align: center;
        color: #9ea7b4;
    }
    /* 右侧头部导航条 */
    .userInfo {
        float: right;
        height: 54px;

        &>* {
            float: left;
        }
    }
    .layout-header-user {
        height: 55px;
        border-bottom: 2px solid #f1f1f1;
    }
    .userInfo-base {
        margin-top: 6px;
        .updatePw {
            color: #666;
            display: block;
            margin: 0 auto;
        }
    }
    /** 当前登录名称 **/
    .userInfo-base-name {
        font-size: 14px;
        color: #666666;
    }
    /** 当前登录角色 **/
    .userInfo-base-role {
        font-size: 12px;
        color: #999999;
    }
    /** 当前登录头像 **/
    .userInfo-icon {
        width: 37px;
        height: 37px;
        position: relative;
        top: 50%;
        transform: translateY(-50%);
        right: 20px;
        img {
            vertical-align: bottom;
            height: 100%;
            border-radius: 50%;
            width: 100%;
        }
    }

    .userInfo-logout {
        height: 54px;
        font-size: 14px;
        color: #666666;
        width: 100px;
        text-align: center;
        line-height: 54px;
    }

    .layout-right {
        position: absolute;
        left: $leftWidth;
        top: 97px;
        bottom: 0;
        right: 0;
        overflow: auto;
        min-width: 810px;
        background-color: #edf1f4;
    }

    .layout-right-content {
        padding: 20px;
    } // 菜单item
    .layout-menu-icon,
    .layout-menu-text {
        display: block;
        margin: 0 auto;
        text-align: center;
    }
    .layout-menu-text {
        font-size: 16px;
        margin-top: 10px;
    }
}

//是否禁用的样式
.enableClasses {
    text-align: center;
    width: 30px;
    color: #ffffff;
    background-color: #F0857D;
    line-height: 30px;
    height: 30px;
    border-radius: 4px;
    margin: 0 auto;
}

//是否根目录样式
.adsValidTag {
    text-align: center;
    width: 30px;
    color: #ffffff;
    background-color: rgba(126, 221, 156, 1);
    line-height: 30px;
    height: 30px;
    border-radius: 4px;
    margin: 0 auto;
}

</style>
<template>
    <div class="container">
        <!-- 顶部信息条 -->
        <div class="layout-header">
            <div class="layout-headerLogoBox">
                <a class="layout-headerLogo" @click="backIndex">
                </a>
            </div>

            <div class="layout-header-user">
                <div class="userInfo">
                    <div class="userInfo-icon">
                        <img src="~assets/img/client/client_dafault_icon.png">
                    </div>
                    <div class="userInfo-base">
                        <iDropdown placement="bottom-end">
                            <a href="javascript:void(0)">
                                <span class="userInfo-base-name">{{userData.nickname}}</span>
                                <br />
                                <span class="userInfo-base-role">{{userData.roleName}}</span>
                            </a>
                            <iDropdown-menu slot="list">
                                <iDropdown-item>
                                    <div class="updatePw" @click="updatePw">修改密码</div>
                                </iDropdown-item>
                            </iDropdown-menu>
                        </iDropdown>
                    </div>
                    <a class="userInfo-logout" @click="loginout">退出</a>
                </div>
            </div>
            <div class="layout-breadcrumb">
                <TyBreadCrumb></TyBreadCrumb>
            </div>

        </div>
        <!-- 左侧菜单 start -->
        <div class="layout-left" ref="layoutLeft">
            <iMenu theme="dark" width="auto" @on-select="selectMenu" :open-names="openNames" :active-name="pathname" ref="iMenu">
                <div class="layout-logo-left"></div>
                <iSubmenu :name="menu.url" v-for="(menu,index) in menuData" :key="index">
                    <template slot="title">
                        <iIcon :type="menu.icon"></iIcon>
                        {{menu.menuName}}
                    </template>
                    <iMenu-item v-for="(children,index) in menu.children" :name="children.url" :key="index" :type="0" v-text="children.menuName"></iMenu-item>
                </iSubmenu>
            </iMenu>
            <!-- <i-Menu @on-select="selectMenu" active-name="1-2" theme="dark" width="auto" :open-names="['1']" :active-name="0">
                                                                    <i-Menu-item :name="index" class="tac" v-for="(menu,index) in menuData" :key="menu.key">
                                                                        <Icon class="iconfont" :class="menu.classList" type="ios-navigate" :size="16"></Icon>
                                                                        <div class="layout-menu-text" v-text="menu.name"></div>
                                                                    </i-Menu-item>
                                                                </i-Menu> -->
        </div>
        <!-- 左侧菜单 end -->
        <div class="layout-right" ref="layoutRight">
            <router-view class="layout-right-content"></router-view>
            <div class="layout-copy">
                2011-2017 &copy; SAO.SO V1.2
            </div>
        </div>
    </div>
</template>
<script>
import iMenu from 'iview/src/components/menu';
import iIcon from 'iview/src/components/icon';
import iDropdown from 'iview/src/components/dropdown';
import TyBreadCrumb from 'components/tyBreadcrumb';
import allRouteInfo from '.././router/index.js'


export default {
    data() {
        return {
            currIndex: 0,
            isIconfont: true,
            userData: {},
            menuData: {},
            openNames: [],
            pathname: this.$root.pathname,
            icon: ['ios-navigate', 'ios-keypad', 'ios-analytics','ios-gear'],
            // menuData: [
            //     {
            //         icon: 'ios-navigate',
            //         name: "客户管理",
            //         key: '1',
            //         pathName: 'clientManager',
            //         classList: 'icon-kehuguanli',
            //         children: [{
            //             name: "客户管理",
            //             // key: '1-1',
            //             pathName: 'clientManagerIndex',
            //             classList: 'icon-kehuguanli',
            //         }]
            //     },
            //     {
            //         icon: 'ios-keypad',
            //         name: "合同管理",
            //         key: '2',
            //         pathName: 'contractIndex',
            //         classList: 'icon-hetongguanli',
            //         children: [{
            //             name: "合同管理",
            //             // key: '2-2',
            //             pathName: 'contractIndex',
            //             classList: 'icon-hetongguanli',
            //         }]
            //     },
            //     {
            //         icon: 'ios-analytics',
            //         name: "系统设置",
            //         key: '3',
            //         pathName: 'sysIndex',
            //         classList: 'icon-xitongshezhi',
            //         children: [
            //             {
            //                 name: "组织机构管理",
            //                 // key: '3-1',
            //                 pathName: 'sysIndex',
            //                 classList: 'icon-hetongguanli',
            //             },
            //             {
            //                 name: "角色管理",
            //                 // key: '3-2',
            //                 pathName: 'roleIndex',
            //                 classList: 'icon-hetongguanli',
            //             },
            //             {
            //                 name: "人员管理",
            //                 // key: '3-3',
            //                 pathName: 'peopleManager',
            //                 classList: 'icon-hetongguanli',
            //             },
            //             // {
            //             //     name: "权限管理",
            //             //     key: '3-4',
            //             //     pathName: '4',
            //             //     classList: 'icon-hetongguanli',
            //             // },
            //             {
            //                 name: "合同收款信息配置",
            //                 // key: '3-5',
            //                 pathName: 'contractReceivablesSetting',
            //                 classList: 'icon-hetongguanli',
            //             },
            //             {
            //                 name: "门店类别配置",
            //                 // key: '3-6',
            //                 pathName: 'storeTypeSettingIndex',
            //                 classList: 'icon-hetongguanli',
            //             },
            //             {
            //                 name: "广告配置",
            //                 // key: '3-7',
            //                 pathName: 'adSetting',
            //                 classList: 'icon-hetongguanli',
            //             },
            //             {
            //                 name: "菜单配置",
            //                 // key: '3-8',
            //                 pathName: 'menuSettingIndex',
            //                 classList: 'icon-hetongguanli',
            //             }
            //         ]
            //     }
            // ]
        }
    },
    created() {
        this.$get(this.$api.getUserMenus).then((result) => {
            this.userData = {
                nickname: result.data.nickname,
                roleName: result.data.roleName,
            }
            this.menuData = result.data.menus;

            this.$store.commit('setRoleType', {
                roleType: result.data.roleType
            });
            if (this.menuData == null) {
                return;
            }
            for (var i = 0; i < this.menuData.length; i++) {
                this.menuData[i].icon = this.icon[i];
            }
            this.$nextTick(() => {
                this.updateMenu();
            });
        }).catch(error => {
            this.$Message.error({
                content: error.message || "获取权限菜单失败"
            })
        })
    },
    methods: {
        selectMenu(pathName) {
            if (this.$route.name == pathName) {
                return;
            }
            // if (this.currIndex == pathName) {
            //     return;
            // }
            this.$store.commit('clearBreadcrumd');
            this.currIndex = pathName;
            this.$router.push({ name: pathName });
        },
        backIndex() {
            if (this.$route.name == 'clientManagerIndex') {
                return;
            }
            this.$store.commit(this.$mutations.BREADCRUMD_CLEAR);
            this.$router.push({ name: 'clientManagerIndex' });
            this.updateMenu();
        },
        //菜单展开更新
        updateMenu() {
            for (var i = 0; i < allRouteInfo.routes.length; i++) {
                if (allRouteInfo.routes[i].children && allRouteInfo.routes[i].children.length) {
                    for (var j = 0; j < allRouteInfo.routes[i].children.length; j++) {
                        if (allRouteInfo.routes[i].children[j].name == this.pathname) {
                            this.openNames.push(allRouteInfo.routes[i].meta.expandName)
                        }
                    }
                }
            }
            this.$refs.iMenu.updateActiveName();
            this.$refs.iMenu.updateOpened();
        },
        updatePw(){
                 this.$store.commit(this.$mutations.BREADCRUMD_CLEAR);
                 this.$router.push({name:"updatePw"})
        },
        loginout() {
            this.$Modal.confirm({
                title: '提示',
                content: '<p>确定退出吗？</p>',
                onOk: () => {
                    this.$store.commit(this.$mutations.BREADCRUMD_CLEAR);
                    this.$router.push({ name: 'login' })
                },

            });
        },
    },
    components: {
        TyBreadCrumb,
        iMenu,
        iIcon,
        iDropdown,
        'iDropdown-menu': iDropdown.Menu,
        'iDropdown-item': iDropdown.Item,
        'iMenu-item': iMenu.Item,
        'iSubmenu': iMenu.Sub
    },
    watch: {
        '$route': function(to, from) {
            // 还原滚动位置。
            this.$refs.layoutRight.scrollTop = 0
        }
    }
}
</script>