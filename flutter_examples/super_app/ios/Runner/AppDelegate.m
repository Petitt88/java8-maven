#include "AppDelegate.h"
#include "GeneratedPluginRegistrant.h"
#import "GoogleMaps/GoogleMaps.h"

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
  [GeneratedPluginRegistrant registerWithRegistry:self];
  // Override point for customization after application launch.

  [GMSServices provideAPIKey:@"AIzaSyA0Z8M0Bu0dTa6PsKt-5EAGHD588U9QIsM"];

  return [super application:application didFinishLaunchingWithOptions:launchOptions];
}

@end
