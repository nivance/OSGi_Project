package nivance

import org.gradle.api.Project
import org.gradle.api.Plugin

class ISpringPlugin implements Plugin<Project> {
	void apply(Project target) {
		target.dependencies {
			compile   'org.springframework:spring-jdbc:3.1.1.RELEASE'
			compile   'org.springframework:spring-ibatis:2.0.8'
			compile   'org.springframework:spring-core:3.1.1.RELEASE'
			compile   'org.springframework:spring-webmvc:3.1.1.RELEASE'
			compile   'org.springframework:spring-beans:3.1.1.RELEASE'
			compile   'org.springframework:spring-context:3.1.1.RELEASE'
			compile   'org.springframework:spring-aop:3.1.1.RELEASE'
			compile   'org.springframework:spring-tx:3.1.1.RELEASE'
		}
	}
	
	
}
