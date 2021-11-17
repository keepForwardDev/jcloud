(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-088f4ce6"],{5301:function(t,e,a){},"7b76":function(t,e,a){"use strict";a.r(e);var s=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("d2-container",[a("div",{staticClass:"card-panel"},[t._l(t.tableData,(function(e){return a("div",{key:e.id,staticClass:"wrapper"},[a("div",{staticClass:"container"},[a("div",{staticClass:"top"},[a("img",{attrs:{src:"/admin/root/svg/"+e.type+"-logo.svg"}})]),a("div",{staticClass:"bottom"},[a("div",{staticClass:"left"},[a("div",{staticClass:"details"},[a("h3",[t._v(t._s(e.name))])]),a("div",{staticClass:"editBtn",on:{click:function(a){return t.editItem(e)}}},[a("i",{staticClass:"el-icon-edit"})]),a("el-popconfirm",{attrs:{icon:"el-icon-info","icon-color":"red",title:"是否确定删除？"},on:{confirm:function(a){return t.deleteDatabase(e)}}},[a("div",{staticClass:"deleteBtn",attrs:{slot:"reference"},slot:"reference"},[a("i",{staticClass:"el-icon-delete"})])])],1)])]),a("div",{staticClass:"inside"},[a("div",{staticClass:"icon"},[a("i",{staticClass:"material-icons"},[t._v("info")])]),a("div",{staticClass:"contents"},[a("table",[a("tr",[a("th",[t._v("链接")]),a("th",[a("span",{attrs:{title:e.url}},[t._v(t._s(e.url?e.url.substring(0,20)+"...":e.url))])])]),a("tr",[a("th",[t._v("用户名")]),a("th",[t._v(t._s(e.username))])]),a("tr",[a("th",[t._v("密码")]),a("th",[t._v("***")])]),a("tr",[a("th",[t._v("说明")]),a("th",[t._v(t._s(e.remark))])])])])])])})),a("div",{staticClass:"item-add",on:{click:t.addDatabase}},[a("i",{staticClass:"el-icon-plus avatar-uploader-icon"})])],2),a("el-dialog",{attrs:{"destroy-on-close":!0,title:"提示",visible:t.dialogVisible,width:"700px","close-on-click-modal":!1},on:{"update:visible":function(e){t.dialogVisible=e}}},[a("el-form",{ref:"form",attrs:{rules:t.rules,model:t.formData,"label-width":"120px"}},[a("el-form-item",{attrs:{label:"配置名称",prop:"name"}},[a("el-input",{model:{value:t.formData.name,callback:function(e){t.$set(t.formData,"name",e)},expression:"formData.name"}})],1),a("el-form-item",{attrs:{label:"数据库类型",prop:"type"}},[a("el-select",{attrs:{placeholder:"请选择数据库类型",clearable:""},model:{value:t.formData.type,callback:function(e){t.$set(t.formData,"type",e)},expression:"formData.type"}},[a("el-option",{attrs:{value:"database"}},[t._v("database")]),a("el-option",{attrs:{value:"elasticsearch"}},[t._v("elasticsearch")])],1)],1),a("el-form-item",{attrs:{label:"URL",prop:"url"}},[a("el-input",{model:{value:t.formData.url,callback:function(e){t.$set(t.formData,"url",e)},expression:"formData.url"}})],1),a("el-form-item",{attrs:{label:"用户名"}},[a("el-input",{model:{value:t.formData.username,callback:function(e){t.$set(t.formData,"username",e)},expression:"formData.username"}})],1),a("el-form-item",{attrs:{label:"密码"}},[a("el-input",{attrs:{placeholder:"请输入密码"},model:{value:t.formData.password,callback:function(e){t.$set(t.formData,"password",e)},expression:"formData.password"}},[a("template",{slot:"append"},[a("el-button",{attrs:{type:"primary"},on:{click:t.testConnection}},[t._v("测试连接")])],1)],2)],1),a("el-form-item",{attrs:{label:"备注"}},[a("el-input",{model:{value:t.formData.remark,callback:function(e){t.$set(t.formData,"remark",e)},expression:"formData.remark"}})],1),a("el-form-item",[a("el-button",{attrs:{type:"primary"},on:{click:t.saveForm}},[t._v("立即创建")]),a("el-button",{on:{click:function(e){t.dialogVisible=!1}}},[t._v("取消")])],1)],1)],1)],1)},i=[],r={name:"database-setting",data:function(){return{formData:{id:"",type:"",url:"",username:"",password:"",name:"",remark:""},tableData:[],dialogVisible:!1,rules:{name:[{required:!0,message:"请输入配置名称",trigger:"blur"}],type:[{required:!0,message:"请选择数据库类型",trigger:"change"}],url:[{required:!0,message:"请输入URL",trigger:"blur"}]}}},mounted:function(){this.getList()},methods:{deleteDatabase:function(t){var e=this;this.$get("/datacenter/metadata/dataSourceDelete/"+t.id).then((function(t){1===t.code?(e.getList(),e.$message.success(t.msg)):e.$message.error(t.msg)}))},editItem:function(t){this.formData=t,this.dialogVisible=!0},addDatabase:function(){this.formData={id:"",type:"",url:"",username:"",password:"",name:"",remark:""},this.dialogVisible=!0},saveForm:function(){var t=this;this.$refs.form.validate((function(e){e&&t.$post("/datacenter/metadata/dataSourceSave",t.formData).then((function(e){1===e.code&&(t.$message.success(e.msg),t.getList(),t.dialogVisible=!1)}))}))},getList:function(){var t=this;this.$get("/datacenter/metadata/dataSourcePageList",{pagseSize:100,currentPage:1}).then((function(e){1===e.code&&(t.tableData=e.data.list)}))},testConnection:function(){var t=this;this.$refs.form.validate((function(e){t.$post("/datacenter/metadata/testConnection",t.formData).then((function(e){1===e.code&&e.data?t.$message.success("连接成功！"):t.$message.error("连接失败，请检查配置！")}))}))}}},o=r,l=(a("f425"),a("cba8")),n=Object(l["a"])(o,s,i,!1,null,"4a74a6de",null);e["default"]=n.exports},f425:function(t,e,a){"use strict";a("5301")}}]);