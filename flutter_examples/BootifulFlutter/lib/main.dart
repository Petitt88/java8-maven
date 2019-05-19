import 'package:flutter/material.dart';
import 'package:flutter_app/widgets/routes/CameraRoute.dart';
import 'package:flutter_app/widgets/routes/InfiniteListRoute.dart';
import 'package:flutter_app/widgets/routes/TabsRoute.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Welcome to Flutter',
      home: InfiniteListRoute(),
    );
  }
}
