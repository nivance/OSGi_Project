package nivance

import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction

class BundleTask extends DefaultTask {
	@TaskAction
	def buildbundle() {
		println "running iPojoTask:@"+project.jar.archivePath

//		org.apache.felix.ipojo.manipulator.Pojoization pojo = new org.apache.felix.ipojo.manipulator.Pojoization()
		org.apache.felix.ipojo.manipulator.Pojoization pojo = new nivance.ipojo.ExtPojoization()

		File jarfile = project.file(project.jar.archivePath)
		File targetJarFile = project.file(project.jar.destinationDir.absolutePath +"/" + project.jar.baseName + "_out.jar")

		if (!jarfile.exists()) throw new InvalidUserDataException("The specified bundle file does not exist: " + jarfile.absolutePath)

		if(targetJarFile.exists()) targetJarFile.delete()
		
		
		
		pojo.pojoization(jarfile, targetJarFile,(File) null);//project.file(project.projectDir.absolutePath+"/src/main/resources/metadata.xml"))

		pojo.getWarnings().each { s -> println s }
		//
		if (jarfile.delete()) {
			if ( !targetJarFile.renameTo(jarfile) ) {
				throw new InvalidUserDataException("Cannot rename the manipulated jar file");
			}
		}else {
			throw new InvalidUserDataException("Cannot delete the input jar file")
		}    
	}
}
