<template>
    <tyAddButton ref="addBtn" :text="text" :class="{'adsConigBtn-finish': (currStatus == finishConfig),
        'adsConigBtn-doing': (currStatus == doingConfig),
        'adsConigBtn-wait': (currStatus == waitConfig)}">
    </tyAddButton>
</template>

<script>
import tyAddButton from 'components/tyAddButton';
export default {
    props: ['text'],
    data() {
        return {
            currStatus: 1,
            waitConfig: 1,
            finishConfig: 2,
            doingConfig: 3
        }
    },
    methods: {
        toggleDoing() {
            this.currStatus = this.doingConfig;
            this.$refs.addBtn.showIcon();
        },
        toggleWaitConfig() {
            this.currStatus = this.waitConfig;
            this.$refs.addBtn.showIcon();
        },
        toggleFinishConfig() {
            this.currStatus = this.finishConfig;
            this.$refs.addBtn.hideIcon();
        }
    },
    components: {
        tyAddButton
    }
}
</script>

<style>
.adsConigBtn-finish {
    background-color: #6aa84f !important;
}

.adsConigBtn-doing {
    background-color: #ea9999 !important;
}

.adsConigBtn-wait {
    background-color: #cccccc!important;
}
</style>
