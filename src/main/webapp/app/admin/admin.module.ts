import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { adminState, DocsComponent } from './';

@NgModule({
    imports: [RouterModule.forChild(adminState)],
    declarations: [DocsComponent],
    entryComponents: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClickbusAdminModule {
    constructor() {}
}
