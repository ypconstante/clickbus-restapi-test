import { NgModule } from '@angular/core';

import { ClickbusSharedLibsModule } from './';

@NgModule({
    imports: [ClickbusSharedLibsModule],
    declarations: [],
    exports: [ClickbusSharedLibsModule]
})
export class ClickbusSharedCommonModule {}
