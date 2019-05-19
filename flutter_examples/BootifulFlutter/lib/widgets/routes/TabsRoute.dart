import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_app/helpers/ToastHelper.dart';
import 'package:photo_view/photo_view.dart';

import 'CameraRoute.dart';

class TabsRoute extends StatefulWidget {
  @override
  _TabsRouteState createState() => _TabsRouteState();
}

class _TabsRouteState extends State<TabsRoute>
    with SingleTickerProviderStateMixin {
  TabController _tabController;
  String _picturePath;

  @override
  void initState() {
    super.initState();

    _tabController = TabController(
      vsync: this,
      length: 2,
    );
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Tabs"),
      ),
      bottomNavigationBar: BottomAppBar(
        color: Colors.blue,
        child: new TabBar(
          indicatorColor: Colors.white,
          controller: _tabController,
          tabs: [
            Tab(icon: Icon(Icons.camera)),
            Tab(icon: Icon(Icons.image)),
          ],
        ),
      ),
      body: TabBarView(controller: _tabController, children: [
        Center(
          child: RaisedButton(
            child: Text("Take a picture"),
            onPressed: () => Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) => CameraRoute((picturePath) async {
                            await ToastHelper.success(
                                "Showing picture from $picturePath");
                            setState(() {
                              _picturePath = picturePath;
                              _tabController.animateTo(1);
                            });
                          })),
                ),
          ),
        ),
        if (_picturePath == null)
          Center(child: Text("No picture has been taken so far...")),
        if (_picturePath != null)
          Container(
            color: Colors.black,
            child: PhotoView(
              imageProvider: FileImage(File(_picturePath)),
            ),
          )
      ]),
    );
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }
}
