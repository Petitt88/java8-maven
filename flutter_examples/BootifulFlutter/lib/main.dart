import 'package:flutter/material.dart';
import 'package:flutter_app/widgets/routes/InfiniteListRoute.dart';

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
