import iButton from 'iview/src/components/button';
import './modalStyle.scss';
export default {
    props: ['editTitle','addTitle'],
    components: {
        iButton
    },
    data() {
        return {
            modalTitle: '',
            modelHeight: 'modelHeight',
            finishBtnText: '添加',
            finishLoading: false,
            loading: true,
            showFlag: false,
            formData:null
        }
    },
    methods: {
        close() {
            this.showFlag = false;
            this.finishLoading = false;
        },
        resetLoading() {
            this.$nextTick(() => {
                this.finishLoading = false;
                this.loading = false;
            });
        },
        toggle(flag) {
            this.finishBtnText = flag ? '保存' : '添加';
            this.modalTitle = flag ? this.editTitle : this.addTitle;
            this.showFlag = !this.showFlag;
        },
        setParams(data){
              this.formData=data
        },
        clearData(obj,defaultObj){
            if(defaultObj){
                Object.assign(obj,defaultObj)
            }else{
                for(var k in obj){
                    obj[k]="";
                }
            }
        },
        setPriceNumber(obj,key){
        //第一个字符是小数点的情况.  
        if(obj[key] !=''&& obj[key].substr(0,1) == '.'){  
            obj[key]="0.";  
        }  
        obj[key] = obj[key].replace(/^0*(0\.|[1-9])/, '$1');
        obj[key] = obj[key].replace(/[^\d.]/g,""); 
        obj[key] = obj[key].replace(/\.{2,}/g,".");
        obj[key] = obj[key].replace(".","$#$").replace(/\./g,"").replace("$#$",".");      
        obj[key] = obj[key].replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); 
        if(obj[key].indexOf(".")< 0 && obj[key] !=""){
            if(obj[key].substr(0,1) == '0' && obj[key].length == 2){  
                obj[key]= obj[key].substr(1,obj[key].length);      
            }  
        }   
        },
        setNumber(obj,key){
            obj[key] =  obj[key].replace(/^[0]{1,}/g, "");
            obj[key] =  obj[key].toString().trim();
            obj[key] =  obj[key].replace(/^\./g, "");
            // 清除非数字和.字符
            obj[key] =  obj[key].replace(/[^\d.]/g, "");
            // 当多个0开头，去掉多个0
            obj[key]=obj[key].replace(/[^0-9]/g,'')
        },
        setString(obj,key){
            obj[key] =  obj[key].replace(/^\s+|\s+$/g, '')
        }
    }
}