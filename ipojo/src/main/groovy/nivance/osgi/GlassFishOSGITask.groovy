package nivance.osgi

import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction
import org.slf4j.LoggerFactory;


class GlassFishOSGITask extends DefaultTask {
		def gogo(lbcmd) {
		
		def procline;
		def jarfile= project.jar.archivePath.toString();
		if (System.properties['os.name'].toLowerCase().contains('windows')) {
			procline='cmd'+ ' /c'+System.getenv('GLASSFISH_HOME')+File.separator+'bin'+File.separator+'/asadmin.bat osgi'
			jarfile=jarfile.replace("\\", "/");
		} else {
//		println "[osgi]envs"+System.getenv()
		println "[osgi]env glassfish passwd file="+System.getenv('glassfishpwd')
			if(System.getenv('glassfishpwd')!=null){
				procline=System.getenv('GLASSFISH_HOME')+'/bin/asadmin --user admin -W '+System.getenv('glassfishpwd')
			}else{
				procline=System.getenv('GLASSFISH_HOME')+'/bin/asadmin'
			}
		}
		
		println "[osgi] GlassFish_Home="+System.getenv('GLASSFISH_HOME')
		procline+=" osgi "+lbcmd+" file:"+ jarfile;
		println "[osgi] execute:"+procline
		def proc=procline.execute();
		proc.in.eachLine {line -> println "[osgi] "+ line}
		proc.err.eachLine {line -> println "[osgi] "+ 'ERROR: ' + line}
		if(proc.waitFor()<0){
			throw new RuntimeException("error to run GlassFish");
		}
		println "[osgi] "+ "osgi"+lbcmd+" Finished"
	}
	
	
}
