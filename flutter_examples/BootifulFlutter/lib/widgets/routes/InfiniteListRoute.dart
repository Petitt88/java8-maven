import 'package:flutter/material.dart';
import 'package:flutter_app/widgets/molecules/RandomWords.dart';

import 'TabsRoute.dart';

class InfiniteListRoute extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('Infinite list'),
        ),
        body: Column(
          children: <Widget>[
            Container(
              padding: EdgeInsets.all(5),
              color: Colors.teal,
              alignment: Alignment.centerLeft,
              child: RaisedButton(
                child: Text('Go to Tabs'),
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => TabsRoute()),
                  );
                },
              ),
            ),
            Expanded(child: RandomWords())
          ],
        ));
  }
}
