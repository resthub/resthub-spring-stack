var URLS = {
    endPointURL: "${endPointURL}",
    templateUserList: "components/user/list.html",
    templateUserEdit: "components/user/edit.html",
    templateUserLogin: "components/user/login.html",
    templateUserHome: "components/user/Home.html",
	templateUserGroupList : "components/user/groupsList.html",
    templateUserView: "components/user/view.html",
	templateUserPassword : "components/user/editPassword.html",
    templateGroupEdit: "components/group/edit.html",
    templateGroupList: "components/group/list.html",
    templateGroupView: "components/group/view.html",
    templateMain: "main.html",
	
    jsUserList: "${nonSecuredPath}components/user/list.js",
    jsUserEdit: "${nonSecuredPath}components/user/edit.js",
    jsUserView: "${nonSecuredPath}components/user/view.js",
    jsUserLoginForm: "${nonSecuredPath}components/user/login.js",
    jsUserLoginProcess: "${nonSecuredPath}components/user/postLogin.js",
    jsUserLogout: "${nonSecuredPath}components/user/logout.js",
	jsUserGroupsList : "${nonSecuredPath}components/user/groupsList.js",
	jsUserPassword: "${nonSecuredPath}components/user/editPassword.js",
	
    jsGroupList: "${nonSecuredPath}components/group/list.js",
    jsGroupEdit: "${nonSecuredPath}components/group/edit.js",
    
    apiUser: "${securedApiPath}api/user/", 
	apiUserName: "${securedApiPath}api/user/name/",
	apiUserPassword : "${securedApiPath}api/user/password/",
	
    apiUserCurrentUser: "${securedApiPath}api/user/me/",
    apiUserList: "${securedApiPath}api/user/all/",
    apiUserGroupsList: "/groups/",
	
    apiGroup: "${securedApiPath}api/group/",
    apiGroupList: "${securedApiPath}api/group/list/",
	
};
