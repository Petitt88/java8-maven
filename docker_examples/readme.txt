To run the "mongodb" image and publish the gui for administrators of mongodb which can be reached from the browser use:
	docker run --name mongo_1 -p 81:28017 -d mongodb
	Then on the host OS go to localhost:81 and voala
	
To start another instace of the image:
	docker run --name mongo_2 -p 82:28017 -d mongodb	# port changed to avoid port conflict on host, and container name changed to avoid name conflict on MobyLinuxVM.vhdx