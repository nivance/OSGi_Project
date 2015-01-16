package nivance.osgi

import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction

class UnInstallTask extends GlassFishOSGITask {
	@TaskAction
	def undeploy() {
		super.gogo("uninstall")
	}
}
