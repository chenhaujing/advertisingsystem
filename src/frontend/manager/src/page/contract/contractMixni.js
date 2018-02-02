import ContractState from './contractState';
export default {
    methods: {
        preView(id) {
            this.$loading("请稍后...");
            this.$post(this.$api.previewContractUrl, {}, {}, {
                contractId: id
            }).then((result) => {
                this.$loading.close();
                window.open(result.data);
            }).catch((e) => {
                this.$loading.close();
                this.$Notice.error({
                    title: '错误',
                    desc: e.message
                })
            })
        },
        gotoEdit(id) {
            this.$router.push({
                name: 'editContract',
                query: {
                    contractId: id
                }
            })
        },
        // 获取合同的统计数据
        getContractTotalData() {
            return new Promise((resolve, reject) => {
                this.$post(this.$api.getContractTotalData).then((result) => {
                    resolve(result.data);
                }).catch((e) => {
                    this.$Notice.error({
                        title: '错误',
                        desc: e.message
                    });
                    reject(e);
                })
            })
        },
        deleteControll(row) {
            return new Promise((resolve, reject) => {
                this.$Modal.confirm({
                    title: '删除提示',
                    content: '<p>确定删除合同 "' + row.contractName + '" ？</p>',
                    onOk: () => {
                        this.$post(this.$api.deleteContract, {}, {}, {
                            id: row.id
                        }).then((result) => {
                            resolve(result);
                        }).catch((e) => {
                            this.$Notice.error({
                                title: '错误',
                                desc: e.message
                            });
                        });
                    }
                });
            })
        },
        submitAudit(id, isGetTotalData) {
            return new Promise((resolve, reject) => {
                this.$Modal.confirm({
                    title: '提示',
                    loading: true,
                    content: '<p>确定将该合同提交审核吗？</p>',
                    onOk: () => {
                        this.$post(this.$api.optionContranctUrl, {
                            "contractId": id,
                            "operation": ContractState.OptionStatus.submit,
                            "successed": true,
                        }).then(() => {
                            if (isGetTotalData) {
                                this.getContractTotalData().then((result) => {
                                    resolve(result);
                                    this.$Notice.success({
                                        title: '提示',
                                        desc: '已提交审核'
                                    })
                                    // this.refresh();
                                    this.$router.push({
                                        name: 'contracatState', query: {
                                            id: id,
                                            type: 'submit'
                                        }
                                    });
                                    this.$Modal.remove();
                                }).catch((e) => {
                                    this.$Notice.error({
                                        title: '错误',
                                        desc: e.message
                                    });
                                    this.$Modal.remove();
                                    reject(e);
                                })
                            } else {
                                this.$Notice.success({
                                    title: '提示',
                                    desc: '已提交审核'
                                })
                                // this.refresh();
                                this.$router.push({
                                    name: 'contracatState', query: {
                                        id: id,
                                        type: 'submit'
                                    }
                                });
                                this.$Modal.remove();
                                resolve();

                            }

                        }).catch((e) => {
                            this.$Notice.error({
                                title: '错误',
                                desc: e.message
                            });
                            this.$Modal.remove();
                            reject(e);
                        })
                    },
                    onCancel: (e) => {
                    }
                });
            })

        }
    }
}