var URLS = {
    endPointURL: "${endPointURL}",
    templateUserList: "components/user/list.html",
    templateUserEdit: "components/user/edit.html",
    templateUserLogin: "components/user/login.html",
    templateUserHome: "components/user/Home.html",
	templateUserGroupsList : "components/generic/groupsList.html",
	templateUserPermissionsList : "components/generic/permissionsList.html",
    templateUserView: "components/user/view.html",
	templateUserPassword : "components/user/editPassword.html",
    templateGroupEdit: "components/group/edit.html",
    templateGroupList: "components/group/list.html",
    templateGroupView: "components/group/view.html",
	templateGroupGroupsList : "components/generic/groupsList.html",
	templateGroupPermissionsList : "components/generic/permissionsList.html",
	templateGenericPermissionsCheckBox : "components/generic/permissionsCheckBox.html",
    templateMain: "main.html",
	
    jsUserList: "${nonSecuredPath}components/user/list.js",
    jsUserEdit: "${nonSecuredPath}components/user/edit.js",
    jsUserView: "${nonSecuredPath}components/user/view.js",
    jsUserLoginForm: "${nonSecuredPath}components/user/login.js",
    jsUserLoginProcess: "${nonSecuredPath}components/user/postLogin.js",
    jsUserLogout: "${nonSecuredPath}components/user/logout.js",
	jsUserGroupsList : "${nonSecuredPath}components/user/groupsList.js",
	jsUserPassword: "${nonSecuredPath}components/user/editPassword.js",
	jsUserPermissionsList : "${nonSecuredPath}components/user/permissionsList.js",
	
    jsGroupList: "${nonSecuredPath}components/group/list.js",
    jsGroupEdit: "${nonSecuredPath}components/group/edit.js",
	jsGroupGroupsList : "${nonSecuredPath}components/group/groupsList.js",
    jsGroupPermissionsList : "${nonSecuredPath}components/group/permissionsList.js",
	
    apiUser: "${securedApiPath}api/user/", 
	apiUserName: "${securedApiPath}api/user/name/",
	apiUserPassword : "${securedApiPath}api/user/password/",
	
    apiUserCurrentUser: "${securedApiPath}api/user/me/",
    apiUserList: "${securedApiPath}api/user/all/",
    apiUserGroupsList: "/groups/",
	apiUserPermissionsList: "/permissions/",
	
	apiGroupName : "${securedApiPath}api/group/name/",
    apiGroup: "${securedApiPath}api/group/",
    apiGroupList: "${securedApiPath}api/group/list/",
	apiGroupGroupsList: "/groups/",
	apiGroupPermissionsList: "/permissions/"
	
};
