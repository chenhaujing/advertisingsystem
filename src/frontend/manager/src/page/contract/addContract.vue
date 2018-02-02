<template>
    <div class="addContract">
        <div class="content" v-show="!loading">
            <div class="header">
                <div class="header-btns">
                    <a href="javaScript:void(0)" class="header-btns-cancel" @click="$router.go(-1)">取消</a>
                    <iButton href="" class="header-btns-save header-btns-submitAudit" :loading="submitAuditIng" @click.native="submitAudit" v-if="formValidate.contractStatus == constractStatus.pendingCommit">提交审核</iButton>
                    <!-- <iButton href="" class="header-btns-save header-btns-submitAudit" :loading="auditIng" @click.native="audit" v-if="$store.state.check($m.contract,$p.audit) && formValidate.contractStatus == constractStatus.pendingAuditing">审核</iButton> -->
                    <iButton href="" class="header-btns-save" :loading="saveing" @click.native="create">保存</iButton>

                    <a @click="preView" href="javaScript:void(0)" class="header-btns-preview iconfont" :class="{'notPreView': formValidate.id == null}">
                        预览
                    </a>
                </div>
                <div class="header-contractTitle">合同名称</div>
                <input v-model="formValidate.contractName" type="text" class="header-contractName" placeholder="请输入广告合同名称"></input>
                <div class="cut"></div>
                <div class="header-contractNo">[合同编号:{{formValidate.contractCode}}]</div>
            </div>
            <iForm :rules="rule" :model="formValidate" ref="formContract">
                <!-- 合同基础信息 -->
                <div class="baseInfo">
                    <div class="common-title">
                        基本信息
                    </div>
                    <div class="baseInfo-Item">
                        <div class="baseInfo-form">
                            <iFormitem label="甲方(广告客户)" prop="firstPartyName" class="baseInfo-formItem">
                                <iInput v-model.trim="formValidate.firstPartyName" placeholder="请输入甲方(广告客户)"></iInput>
                            </iFormitem>
                            <iFormitem label="责任联系人" prop="firstPartyResponsibilityPerson" class="baseInfo-formItem">
                                <iInput v-model.trim="formValidate.firstPartyResponsibilityPerson" placeholder="请输入责任联系人"></iInput>
                            </iFormitem>
                            <iFormitem label="联系电话" prop="firstPartyPhone" class="baseInfo-formItem">
                                <iInput v-model.trim="formValidate.firstPartyPhone" placeholder="请输入联系电话"></iInput>
                            </iFormitem>
                            <iFormitem label="指定送达地址" prop="firstPartyContractReceiveAddress" class="baseInfo-formItem">
                                <iInput v-model.trim="formValidate.firstPartyContractReceiveAddress" placeholder="请输入指定送达地址"></iInput>
                            </iFormitem>
                            <iFormitem label="邮箱" prop="firstPartyEmail" class="baseInfo-formItem">
                                <iInput v-model.trim="formValidate.firstPartyEmail" placeholder="请输入邮箱"></iInput>
                            </iFormitem>
                        </div>
                    </div>
                    <div class="baseInfoCut"></div>
                    <div class="baseInfo-Item paddingLeft">
                        <div class="baseInfo-form">
                            <iFormitem label="乙方" prop="secondPartyName" class="baseInfo-formItem">
                                <iInput v-model.trim="formValidate.secondPartyName" placeholder="请输入乙方"></iInput>
                            </iFormitem>
                            <iFormitem label="责任联系人" prop="secondPartyResponsibilityPerson" class="baseInfo-formItem">
                                <iInput v-model.trim="formValidate.secondPartyResponsibilityPerson" placeholder="请输入责任联系人"></iInput>
                            </iFormitem>
                            <iFormitem label="联系电话" prop="secondPartyPhone" class="baseInfo-formItem">
                                <iInput v-model.trim="formValidate.secondPartyPhone" placeholder="请输入联系电话"></iInput>
                            </iFormitem>
                            <iFormitem label="指定送达地址" prop="secondPartyContractReceiveAddress" class="baseInfo-formItem">
                                <iInput v-model.trim="formValidate.secondPartyContractReceiveAddress" placeholder="请输入指定送达地址"></iInput>
                            </iFormitem>
                            <iFormitem label="邮箱" prop="secondPartyEmail" class="baseInfo-formItem">
                                <iInput v-model.trim="formValidate.secondPartyEmail" placeholder="请输入邮箱"></iInput>
                            </iFormitem>
                        </div>
                    </div>
                </div>
                <!-- 协议期限 -->
                <div class="adsTime">
                    <div class="common-title">协议期限</div>
                    <div class="adsTime-form">
                        <iFormitem label="合同周期（月）" prop="totalMonths" class="floatLeft adsTime-formItem">
                            <iInput v-model="formValidate.totalMonths" placeholder="投放网点总数" disabled></iInput>
                        </iFormitem>
                        <iFormitem label="开始时间" prop="startTimeObj" class="floatLeft adsTime-formItem">
                            <!-- <iInput v-model="formValidate.name" placeholder="请输入姓名"></iInput> -->
                            <iDatePicker :editable="false" @on-change="timeChange('endTimeObj')" type="date" placeholder="选择日期" v-model="formValidate.startTimeObj"></iDatePicker>
                        </iFormitem>
                        <iFormitem label="结束时间" prop="endTimeObj" class="floatLeft adsTime-formItem">
                            <iDatePicker :editable="false" @on-change="timeChange('startTimeObj')" type="date" placeholder="选择日期" v-model="formValidate.endTimeObj"></iDatePicker>
                        </iFormitem>
                    </div>
                </div>
                <!-- 投放配置 -->
                <div class="deliveryOption">
                    <div class="common-title">投放配置</div>
                    <div class="adsTime-form">
                        <iFormitem label="投放位置" prop="advertisementSizeConfigId" class="floatLeft adsTime-formItem">
                            <iSelect v-model="formValidate.advertisementSizeConfigId"
                            placeholder="请选择投放位置" >
                                <iOption v-for="item in devPosition" :key="item.id" :value="item.id">
                                    {{item.sizeName}}
                                </iOption>
                            </iSelect>
                        </iFormitem>
                        <iFormitem label="广告时长" prop="advertisementDurationConfigId" class="floatLeft adsTime-formItem">
                            <iSelect 
                            v-model="formValidate.advertisementDurationConfigId" 
                            placeholder="请选择广告时长" >
                                <iOption v-for="item in durationConfig" :key="item.id" :value="item.id">
                                    {{item.duration+item.unitName+'/次'}}
                                </iOption>
                            </iSelect>
                        </iFormitem>
                        <iFormitem label="展示次数" prop="advertisementDisplayTimesConfigId" class="floatLeft adsTime-formItem">
                            <iSelect
                            v-model="formValidate.advertisementDisplayTimesConfigId" 
                            placeholder="请选择展示次数" >
                                <iOption v-for="item in adTimesConfig" :key="item.id" :value="item.id">
                                    {{item.displayTimes+'次/'+item.timeUnitName }}
                                </iOption>
                            </iSelect>
                        </iFormitem>
                    </div>
                </div>
                <!-- 广告位置 -->
                <div class="adsPosition">
                    <div class="common-title">广告位置</div>
                    <div class="adsPostion-main">
                        <div class="adsPosition-form">
                            <iFormitem label="投放网点总数" prop="name" class="floatLeft adsPosition-formItem">
                                <iInput :number="true" v-model="formValidate.storeCount" placeholder="投放网点总数" disabled></iInput>
                            </iFormitem>
                            <i class="iconfont icon-dengyu floatLeft"></i>
                            <iFormitem label="A类门店" prop="name" class="floatLeft adsPosition-formItem">
                                <iInput :number="true" v-model="formValidate.storeACount" placeholder="请输入A类门店数量" @on-keyup="inputChangeEventByStore" @on-change="computeStoreCount"></iInput>
                            </iFormitem>
                            <i class="iconfont icon-jia floatLeft"></i>
                            <iFormitem label="B类门店" prop="name" class="floatLeft adsPosition-formItem">
                                <iInput :number="true" v-model="formValidate.storeBCount" placeholder="请输入B类门店数量" @on-keyup="inputChangeEventByStore" @on-change="computeStoreCount"></iInput>
                            </iFormitem>
                            <i class="iconfont icon-jia floatLeft"></i>
                            <iFormitem label="C类门店" prop="name" class="floatLeft adsPosition-formItem">
                                <iInput :number="true" v-model="formValidate.storeCCount" placeholder="请输入C类门店数量" @on-keyup="inputChangeEventByStore" @on-change="computeStoreCount"></iInput>
                            </iFormitem>
                        </div>

                    </div>
                    <tyDivider title="投放广告网点" class="divider"></tyDivider>
                    <iRow :gutter="25" class="adsPostion-btns">
                        <iCol :span="8">
                            <div>
                                <tyAddConfigButton ref="configABtn" text="配置A类网点" class="configBtn btnConfigA selectStoreBtnBg" @click.native="configStore(1)"></tyAddConfigButton>
                                <div class="lackBox" v-if="formValidate.storeACountChecked!=0 && formValidate.storeACountChecked < formValidate.storeACount">[缺少{{formValidate.waitSelectStoreCountByA}}个]</div>
                            </div>
                        </iCol>
                        <iCol :span="8">
                            <div>
                                <tyAddConfigButton ref="configBBtn" text="配置B类网点" class="configBtn btnConfigB selectStoreBtnBg" @click.native="configStore(2)"></tyAddConfigButton>
                                <div class="lackBox" v-if="formValidate.storeBCountChecked!=0 && formValidate.storeBCountChecked < formValidate.storeBCount">[缺少{{formValidate.waitSelectStoreCountByB}}个]</div>
                            </div>
                        </iCol>
                        <iCol :span="8">
                            <div>
                                <tyAddConfigButton ref="configCBtn" text="配置C类网点" class="configBtn btnConfigC selectStoreBtnBg" @click.native="configStore(3)"></tyAddConfigButton>
                                <div class="lackBox" v-if="formValidate.storeCCountChecked!=0 && formValidate.storeCCountChecked < formValidate.storeCCount">[缺少{{formValidate.waitSelectStoreCountByC}}个]</div>
                            </div>
                        </iCol>
                    </iRow>
                </div>
                <!-- 广告费用 -->
                <div class="adsExpenses">
                    <div class="common-title">广告费用</div>
                    <div class="adsExpenses-form">
                        <iFormitem label="广告总额度" prop="adsTotalPrice" class="floatLeft adsExpenses-formItem">
                            <iInput v-model="totalPrice" disabled></iInput>
                        </iFormitem>
                        <i class="iconfont icon-dengyu floatLeft iconOption"></i>
                        <i class="iconfont icon-kuohaozuo floatLeft iconOption"></i>
                        <iFormitem label="媒体费用" prop="mediumCost" class="floatLeft adsExpenses-formItem">
                            <iInput @on-keyup="adsPrickChangeEvent('mediumCost')" placeholder="请输入媒体费用" v-model="formValidate.mediumCost"></iInput>
                        </iFormitem>
                        <i class="iconfont icon-jia floatLeft iconOption"></i>
                        <iFormitem label="制作费用" prop="productCost" class="floatLeft adsExpenses-formItem">
                            <iInput placeholder="请输入制作费用" @on-keyup="adsPrickChangeEvent('productCost')" v-model="formValidate.productCost"></iInput>
                        </iFormitem>
                        <i class="iconfont icon-kuohaoyou floatLeft iconOption"></i>
                        <i class="iconfont icon-asterisk floatLeft iconOption"></i>
                        <iFormitem label="广告折扣" prop="discount" class="floatLeft adsExpenses-formItem">
                            <iInput placeholder="请输入广告折扣" @on-keyup="adsPrickChangeEvent('discount')" v-model="formValidate.discount"></iInput>
                        </iFormitem>
                    </div>
                    <div class="adsExpenses-bottom">
                        <div class="adsExpenses-bottom-item">
                            <div class="adsExpenses-bottom-item-title">前述费用支付日期（签约后第几天）</div>
                            <div class="adsExpenses-bottom-item-dayInput">
                                <div>第</div>
                                <iInput :maxlength="2" @on-keyup="keyupFormNumberEvent(false,'signAfterDay')" class="adsExpenses-bottom-item-input" v-model="formValidate.signAfterDay"></iInput>
                                <div>天</div>
                            </div>
                        </div>
                        <div class="adsExpenses-bottom-item adsExpenses-bottom-item-warn">
                            <div class="iconfont icon-jinggao"></div>
                            <div>
                                <p>前述费用支付日期 -- 是指甲乙双方合同约定，协议签署后，第X天缴纳相</p>
                                <p>应的费用，每日逾期违约金为未付金额的0.3%</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 收款方式 -->
                <div class="adsCollection">
                    <div class="common-title">收款方式</div>
                    <iRow class="adsCollectionMain">
                        <iCol :span="8">
                            <tyFields title="乙方户名：" :value="formValidate.bankAccountName"></tyFields>
                        </iCol>
                        <iCol :span="8">
                            <tyFields title="乙方账号：" :value="formValidate.bankAccountNumber"></tyFields>
                        </iCol>
                        <iCol :span="8">
                            <tyFields title="乙方开户行：" :value="formValidate.bankName"></tyFields>
                        </iCol>
                    </iRow>
                </div>
                <!-- 收款方式 -->
                <div class="adsContractRemake">
                    <div class="common-title adsContractRemake-title">合同备注</div>
                    <iInput v-model.trim="formValidate.remark" type="textarea" :autosize="{minRows: 6,maxRows: 6}" placeholder="请输入..."></iInput>
                </div>
            </iForm>
        </div>
        <AuditModal ref="auditModal" @optionSuccess="auditResult"></AuditModal>
        <SelectStoreModal ref="selectStoreModal" @closeModal="closeModal"></SelectStoreModal>
        <iSpin size="large" fix v-show="loading" class="mainContent"></iSpin>
    </div>
</template>

<script>
import AuditModal from './auditModal';
import tyFields from 'components/tyFields';
import { Row as iRow, Col as iCol } from 'iview/src/components/grid';
import { Select as iSelect, Option as iOption }  from 'iview/src/components/select';
import tyAddConfigButton from './adsConfigBtn';
import tyDivider from 'components/tyDivider';
import iInput from 'iview/src/components/input';
import iForm from 'iview/src/components/form';
import tyIconText from 'components/tyIconText';
import iDatePicker from 'iview/src/components/date-picker';
import SelectStoreModal from './selectStoreModal';
import iSpin from 'iview/src/components/spin';
import iButton from 'iview/src/components/button';
import ContractState from './contractState';
export default {
    computed: {
        totalPrice() {
            let mediumCost = 0;
            let productCost = 0;
            let discount = 1;
            if (!this.formValidate.mediumCost != null && !isNaN(this.formValidate.mediumCost)) {
                mediumCost = this.formValidate.mediumCost;
            }
            if (this.formValidate.productCost != null && !isNaN(this.formValidate.productCost)) {
                productCost = this.formValidate.productCost;
            }
            if(this.formValidate.discount != null && !isNaN(this.formValidate.discount) && this.formValidate.discount!=0){
                discount = this.formValidate.discount
            }
            
            let price = this.numKeep2((parseFloat(mediumCost) + parseFloat(productCost))*discount);
            this.formValidate.adsTotalPrice = price 
            return this.formValidate.adsTotalPrice;
        }
    },
    mounted() {
        this.$get(this.$api.hasCompletedPriceConfigUrl).then((result) => {
            this.hasCompletedPriceConfig = result.data;
        }).catch((e) => {
            this.$Notice.error({
                title: '错误',
                desc: e.message
            })
        });
        if (this.$route.query.contractId) {
            // 编辑合同信息
            // 获取合同详情
            this.$get(this.$api.getSaveDataContactUrl, {
                id: this.$route.query.contractId
            }).then((result) => {
                this.formValidate = result.data;
                this.tempStoreCountA = this.formValidate.storeACount;
                this.tempStoreCountB = this.formValidate.storeBCount;
                this.tempStoreCountC = this.formValidate.storeCCount;
                if(this.formValidate.discount<1){
                    this.formValidate.discount = this.$format.toKeepPoint(this.formValidate.discount)
                }
                this.afterGetInfo();
                this.loading = false;
                this.updateBtnState();
            }).catch((e) => {
                this.$Notice.error({
                    title: '提示',
                    desc: e.message
                });
                this.loading = false;
                this.$router.go(-1);
            })
        } else {
            // 新增合同
            if (!this.$route.query.clientId) {
                this.$Message.error('客户信息不存在');
                return;
            }
            // this.formValidate.firstPartyName = this.$route.query.data.name;
            this.formValidate.customerId = this.$route.query.clientId;
            // 获取合同编号。
            this.$postForm(this.$api.getContractNoUrl, {
                codeType: 1
            }).then((result) => {
                this.formValidate.contractCode = result.data;
                // 获取收款信息
                this.$post(this.$api.getContractReceivablesInfoUrl).then((result) => {
                    if (result.data) {
                        this.formValidate.bankName = result.data.bank;
                        this.formValidate.bankAccountNumber = result.data.bankAccount;
                        this.formValidate.bankAccountName = result.data.name;
                    }
                    this.loading = false;
                }).catch(() => {
                    this.loading = false;
                })
            }).catch((e) => {
                this.$Notice.error({
                    title: '提示',
                    desc: e.message
                })
                this.loading = false;
            })
        }
        // 广告展示次数配置
        let params =  {
            "pageIndex": 0,
            "pageSize": 999
        }
        this.$post(this.$api.getDisplayTimesConfigUrl, params)
        .then((result) =>{
            this.adTimesConfig =  result.data.list;
            if(this.formValidate.id){
            }else{
                this.formValidate.advertisementDisplayTimesConfigId = result.data.list[0].id
            }
        })
        // 广告时长次数配置
        this.$post(this.$api.getDurationConfigUrl, params)
        .then((result) =>{
            this.durationConfig =  result.data.list;
             if(this.formValidate.id){
            }else{
                this.formValidate.advertisementDurationConfigId = result.data.list[0].id
            }
        })
        // 广告投放位置
        this.$post(this.$api.getSizeConfigUrl, params)
        .then((result) =>{
            this.devPosition =  result.data.list;
            if(this.formValidate.id){
            }else{
                this.formValidate.advertisementSizeConfigId = result.data.list[0].id
            }
        })
        
    },
    components: {
        AuditModal,
        iSpin,
        SelectStoreModal,
        iDatePicker,
        iInput,
        iForm,
        tyFields,
        tyIconText,
        iRow,
        iCol,
        iSelect,
		iOption,
        iButton,
        tyAddConfigButton,
        'iFormitem': iForm.Item,
        tyDivider
    },
    methods: {
        auditResult() {
            this.$router.go(-1);
        },
        audit() {
            this.auditIng = true;
            this.createContract(true).then((e) => {
                this.auditIng = false;
                this.$refs.auditModal.setParams(this.formValidate.id, 1)
                this.$refs.auditModal.toggle();
            }).catch((e) => {
                this.auditIng = false;
            })

        },
        submitAudit() {
            this.submitAuditIng = true;
            this.createContract(true).then((e) => {
                this.submitAuditIng = false;
                this.$Modal.confirm({
                    title: '提示',
                    loading: true,
                    content: '<p>确定将该合同提交审核吗？</p>',
                    onOk: () => {
                        this.$post(this.$api.optionContranctUrl, {
                            "contractId": this.formValidate.id,
                            "operation": ContractState.OptionStatus.submit,
                            "successed": true,
                        }).then(() => {
                            // this.$Notice.success({
                            //     title: '提示',
                            //     desc: '已提交审核'
                            // })
                            this.$Modal.remove();
                            this.$router.push({
                                name: 'contracatState', query: {
                                    id: this.formValidate.id,
                                    type: 'submit'
                                }
                            });
                        }).catch((e) => {
                            this.$Notice.error({
                                title: '错误',
                                desc: e.message
                            });
                            this.$Modal.remove();
                        })
                    },
                    onCancel: (e) => {

                    }
                });
            }).catch((e) => {
                this.submitAuditIng = false;
            });
        },
        timeChange(key) {
            this.$nextTick(() => {
                this.$refs.formContract.validateField(key, () => { });
            })
        },
        computeStoreCount() {
            var aCount = parseInt(this.formValidate.storeACount);
            var bCount = parseInt(this.formValidate.storeBCount);
            var cCount = parseInt(this.formValidate.storeCCount);
            if (isNaN(aCount)) {
                aCount = 0;
            }
            if (isNaN(bCount)) {
                bCount = 0;
            }
            if (isNaN(cCount)) {
                cCount = 0;
            }
            // 计算出门店总量
            var tempStoreCount = aCount + bCount + cCount;
            this.$set(this.formValidate, 'storeCount', tempStoreCount);
            // 计算出缺省数量
            this.formValidate.waitSelectStoreCountByA = this.formValidate.storeACount - this.formValidate.storeACountChecked;
            this.formValidate.waitSelectStoreCountByB = this.formValidate.storeBCount - this.formValidate.storeBCountChecked;
            this.formValidate.waitSelectStoreCountByC = this.formValidate.storeCCount - this.formValidate.storeCCountChecked;
            this.updateBtnState();
        },
        inputChangeEventByStore() {
            if (this.formValidate.storeACount) {
                this.formValidate.storeACount = this.formValidate.storeACount.toString().replace(/[^\d]/g, '')
            } else {
                this.formValidate.storeACount = 0;
            }
            if (this.formValidate.storeBCount) {
                this.formValidate.storeBCount = this.formValidate.storeBCount.toString().replace(/[^\d]/g, '')
            } else {
                this.formValidate.storeBCount = 0;
            }
            if (this.formValidate.storeCCount) {
                this.formValidate.storeCCount = this.formValidate.storeCCount.toString().replace(/[^\d]/g, '')
            } else {
                this.formValidate.storeCCount = 0;
            }
        },
        updateBtnState() {
            // 选择完毕
            console.log(this.formValidate.storeACountChecked + ' =A= ' + this.formValidate.storeACount)
            console.log(this.formValidate.storeBCountChecked + ' =B= ' + this.formValidate.storeBCount)
            console.log(this.formValidate.storeCCountChecked + ' =C= ' + this.formValidate.storeCCount)
            if (this.formValidate.storeACountChecked == this.formValidate.storeACount && this.formValidate.storeACount != 0) {
                this.$refs.configABtn.toggleFinishConfig();
            }
            if (this.formValidate.storeBCountChecked == this.formValidate.storeBCount && this.formValidate.storeBCount != 0) {
                this.$refs.configBBtn.toggleFinishConfig();
            }
            if (this.formValidate.storeCCountChecked == this.formValidate.storeCCount && this.formValidate.storeCCount != 0) {
                this.$refs.configCBtn.toggleFinishConfig();
            }
            if (this.formValidate.storeACountChecked != 0 && this.formValidate.storeACountChecked != this.formValidate.storeACount) {
                this.$refs.configABtn.toggleDoing();
            }
            if (this.formValidate.storeACountChecked == 0) {
                this.$refs.configABtn.toggleWaitConfig();
            }
            if (this.formValidate.storeBCountChecked != 0 && this.formValidate.storeBCountChecked != this.formValidate.storeBCount) {
                this.$refs.configBBtn.toggleDoing();
            }
            if (this.formValidate.storeBCountChecked == 0) {
                this.$refs.configBBtn.toggleWaitConfig();
            }
            if (this.formValidate.storeCCountChecked != 0 && this.formValidate.storeCCountChecked != this.formValidate.storeCCount) {
                this.$refs.configCBtn.toggleDoing();
            }
            if (this.formValidate.storeCCountChecked == 0) {
                this.$refs.configCBtn.toggleWaitConfig();
            }
        },
        closeModal(data) {
            switch (data.storeType) {
                case 1:
                    // 已选择的
                    this.formValidate.storeACountChecked = data.currSelect;
                    // 待选择的
                    this.formValidate.waitSelectStoreCountByA = data.waitSelect;
                    break;
                case 2:
                    this.formValidate.storeBCountChecked = data.currSelect;
                    this.formValidate.waitSelectStoreCountByB = data.waitSelect;
                    break;
                case 3:
                    this.formValidate.storeCCountChecked = data.currSelect;
                    this.formValidate.waitSelectStoreCountByC = data.waitSelect;
                    break;
            }
            this.$forceUpdate();
            this.updateBtnState();

        },
        adsChangeEvent() {
            let mediumCost = 0;
            let productCost = 0;
            if (!this.formValidate.mediumCost != null && !isNaN(this.formValidate.mediumCost)) {
                mediumCost = this.formValidate.mediumCost;
            }
            if (this.formValidate.productCost != null && !isNaN(this.formValidate.productCost)) {
                productCost = this.formValidate.productCost;
            }
            this.formValidate.adsTotalPrice = this.numKeep2(parseFloat(mediumCost) + parseFloat(productCost));
        },
        // 计算广告费用。
        adsPrickChangeEvent(obj) {
            this.formValidate[obj] = this.formValidate[obj].toString().trim();
            // 清除非数字和.字符
            this.formValidate[obj] = this.formValidate[obj].replace(/^\./g, "");
            // 当多个0开头，去掉多个0
            this.formValidate[obj] = this.formValidate[obj].replace(/[^\d.]/g, "");
            // 限制只能输入两位小数。
            this.formValidate[obj] = this.formValidate[obj].replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
            this.formValidate[obj] = this.formValidate[obj].replace(/^[0]{2,}/g, "0");
            if (!this.formValidate.mediumCost || this.formValidate.mediumCost.toString().trim().length == 0) {
                this.formValidate.mediumCost = null;
            }
            if (!this.formValidate.productCost || this.formValidate.productCost.toString().trim().length == 0) {
                this.formValidate.productCost = null;
            }
            if (!this.formValidate.discount || this.formValidate.discount.toString().trim().length == 0) {
                this.formValidate.discount = null;
            }
            // this.adsChangeEvent();
        },
        // 在拉取合同信息之后，对数据进行处理
        afterGetInfo() {
            // Date转化
            this.formValidate.startTimeObj = new Date(Date.parse(this.formValidate.startTime));
            this.formValidate.endTimeObj = new Date(Date.parse(this.formValidate.endTime));
            // 计算出待选择的门店数量
            var tempWaitA = this.formValidate.storeACount - this.formValidate.storeACountChecked;
            var tempWaitB = this.formValidate.storeBCount - this.formValidate.storeBCountChecked;
            var tempWaitC = this.formValidate.storeCCount - this.formValidate.storeCCountChecked;

            // 计算出门店总量
            this.formValidate.storeCount = this.formValidate.storeACount
                + this.formValidate.storeBCount
                + this.formValidate.storeCCount;

            // 计算出广告总额
            this.formValidate.adsTotalPrice = this.numKeep2(parseFloat(this.formValidate.mediumCost) + parseFloat(this.formValidate.productCost));
            if (tempWaitA > 0) {
                this.formValidate.waitSelectStoreCountByA = tempWaitA;
            }
            if (tempWaitB > 0) {
                this.formValidate.waitSelectStoreCountByB = tempWaitB;
            }
            if (tempWaitC > 0) {
                this.formValidate.waitSelectStoreCountByC = tempWaitC;
            }

            if (this.formValidate.storeACountChecked != 0 && this.formValidate.storeACountChecked != this.formValidate.storeACountChecked.storeACount) {
                this.$refs.configABtn.toggleDoing();
            }
            if (this.formValidate.storeBCountChecked != 0 && this.formValidate.storeBCountChecked != this.formValidate.storeBCountChecked.storeBCount) {
                this.$refs.configBBtn.toggleDoing();
            }
            if (this.formValidate.storeCCountChecked != 0 && this.formValidate.storeCCountChecked != this.formValidate.storeCCountChecked.storeCCount) {
                this.$refs.configCBtn.toggleDoing();
            }
        },

        keyupFormNumberEvent(flag, obj) {
            if (!this.formValidate[obj]) {
                return;
            }
            if (flag) {
                // 包含小数点
                this.formValidate[obj] = this.formValidate[obj].replace(/[^\d,.]/g, '')
            } else {
                this.formValidate[obj] = this.formValidate[obj].replace(/[^\d]/g, '')
            }

        },
        numKeep2(num) {
            if (isNaN(num)) {
                return 0;
            }
            var strNum = num.toString();
            if (strNum.length - strNum.indexOf('.') < 2) {
                return num;
            }
            return num.toFixed(2);
        },
        // 合同预览
        preView() {
            if (this.formValidate.id == null) {
                return;
            }
            this.createContract().then((e) => {
                if (this.formValidate.id) {
                    this.$loading("请稍后...");
                    this.$post(this.$api.previewContractUrl, {}, {}, {
                        contractId: this.formValidate.id
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
                }
            }).catch((e) => {
                if (e) {
                    this.$Notice.warning({
                        title: '提交',
                        desc: e.message
                    })
                }
            });
        },
        // 提交前对表单数据进行格式化
        beforeSubmit() {
            this.formValidate.startTime = this.$dateFormat(this.formValidate.startTimeObj, 'yyyy-MM-dd');
            this.formValidate.endTime = this.$dateFormat(this.formValidate.endTimeObj, 'yyyy-MM-dd');
        },
        beforeCheck() {
            if (this.formValidate.contractName <= 0) {
                this.$Modal.remove();
                this.$Notice.error({
                    title: '错误',
                    desc: '合同名称不能为空'
                })
                this.$loading.close();
                return false;
            }
            if (this.formValidate.adsTotalPrice <= 0) {
                this.$Modal.remove();
                this.$Notice.error({
                    title: '错误',
                    desc: '广告费用总额不能低于0'
                })
                this.$loading.close();
                return false;
            }
            // if (this.formValidate.id) {
            //     // 需要判断门店是否选择完毕。
            //     if (this.formValidate.storeACountChecked != this.formValidate.storeACount) {
            //         this.$Notice.error({
            //             title: '错误',
            //             desc: 'A类门店选择数量有误，请修改'
            //         })
            //         this.$Modal.remove();
            //         return false;
            //     }
            //     // 需要判断门店是否选择完毕。
            //     if (this.formValidate.storeBCountChecked != this.formValidate.storeBCount) {
            //         this.$Notice.error({
            //             title: '错误',
            //             desc: 'B类门店选择数量有误，请修改'
            //         })
            //         this.$Modal.remove();
            //         return false;
            //     }
            //     // 需要判断门店是否选择完毕。
            //     if (this.formValidate.storeCCountChecked != this.formValidate.storeCCount) {
            //         this.$Notice.error({
            //             title: '错误',
            //             desc: 'C类门店选择数量有误，请修改'
            //         })
            //         this.$Modal.remove();
            //         return false;
            //     }
            // }
            return true;

        },
        createContract(isSaveloading) {
            return new Promise((resolve, reject) => {
                if (!this.beforeCheck()) {
                    this.$Modal.remove();
                    reject();
                    return;
                }
                this.$refs.formContract.validate((flag) => {
                    if (!flag) {
                        reject();
                        this.$Modal.remove();
                    } else {
                        if (!isSaveloading) {
                            this.saveing = true;
                        }
                        this.beforeSubmit();
                        var url = this.formValidate.id ? this.$api.updateContractUrl : this.$api.createdContractUrl;
                        if(!this.formValidate.discount) {
                            this.formValidate.discount = 1
                        } 
                        this.$post(url, this.formValidate).then((result) => {
                            this.$Modal.remove();
                            this.saveing = false;
                            resolve(result);
                        }).catch((e) => {
                            this.saveing = false;
                            this.$Modal.remove();
                            reject(e);
                        })
                    }
                });
            })

        },
        // 创建合同。
        create(event, flag) {
            this.createContract(true).then((result) => {
                this.$Notice.success({
                    title: '提示',
                    desc: '保存成功'
                });
                if (!this.formValidate.id) {
                    this.formValidate.contractStatus = this.constractStatus.pendingCommit;
                    this.formValidate.id = result.data;
                }
                if (!flag) {
                    this.$router.push({
                        name: 'contracatState', query: {
                            id: this.formValidate.id,
                            type: 'save'
                        }
                    });
                }

            }).catch((e) => {
                if (e) {
                    this.$Notice.warning({
                        title: '提交',
                        desc: e.message
                    })
                }

            })

        },
        // 计算合作周期
        updateContractMonth() {
            if (!this.formValidate.startTimeObj || !this.formValidate.startTimeObj instanceof Date) {
                return;
            }
            if (!this.formValidate.endTimeObj || !this.formValidate.endTimeObj instanceof Date) {
                return;
            }
            var endTimeObj = this.formValidate.endTimeObj;
            var startTimeObj = this.formValidate.startTimeObj;
            var startTimeObjMonth = startTimeObj.getFullYear() * 12 + startTimeObj.getMonth();
            var endTimeObjMonth = endTimeObj.getFullYear() * 12 + endTimeObj.getMonth();
            this.formValidate.totalMonths = endTimeObjMonth - startTimeObjMonth
        },
        configStore(storeType) {
            // 如果合同没有保存过
            if (!this.formValidate.id) {
                var flag = true;
                this.$refs.formContract.validate((f) => {
                    flag = f;
                });
                if (flag) {
                    flag = this.beforeCheck();
                }
                if (flag) {
                    switch (storeType) {
                        case 1:
                            if (this.formValidate.storeACount <= 0) {
                                this.$Notice.error({
                                    title: '错误',
                                    desc: '请输入A类门店数量'
                                })
                                return;
                            }
                            break;
                        case 2:
                            if (this.formValidate.storeBCount <= 0) {
                                this.$Notice.error({
                                    title: '错误',
                                    desc: '请输入B类门店数量'
                                })
                                return;
                            }
                            break;
                        case 3:
                            if (this.formValidate.storeCCount <= 0) {
                                this.$Notice.error({
                                    title: '错误',
                                    desc: '请输入C类门店数量'
                                })
                                return;
                            }
                            break;
                    }
                    if (!this.hasCompletedPriceConfig) {
                        this.$Notice.error({
                            title: '错误',
                            desc: '暂无门店类别价格配置，请联系系统管理员'
                        });
                        return;
                    }
                    this.$Modal.confirm({
                        title: '提示',
                        loading: true,
                        content: "<p class='dialogText'>请先保存合同记录，再配置广告位置数据</p><p class='dialogText'>是否保存合同记录？</p>",
                        onOk: () => {
                            this.create({}, true);
                        },
                        onCancel: (e) => {
                        }
                    });
                    return;
                } else {
                    return;
                }
            }
            if (!this.hasCompletedPriceConfig) {
                this.$Notice.error({
                    title: '错误',
                    desc: '暂无门店类别价格配置，请联系系统管理员'
                });
                return;
            }
            // 传递每个门店类型需要选择的数量
            var targetSelectCount = 0;
            // 当前已选择的数量
            var currSelectCount = 0;
            switch (storeType) {
                case 1:
                    if (this.formValidate.storeACount <= 0) {
                        this.$Notice.error({
                            title: '错误',
                            desc: '请输入A类门店数量'
                        })
                        return;
                    }
                    targetSelectCount = this.formValidate.storeACount;
                    currSelectCount = this.formValidate.storeACountChecked;
                    break;
                case 2:
                    if (this.formValidate.storeBCount <= 0) {
                        this.$Notice.error({
                            title: '错误',
                            desc: '请输入B类门店数量'
                        })
                        return;
                    }
                    targetSelectCount = this.formValidate.storeBCount;
                    currSelectCount = this.formValidate.storeBCountChecked;
                    break;
                case 3:
                    if (this.formValidate.storeCCount <= 0) {
                        this.$Notice.error({
                            title: '错误',
                            desc: '请输入C类门店数量'
                        })
                        return;
                    }
                    targetSelectCount = this.formValidate.storeCCount;
                    currSelectCount = this.formValidate.storeCCountChecked;
                    break;
            }
            this.$refs.selectStoreModal.setParams({
                areaIds: '0',
                storeType: storeType,
                contractId: this.formValidate.id
            }, targetSelectCount, currSelectCount)
            this.$refs.selectStoreModal.toggle();
        },
        ruleRequest(msg) {
            return {
                required: true,
                message: msg,
                trigger: 'blur'
            }
        }
    },
    data() {
        const numberVerify = (rule, value, callback) => {
            if (isNaN(value)) {
                callback(new Error('非法字符，请重新输入'))
            } else {
                callback();
            }
        }
        const lessthan = (rule, value, callback) => {
            var reg= /^(0\.(0[1-9]|[1-9]{1,2}|[1-9]0)$)|^1$/;
            var reg1 = /^0\.[0-9]{0,2}$/;
            if(!value || value=='1'){
                return callback();
            }
            
            if(value> 0 && value<1 && !reg1.test(value)){
                return callback(new Error('广告折扣只能精确小数点后2位'))
            }
            if(!reg.test(value)){
                callback(new Error('广告折扣值必须大于0或小于等于1'))
            }else{
                callback();
            }
        }
        const startTimeObjVerify = (rule, value, callback) => {
            if (!this.formValidate.startTimeObj || !this.formValidate.startTimeObj instanceof Date) {
                callback(new Error('请选择开始时间'));
                return;
            }
            if (this.formValidate.endTimeObj && this.formValidate.endTimeObj instanceof Date) {
                var endTimeObj = this.formValidate.endTimeObj.getTime();
                var startTimeObj = this.formValidate.startTimeObj.getTime();
                if (startTimeObj - endTimeObj >= 0) {
                    callback(new Error('开始时间必须小于结束时间'));
                    return;
                }
                var startYear = this.formValidate.startTimeObj.getUTCFullYear();
                var endYear = this.formValidate.endTimeObj.getUTCFullYear();
                var startTimelong = this.formValidate.startTimeObj.getDate();
                var endTimelong = this.formValidate.endTimeObj.getDate();
                var startMonth = this.formValidate.startTimeObj.getMonth();
                var endMonth = this.formValidate.endTimeObj.getMonth();

                if (startTimelong != endTimelong) {
                    // 非整数月,超出或小于给不同的提示
                    if (startYear != endYear) {
                        callback(new Error('广告周期只能为整数月，请调整广告周期时间'));
                        return;
                    }
                    if (startMonth < endMonth && endTimelong > startTimelong) {
                        callback(new Error('广告周期只能为整数月，请调整广告周期时间'));
                        return;
                    } else {
                        callback(new Error('广告周期不足一个月，请调整广告周期时间'));
                    }

                } else {
                    this.updateContractMonth();
                    callback();
                }
            }

        };
        const endTimeObjVerify = (rule, value, callback) => {
            if (!this.formValidate.endTimeObj || !this.formValidate.endTimeObj instanceof Date) {
                callback(new Error('请选择结束时间'));
                return;
            }
            if (this.formValidate.startTimeObj && this.formValidate.startTimeObj instanceof Date) {
                var endTimeObj = this.formValidate.endTimeObj.getTime();
                var startTimeObj = this.formValidate.startTimeObj.getTime();
                if (endTimeObj - startTimeObj <= 0) {
                    callback(new Error('结束时间必须大于开始时间'));
                }
                var startYear = this.formValidate.startTimeObj.getUTCFullYear();
                var endYear = this.formValidate.endTimeObj.getUTCFullYear();
                var startTimelong = this.formValidate.startTimeObj.getDate();
                var endTimelong = this.formValidate.endTimeObj.getDate();
                var startMonth = this.formValidate.startTimeObj.getMonth();
                var endMonth = this.formValidate.endTimeObj.getMonth();

                if (startTimelong != endTimelong) {
                    // 非整数月,超出或小于给不同的提示
                    if (startYear != endYear) {
                        callback(new Error('广告周期只能为整数月，请调整广告周期时间'));
                        return;
                    }
                    if (startMonth < endMonth && endTimelong > startTimelong) {
                        callback(new Error('广告周期只能为整数月，请调整广告周期时间'));
                        return;
                    } else {
                        callback(new Error('广告周期不足一个月，请调整广告周期时间'));
                    }

                } else {
                    this.updateContractMonth();
                    callback();

                }
            }
        };
        const adsTotalPriceVerify = (rule, value, callback) => {
            if (this.formValidate.adsTotalPrice <= 0) {
                callback(new Error('请输入广告费用'));
            } else {
                callback();
            }
        }
        return {
            // 广告次数
            adTimesConfig: [],
            // 广告时长
            durationConfig: [],
            // 广告位置
            devPosition: [],
            hasCompletedPriceConfig: false,
            constractStatus: ContractState.Status,
            submitAuditIng: false,
            auditIng: false,
            saveing: false,
            loading: true,
            formValidate: {
                contractStatus: 0,
                adsTotalPrice: 0,
                waitSelectStoreCountByA: 0, // A类门店待选数量
                waitSelectStoreCountByB: 0,
                waitSelectStoreCountByC: 0,
                bankAccountName: '',
                bankAccountNumber: '',
                bankName: '',
                contractCode: '',
                storeCount: '',
                firstPartyName: '',
                firstPartyPhone: '',
                firstPartyResponsibilityPerson: '',
                firstPartyContractReceiveAddress: '',
                firstPartyEmail: '',
                customerId: null,
                startTimeObj: null,
                endTimeObj: null,
                totalMonths: 0,
                storeACount: 0,
                storeBCount: 0,
                storeCCount: 0,
                remark: '',
                id: null,
                mediumCost: null,
                playDuration: 15,
                playTime: 80,
                productCost: null,
                // 广告折扣
                discount: null,
                signAfterDay: 0,
                secondPartyName: '',
                secondPartyPhone: '',
                secondPartyResponsibilityPerson: '',
                secondPartyContractReceiveAddress: '',
                secondPartyEmail: '',
                storeACountChecked: 0,
                storeBCountChecked: 0,
                storeCCountChecked: 0,
                // 广告时长配置id 
                advertisementDurationConfigId: '',
                //  广告展示次数配置
                advertisementDisplayTimesConfigId: '',
                // 广告尺寸配置id 
                advertisementSizeConfigId : '',
            },
            rule: {
                mediumCost: [
                    { validator: numberVerify, trigger: 'change' }
                ],
                productCost: [
                    { validator: numberVerify, trigger: 'change' }
                ],
                discount: [
                    { validator: lessthan, trigger: 'change' }
                ],
                startTimeObj: [
                    { validator: startTimeObjVerify, trigger: 'change' }
                ],
                endTimeObj: [
                    { validator: endTimeObjVerify, trigger: 'change' }
                ],
                firstPartyName: [
                    this.ruleRequest('请输入甲方名称')
                ],
                advertisementDurationConfigId: [
                    {  required: true, message: '请选择广告时长', trigger: 'change'  }
                ],
                advertisementDisplayTimesConfigId: [
                    {  required: true, message: '请选择展示次数', trigger: 'change'  }
                ],
                advertisementSizeConfigId: [
                     {  required: true, message: '请选择投放位置', trigger: 'change'  }
                ],
                firstPartyResponsibilityPerson: [
                    this.ruleRequest('请输入甲方责任联系人')
                ],
                firstPartyPhone: [
                    this.ruleRequest('请输入甲方电话信息')
                ],
                firstPartyContractReceiveAddress: [
                    this.ruleRequest('请输入指定送达地址')
                ],
                firstPartyEmail: [
                    this.ruleRequest('请输入甲方邮箱信息'),
                    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
                ],
                secondPartyName: [
                    this.ruleRequest('请输入乙方名称')
                ],
                secondPartyResponsibilityPerson: [
                    this.ruleRequest('请输入乙方责任联系人')
                ],
                secondPartyPhone: [
                    this.ruleRequest('请输入乙方电话信息')
                ],
                secondPartyContractReceiveAddress: [
                    this.ruleRequest('请输入指定乙方送达地址')
                ],
                secondPartyEmail: [
                    this.ruleRequest('请输入乙方邮箱信息'),
                    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
                ],
            }
        }
    }
}
</script>


<style lang="scss" scoped>
@import '~assets/css/base.scss';
%mt30 {
    margin-top: 30px;
}

%mt20 {
    margin-top: 20px;
}

%commonPadding {
    padding: 30px 50px;
}

%btnStyle {
    padding: 0;
    font-size: 16px;
    text-align: center;
    line-height: 34px;
    float: right;
    margin-right: 20px;
    display: block;
    color: #ffffff;
    width: 120px;
    height: 34px;
    border-radius: 5px;
}

.lackBox {
    padding: 5px;
    text-align: center;
    color: #f0857d;
    font-size: 14px;
}

.floatLeft {
    float: left;
}

.header-btns {
    width: 100%;
    float: right;
    height: 34px;
    overflow: hidden;
}

.header {
    height: 223px;
}

.header-btns-preview {
    float: right;
    font-size: 16px;
    color: #4cabe0;
    width: 100px;
    height: 34px;
    line-height: 34px;
    font-weight: bolder;
    &::before {
        line-height: 34px;
        font-size: 22px;
        content: '\e6d1'
    } // @extend %btnStyle;
    // background-color: #7edd9c;
}

.header-btns-save {
    @extend %btnStyle;
    background-color: $mainColor;
}

.header-btns-submitAudit {
    background-color: #7edd9c;
    &:hover {
        border-color: #7edd9c;
    }
}

.header-btns-cancel {
    @extend %btnStyle;
    background-color: #dcdee0;
}

.header-contractTitle {
    padding-top: 60px;
    text-align: center;
    font-size: 14px;
    color: #666666;
}

.header-contractName {
    outline: none;
    font-size: 24px;
    color: #333333;
    text-align: center;
    min-width: 60%;
    padding: 0;
    background-color: #f1f1f1;
    border: 0;
    display: block;
    margin: 0 auto;
    margin-top: 20px;
}

.cut {
    height: 1px;
    margin: 0 auto;
    background-color: #cccccc;
    width: 800px;
    margin-top: 20px;
}

.header-contractNo {
    height: 68px;
    line-height: 68px;
    text-align: center;
    color: #666666;
    font-size: 14px;
}

// 基础信息
.baseInfo {
    @extend %commonPadding;
    box-sizing: border-box;
    height: 544px;
    width: 100%;
    background-color: #ffffff;
    .baseInfoCut {
        height: 100%;
        float: left;
        width: 275px;
    }
}

.common-title {
    font-size: 18px;
    color: #999999;
}

.baseInfo-Item {
    // @extend %mt20;
    margin-top: 10px;
    background-color: #ffffff;
    float: left;
    width: calc(50% - 140px);
    .baseInfo-form {
        width: 100%;
        &>.baseInfo-formItem {
            margin-top: 10px;
        }
        &>.baseInfo-formItem:first-child {
            margin-top: 0;
        }
    }
}

// 协议期限
.adsTime {
    @extend %mt30;
    @extend %commonPadding;
    height: 200px;
    background-color: #ffffff;
}

.adsTime-formItem {
    width: calc(33% - 40px);
    margin-right: 60px;
}

.adsTime-formItem:last-child {
    margin-right: 0;
}

// 投放配置
.deliveryOption{
    @extend %commonPadding;
    @extend %mt30;
    background-color: #ffffff;
    height: 200px;
}
.adsTime-form{
    margin-top: 25px;
}

// 广告位置
.adsPosition {
    @extend %commonPadding;
    @extend %mt30;
    height: 367px;
    box-sizing: border-box;
    background-color: #ffffff;
}

.adsPostion-main {
    height: 70px;
    margin-top: 25px;
}

.adsPosition-form {

    .adsPosition-formItem {
        width: calc(25% - 50px);
    }
    .iconfont {
        text-align: center;
        width: 64px;
        height: 70px;
        position: relative;
    }
    .icon-dengyu:before,
    .icon-jia:before {
        position: absolute;
        bottom: 5px;
        font-weight: bolder;
        width: 20px;
        right: 50%;
        display: block;
        margin-right: -10px;
    }
}

.divider {
    clear: both;
    width: 423px;
}

.adsPostion-btns {
    box-sizing: border-box;
    padding-top: 20px;
}


// 广告费用
.adsExpenses {

    background-color: #ffffff;
    @extend %commonPadding;
    @extend %mt30;
    height: 280px;
    .adsExpenses-form {
        @extend %mt20;
        overflow: hidden;
    }
    .adsExpenses-formItem {
        width: calc(25% - 50px);
    }
    .iconOption {
        text-align: center;
        width: 64px;
        height: 70px;
        position: relative;
    }

    .icon-dengyu,
    .icon-kuohaozuo,
    .icon-kuohaoyou,
    .icon-asterisk{
        width: 40px;
    }

    .icon-kuohaozuo,
    .icon-asterisk{
        margin-left: -15px;
    }
    .icon-dengyu:before,
    .icon-kuohaozuo:before,
    .icon-kuohaoyou:before,
    .icon-asterisk:before,
    .icon-jia:before {
        position: absolute;
        bottom: 5px;
        font-weight: bolder;
        width: 20px;
        right: 50%;
        display: block;
        margin-right: -10px;
    }
    .adsExpenses-bottom {
        height: 90px;
        clear: both;
    }
    .adsExpenses-bottom-item {

        width: 40%;
        float: left;
        font-size: 14px;
        color: #666666;
        padding-top: 20px;
    }
    .adsExpenses-bottom-item-dayInput>* {
        float: left;
        height: 50px;
        line-height: 50px;
        margin-right: 10px;
    }
    .adsExpenses-bottom-item-input {
        width: 105px;
    }
    .adsExpenses-bottom-item-warn {
        width: 60%;
        float: right;
        @extend %mt20;
        .iconfont {
            line-height: 40px;
            margin-right: 20px;
            color: #fcb322;
        }
    }
    .adsExpenses-bottom-item-warn>div {
        float: left;
    }
}

// 收款方式
.adsCollection {
    background-color: #ffffff;
    @extend %commonPadding;
    @extend %mt30;
}

// 合同备注
.adsContractRemake {
    @extend %mt30;
    background-color: #ffffff;
    height: 160px;
    .adsContractRemake-title {
        padding-bottom: 10px;
        background-color: #f1f1f1;
    }
}

.dialogText {
    font-size: 16px;
}
</style>


<style lang="scss">
.addContract {

    .configBtn {
        border: 0;
    }
    .notPreView {
        color: #cccccc;
    }
    .cut {
        border-bottom: 1px solid #999;
    }
    .divider {
        background-color: initial;
        border-bottom: 0;
    }
    .ivu-date-picker {
        width: 100%;
    }
    .ivu-form .ivu-form-item-label {
        font-size: 16px!important;
        color: #666666!important;
    }
    .ivu-input {
        font-size: 14px;
        color: #333333;
    } // .btnConfigA {
    //     background-color: #6aa84f !important;
    // }
    // .btnConfigB {
    //     background-color: #ea9999 !important;
    // }
    // .btnConfigC {
    //     background-color: #dddddd !important;
    // }
    textarea {
        border: 0 !important;
        outline: none !important;
    } // .selectStoreBtnBg {
    //     background-color: #cccccc!important;
    // }
}
</style>