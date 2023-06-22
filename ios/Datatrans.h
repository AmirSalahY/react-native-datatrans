
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNDatatransSpec.h"

@interface Datatrans : NSObject <NativeDatatransSpec>
#else
#import <React/RCTBridgeModule.h>

@interface Datatrans : NSObject <RCTBridgeModule>
#endif

@end
