package org.resthub.identity.tools;

import java.util.ArrayList;
import java.util.List;

import org.resthub.identity.model.AbstractPermissionsOwner;
import org.resthub.identity.model.Group;

public class PermissionsOwnerTools {

	public static List<String> getInheritedPermission(AbstractPermissionsOwner p){
		List<String> l = new ArrayList<String>();
		List<String> tmpPermissions;
		tmpPermissions =p.getPermissions();
		if(tmpPermissions!=null){l.addAll(tmpPermissions);}
		List<Group> lg;
		lg=p.getGroups();
		if(lg!=null){
			for(Group g : lg){
				if(g!=null){
					tmpPermissions=getInheritedPermission(g);
					if(tmpPermissions!=null){l.addAll(tmpPermissions);}
				}
			}
		}
		return l;	
	}
}
