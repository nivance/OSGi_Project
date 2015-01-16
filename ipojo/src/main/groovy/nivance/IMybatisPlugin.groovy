package nivance

import org.gradle.api.Project
import org.gradle.api.Plugin

class IMybatisPlugin implements Plugin<Project> {
	void apply(Project target) {
		
		target.dependencies {
			compile   'org.mybatis:mybatis:3.1.1'
			compile   'org.mybatis:mybatis-spring:1.1.1'
		}
	}
}
