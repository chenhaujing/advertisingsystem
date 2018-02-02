<template>
    <iBreadcrumb>
        <iBreadcrumb-item 
            v-for="item in leveList"
            :href="item.path"
            :key="item.to.meta.path"
            >
            {{item.to.meta.title}}
        </iBreadcrumb-item>
    </iBreadcrumb>
</template>

<script>

import iBreadcrumb  from 'iview/src/components/breadcrumb';
export default {
    name: 'ty-breadcrumb',
    components: {
        iBreadcrumb,
        'iBreadcrumb-item': iBreadcrumb.Item
    },
    mounted() {
       
    },
    data() {
        return {
        }
    },
    computed: {
        leveList() {
            return this.$store.state.leveList
        }
    }
}
</script>

<style>

</style>
