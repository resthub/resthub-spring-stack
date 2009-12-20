package org.resthub.core.domain.dao.jpa;

import java.net.URL;
import java.util.List;

import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;

public class ResthubPersistenceUnitManager extends DefaultPersistenceUnitManager {

	@Override
	 protected void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
	    super.postProcessPersistenceUnitInfo(pui);
	    pui.addJarFileUrl(pui.getPersistenceUnitRootUrl());

	    MutablePersistenceUnitInfo oldPui = getPersistenceUnitInfo(pui.getPersistenceUnitName());
	    if (oldPui != null) {
	      List<URL> urls = oldPui.getJarFileUrls();
	      for (URL url : urls) {
	        pui.addJarFileUrl(url);
	      }
	    }
	  }
}
