import tyIconTextButton from 'components/tyIconTextButton';
import adState from './adState.js';

export default {
    // 根据广告状态，返回操作按钮
    components: {
        tyIconTextButton
    },
    data(){
        return {
            columns1: [
                {
                   title: '编号',
                   width: 60,
                   key: '_NUMBER_',
                   align: 'center',
               },
               { title: '广告名称', key: 'advertisementName', align: 'center', ellipsis: true },
               { title: '广告类型', key: 'advertisementType', align: 'center', ellipsis: true },
               { title: '投放客户', key: 'customerName', align: 'center', ellipsis: true },
               { title: '广告合同', key: 'contractName', align: 'center', ellipsis: true },
               { 
                   title: '广告状态', 
                   width: 100,
                   key: 'advertisementStatusName', 
                   align: 'center'
               },
               { 
                    title: '总价', 
                    key: 'totalCost', 
                    align: 'center',
                    width: 120,
                    render:(h, params)=>{
                        var text = this.$format.toKeepPoint(params.row.totalCost);
                        return h('span', text);
                    }
                },
               { title: '投放周期', key: 'period', align: 'center', ellipsis: true },
               { 
                   title: '开始时间', 
                   key: 'startTime', 
                   align: 'center', 
                   width: 170
               },
               { title: '维护人', key: 'owner', align: 'center', ellipsis: true },
               {
                  
                   title: '操作',
                   key: 'action',
                   width: 260,
                   align: 'center',
                   render: (h, params) => {
                       return this.getTableButtonByStatus(h, params.row)
                   }
               }
            ]
        }
    },
    methods: {
        getTableButtonByStatus(h, row){
            var show = h(tyIconTextButton, {
                props: {
                    text: '查看',
                    iconClass: 'icon-chakan'
                },
                nativeOn: {
                    click: () => {
                        this.dblclickEvent(row);
                    }
                }
            });
            var submitAudit = h(tyIconTextButton, {
                props: {
                    text: '提交审核',
                    iconClass: 'icon-tijiaoshenhe'
                },
                nativeOn: {
                    click: () => {
                       this.submitAudit(row)
                    }
                }
            });
            var audit = h(tyIconTextButton, {
                props: {
                    text: '审核',
                    iconClass: 'icon-audit'
                },
                nativeOn: {
                    click: () => {
                        this.openOptionModal(row, adState.OptionStatus.audit);
                    }
                }
            });
            var xiajia = h(tyIconTextButton, {
                props: {
                    text: '下架',
                    iconClass: 'icon-soldout'
                },
                nativeOn: {
                    click: () => {
                        this.openOptionModal(row,adState.OptionStatus.over);
                    }
                }
            });
            var edit = h(tyIconTextButton, {
                props: {
                    text: '编辑',
                    iconClass: 'icon-bianji'
                },
                nativeOn: {
                    click: () => {
                        this.gotoEdit(row);
                    }
                }
            });
            var put = h(tyIconTextButton, {
                props: {
                    text: '投放',
                    iconClass: 'icon-toufangpingtai'
                },
                nativeOn: {
                    click: () => {
                       this.put(row)
                    }
                }
            });
            var del = h(tyIconTextButton, {
                props: {
                    text: '删除',
                    iconClass: 'icon-laji'
                },
                nativeOn: {
                    click: () => {
                        this.deleteConfirm(row);
                    }
                }
            });
            var btns = [];
            var editFlag = this.$store.state.check(this.$m.adManage, this.$p.u);
            var auditFlag = this.$store.state.check(this.$m.adManage, this.$p.audit);
            // 不同状态的合同，显示不同的操作按钮
            switch (row.advertisementStatus) {
                // 待提交
                case this.ADState.pendingCommit:
                    btns = [submitAudit, edit, del];
                    break;
                // 待审核
                case this.ADState.pendingAuditing:
                    btns = [show];
                    editFlag && btns.push(edit);
                    auditFlag && btns.push(audit);
                    break;
                // 待投放
                case this.ADState.pendingDelivery:
                    btns = [show, put, edit];
                    break;
                // 投放中
                case this.ADState.deliverying:
                    btns = [show, xiajia];
                    break;
                // 已下架
                case this.ADState.abruptlyTerminated:
                    btns = [show];
                    break;
                // 已完成
                case this.ADState.finished:
                    btns = [show];
                    break;
            }
            return h('div', {
                class: {
                    optionBar: true
                }
            }, btns);
        },
        clearSearchKeyWord(){
            this.params.advertisementName = ''
            this.params.contractName = ''
            this.params.customerName = ''
        },
      
        // 双击触发的事件 查看事件
        dblclickEvent(row) {
            this.$router.push({ name: 'advierInfo', query: { ADid: row.id } });
        },
        openOptionModal(row, mode) {
            this.$refs.auditModal.setParams(row.id, mode)
            this.$refs.auditModal.toggle();
        },
        operate(option,fn){
            var options={
                title:option.title||"提示",
                content:option.content||"",
                desc:option.desc||"操作成功",
                params:option.params,
            }
            this.$Modal.confirm({
                title: options.title,
                content:options.content,
                onOk: () => {
                    this.$post(this.$api.adsOperate, options.params).then((result) => {
                        this.$Notice.success({
                            title: '提示',
                            desc: options.desc
                        })
                        //判断是否有表格要刷新
                        if(this.tableRef){
                            this.$refs[this.tableRef].refresh()
                        //判断是否数量更新
                        if(this.statusToTal){
                            this.getAdListTotal()
                        }
                        }
                        if(fn){
                            fn(result.data)
                        }
                    }).catch((e) => {
                        e.message = e.message || '操作失败，请稍后再试！';
                        this.$Notice.error({
                            title: '错误',
                            desc: e.message
                        });
                    });
                },
            });
        },
        put(row,fn){
            this.operate({
                content:'确认要投放广告"'+row.advertisementName+'"？',
                params:{
                    advertisementId : row.id,
                    finishContract:false,
                    operation:adState.OptionStatus.put,
                    remark:'',
                    successed:true
                }
            },fn);
        },
        submitAudit(row,fn){
            this.operate({
                content:'确认要提交审核？',
                params:{
                    advertisementId : row.id,
                    finishContract:true,
                    operation:adState.OptionStatus.submit,
                    remark:'',
                    successed:true
                }
            },data=>{
             this.$router.push({
                name: "adsStatus", 
                query:{
                    type:"submit",
                    id:row.id
                }
             })  
            });
        },
        // 跳转到编辑页面
        gotoEdit(row) {
            this.$router.push({
                name: 'editPutAds',
                query: {
                    contractId: row.contractId,
                    customerId: row.customerId,
                    id: row.id,
                }
            })
        }, 
        // 删除列表
        deleteConfirm(row, fn) {
                    this.$Modal.confirm({
                        title: "提示",
                        content:'确认删除"'+row.advertisementName+'"广告？',
                        onOk: () => {
                            this.$post(this.$api.deletePutAds, {},{},{id:row.id}).then((result) => {
                                this.$Notice.success({
                                    title: '提示',
                                    desc: "删除成功"
                                })
                              //判断是否有表格要刷新
                         if(this.tableRef){
                            this.$refs[this.tableRef].refresh()
                        //判断是否数量更新
                         if(this.statusToTal){
                            this.getAdListTotal()
                         }
                         }
                         fn && fn()
                            }).catch((e) => {
                                e.message = e.message || '操作失败，请稍后再试！';
                                this.$Notice.error({
                                    title: '错误',
                                    desc: e.message
                                });
                            });
                        },
                        onCancel: () => {
                        }
                    });
                },     
    }
    
}