import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_app/helpers/ToastHelper.dart';
import 'package:path/path.dart';
import 'package:path_provider/path_provider.dart';

class CameraRoute extends StatefulWidget {
  final void Function(String path) onPictureTaken;

  const CameraRoute(
    this.onPictureTaken, {
    Key key,
  }) : super(key: key);

  @override
  CameraRouteState createState() => CameraRouteState();
}

class CameraRouteState extends State<CameraRoute> {
  // Add two variables to the state class to store the CameraController and
  // the Future
  CameraController _controller;
  Future<void> _initCamera;

  @override
  void initState() {
    super.initState();
    _initCamera = _initAsync();
  }

  Future<void> _initAsync() async {
    await SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);

    final cameras = await availableCameras();
    final camera = cameras.first;

    // In order to display the current output from the Camera, you need to
    // create a CameraController.
    _controller = CameraController(
      // Get a specific camera from the list of available cameras
      camera,
      // Define the resolution to use
      ResolutionPreset.medium,
    );

    // Next, you need to initialize the controller. This returns a Future
    await _controller.initialize();
  }

  @override
  void dispose() {
    super.dispose();
    // Make sure to dispose of the controller when the Widget is disposed
    _controller?.dispose();

    SystemChrome.setPreferredOrientations([...DeviceOrientation.values]);
  }

  @override
  Widget build(BuildContext context) {
    // You must wait until the controller is initialized before displaying the
// camera preview. Use a FutureBuilder to display a loading spinner until the
// controller has finished initializing
    return FutureBuilder<void>(
      future: _initCamera,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.done) {
          // If the Future is complete, display the preview
          return Scaffold(
            body: CameraPreview(_controller),
            floatingActionButton: FloatingActionButton(
              onPressed: () async {
                // Take the Picture in a try / catch block. If anything goes wrong,
                // catch the error.
                try {
                  // Construct the path where the image should be saved using the path
                  // package.
                  final path = join(
                    // In this example, store the picture in the temp directory. Find
                    // the temp directory using the `path_provider` plugin.
                    (await getTemporaryDirectory()).path,
                    '${DateTime.now()}.png',
                  );

                  // Attempt to take a picture and log where it's been saved
                  await _controller.takePicture(path);

                  widget.onPictureTaken(path);
                  Navigator.pop(context);
                } catch (e) {
                  await ToastHelper.error(e.toString());
                }
              },
              child: Icon(Icons.camera),
            ),
            floatingActionButtonLocation:
                FloatingActionButtonLocation.centerFloat,
          );
        } else {
          // Otherwise, display a loading indicator
          return Center(child: CircularProgressIndicator());
        }
      },
    );
  }
}
