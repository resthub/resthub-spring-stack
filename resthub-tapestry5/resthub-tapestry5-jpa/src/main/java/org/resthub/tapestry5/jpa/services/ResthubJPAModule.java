package org.resthub.tapestry5.jpa.services;

import java.util.Collection;

import javax.persistence.EntityManagerFactory;

import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.slf4j.Logger;
import org.tynamo.jpa.JPACoreModule;
import org.tynamo.jpa.JPAEntityManagerSource;
import org.tynamo.jpa.JPAEntityPackageManager;
import org.tynamo.jpa.JPAModule;

@SubModule({JPAModule.class, JPACoreModule.class})
public class ResthubJPAModule
{
    @SuppressWarnings("unchecked")
	public static void contributeServiceOverride(
			MappedConfiguration<Class, Object> configuration, @Local JPAEntityManagerSource directJPAEntityManagerSource) {

		configuration
				.add(JPAEntityManagerSource.class, directJPAEntityManagerSource);
	}
    
    public static JPAEntityManagerSource buildDirectJPAEntityManagerSource(Logger logger,
	        @Inject @Service("entityManagerFactory") EntityManagerFactory entityManagerFactory, RegistryShutdownHub hub)
	{
    	DirectJPAEntityManagerSourceImpl hss = new DirectJPAEntityManagerSourceImpl(logger, entityManagerFactory);

		hub.addRegistryShutdownListener(hss);

		return hss;
	}
    
  public static JPAEntityPackageManager buildJPAEntityPackageManager(
          final Collection<String> packageNames)
  {
      return new JPAEntityPackageManager()
      {
          public Collection<String> getPackageNames()
          {
              return packageNames;
          }
      };
  }

}
