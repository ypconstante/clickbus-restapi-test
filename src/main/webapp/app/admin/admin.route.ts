import { Routes } from '@angular/router';

import { docsRoute } from './';

const ADMIN_ROUTES = [docsRoute];

export const adminState: Routes = [
    {
        path: '',
        data: {
            authorities: []
        },
        canActivate: [],
        children: ADMIN_ROUTES
    }
];
