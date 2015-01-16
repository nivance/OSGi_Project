package nivance

import org.gradle.api.Plugin;
import org.gradle.api.Project;

class IFrameworkSIPlugin implements Plugin<Project> {
	//	def String SpringVersion = '3.2.4.RELEASE'

	void apply(Project target) {

		if(!target.hasProperty('springversion'))
		{
			target.ext.springversion='3.1.4.RELEASE'
		}
		if(!target.hasProperty('mybatisversion'))
		{
			target.ext.mybatisversion='3.1.1'
		}

		if(!target.hasProperty('spring_ibatis'))
		{
			target.ext.spring_ibatis='2.0.8'
		}

		if(!target.hasProperty('mybatis_spring'))
		{
			target.ext.mybatis_spring='1.1.1'
		}

		target.dependencies {

			compile   'org.springframework:spring-jdbc:'+target.ext.springversion,
			  'org.springframework:spring-core:'+target.ext.springversion,
			   'org.springframework:spring-webmvc:'+target.ext.springversion,
			   'org.springframework:spring-beans:'+target.ext.springversion,
			   'org.springframework:spring-context:'+target.ext.springversion,
			   'org.springframework:spring-aop:'+target.ext.springversion,
			   'org.springframework:spring-tx:'+target.ext.springversion,
			   'org.springframework:spring-jdbc:'+target.ext.springversion,
			   
			   'org.springframework:spring-ibatis:'+target.ext.spring_ibatis,
			   'org.mybatis:mybatis:'+target.ext.mybatisversion,
			   'org.mybatis:mybatis-spring:'+target.ext.mybatis_spring
		}
	}
}

