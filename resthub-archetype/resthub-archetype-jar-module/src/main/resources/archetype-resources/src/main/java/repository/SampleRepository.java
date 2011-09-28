#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ${package}.model.Sample;

public interface SampleRepository extends JpaRepository<Sample, Long>  {

}
