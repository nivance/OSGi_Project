package nivance

import org.gradle.api.Plugin
import org.gradle.api.Project

import nivance.osgi.InstallTask
import nivance.osgi.StartTask
import nivance.osgi.StopTask
import nivance.osgi.UnInstallTask


/**
 * nivance_repo 必须配置在gradle的repos.gradle文件中<p>
 * 例如：nivance_repo = "http://repo1.maven.org/maven2"
 */
class IPojoPlugin implements Plugin<Project> {

	void apply(Project target) {
		target.dependencies()
		target.task('buildbundle',type:BundleTask)

		target.task('oinstall',type:InstallTask).setDescription("GlassFish OSGI Install Task")
		target.task('ostart',type:StartTask).setDescription("GlassFish OSGI Start Task")
		target.task('ouninstall',type:UnInstallTask).setDescription("GlassFish OSGI UnInstall Task")
		target.task('ostop',type:StopTask).setDescription("GlassFish OSGI Stop Task")

		String nivance_repo=target.project.ext.nivance_repo;

		target.project.apply([ plugin: "maven"]);
		target.project.apply([ plugin: "osgi"]);
		target.project.apply([ plugin: 'java']);
		target.project.apply([ plugin: 'eclipse']);
		target.project.apply([ plugin: 'gSI']);
		
		target.buildscript {
			repositories{
				mavenLocal()
				mavenRepo urls: target.nivance_repo
				//				maven(new URL(obrurl))
			}
		}

		//		doLast{
		//			target.buildscript.dependencies { classpath 'org.gradle.api.plugins:gradle-nexus-plugin:0.3' }
		//		}

		target.configurations {
			includeInJar
			deployerJars
		}
		target.repositories{
			mavenLocal()
			mavenRepo urls: target.nivance_repo
		}

		target.project.sourceCompatibility = 1.7
		target.project.targetCompatibility = 1.7

		target.dependencies {

			compile   'org.apache.felix:org.apache.felix.ipojo.annotations:1.8.4'
			compile   'org.apache.felix:org.apache.felix.ipojo.api:1.11.0'
			compile   'org.apache.felix:org.apache.felix.framework:4.2.1'
			compile   'org.apache.felix:org.apache.felix.main:4.2.1'
			deployerJars "org.apache.maven.wagon:wagon-ssh:2.2"
			compile   'ch.qos.logback:logback-classic:1.0.0'

			target.configurations.compile.extendsFrom(target.configurations.includeInJar)
		}

		if(target.hasProperty('obr_url')){
			target.uploadArchives  {
				repositories {
					mavenDeployer {
						repository(url: target.obr_url) {
							authentication(userName:target.obr_username,password:target.obr_passwd)
						}
					}
				}
			}
		}

		//		update.updateRepository();

		target.jar {
			into('lib') { from target.configurations.includeInJar }
			manifest{ instruction 'Export-Package','*' //attributes( 'Bundle-Activator': 'org.nights.stringio.Activator')
			}
		}

		target.jar.doLast{
			target.tasks.buildbundle.execute();
		}
	}

	public String getProp(String key,String defaultv){
		if(System.getProperty(key)!=null) {
			return System.getProperty(key);
		}
		if(System.getenv(key)!=null){
			return System.getenv(key);
		}
		return defaultv;
	}
}
