var cVe= new EIO.ve();
var cVei = new EIO.vei();
var cVeUti = new EIO.veUti();
var cVeName="VeDemo1";//定义本应用的Ve引擎名称，用户自定义，用作服务按的消息处理调度
var cServerUri="EIOServletMsgEngine";
var cLoginPermission =cVeUti.readLoginPermit();
	
function injectSideTree()
{
	
	Tree = new dTree("module");
 	Tree.add(0,-1,"功能模块");
 	window["module"]= Tree;
 	var modules3=[
 				 [1,0,"新建机构","addInstitution.html"],
 				 [2,0,"下级机构","frame-institutionmanage.html"],
 				 [3,0,"创建用户","createuser-usertypechoose.html"],
 				 [4,0,"用户管理","frame-usermanage.html"],
 				 [5,0,"批量创建","createuser-batch.html"],
 				 [6,0,"添加角色","addrole.html"],
 				 [7,0,"角色管理","frame-rolemanage.html"],
 				 [8,3,"普通用户","createuser-commonuser.html"],
 				 [9,3,"管理员用户","createuser-manageuser.html"],
 				 [10,3,"机构用户","createuser-institutionuser.html"],
 				 [11,3,"临时用户","createuser-temporaryuser.html"],
 				 [12,0,"账户信息维护","accountmaintain-educationcenter.html"],
 				 ];
 	var modules2=[
 				 [1,0,"新建机构","addInstitution.html"],
 				 [2,0,"向上级机构注册","register-2parentinstitution.html"],
 				 [3,0,"下级机构","frame-institutionmanage.html"],
 				 [4,0,"创建用户","createuser-usertypechoose.html"],
 				 [5,0,"用户管理","frame-usermanage.html"],
 				 [6,0,"批量创建","createuser-batch.html"],
 				 [7,4,"普通用户","createuser-commonuser.html"],
 				 [8,4,"管理员用户","createuser-manageuser.html"],
 				 [9,4,"机构用户","createuser-institutionuser.html"],
 				 [10,4,"临时用户","createuser-temporaryuser.html"],
 				 [11,0,"账户信息维护","accountmaintain-institutionuser.html"],
 				 ];
 	var modules0=[
  				 [1,0,"向上级机构注册","accountregister-commonuser.html"],
  				 [2,0,"查看账户","frame-commonuseraccountcheck.html"],
  				 [3,0,"创建临时用户","createuser-temporaryuser.html"],
  				 [4,0,"账户信息维护","accountmaintain-commonuser.html"],	
  				 ];
 	var modules1=[
   				 [1,0,"向上级机构注册","accountregister-manageuser.html"],
   				 [2,0,"查看账户","frame-manageuseraccountcheck.html"],
   				 [3,0,"创建临时用户","createuser-temporaryuser.html"],
   				 [4,0,"账户信息维护","accountmaintain-manageuser.html"],
   				 ];
 	
 	var modules4=[
    			 [1,0,"向上级机构注册","accountregister-temporaryuser.html"],
    			 [2,0,"查看账户","frame-temporaryuseraccountcheck.html"],
    			 [3,0,"创建临时用户","createuser-temporaryuser.html"],
    			 [4,0,"账户信息维护","accountmaintain-temporaryuser.html"],
    			 ];
 	
 	var modules5=[
   				 [1,0,"向上级机构注册","accountregister-commonuser.html"],
   				 [2,0,"查看账户","frame-commonuseraccountcheck.html"],
   				 [3,0,"创建临时用户","createuser-temporaryuser.html"],
   				 [4,0,"账户信息维护","accountmaintain-commonuser.html"],	
   				 ];
 	var usertype=cLoginPermission["cUserType"];
 	if (usertype=='0')
 	{
 		modules=modules0;
 	}else if(usertype=='1')
 	{
 		modules=modules1;
 	}else if(usertype=='2')
 	{
 		modules=modules2;
 	}else if(usertype=='3')
 	{
 		modules=modules3;
 	}else if(usertype=='4')
 	{
 		modules=modules4;
 	}else if(usertype=='5')
 	{
 		modules=modules5;
 	}
 	
 	
 	for (var i=0;i<modules.length;i++){
 		Tree.add(modules[i][0],modules[i][1],modules[i][2],modules[i][3]); 		
 	}
 	
 	//Tree.add(0,-1,RootName);
 	document.getElementById("treeDiv").innerHTML = Tree;
 	//$("#treeDiv").html(Tree);
}