export default {
    install: (Vue) => {
        Vue.directive('imgError', {
            inserted: function (el, binding) {
                el.onerror = function () {
                    el.src = binding.value;
                }

            }
        })
    }
}