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
  OverlayState _topButtonsOverlay;
  List<CameraDescription> _cameras;
  CameraType _cameraType = CameraType.back;

  @override
  void initState() {
    super.initState();
    _initCamera = _initAsync();
  }

  Future<void> _initAsync() async {
    await SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);

    _cameras = await availableCameras();
    await _changeCamera(CameraType.back);
  }

  @override
  void dispose() {
    super.dispose();
    // Make sure to dispose of the controller when the Widget is disposed
    _controller?.dispose();
    _topButtonsOverlay?.dispose();

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
            body: _cameraBody(context),
            floatingActionButton: FloatingActionButton(
              onPressed: () => _takePicture(context),
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

  Widget _cameraBody(BuildContext context) {
    return Stack(
      children: <Widget>[
        CameraPreview(_controller),
        Positioned(
          //width: MediaQuery.of(context).size.width,
          child: Container(
            decoration: BoxDecoration(color: Colors.black),
            child: Padding(
              padding: EdgeInsets.only(left: 8, right: 8, top: 20),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: <Widget>[
                  IconButton(
                    icon: Icon(Icons.camera_alt),
                    color: Colors.white,
                    onPressed: () => _changeCamera(
                        _cameraType == CameraType.back
                            ? CameraType.front
                            : CameraType.back),
                  ),
                  IconButton(
                    icon: Icon(Icons.flash_auto),
                    color: Colors.white,
                    onPressed: () {
                      print("filled background");
                    },
                  ),
                ],
              ),
            ),
          ),
        ),
      ],
    );
  }

  Future<void> _takePicture(BuildContext context) async {
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
  }

  Future<void> _changeCamera(CameraType cameraType) async {
    // if there is no front camera
    if (_cameraType == CameraType.front && _cameras.length == 1) return;

    _cameraType = cameraType;

    final camera = _cameraType == CameraType.front
        ? _cameras.elementAt(1)
        : _cameras.first;

    // In order to display the current output from the Camera, you need to
    // create a CameraController.
    final controller = CameraController(
      // Get a specific camera from the list of available cameras
      camera,
      // Define the resolution to use
      ResolutionPreset.medium,
    );

    // Next, you need to initialize the controller. This returns a Future
    await controller.initialize();

    setState(() {
      _controller = controller;
    });
  }
}

enum CameraType { back, front }
