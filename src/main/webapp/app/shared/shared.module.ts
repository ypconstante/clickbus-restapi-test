import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { ClickbusSharedLibsModule, ClickbusSharedCommonModule } from './';

@NgModule({
    imports: [ClickbusSharedLibsModule, ClickbusSharedCommonModule],
    declarations: [],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [],
    exports: [ClickbusSharedCommonModule],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClickbusSharedModule {}
