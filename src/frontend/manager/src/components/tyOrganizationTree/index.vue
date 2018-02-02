<template>
    <iTree class="zTree" :data="baseData">
    </iTree>
</template>

<script>
import iTree from 'iview/src/components/tree';
export default {
    components: {
        iTree
    },
    created() {
        this.$getAreaData().then((result) => {

            this.adapterBaseData(result);

            this.baseData = result;
            this.$emit('loadedEvent');
            console.log(result);
        }).catch((e) => {
            this.$emit('loadedEvent');
        })
        // this.$post(this.$api.getAllOrganizationUrl).then((result) => {
        //     this.adapterBaseData(result.data);
        //     this.baseData = result.data;
        //     this.$emit('loadedEvent');
        // }).catch((e) => {
        //     this.$emit('loadedEvent');
        // })
    },
    methods: {
        // 对后端返回的 ztree 数据进行转化
        adapterBaseData(baseData) {
            if (!baseData) {
                return;
            }
            for (let i = 0; i < baseData.length; i++) {
                baseData[i].title = baseData[i].name;
                baseData[i].children = baseData[i].areaList;
                this.adapterBaseData(baseData[i].children);
            }
        }
    },
    data() {
        return {
            baseData: []
        }
    }
}
</script>

<style>

</style>
