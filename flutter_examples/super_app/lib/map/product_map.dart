import 'package:flutter/cupertino.dart';
import 'package:geolocator/geolocator.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';

class ProductMap extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _ProductMapState();
}

class _ProductMapState extends State<ProductMap> {
  GoogleMapController mapController;

  LatLng _userPosition = const LatLng(45.521563, -122.677433);

  void _onMapCreated(GoogleMapController controller) {
    mapController = controller;
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: _findUserLocation(),
      builder: (context, snapshot) {
        switch (snapshot.connectionState) {
          case ConnectionState.done:
            return _buildUI();
          default:
            return Center(
              child: CupertinoActivityIndicator(
                animating: true,
              ),
            );
        }
      },
    );
  }

  Widget _buildUI() {
    return Stack(
      children: <Widget>[
        GoogleMap(
          onMapCreated: _onMapCreated,
          initialCameraPosition: CameraPosition(target: _userPosition, zoom: 13),
          markers: {Marker(position: _userPosition, markerId: MarkerId("User"))},
        ),
      ],
    );
  }

  Future<void> _findUserLocation() async {
    final geolocator = Geolocator();
    Position position = await geolocator.getCurrentPosition(desiredAccuracy: LocationAccuracy.high);
    _userPosition = LatLng(position.latitude, position.longitude);
  }
}
