<template>
    <iInput v-model="inputData" @on-keyup="setInputType()"/>
</template>
<script>
import iInput from 'iview/src/components/input';
export default {
    name: "tyNumberInput",
    components:{
iInput
    },
    props: ['type', 'value'],
    data() {
        return {
            inputData: '',
        }
    },
    watch: {
        value(val) {
            this.inputData = val
        }
    },
    methods: {
        setInputType() {
            //判断默认是数字类型
            if (!this.type || this.type == 'number') {
                if (this.inputData + "" === "0") {
                    return
                }
                this.inputData = this.inputData.replace(/^[0]{1,}/g, "");
                this.inputData = this.inputData.toString().trim();
                this.inputData = this.inputData.replace(/^\./g, "");
                // 清除非数字和.字符
                this.inputData = this.inputData.replace(/[^\d.]/g, "");
                // 当多个0开头，去掉多个0
                this.inputData = this.inputData.replace(/[^0-9]/g, '')
                return
            }
            //价格类型
            if (!this.type == 'price') {
                //第一个字符是小数点的情况.  
                if (this.inputData != '' && this.inputData.substr(0, 1) == '.') {
                    this.inputData = "0.";
                }
                this.inputData = this.inputData.replace(/^0*(0\.|[1-9])/, '$1');
                this.inputData = this.inputData.replace(/[^\d.]/g, "");
                this.inputData = this.inputData.replace(/\.{2,}/g, ".");
                this.inputData = this.inputData.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
                this.inputData = this.inputData.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
                if (this.inputData.indexOf(".") < 0 && this.inputData != "") {
                    if (this.inputData.substr(0, 1) == '0' && this.inputData.length == 2) {
                        this.inputData = this.inputData.substr(1, this.inputData.length);
                    }
                }
            }
            return
        }
    },
}
</script>