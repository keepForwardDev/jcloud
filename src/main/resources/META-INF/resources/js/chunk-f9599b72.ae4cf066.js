(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-f9599b72","chunk-6c03652f"],{"0777":function(e,t,i){"use strict";i.d(t,"b",(function(){return a})),i.d(t,"a",(function(){return r}));i("3fab"),i("3f27"),i("8130"),i("a9b6");var a=function(e){return e?[{required:!0,message:e,trigger:"blur"}]:[{required:!0,message:"该项为必填项，请填写完整！",trigger:"blur"}]},r=function(e,t,i){if(""!==t){var a=/^\w+$/;a.test(t)?i():i(new Error("只能填写 字母、数字、下划线组成的字符串！"))}else i()}},5135:function(e,t,i){"use strict";i.r(t);var a=function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",[i("el-drawer",{attrs:{title:e.drawerTitle,visible:e.drawerVisible,direction:"rtl",size:"30%","before-close":e.handleDrawerClose},on:{"update:visible":function(t){e.drawerVisible=t}}},[i("el-scrollbar",{staticClass:"drawer-scrollbar",staticStyle:{height:"calc(100vh - 150px)"}},[i("div",{staticClass:"drawer-content"},[i("div",{staticClass:"drawer-item"},[i("el-tree",{ref:"treeBox",attrs:{"default-expand-all":"","empty-text":"当前暂无数据","show-checkbox":"","node-key":"id",data:e.treeList},scopedSlots:e._u([{key:"default",fn:function(t){var a=t.data;return i("span",{staticClass:"custom-tree-node"},[i("span",{staticClass:"tree-title",attrs:{title:a.extra}},[e._v(e._s(a.label))]),1===e.privileges.resourceType?i("i",{staticClass:"el-icon-edit-outline closeBtn",attrs:{title:"修改api调用次数"},on:{click:function(t){return e.editLimit(a)}}}):e._e()])}}])})],1)])]),i("div",{staticClass:"drawer-footer"},[i("el-button",{attrs:{type:"primary"},on:{click:e.savePrivileges}},[e._v("保存")])],1)],1),i("el-dialog",{attrs:{title:"api调用限制",visible:e.dialogFormVisible1,width:"500px"},on:{"update:visible":function(t){e.dialogFormVisible1=t}}},[i("el-form",{ref:"form1",attrs:{model:e.formData1,"label-width":"120px",rules:e.formRules1}},[i("el-form-item",{attrs:{label:"api调用路径"}},[i("span",[e._v(e._s(e.formData1.apiPath))])]),i("el-form-item",{attrs:{label:"调用次数",prop:"apiLimit"}},[i("el-input",{attrs:{type:"number"},model:{value:e.formData1.apiLimit,callback:function(t){e.$set(e.formData1,"apiLimit",t)},expression:"formData1.apiLimit"}})],1),i("el-form-item",{attrs:{label:"调用策略"}},[i("el-select",{model:{value:e.formData1.apiLimitStrategy,callback:function(t){e.$set(e.formData1,"apiLimitStrategy",t)},expression:"formData1.apiLimitStrategy"}},[i("el-option",{attrs:{label:"次数",value:0}}),i("el-option",{attrs:{label:"每天",value:1}})],1)],1)],1),i("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[i("el-button",{on:{click:function(t){e.dialogFormVisible1=!1}}},[e._v("取 消")]),i("el-button",{attrs:{type:"primary"},on:{click:e.saveForm1}},[e._v("确定")])],1)],1)],1)},r=[],s=i("efbe"),o=(i("a9b6"),i("c284"),i("44ad"),i("0777")),n={name:"privileges",data:function(){return{treeList:[],dialogFormVisible1:!1,drawerVisible:!1,formData1:{apiPath:"",apiLimit:"",apiLimitStrategy:0},editNode:{},formRules1:{apiLimit:Object(o["b"])()}}},props:{drawerTitle:{type:String,default:""},privileges:{type:Object,default:function(){return{resourceIds:[],apiConfig:[],id:"",resourceType:0,privilegeType:1,serviceId:""}}},apiResources:{type:Array,default:function(){return[]}},menuTreeList:{type:Array,default:function(){return[]}},resourceTreeList:{type:Array,default:function(){return[]}}},mounted:function(){},methods:{changeService:function(e){this.getPrivileges()},handleDrawerClose:function(e){e()},savePrivileges:function(){var e=this,t=this.$refs.treeBox.getCheckedNodes(),i=this.$refs.treeBox.getCheckedKeys(),a=this.$refs.treeBox.getHalfCheckedKeys(),r=[];1===this.privileges.resourceType?(t.forEach((function(e){e.children||r.push({apiPath:e.data.apiPath,apiLimit:e.data.apiLimit,apiLimitStrategy:e.data.apiLimitStrategy})})),this.privileges.resourceIds=[],this.privileges.apiConfig=r):this.privileges.resourceIds=[].concat(Object(s["a"])(i),Object(s["a"])(a)),this.$postJson("/privilege/savePrivileges",this.privileges).then((function(t){1===t.code&&e.$message.success(t.msg)}))},getPrivileges:function(){var e=this;this.$get("/privilege/getPrivileges",{resourceType:this.privileges.resourceType,id:this.privileges.id,privilegeType:this.privileges.privilegeType,serviceId:this.privileges.serviceId}).then((function(t){1===t.code&&(0===e.privileges.resourceType?e.treeList=e.menuTreeList:1===e.privileges.resourceType?(e.treeList=t.data.apiTree,e.privileges.apiConfig=t.data.apiConfig):e.treeList=e.resourceTreeList,e.$nextTick((function(){t.data.resourceIds.forEach((function(t){e.$refs.treeBox.setChecked(t,!0,!1)}))})))}))},editLimit:function(e){this.dialogFormVisible1=!0,this.editNode=e,this.formData1.apiPath=e.data.apiPath,this.formData1.apiLimit=e.data.apiLimit,this.formData1.apiLimitStrategy=e.data.apiLimitStrategy},saveForm1:function(){var e=this;this.$refs.form1.validate((function(t,i){t?(e.editNode.data.apiPath=e.formData1.apiPath,e.editNode.data.apiLimit=e.formData1.apiLimit,e.editNode.data.apiLimitStrategy=e.formData1.apiLimitStrategy,e.dialogFormVisible1=!1):e.$message({message:"请完善相关信息填写！",type:"warning"})}))},showDrawer:function(){this.drawerVisible=!0}}},l=n,c=(i("db50"),i("cba8")),d=Object(c["a"])(l,a,r,!1,null,"a293bd66",null);t["default"]=d.exports},"533d":function(e,t,i){"use strict";i.r(t);var a=function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("d2-container",[i("div",{staticClass:"search-area"},[i("el-form",{attrs:{inline:!0,model:e.search}},[i("el-form-item",{attrs:{label:"角色名称"}},[i("el-input",{attrs:{placeholder:"请输入角色名称",size:"mini"},model:{value:e.search.name,callback:function(t){e.$set(e.search,"name",t)},expression:"search.name"}})],1),i("el-form-item",{attrs:{label:"角色编码"}},[i("el-input",{attrs:{placeholder:"请输入角色唯一编码",size:"mini"},model:{value:e.search.code,callback:function(t){e.$set(e.search,"code",t)},expression:"search.code"}})],1),i("el-form-item",[i("el-button",{attrs:{type:"primary",size:"mini"},on:{click:e.getList}},[e._v("查询")])],1)],1)],1),i("div",{staticClass:"btn-area"},[i("el-button",{attrs:{type:"primary",size:"mini"},on:{click:e.showForm}},[e._v("新建")])],1),i("div",{staticClass:"table-area"},[i("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.listLoading,expression:"listLoading"}],staticStyle:{width:"100%"},attrs:{data:e.tableData}},[i("el-table-column",{attrs:{type:"selection",width:"55"}}),i("el-table-column",{attrs:{prop:"name",label:"角色名称"}}),i("el-table-column",{attrs:{prop:"code",label:"角色编码"}}),i("el-table-column",{attrs:{prop:"description",label:"描述"}}),i("el-table-column",{attrs:{label:"操作",width:"230"},scopedSlots:e._u([{key:"default",fn:function(t){return[i("el-button",{attrs:{size:"mini",type:"primary"},on:{click:function(i){return e.editData(t.row)}}},[e._v("编辑 ")]),i("el-button",{attrs:{size:"mini",type:"danger"},on:{click:function(i){return e.deleteData(t.row)}}},[e._v("删除 ")]),i("el-dropdown",{staticStyle:{"padding-left":"10px"}},[i("el-button",{attrs:{type:"primary",size:"mini"}},[e._v(" 更多"),i("i",{staticClass:"el-icon-arrow-down el-icon--right"})]),i("el-dropdown-menu",{attrs:{slot:"dropdown"},slot:"dropdown"},[i("el-dropdown-item",{nativeOn:{click:function(i){return e.grant(t.row,0)}}},[e._v("授权菜单")]),i("el-dropdown-item",{nativeOn:{click:function(i){return e.grant(t.row,1)}}},[e._v("授权API")]),i("el-dropdown-item",{nativeOn:{click:function(i){return e.grant(t.row,2)}}},[e._v("授权自定义权限")])],1)],1)]}}])})],1),i("div",{staticClass:"el-page"},[i("el-pagination",{attrs:{"current-page":e.pager.currentPage,"page-sizes":e.pageSizes,"page-size":e.pager.pageSize,layout:e.pagerSetting,total:e.pager.totalCount},on:{"size-change":e.handlePageSizeChange,"current-change":e.handleCurrentChange}})],1)],1),i("el-dialog",{attrs:{title:e.dialogTitle,visible:e.dialogFormVisible,width:"500px"},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[i("el-form",{ref:"form",attrs:{model:e.formData,"label-width":"120px",rules:e.formRules}},[i("el-form-item",{attrs:{label:"角色名称",prop:"name"}},[i("el-input",{model:{value:e.formData.name,callback:function(t){e.$set(e.formData,"name",t)},expression:"formData.name"}})],1),i("el-form-item",{attrs:{label:"角色唯一编码",prop:"code"}},[i("el-input",{model:{value:e.formData.code,callback:function(t){e.$set(e.formData,"code",t)},expression:"formData.code"}})],1),i("el-form-item",{attrs:{label:"描述"}},[i("el-input",{model:{value:e.formData.description,callback:function(t){e.$set(e.formData,"description",t)},expression:"formData.description"}})],1)],1),i("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[i("el-button",{on:{click:function(t){e.dialogFormVisible=!1}}},[e._v("取 消")]),i("el-button",{attrs:{type:"primary",disabled:e.saveBtnDisabled},on:{click:e.saveForm}},[e._v("保存")])],1)],1),i("privileges-component",{ref:"privilegeRef",attrs:{privileges:e.privileges,drawerTitle:e.drawerTitle,apiResources:e.apiResources,resourceTreeList:e.resourceTreeList,menuTreeList:e.menuTreeList}})],1)},r=[],s=(i("3fab"),i("814e"),i("3e22"),i("49e9"),i("c2e6"),i("0777")),o=i("5135"),n={name:"admin-clients",data:function(){return{search:{name:"",code:""},formData:{id:"",name:"",code:"",description:""},formRules:{name:Object(s["b"])(),code:[{required:!0,message:"该项为必填项，请填写完整！"},{validator:s["a"],trigger:"blur"}]},pager:{totalCount:0,pageCount:1,pageSize:10,currentPage:1},pageSizes:[10,20,40,60,100],pagerSetting:"total, sizes, prev, pager, next, jumper",tableData:[],listLoading:!1,dialogTitle:"",newDataTitle:"新建角色",editDataTitle:"修改角色",dialogFormVisible:!1,listPath:"/role/list",savePath:"/role/save",deletePath:"/role/delete",statusList:["待审核","已通过","未通过"],saveBtnDisabled:!1,drawerTitle:"",privileges:{resourceIds:[],apiConfig:[],id:"",resourceType:0,privilegeType:1,serviceId:""},apiResources:[],menuTreeList:[],resourceTreeList:[]}},components:{privilegesComponent:o["default"]},created:function(){this.getList(),this.getMenuTree(),this.getCustomResource()},methods:{getList:function(e){var t=this;this.listLoading=!0,e&&(this.pager.currentPage=1);var i=Object.assign({},this.search,{pageSize:this.pager.pageSize,currentPage:this.pager.currentPage});this.$get(this.listPath,i).then((function(e){t.listLoading=!1,1===e.code&&(t.tableData=e.data.list,t.pager.totalCount=e.data.totalCount,t.pager.pageCount=e.data.pageCount,t.pager.pageSize=e.data.pageSize,t.pager.currentPage=e.data.currentPage)})).catch((function(e){t.listLoading=!1}))},showForm:function(){this.dialogTitle=this.newDataTitle,this.formData={id:"",name:"",code:"",description:""},this.dialogFormVisible=!0},saveForm:function(){var e=this;this.$refs.form.validate((function(t,i){t?(e.saveBtnDisabled=!0,e.$post(e.savePath,e.formData).then((function(t){1===t.code&&(e.$message.success(t.msg),e.getList(!0))})).then((function(){e.saveBtnDisabled=!1})),e.dialogFormVisible=!1):e.$message({message:"请完善相关信息填写！",type:"warning"})}))},editData:function(e){this.step=0,this.dialogTitle=this.editDataTitle,this.formData={id:e.id,name:e.name,code:e.code,description:e.description},this.dialogFormVisible=!0},deleteData:function(e){var t=this;this.$confirm("确定删除吗？","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){t.$get(t.deletePath+"/"+e.id).then((function(e){1===e.code&&(t.$message({message:"删除成功！",type:"success"}),t.getList(!0))}))}))},resetQuery:function(){this.search={},this.getList(!0)},handleCurrentChange:function(e){this.pager.currentPage=e,this.getList()},handlePageSizeChange:function(e){this.pager.pageSize=e,this.getList(!0)},grant:function(e,t){var i=this;this.privileges.resourceType=t,this.privileges.id=e.id,0===t?(this.drawerTitle=e.name+"菜单权限",this.privileges.serviceId=""):1===t?(this.drawerTitle=e.name+"API权限",this.apiResources.length>0&&(this.privileges.serviceId=this.apiResources[0].url)):(this.drawerTitle=e.name+"自定义权限",this.privileges.serviceId=""),this.$refs.privilegeRef.showDrawer(),this.$nextTick((function(){i.$refs.privilegeRef.getPrivileges()}))},getMenuTree:function(){var e=this;this.$get("/menu/treeList").then((function(t){1===t.code&&(e.menuTreeList=t.data)}))},getCustomResource:function(){var e=this;this.$get("/resource/treeList").then((function(t){1===t.code&&(e.resourceTreeList=t.data)}))}}},l=n,c=(i("ad29"),i("cba8")),d=Object(c["a"])(l,a,r,!1,null,"42754e37",null);t["default"]=d.exports},5426:function(e,t,i){var a=i("1111").PROPER,r=i("b68d"),s=i("de35"),o="​᠎";e.exports=function(e){return r((function(){return!!s[e]()||o[e]()!==o||a&&s[e].name!==e}))}},8130:function(e,t,i){"use strict";var a=i("a5ba"),r=i("e280").trim,s=i("5426");a({target:"String",proto:!0,forced:s("trim")},{trim:function(){return r(this)}})},"826a":function(e,t,i){},ad29:function(e,t,i){"use strict";i("826a")},bd5a:function(e,t,i){},db50:function(e,t,i){"use strict";i("bd5a")}}]);