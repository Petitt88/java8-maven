import 'package:flutter/cupertino.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';

class ProductMap extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _ProductMapState();
}

class _ProductMapState extends State<ProductMap> {
  GoogleMapController mapController;

  final LatLng _center = const LatLng(45.521563, -122.677433);
  bool mapCreated = false;

  void _onMapCreated(GoogleMapController controller) {
    mapController = controller;
    this.setState(() => mapCreated = true);
    mapCreated = true;
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: <Widget>[
        GoogleMap(
          onMapCreated: _onMapCreated,
          initialCameraPosition: CameraPosition(target: _center, zoom: 11),
        ),
        if (!mapCreated)
          Center(
            child: CupertinoActivityIndicator(
              animating: true,
            ),
          ),
      ],
    );
  }
}
