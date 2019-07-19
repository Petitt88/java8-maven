import 'package:barcode_scan/barcode_scan.dart';
import 'package:cupertino_store/icons/bar_code_icons.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';

import 'model/app_state_model.dart';
import 'product_row_item.dart';

class ProductListTab extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Consumer<AppStateModel>(
      builder: (context, model, child) {
        final products = model.getProducts();
        return CustomScrollView(
          semanticChildCount: products.length,
          slivers: <Widget>[
            CupertinoSliverNavigationBar(
              leading: CupertinoButton(
                child: Icon(
                  BarCode.barcode,
                ),
                onPressed: () async {
                  try {
                    final code = await BarcodeScanner.scan();

                    await showCupertinoDialog(
                        context: context,
                        builder: (context) => CupertinoAlertDialog(
                              title: Text('Barcode scan success'),
                              content: Text("Nice! Code:$code"),
                              actions: <Widget>[
                                CupertinoButton(
                                  child: Text("Close"),
                                  onPressed: () => Navigator.pop(context),
                                )
                              ],
                            ));
                  } on PlatformException catch (ex) {
                    await showCupertinoDialog(
                        context: context,
                        builder: (context) => CupertinoAlertDialog(
                              title: Text('Barcode scan error'),
                              content: Text(ex.toString()),
                              actions: <Widget>[
                                CupertinoButton(
                                  child: Text("Close"),
                                  onPressed: () => Navigator.pop(context),
                                )
                              ],
                            ));
                  }
                },
              ),
              largeTitle: const Text('Cupertino Store'),
            ),
            SliverSafeArea(
              top: false,
              minimum: const EdgeInsets.only(top: 8),
              sliver: SliverList(
                delegate: SliverChildBuilderDelegate(
                  (context, index) {
                    if (index < products.length) {
                      return ProductRowItem(
                        index: index,
                        product: products[index],
                        lastItem: index == products.length - 1,
                      );
                    }
                    return null;
                  },
                  //childCount: 20
                ),
              ),
            )
          ],
        );
      },
    );
  }
}
