package nivance.osgi

import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction

class StopTask extends GlassFishOSGITask {
	@TaskAction
	def deploy() {
		super.gogo("stop")
	}
	
	
}
