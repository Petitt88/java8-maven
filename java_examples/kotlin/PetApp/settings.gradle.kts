pluginManagement {
	repositories {
		gradlePluginPortal()
	}
}
rootProject.name = "PetApp"

include("Common/Utils.Core")
include("Common/Utils")
include("EVModule")
include("SADModule")
include("VNextSpringModule")

project(":Common/Utils.Core").name = "Utils.Core"
project(":Common/Utils").name = "Utils"
