import 'package:cupertino_store/charts/donut_pie_chart.dart';
import 'package:cupertino_store/map/product_map.dart';
import 'package:cupertino_store/product_list_tab.dart';
import 'package:cupertino_store/search_tab.dart';
import 'package:cupertino_store/shopping_cart_tab.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';

import 'model/app_state_model.dart';

void main() {
  // This app is designed only to work vertically, so we limit
  // orientations to portrait up and down.
  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp, DeviceOrientation.portraitDown]);

  runApp(CupertinoApp(
    home: CupertinoStoreApp(),
  ));
}

class CupertinoStoreApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider<AppStateModel>(
      builder: (context) => AppStateModel()..loadProducts(),
      child: CupertinoTabScaffold(
        tabBar: CupertinoTabBar(
          items: const <BottomNavigationBarItem>[
            BottomNavigationBarItem(
              icon: Icon(CupertinoIcons.home),
              title: Text('Products'),
            ),
            BottomNavigationBarItem(
              icon: Icon(CupertinoIcons.search),
              title: Text('Search'),
            ),
            BottomNavigationBarItem(
              icon: Icon(CupertinoIcons.shopping_cart),
              title: Text('Cart'),
            ),
            BottomNavigationBarItem(
              icon: Icon(CupertinoIcons.bell),
              title: Text('Charts'),
            ),
            BottomNavigationBarItem(
              icon: Icon(CupertinoIcons.location),
              title: Text('Map'),
            ),
          ],
        ),
        tabBuilder: (context, index) {
          switch (index) {
            case 0:
              return CupertinoTabView(builder: (context) {
                return CupertinoPageScaffold(
                  child: ProductListTab(),
                );
              });
            case 1:
              return CupertinoTabView(builder: (context) {
                return CupertinoPageScaffold(
                  child: SearchTab(),
                );
              });
            case 2:
              return CupertinoTabView(builder: (context) {
                return CupertinoPageScaffold(
                  navigationBar: CupertinoNavigationBar(
                    leading: CupertinoButton(
                      child: Icon(CupertinoIcons.left_chevron),
                      onPressed: () => Navigator.of(context).pop(),
                    ),
//                  trailing: CupertinoButton(
//                    child: Icon(CupertinoIcons.right_chevron),
//                    onPressed: () => Navigator.of(context).push(CupertinoPageRoute(
//                        builder: (context) => CupertinoPageScaffold(
//                              child: ShoppingCartTab(),
//                            ))),
//                  ),
                    middle: Text("Middle text"),
                  ),
                  child: ShoppingCartTab(),
                );
              });
            case 3:
              return CupertinoTabView(builder: (context) {
                return CupertinoPageScaffold(
                  backgroundColor: CupertinoColors.activeGreen,
                  child: DonutAutoLabelChart.withSampleData(),
                );
              });
            case 4:
              return CupertinoTabView(builder: (context) {
                return CupertinoPageScaffold(
                  child: ProductMap(),
                );
              });
          }
          return null;
        },
      ),
    );
  }
}
